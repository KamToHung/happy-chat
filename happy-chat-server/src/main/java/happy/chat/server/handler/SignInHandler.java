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
public class SignInHandler extends SimpleChannelInboundHandler<RequestBody.SignIn> {

    private static final Logger logger = LoggerFactory.getLogger(SignInHandler.class);

    protected void channelRead0(ChannelHandlerContext ctx, RequestBody.SignIn signInRequest) throws Exception {
        ResponseBody.SignIn.Builder signInResponse = ResponseBody.SignIn.newBuilder();
        signInResponse.setUsername(signInRequest.getUsername());
        // 处理登录response
        if (valid(signInRequest)) {
            String userId = UUID.randomUUID().toString();
            signInResponse.setUserId(userId);
            signInResponse.setSuccess(true);
            logger.info(LocalDateTime.now() + ": 用户[" + signInRequest.getUsername() + "]" + "登录成功");
            // 绑定用户信息
            UserInfo userInfo = new UserInfo(userId, signInRequest.getUsername());
            SessionUtils.addSession(userInfo, ctx.channel());
        } else {
            signInResponse.setReason("账号密码错误");
            signInResponse.setSuccess(false);
            logger.error(LocalDateTime.now() + ": 用户[" + signInRequest.getUsername() + "]" + "登录失败");
        }
        ctx.channel().writeAndFlush(signInResponse.build());
    }

    private boolean valid(RequestBody.SignIn msg) {
        //TODO 处理登录
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 和服务器断开后移除通道
        SessionUtils.delSession(ctx.channel());
    }
}