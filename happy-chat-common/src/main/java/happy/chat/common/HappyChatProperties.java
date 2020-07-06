package happy.chat.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置信息
 *
 * @author Terry
 */
@ConfigurationProperties(prefix = "happy.chat.server")
public class HappyChatProperties {

    /**
     * ip
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
