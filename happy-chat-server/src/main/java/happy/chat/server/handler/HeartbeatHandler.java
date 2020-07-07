package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class HeartbeatHandler extends SimpleChannelInboundHandler<RequestBody.Heartbeat> {

    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.Heartbeat heartbeatRequest) throws Exception {
        ResponseBody.Heartbeat.Builder heartbeatResponse = ResponseBody.Heartbeat.newBuilder();
        ctx.writeAndFlush(heartbeatResponse.build());
    }
}
