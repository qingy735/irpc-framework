package org.qingy.irpc.framefork.core.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: QingY
 * @Date: Created in 21:50 2024-04-22
 * @Description:
 */
public class NioSocketServer extends Thread {

    ServerSocketChannel serverSocketChannel = null;
    Selector selector = null;
    SelectionKey selectionKey = null;

    private void initServer() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞模式，默认serverSocketChannel采用阻塞模式
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 默认堵塞
                int selectKey = selector.select();
                if (selectKey > 0) {
                    // 获取到所有处于就绪状态的channel，selectKey中包含了channel的信息
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectionKeys.iterator();
                    // 对selectKey进行遍历
                    while (iter.hasNext()) {
                        SelectionKey selectionKey = iter.next();
                        // 清空 防止下次重复处理
                        iter.remove();
                        // 就绪事件 处理连接
                        if (selectionKey.isAcceptable()) {
                            accept(selectionKey);
                        }
                        // 读事件 处理数据读取
                        if (selectionKey.isReadable()) {
                            read(selectionKey);
                        }
                        // 写事件 处理写数据
                        if (selectionKey.isWritable()) {

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void read(SelectionKey key) {
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int len = channel.read(buf);
            if (len > 0) {
                // 切换为读模式
                buf.flip();
                byte[] bytes = new byte[buf.limit()];
                buf.get(bytes);
                System.out.println("NioSocketServer receive from client:" + new String(bytes, 0, len));
                // key.interestOps(SelectionKey.OP_READ);
            }
        } catch (IOException e) {
            try {
                serverSocketChannel.close();
                selectionKey.cancel();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel accept = channel.accept();
            System.out.println("connection is acceptable...");
            accept.configureBlocking(false);
            // 将当前channel交给selector对象监管 并且selector对象管理它的读事件
            accept.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        NioSocketServer server = new NioSocketServer();
        server.initServer();
        server.start();
    }
}
