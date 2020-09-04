package happy.chat.client.handler.sign;

import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class SignOutHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg.SignOut> {

    private static final Logger logger = LoggerFactory.getLogger(SignInHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg.SignOut msg) throws Exception {
        if (msg.getSuccess()) {
            logger.info("退出登录成功");
        } else {
            logger.error("退出登录失败,原因:" + msg.getReason());
        }
        //关闭通道
        ctx.close();
    }

}
