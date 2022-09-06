package com.dk.io.server;

import com.dk.io.client.MyClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dk.io.client.MyClient.getCharBuffer;

/**
 * @author dkay
 * @version 1.0
 */
public class MyServer {
    private final AtomicInteger clientCount = new AtomicInteger(0);
    private ByteBuffer byteBuffer;
    private final int READ_SIZE;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public MyServer(int readSize, int listenPort) {
        this.READ_SIZE = readSize;
        initServer(listenPort);
    }

    public void initServer(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
            serverSocketChannel.bind(inetSocketAddress);
            System.out.println("MyServer # 开始监听端口 port : " + port);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            byteBuffer = ByteBuffer.allocate(READ_SIZE);
            System.out.println("MyServer # 初始化结束");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() throws IOException {
        while (true) {
            try {
                int selectNum = selector.select();
                if (selectNum == 0) {
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Iterator<SelectionKey> iterator = selector.keys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 建立双向socket通讯
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel channel = server.accept();
                    if (Objects.nonNull(channel)) {
                        registerChannel(channel);
                        int currentClient = clientCount.incrementAndGet();
                        System.out.println("当前客户端数量 : " + currentClient);
                        write(channel, "你好, client.".getBytes());
                    }
                }
                if (selectionKey.isReadable()) {
                    SocketChannel readChannel = ((SocketChannel) selectionKey.channel());
                    read(readChannel);
                }
            }
        }
    }

    private void registerChannel(SocketChannel channel) throws IOException {
        if (Objects.nonNull(channel)) {
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        }
    }

    public void write(SocketChannel channel, byte[] data) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(data);
        byteBuffer.flip();
        channel.write(byteBuffer);
    }

    public void read(SocketChannel channel) throws IOException {
        byteBuffer.clear();
        StringBuilder builder = new StringBuilder();
        if (channel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                byteBuffer.asCharBuffer();
                CharBuffer decode = getCharBuffer(byteBuffer);
                builder.append(decode);
            }
            byteBuffer.clear();
        } else {
            channel.close();
            return;
        }
        System.out.println(builder);
    }


    public static void main(String[] args) throws IOException {
        new MyServer(10240, 9999).listen();
    }
}
