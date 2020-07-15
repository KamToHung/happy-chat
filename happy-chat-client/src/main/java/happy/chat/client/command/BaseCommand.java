package happy.chat.client.command;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 调用命令
 */
public interface BaseCommand {

    void exec(Channel channel, Scanner scanner);

    int command();
}
