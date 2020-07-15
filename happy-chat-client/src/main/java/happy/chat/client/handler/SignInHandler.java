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
public class SignInHandler extends SimpleChannelInboundHandler<ResponseBody.ResponseMsg> {

    private static final Logger logger = LoggerFactory.getLogger(SignInHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBody.ResponseMsg msg) throws Exception {
        if (msg.getCommand() == ResponseBody.ResponseMsg.Command.SIGN_IN) {
            String userId = msg.getSignIn().getUserId();
            String username = msg.getSignIn().getUsername();
            if (msg.getSignIn().getSuccess()) {
                logger.info("用户:" + username + "(" + userId + ")" + "登录成功");
            } else {
                logger.info("用户:" + username + "(" + userId + ")" + "登录失败,原因为:" + msg.getSignIn().getReason());
            }
        }
    }


}
