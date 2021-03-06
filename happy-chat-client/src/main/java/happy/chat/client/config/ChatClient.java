package happy.chat.client.config;

import happy.chat.client.command.BaseCommand;
import happy.chat.client.handler.ClientInitHandler;
import happy.chat.common.HappyChatProperties;
import happy.chat.common.protobuf.request.RequestBody;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Component
public class ChatClient {

    private static final Logger logger = LoggerFactory.getLogger(ChatClient.class);

    private final ClientInitHandler clientInitHandler;

    private final HappyChatProperties happyChatProperties;

    private final CommandInfo commandInfo;

    private final EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    public ChatClient(ClientInitHandler clientInitHandler, HappyChatProperties happyChatProperties, CommandInfo commandInfo) {
        this.clientInitHandler = clientInitHandler;
        this.happyChatProperties = happyChatProperties;
        this.commandInfo = commandInfo;
    }

    @PostConstruct
    public void start() {
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(clientInitHandler);
        connect(bootstrap, happyChatProperties.getClient().getReconnectNum());
    }

    private void connect(Bootstrap bootstrap, int retry) {
        int reconnectNum = happyChatProperties.getClient().getReconnectNum();
        bootstrap.connect(happyChatProperties.getServer().getHost(), happyChatProperties.getServer().getPort())
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        this.channel = future.channel();
                        logger.info(LocalDateTime.now() + ":启动成功");
                        startConsoleThread(channel);
                    } else {
                        //TODO 连接失败处理
                        if (retry == 0) {
                            logger.error("超过重试次数,停止连接");
                        }
                        int count = (reconnectNum - retry) + 1;
                        logger.error(LocalDateTime.now() + ": 连接失败，第" + count + "次重连");
                        bootstrap.config().group().schedule(() -> connect(bootstrap, retry - 1),
                                happyChatProperties.getClient().getReconnectTime(), TimeUnit.SECONDS);
                    }
                });
    }

    @PreDestroy
    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }

    private void startConsoleThread(Channel channel) {
        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            login(channel, scanner);
            order(channel, scanner);
        }).start();
    }

    /**
     * 登录
     *
     * @param channel
     */
    private void login(Channel channel, Scanner scanner) {
        BaseCommand command = commandInfo.getCommand(RequestBody.RequestMsg.Command.SIGN_IN_VALUE);
        command.exec(channel, scanner);
    }

    private void order(Channel channel, Scanner scanner) {
        while (true) {
            logger.info("请输入指令(1:发送信息,2:退出登录):");
            String command = scanner.next();
            BaseCommand infoCommand = commandInfo.getCommand(Integer.parseInt(command));
            infoCommand.exec(channel, scanner);
        }
    }

}
