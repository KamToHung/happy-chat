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
public class SignInHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg.SignIn> {

    private static final Logger logger = LoggerFactory.getLogger(SignInHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg.SignIn msg) throws Exception {
        String userId = msg.getUserId();
        String username = msg.getUsername();
        if (msg.getSuccess()) {
            logger.info("用户:" + username + "(" + userId + ")" + "登录成功");
        } else {
            logger.info("用户:" + username + "(" + userId + ")" + "登录失败,原因为:" + msg.getReason());
        }
    }


}
