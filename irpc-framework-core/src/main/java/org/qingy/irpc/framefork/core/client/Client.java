package org.qingy.irpc.framefork.core.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.qingy.irpc.framefork.core.common.RpcDecoder;
import org.qingy.irpc.framefork.core.common.RpcEncoder;
import org.qingy.irpc.framefork.core.common.RpcInvocation;
import org.qingy.irpc.framefork.core.common.RpcProtocol;
import org.qingy.irpc.framefork.core.common.config.ClientConfig;
import org.qingy.irpc.framefork.core.proxy.jdk.JDKProxyFactory;
import org.qingy.irpc.framefork.interfaces.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.qingy.irpc.framefork.core.common.cache.CommonClientCache.RESP_MAP;
import static org.qingy.irpc.framefork.core.common.cache.CommonClientCache.SEND_QUEUE;

/**
 * @Author: QingY
 * @Date: Created in 22:13 2024-04-24
 * @Description:
 */
public class Client {
    private Logger logger = LoggerFactory.getLogger(Client.class);

    private static EventLoopGroup clientGroup = null;
    private ClientConfig clientConfig;

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcReference startClientApplication() throws InterruptedException {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                // 管道中初始化一些逻辑，这里包含了上边所说的编解码器和客户端响应类
                socketChannel.pipeline().addLast(new RpcEncoder());
                socketChannel.pipeline().addLast(new RpcDecoder());
                socketChannel.pipeline().addLast(new ClientHandler());
            }
        });
        // 常规的链接netty服务端
        ChannelFuture channelFuture = bootstrap.connect(clientConfig.getServerAddr(), clientConfig.getPort()).sync();
        logger.info("============ 服务启动 ============");
        this.startClient(channelFuture);
        // 注入一个代理工厂
        RpcReference rpcReference = new RpcReference(new JDKProxyFactory());
        return rpcReference;
    }

    /**
     * 开启发送线程，专门从事将数据包发送给服务端，起到一个解耦的效果
     *
     * @param channelFuture
     */
    private void startClient(ChannelFuture channelFuture) {
        Thread asyncSendJob = new Thread(new AsyncSendJob(channelFuture));
        asyncSendJob.start();
    }

    class AsyncSendJob implements Runnable {
        private ChannelFuture channelFuture;

        public AsyncSendJob(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞模式
                    RpcInvocation data = SEND_QUEUE.take();
                    // 将RpcInvocation封装到RpcProtocol对象中，然后发送给服务端，这里正好对应了上文中的ServerHandler
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        ClientConfig config = new ClientConfig();
        config.setPort(9090);
        config.setServerAddr("localhost");
        client.setClientConfig(config);
        RpcReference rpcReference = client.startClientApplication();
        DataService dataService = rpcReference.get(DataService.class);
        for (int i = 0; i < 3; i++) {
            System.out.println(System.currentTimeMillis());
            String result = dataService.sendData("test");
            System.out.println(result);
        }
        for (String key : RESP_MAP.keySet()) {
            System.out.println("key：" + key + ", value: " + RESP_MAP.get(key));
        }
    }

}
