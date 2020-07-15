package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.common.protobuf.response.ResponseBody;
import happy.chat.server.entity.UserInfo;
import happy.chat.server.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 登录handler
 *
 * @author Terry
 */
@Component
@ChannelHandler.Sharable
public class SignInHandler extends SimpleChannelInboundHandler<RequestBody.RequestMsg> {

    private static final Logger logger = LoggerFactory.getLogger(SignInHandler.class);

    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.RequestMsg signInRequest) throws Exception {
        if (signInRequest.getCommand() == RequestBody.RequestMsg.Command.SIGN_IN) {
            ResponseBody.ResponseMsg.SignIn.Builder signInResponse = ResponseBody.ResponseMsg.SignIn.newBuilder();
            signInResponse.setUsername(signInRequest.getSignIn().getUsername());
            // 处理登录response
            if (valid(signInRequest.getSignIn())) {
                String userId = UUID.randomUUID().toString();
                signInResponse.setUserId(userId);
                signInResponse.setSuccess(true);
                logger.info(LocalDateTime.now() + ": 用户[" + signInRequest.getSignIn().getUsername() + "]" + "登录成功");
                // 绑定用户信息
                UserInfo userInfo = new UserInfo(userId, signInRequest.getSignIn().getUsername());
                SessionUtils.addSession(userInfo, ctx.channel());
            } else {
                signInResponse.setReason("账号密码错误");
                signInResponse.setSuccess(false);
                logger.error(LocalDateTime.now() + ": 用户[" + signInRequest.getSignIn().getUsername() + "]" + "登录失败");
            }
            ctx.channel().writeAndFlush(ResponseBody.ResponseMsg.newBuilder().setSignIn(signInResponse.build()).build());
        }
    }

    private boolean valid(RequestBody.RequestMsg.SignIn msg) {
        //TODO 处理登录
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 和服务器断开后移除通道
        SessionUtils.delSession(ctx.channel());
    }
}
