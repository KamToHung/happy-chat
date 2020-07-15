package happy.chat.client.config;

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

    @Autowired
    private ClientInitHandler clientInitHandler;

    @Autowired
    private HappyChatProperties happyChatProperties;

    private final EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

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

    private static void startConsoleThread(Channel channel) {
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                login(channel, scanner);
            }
        }).start();
    }

    /**
     * 登录
     *
     * @param channel
     */
    private static void login(Channel channel, Scanner scanner) {
//        RequestBody
        logger.info("开始登录,请输入用户名:");
        RequestBody.RequestMsg.SignIn.Builder signInBuilder = RequestBody.RequestMsg.SignIn.newBuilder();
        signInBuilder.setUsername(scanner.nextLine());
        logger.info("请输入密码:");
        signInBuilder.setPassword(scanner.nextLine());
        RequestBody.RequestMsg.Builder builder = RequestBody.RequestMsg.newBuilder();
        builder.setSignIn(signInBuilder);
        channel.writeAndFlush(builder.build());
        try {
            Thread.sleep(50000);
        } catch (InterruptedException ignored) {
        }
    }

}
