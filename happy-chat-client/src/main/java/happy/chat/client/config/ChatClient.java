package happy.chat.client.config;

import com.google.common.util.concurrent.Futures;
import happy.chat.client.handler.ClientInitHandler;
import happy.chat.common.HappyChatProperties;
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
import java.util.concurrent.TimeUnit;

@Component
public class ChatClient {

    private Logger logger = LoggerFactory.getLogger(ChatClient.class);

    @Autowired
    private ClientInitHandler clientInitHandler;

    @Autowired
    private HappyChatProperties happyChatProperties;

    private EventLoopGroup group = new NioEventLoopGroup();

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
        connect(bootstrap, happyChatProperties.getClient().getGetReconnectNum());
    }

    private void connect(Bootstrap bootstrap, int retry) {
        int reconnectNum = happyChatProperties.getClient().getGetReconnectNum();
        bootstrap.connect(happyChatProperties.getServer().getHost(), happyChatProperties.getServer().getPort())
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        this.channel = future.channel();
                        logger.info(LocalDateTime.now() + ":启动成功");
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

}
