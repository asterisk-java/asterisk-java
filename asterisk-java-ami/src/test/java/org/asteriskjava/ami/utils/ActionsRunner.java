/*
 * Copyright 2004-2023 Asterisk Java contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asteriskjava.ami.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.asteriskjava.ami.action.annotation.ExpectedResponse;
import org.asteriskjava.ami.action.annotation.GeneratedEvents;
import org.asteriskjava.ami.action.api.ChallengeAction;
import org.asteriskjava.ami.action.api.LoginAction;
import org.asteriskjava.ami.action.api.ManagerAction;
import org.asteriskjava.ami.action.api.response.ChallengeActionResponse;
import org.asteriskjava.ami.action.api.response.ManagerActionResponse;
import org.asteriskjava.core.databind.AsteriskDecoder;
import org.asteriskjava.core.databind.AsteriskEncoder;
import org.testcontainers.containers.GenericContainer;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static io.netty.handler.timeout.IdleState.ALL_IDLE;
import static java.time.Instant.now;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.asteriskjava.ami.action.api.AuthType.MD5;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;

public class ActionsRunner {
    private final List<Pair<Class<? extends ManagerAction>, Function<ManagerActionResponse, ManagerAction>>> actions = new LinkedList<>();
    private final GenericContainer<?> asteriskDocker;

    private Instant now = now();

    public ActionsRunner(GenericContainer<?> asteriskDocker) {
        this.asteriskDocker = asteriskDocker;
    }

    public ActionsRunner withFixedTime(Instant now) {
        this.now = now;
        return this;
    }

    public ActionsRunner registerLoginSequence() {
        ChallengeAction challengeAction = new ChallengeAction();
        challengeAction.setActionId("login-seq-1");
        challengeAction.setAuthType(MD5);

        Function<ManagerActionResponse, ManagerAction> loginActionProvider = prevResponse -> {
            ChallengeActionResponse challengeActionResponse = (ChallengeActionResponse) prevResponse;

            String challenge = challengeActionResponse.getChallenge();
            String bytes = md5Hex(challenge + "123qwe");

            LoginAction loginAction = new LoginAction("asterisk-java-it-tests", MD5, bytes);
            loginAction.setActionId("login-seq-2");
            return loginAction;
        };
        return registerAction(ChallengeAction.class, challengeAction)
                .registerAction(LoginAction.class, loginActionProvider);
    }

    public <A extends ManagerAction> ActionsRunner registerAction(Class<A> actionClass, Function<ManagerActionResponse, ManagerAction> actionProvider) {
        actions.add(Pair.of(actionClass, actionProvider));
        return this;
    }

    public <A extends ManagerAction> ActionsRunner registerAction(Class<A> actionClass, ManagerAction managerAction) {
        registerAction(actionClass, managerActionResponse -> managerAction);
        return this;
    }

    public <A extends ManagerAction> ActionsRunner registerAction(ManagerAction managerAction) {
        registerAction(managerAction.getClass(), managerAction);
        return this;
    }

    public ResponseRecorder run() throws InterruptedException {
        Queue<Pair<Class<? extends ManagerAction>, Function<ManagerActionResponse, ManagerAction>>> queue = new ArrayBlockingQueue<>(actions.size(), false, actions);

        ResponseRecorder responseRecorder = new ResponseRecorder();

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new LineBasedFrameDecoder(8192))
                                    .addLast("decoder", new StringDecoder())
                                    .addLast("encoder", new StringEncoder())
                                    .addLast(new IdleStateHandler(0, 0, 2, SECONDS))
                                    .addLast("actionResponse", new ActionResponseHandler(queue, now, responseRecorder));
                        }
                    });
            Channel channel = bootstrap.connect(asteriskDocker.getHost(), asteriskDocker.getFirstMappedPort()).sync().channel();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

        return responseRecorder;
    }

    static class ActionResponseHandler extends SimpleChannelInboundHandler<String> {
        private static final AsteriskEncoder asteriskEncoder = new AsteriskEncoder(CRLF);
        private static final AsteriskDecoder asteriskDecoder = new AsteriskDecoder();

        private final Map<String, Class<? extends ManagerActionResponse>> mapResponses = new ConcurrentHashMap<>();
        private final Map<String, Map<String, Class<?>>> mapResponseEvents = new ConcurrentHashMap<>();

        private final Queue<Pair<Class<? extends ManagerAction>, Function<ManagerActionResponse, ManagerAction>>> actions;
        private final Instant instant;
        private final ResponseRecorder responseRecorder;

        private final StringBuilder accumulatedResponse = new StringBuilder();

        ActionResponseHandler(
                Queue<Pair<Class<? extends ManagerAction>, Function<ManagerActionResponse, ManagerAction>>> actions,
                Instant instant,
                ResponseRecorder responseRecorder
        ) {
            this.actions = actions;
            this.instant = instant;
            this.responseRecorder = responseRecorder;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String message) {
            if (message.isBlank()) {
                String accumulatedMessage = accumulatedResponse.toString().trim();

                System.out.println("<<<<<<<<<<<<INCOMING<<<<<<<<<<<");
                System.out.println(accumulatedMessage.trim());
                System.out.println("===============================");

                if (accumulatedMessage.startsWith("Response:")) {
                    sendNextAction(ctx, getManagerActionResponseAndRecord(accumulatedMessage));
                } else if (accumulatedMessage.startsWith("Event:")) {
                    if (accumulatedMessage.contains("ActionID:")) {
                        recordManagerActionResponseEventIfNeeded(accumulatedMessage);
                    }
                }

                accumulatedResponse.setLength(0);
            } else if (isWelcomeMessage(message)) {
                sendNextAction(ctx, null);
            } else {
                accumulatedResponse.append(message).append(CRLF.getPattern());
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            if (evt instanceof IdleStateEvent e) {
                if (e.state() == ALL_IDLE) {
                    System.out.println("Close inactive connection");
                    ctx.close();
                }
            }
        }

        private void sendNextAction(ChannelHandlerContext ctx, ManagerActionResponse managerActionResponse) {
            Pair<Class<? extends ManagerAction>, Function<ManagerActionResponse, ManagerAction>> actionFunctionPair = actions.poll();
            if (actionFunctionPair != null) {
                Class<?> actionClass = actionFunctionPair.getKey();
                Function<ManagerActionResponse, ManagerAction> actionFunction = actionFunctionPair.getValue();
                ManagerAction action = actionFunction.apply(managerActionResponse);

                String actionId = action.getActionId();

                ExpectedResponse expectedResponse = actionClass.getAnnotation(ExpectedResponse.class);
                if (expectedResponse == null) {
                    throw new RuntimeException("Action does not have @ExpectedResponse annotation");
                }
                mapResponses.put(actionId, expectedResponse.value());

                GeneratedEvents generatedEvents = actionClass.getAnnotation(GeneratedEvents.class);
                if (generatedEvents != null) {
                    Map<String, Class<?>> nameToClass = Arrays.stream(generatedEvents.value())
                            .collect(toMap(GeneratedEvents.Event::name, GeneratedEvents.Event::value, (a, b) -> b));
                    mapResponseEvents.put(actionId, nameToClass);
                }

                String encode = asteriskEncoder.encode(action);
                ctx.writeAndFlush(encode);

                System.out.println(">>>>>>>>>>>>OUTGOING>>>>>>>>>>>>");
                System.out.println(encode.trim());
                System.out.println("===============================");
            }
        }

        private ManagerActionResponse getManagerActionResponseAndRecord(String message) {
            String[] split = message.split(CRLF.getPattern());
            Map<String, Object> map = AsteriskDecoder.toMap(split);
            String actionId = (String) map.get("ActionID");

            Class<? extends ManagerActionResponse> clazz = mapResponses.remove(actionId);
            ManagerActionResponse managerActionResponse = asteriskDecoder.decode(map, clazz);
            managerActionResponse.setDateReceived(instant);

            responseRecorder.recordForAction(actionId).doRecord(managerActionResponse);
            return managerActionResponse;
        }

        private void recordManagerActionResponseEventIfNeeded(String message) {
            String[] split = message.split(CRLF.getPattern());
            Map<String, Object> map = AsteriskDecoder.toMap(split);
            String actionId = (String) map.get("ActionID");

            Map<String, Class<?>> events = mapResponseEvents.get(actionId);
            if (events != null) {
                String event = (String) map.get("Event");
                Class<?> clazz = events.get(event);
                Object responseEvent = asteriskDecoder.decode(map, clazz);
                responseRecorder.recordForAction(actionId).doRecord(responseEvent);
            }
        }

        private static boolean isWelcomeMessage(String message) {
            return message.startsWith("Asterisk Call Manager");
        }
    }

    public static class ResponseRecorder {
        private final Map<String, ManagerActionResponse> mapResponses = new ConcurrentHashMap<>();
        private final Map<String, Map<Class<?>, List<Object>>> mapResponseEvents = new ConcurrentHashMap<>();

        private ResponseRecorder() {
        }

        Recorder recordForAction(String actionId) {
            return new Recorder(actionId);
        }

        public Provider getForAction(String actionId) {
            return new Provider(actionId);
        }

        public <T extends ManagerActionResponse> T getRecorderResponse(String actionId, Class<T> clazz) {
            ManagerActionResponse managerActionResponse = mapResponses.get(actionId);
            return clazz.cast(managerActionResponse);
        }

        class Recorder {
            private final String actionId;

            Recorder(String actionId) {
                this.actionId = actionId;
            }

            void doRecord(ManagerActionResponse managerActionResponse) {
                mapResponses.put(actionId, managerActionResponse);
            }

            void doRecord(Object responseEvent) {
                mapResponseEvents.compute(actionId, (s, o) -> {
                    if (o == null) {
                        Map<Class<?>, List<Object>> map = new HashMap<>();
                        List<Object> objects = new ArrayList<>();
                        objects.add(responseEvent);
                        map.put(responseEvent.getClass(), objects);
                        return map;
                    }

                    List<Object> responseEvents = o.getOrDefault(responseEvent.getClass(), null);
                    if (responseEvents == null) {
                        List<Object> objects = new ArrayList<>();
                        objects.add(responseEvent);
                        o.put(responseEvent.getClass(), objects);
                    } else {
                        responseEvents.add(responseEvent);
                        o.put(responseEvent.getClass(), responseEvents);
                    }

                    return o;
                });
            }
        }

        public class Provider {
            private final String actionId;

            Provider(String actionId) {
                this.actionId = actionId;
            }

            public <T extends ManagerActionResponse> T getResponse(Class<T> clazz) {
                ManagerActionResponse managerActionResponse = mapResponses.get(actionId);
                return clazz.cast(managerActionResponse);
            }

            public <T> List<T> getResponseEvents(Class<T> clazz) {
                Map<Class<?>, List<Object>> classToEvents = mapResponseEvents.get(actionId);
                List<Object> responseEvents = classToEvents.getOrDefault(clazz, null);
                if (responseEvents == null) {
                    return emptyList();
                }
                return responseEvents.stream()
                        .map(clazz::cast)
                        .toList();
            }
        }
    }
}
