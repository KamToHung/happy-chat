package happy.chat.client.handler;

import happy.chat.common.protobuf.response.ResponseBody;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ClientInitHandler extends ChannelInitializer<NioSocketChannel> {

    private final ChatStateHandler chatStateHandler;

    private final ChatClientHandler chatClientHandler;

    private final NettyClientHandler nettyClientHandler;

    @Autowired
    public ClientInitHandler(ChatStateHandler chatStateHandler, ChatClientHandler chatClientHandler, NettyClientHandler nettyClientHandler) {
        this.chatStateHandler = chatStateHandler;
        this.chatClientHandler = chatClientHandler;
        this.nettyClientHandler = nettyClientHandler;
    }


    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(chatStateHandler)
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(ResponseBody.ResponseMsg.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(chatClientHandler)
                .addLast(nettyClientHandler);
    }
}
