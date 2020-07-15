package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * 心跳判断handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class HeartbeatHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg.Heartbeat> {

    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg.Heartbeat heartbeatRequest) throws Exception {
        ResponseBody.ResponseMsg.Heartbeat.Builder heartbeatResponse = ResponseBody.ResponseMsg.Heartbeat.newBuilder();
        ctx.writeAndFlush(heartbeatResponse.build());
    }
}
