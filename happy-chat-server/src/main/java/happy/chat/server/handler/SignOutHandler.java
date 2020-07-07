package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class SignOutHandler extends SimpleChannelInboundHandler<RequestBody.SignOut> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.SignOut signOutRequest) throws Exception {
        ResponseBody.SignOut.Builder signOutResponse = ResponseBody.SignOut.newBuilder();
        signOutResponse.setSuccess(true);
        signOutResponse.setReason("退出成功");
        //TODO 移除用户信息
        ctx.channel().writeAndFlush(signOutResponse.build());
    }

}
