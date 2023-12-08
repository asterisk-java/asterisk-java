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
package org.asteriskjava.ami.action;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.asteriskjava.ami.ActionFieldsComparator;
import org.asteriskjava.ami.action.response.ChallengeActionResponse;
import org.asteriskjava.ami.action.response.ManagerActionResponse;
import org.asteriskjava.core.databind.AsteriskDecoder;
import org.asteriskjava.core.databind.AsteriskEncoder;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.Duration.ofSeconds;
import static java.time.Instant.now;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.AuthType.MD5;
import static org.asteriskjava.ami.action.response.ResponseType.Success;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.testcontainers.containers.BindMode.READ_ONLY;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;

@Testcontainers
class ChallengeActionItTest {
    @SuppressWarnings("rawtypes")
    @Container
    private static final GenericContainer<?> asterisk = new GenericContainer("andrius/asterisk:alpine-18.15.1")
            .withClasspathResourceMapping("manager.conf", "/etc/asterisk/manager.conf", READ_ONLY)
            .withAccessToHost(true)
            .withExposedPorts(5038)
            .waitingFor(forLogMessage(".*Asterisk Ready.*", 1));

    @Test
    void shouldSendChallengeActionAndReceiveChallengeResponse() throws InterruptedException {
        //given
        Instant now = now();

        ChallengeAction challengeAction = new ChallengeAction();
        challengeAction.setActionId("id-1");
        challengeAction.setAuthType(MD5);

        //when
        ChallengeActionResponse actual = testAction(challengeAction, ChallengeActionResponse.class, now);

        //then
        assertThat(actual.getResponse()).isEqualTo(Success);
        assertThat(actual.getActionId()).isEqualTo("id-1");
        assertThat(actual.getDateReceived()).isEqualTo(now);
        assertThat(actual.getChallenge()).isNotBlank();
    }

    private static <R extends ManagerActionResponse> R testAction(ManagerAction action, Class<R> responseClass, Instant instant) throws InterruptedException {
        AsteriskEncoder asteriskEncoder = AsteriskEncoder
                .builder()
                .crlfNewlineDelimiter()
                .fieldNamesComparator(new ActionFieldsComparator())
                .build();
        AtomicReference<R> response = new AtomicReference<>();

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("decoder", new StringDecoder())
                                .addLast("encoder", new StringEncoder())
                                .addLast("actionResponse", new ActionResponseHandler<>(response, responseClass, instant))
                                .addLast(new IdleStateHandler(0, 0, 2, SECONDS));
                    }
                });
        Channel channel = bootstrap.connect(asterisk.getHost(), asterisk.getFirstMappedPort()).sync().channel();
        channel.writeAndFlush(asteriskEncoder.encode(action));

        await().atMost(ofSeconds(5)).untilAtomic(response, instanceOf(responseClass));

        return response.get();
    }

    static class ActionResponseHandler<R extends ManagerActionResponse> extends SimpleChannelInboundHandler<String> {
        private final AsteriskDecoder asteriskDecoder = new AsteriskDecoder();
        private final AtomicReference<R> response;
        private final Class<R> clazz;
        private final Instant instant;

        public ActionResponseHandler(AtomicReference<R> response, Class<R> clazz, Instant instant) {
            this.response = response;
            this.clazz = clazz;
            this.instant = instant;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String message) {
            System.out.println("Received message from Asterisk: " + message);

            if (message.startsWith("Asterisk Call Manager/")) {
                return;
            }

            String[] split = message.split(CRLF.getPattern());

            R decode = asteriskDecoder.decode(split, clazz);
            decode.setDateReceived(instant);
            response.set(decode);
        }
    }
}
