package happy.chat.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置信息
 *
 * @author Terry
 */
@ConfigurationProperties(prefix = "happy.chat")
public class HappyChatProperties {

    private Server server = new Server();

    private Client client = new Client();

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
        private String host = "127.0.0.1";

        /**
         * 端口
         */
        private int port = 8080;

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
        private int reconnectTime = 10;

        /**
         * 重连次数
         */
        private int reconnectNum = 3;

        /**
         * 心跳间隔,单位秒
         */
        private int heartbeatTime = 5;

        public int getReconnectTime() {
            return reconnectTime;
        }

        public void setReconnectTime(int reconnectTime) {
            this.reconnectTime = reconnectTime;
        }

        public int getReconnectNum() {
            return reconnectNum;
        }

        public void setReconnectNum(int reconnectNum) {
            this.reconnectNum = reconnectNum;
        }

        public int getHeartbeatTime() {
            return heartbeatTime;
        }

        public void setHeartbeatTime(int heartbeatTime) {
            this.heartbeatTime = heartbeatTime;
        }
    }
}
