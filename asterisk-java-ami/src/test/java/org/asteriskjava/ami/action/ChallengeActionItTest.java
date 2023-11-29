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
import org.asteriskjava.core.databind.AsteriskObjectMapper;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.asteriskjava.ami.action.AuthType.MD5;
import static org.testcontainers.containers.BindMode.READ_ONLY;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;

@Testcontainers
class ChallengeActionItTest {
    @Container
    static final GenericContainer<?> asterisk = new GenericContainer<>("andrius/asterisk:alpine-18.15.1")
            .withClasspathResourceMapping("manager.conf", "/etc/asterisk/manager.conf", READ_ONLY)
            .withAccessToHost(true)
            .withExposedPorts(5038)
            .waitingFor(forLogMessage(".*Asterisk Ready.*", 1));

    @Test
    void shouldName() throws InterruptedException {
        //given
        ChallengeAction challengeAction = new ChallengeAction();
        challengeAction.setActionId("id-1");
        challengeAction.setAuthType(MD5);

        AsteriskObjectMapper asteriskObjectMapper = AsteriskObjectMapper
                .builder()
                .crlfNewlineDelimiter()
                .fieldNamesComparator(new ActionFieldsComparator())
                .build();

        String action = asteriskObjectMapper.writeValue(challengeAction);

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
                                .addLast("actionResponse", new ActionResponseHandler())
                                .addLast(new IdleStateHandler(0, 0, 2, SECONDS));
                    }
                });
        Channel channel = bootstrap.connect(asterisk.getHost(), asterisk.getFirstMappedPort()).sync().channel();
        channel.writeAndFlush(action);
        channel.closeFuture().sync();

        //when

        //then
    }

    static class ActionResponseHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String message) {
            System.out.println("Received message from Asterisk: " + message);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
