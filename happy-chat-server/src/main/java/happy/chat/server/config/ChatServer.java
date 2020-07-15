package happy.chat.server.config;

import happy.chat.common.HappyChatProperties;
import happy.chat.server.handler.ServerInitHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class ChatServer {

    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    private final HappyChatProperties happyChatProperties;

    private final ServerInitHandler serverInitHandler;

    private final EventLoopGroup bossGroup;

    private final EventLoopGroup workerGroup;

    private Channel channel;

    @Autowired
    public ChatServer(HappyChatProperties happyChatProperties, ServerInitHandler serverInitHandler) {
        this.happyChatProperties = happyChatProperties;
        this.serverInitHandler = serverInitHandler;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup(10);
    }

    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(serverInitHandler);
        ChannelFuture future = bootstrap.bind(happyChatProperties.getServer().getPort()).sync();
        if (future.isSuccess()) {
            this.channel = future.channel();
            logger.info("快乐聊天服务器启动成功,端口为: " + happyChatProperties.getServer().getPort());
        }
    }

    @PreDestroy
    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
