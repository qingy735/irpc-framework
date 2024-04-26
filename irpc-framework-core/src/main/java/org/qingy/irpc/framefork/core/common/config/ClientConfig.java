package org.qingy.irpc.framefork.core.common.config;

/**
 * @Author: QingY
 * @Date: Created in 21:26 2024-04-24
 * @Description:
 */
public class ClientConfig {
    private Integer port;
    private String serverAddr;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
}
