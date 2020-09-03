package happy.chat.client.handler;

import happy.chat.common.protobuf.request.RequestBody;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Terry
 * @since 2020/9/3 18:02
 */
@Component
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            logger.info("发起心跳");
            RequestBody.RequestMsg.Heartbeat.Builder heartBuilder = RequestBody.RequestMsg.Heartbeat.newBuilder();
            ctx.writeAndFlush(RequestBody.RequestMsg.newBuilder()
                    .setCommandValue(RequestBody.RequestMsg.Command.HEARTBEAT_VALUE).setHeartbeat(heartBuilder.build()).build())
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("[exceptionCaught][连接({}) 发生异常]", ctx.channel().id(), cause);
        // 断开连接
        ctx.channel().close();
    }
}
