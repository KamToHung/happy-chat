package happy.chat.client.command;

import happy.chat.common.protobuf.request.RequestBody;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MessageCommand implements BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(MessageCommand.class);


    @Override
    public void exec(Channel channel, Scanner scanner) {
        logger.info("请输入发送用户的ID:");
        String toUserId = scanner.next();
        logger.info("请输入消息:");
        String message = scanner.next();
        while (!message.equals("exit")) {
            this.exec(channel, toUserId, message);
            message = scanner.next();
        }
    }

    @Override
    public int command() {
        return RequestBody.RequestMsg.Command.MESSAGE_VALUE;
    }

    private void exec(Channel channel, String toUserId, String message) {
        RequestBody.RequestMsg.UserMessage.Builder messageBuilder = RequestBody.RequestMsg.UserMessage.newBuilder()
                .setToUserId(toUserId)
                .setMessage(message);
        channel.writeAndFlush(RequestBody.RequestMsg.newBuilder()
                .setCommand(RequestBody.RequestMsg.Command.MESSAGE)
                .setUserMessage(messageBuilder)
                .build());
    }

}
