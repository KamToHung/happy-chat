package happy.chat.client.command;

import happy.chat.common.protobuf.request.RequestBody;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class SignInCommand implements BaseCommand{

    private static final Logger logger = LoggerFactory.getLogger(SignInCommand.class);

    @Override
    public void exec(Channel channel, Scanner scanner) {
        logger.info("开始登录,请输入用户名:");
        RequestBody.RequestMsg.SignIn.Builder signInBuilder = RequestBody.RequestMsg.SignIn.newBuilder();
        signInBuilder.setUsername(scanner.nextLine());
        logger.info("请输入密码:");
        signInBuilder.setPassword(scanner.nextLine());
        RequestBody.RequestMsg.Builder builder = RequestBody.RequestMsg.newBuilder();
        builder.setSignIn(signInBuilder);
        channel.writeAndFlush(builder.build());
    }

    @Override
    public int command() {
        return RequestBody.RequestMsg.Command.SIGN_IN_VALUE;
    }
}
