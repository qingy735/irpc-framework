package org.qingy.irpc.framefork.core.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: QingY
 * @Date: Created in 20:51 2024-04-24
 * @Description:
 */
public class Server {
    private static EventLoopGroup bossGroup = null;
    private static EventLoopGroup workerGroup = null;

    public static void main(String[] args) throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                System.out.println("初始化连接通道信息,编解码处理器：定长处理器");
                socketChannel.pipeline().addLast(new StringEncoder());
                // ch.pipeline().addLast(new FixedLengthFrameDecoder(3));
                // 指定通过回车换行符来识别每次发送的数据，但是一旦当文本数据超过类maxLength就会抛出异常
                // ch.pipeline().addLast(new LineBasedFrameDecoder(5));
                // 指定特殊符号的分割处理
                // ch.pipeline().addLast(new DelimiterBasedFrameDecoder(50, Unpooled.copiedBuffer("[end]".getBytes())));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new ServerHandler());
            }
        });

        serverBootstrap.bind(9090).sync();
        System.out.println("server is open");

    }
}
