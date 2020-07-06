package happy.chat.common.constants;

/**
 * 信息操作
 *
 * @author Terry
 */
public enum Command {

    LOGIN_REQUEST(1),

    MESSAGE_REQUEST(2),

    LOGIN_OUT_REQEUST(3),

    CREATE_GROUP_REQUEST(4),

    LIST_GROUP_MEMBERS_REQUEST(5),

    JOIN_GROUP_REQUEST(6),

    QUIT_GROUP_REQUEST(7),

    GROUP_MESSAGE_REQUEST(8),

    HEARTBEAT_REQUEST(9),

    LOGIN_RESPONSE(-1),

    MESSAGE_RESPONSE(-2),

    LOGIN_OUT_RESPONSE(-3),

    CREATE_GROUP_RESPONSE(-4),

    LIST_GROUP_MEMBERS_RESPONSE(-5),

    JOIN_GROUP_RESPONSE(-6),

    QUIT_GROUP_RESPONSE(-7),

    GROUP_MESSAGE_RESPONSE(-8),

    HEARTBEAT_RESPONSE(-9);

    private int value;

    Command(int value) {
        this.value = value;
    }
}
