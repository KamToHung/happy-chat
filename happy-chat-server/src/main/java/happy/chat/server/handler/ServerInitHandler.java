package happy.chat.server.handler;

import happy.chat.common.protobuf.request.RequestBody;
import happy.chat.server.handler.status.ChatStateHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * handler初始化
 *
 * @author Terry
 */
@Component
public class ServerInitHandler extends ChannelInitializer<NioSocketChannel> {

    @Autowired
    private ChatStateHandler chatStateHandler;

    @Autowired
    private MessageDispatcherHandler messageDispatcherHandler;

    protected void initChannel(NioSocketChannel ch) {
        ch.pipeline().addLast(chatStateHandler)
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(RequestBody.RequestMsg.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(messageDispatcherHandler);
    }
}
