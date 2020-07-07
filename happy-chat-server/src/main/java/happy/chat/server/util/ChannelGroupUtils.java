package happy.chat.server.util;

import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * channelGroup管理
 *
 * @author Terry
 */
public class ChannelGroupUtils {

    /**
     * Key:groupId,Value:channels
     */
    private static final Map<String, ChannelGroup> CHANNEL_GROUP_MAP = new ConcurrentHashMap<>();


    public static void addChannelGroup(String groupId, ChannelGroup group) {
        CHANNEL_GROUP_MAP.put(groupId, group);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return CHANNEL_GROUP_MAP.get(groupId);
    }
}
