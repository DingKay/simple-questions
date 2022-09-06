package com.dk.io.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author dkay
 * @version 1.0
 */
public class MyClient {
    private ByteBuffer byteBuffer;
    private final int READ_SIZE;
    private SocketChannel socketChannel;

    public MyClient(int maxSize, int port) {
        this.READ_SIZE = maxSize;
        init(port);
    }

    public void init(int port) {
        try {
            byteBuffer = ByteBuffer.allocate(READ_SIZE);
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", port));
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receive() throws IOException {
        while (true) {
            byteBuffer.clear();
            StringBuilder builder = new StringBuilder();
            while (socketChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    CharBuffer decode = getCharBuffer(byteBuffer);
                    builder.append(decode);
                }
                System.out.println("form server:" + builder);
                byteBuffer.clear();
                sendToServer("我是客户端".getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    public static CharBuffer getCharBuffer(ByteBuffer byteBuffer) throws CharacterCodingException {
        Charset charset = StandardCharsets.UTF_8;
        CharsetDecoder charsetDecoder = charset.newDecoder();
        return charsetDecoder.decode(byteBuffer);
    }

    public void sendToServer(byte[] data) {
        try {
            byteBuffer.clear();
            byteBuffer.put(data);
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new MyClient(10240, 9999).receive();
    }
}
