package org.qingy.irpc.framefork.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.qingy.irpc.framefork.core.common.RpcDecoder;
import org.qingy.irpc.framefork.core.common.RpcEncoder;
import org.qingy.irpc.framefork.core.common.config.ServerConfig;

import static org.qingy.irpc.framefork.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;

/**
 * @Author: QingY
 * @Date: Created in 21:21 2024-04-24
 * @Description:
 */
public class Server {

    private static EventLoopGroup bossGroup = null;
    private static EventLoopGroup workerGroup = null;
    private ServerConfig serverConfig;

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void startApplication() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                System.out.println("初始化provider过程");
                socketChannel.pipeline().addLast(new RpcEncoder());
                socketChannel.pipeline().addLast(new RpcDecoder());
                socketChannel.pipeline().addLast(new ServerHandler());
            }
        });
        bootstrap.bind(serverConfig.getPort()).sync();
    }

    public void registyService(Object serviceBean) {
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces!");
        }
        Class<?>[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        Class<?> interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        ServerConfig config = new ServerConfig();
        config.setPort(9090);
        server.setServerConfig(config);
        server.registyService(new DataServiceImpl());
        server.startApplication();
    }
}
