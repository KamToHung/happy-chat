package happy.chat.client.handler;


import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class HeartbeatHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg.Heartbeat> {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg.Heartbeat msg) throws Exception {
        logger.info("客户端接收到心跳");
        ctx.writeAndFlush(msg);
    }

}
