package happy.chat.server.util;

import happy.chat.server.entity.UserInfo;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * channel管理器
 *
 * @author Terry
 */
public class SessionUtils {

    private static final Logger logger = LoggerFactory.getLogger(SessionUtils.class);

    private static final AttributeKey<UserInfo> USER_INFO = AttributeKey.newInstance("session");

    /**
     * Key:userId,Value:channel
     */
    private static final Map<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>();

    public static void addSession(UserInfo userInfo, Channel channel) {
        logger.info("userId:[" + userInfo.getUserId() + "]已登录");
        USER_CHANNEL_MAP.put(userInfo.getUserId(), channel);
        channel.attr(USER_INFO).set(userInfo);
    }

    public static UserInfo getSession(Channel channel) {
        return channel.attr(USER_INFO).get();
    }

    public static void delSession(Channel channel) {
        UserInfo userInfo = getSession(channel);
        if (userInfo != null) {
            USER_CHANNEL_MAP.remove(userInfo.getUserId());
            channel.attr(USER_INFO).set(null);
            logger.info("userId:[" + userInfo.getUserId() + "]退出登录");
        }
    }

    public static Channel getChannel(String userId) {
        return USER_CHANNEL_MAP.get(userId);
    }
}
