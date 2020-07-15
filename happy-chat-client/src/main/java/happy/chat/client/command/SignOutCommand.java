package happy.chat.client.command;

import happy.chat.common.protobuf.request.RequestBody;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SignOutCommand implements BaseCommand {

    @Override
    public void exec(Channel channel, Scanner scanner) {
        RequestBody.RequestMsg.SignOut.Builder signOutBuilder = RequestBody.RequestMsg.SignOut.newBuilder();
        channel.writeAndFlush(RequestBody.RequestMsg.newBuilder().setCommand(RequestBody.RequestMsg.Command.SIGN_OUT).setSignOut(signOutBuilder).build());
    }

    @Override
    public int command() {
        return RequestBody.RequestMsg.Command.SIGN_OUT_VALUE;
    }
}
