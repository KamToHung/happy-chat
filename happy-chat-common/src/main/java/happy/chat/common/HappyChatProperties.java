package happy.chat.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置信息
 *
 * @author Terry
 */
@ConfigurationProperties(prefix = "happy.chat")
public class HappyChatProperties {

    private Server server;

    private Client client;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static class Server {

        /**
         * 主机
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

    public static class Client {

        /**
         * 重连时间间隔,单位秒
         */
        private int reconnectTime;

        /**
         * 重连次数
         */
        private int getReconnectNum;

        public int getReconnectTime() {
            return reconnectTime;
        }

        public void setReconnectTime(int reconnectTime) {
            this.reconnectTime = reconnectTime;
        }

        public int getGetReconnectNum() {
            return getReconnectNum;
        }

        public void setGetReconnectNum(int getReconnectNum) {
            this.getReconnectNum = getReconnectNum;
        }
    }
}
