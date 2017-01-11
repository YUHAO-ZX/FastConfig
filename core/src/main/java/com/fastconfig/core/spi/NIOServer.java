package com.fastconfig.core.spi;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by xinz on 2017/1/10.
 */
public class NIOServer {
    private static final int port = 8908;
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    RequestsConsumer consumer ;
    private BlockingDeque requestQueue = new LinkedBlockingDeque();
    public static void main(String[] args) {
        NIOServer server = new NIOServer();
        server.init();
    }

    public void init(){
        consumer = new RequestsConsumer(requestQueue);
        consumer.start();

        /*
        1.建立NIO服务
         */
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverChannel.socket();
            Selector selector = Selector.open();
            serverSocket.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(true){
                int n = selector.select();
                if (n == 0) {
                    continue; // nothing to do
                }
                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    // Is a new connection coming in?
                    if (key.isAcceptable()) {
                        ServerSocketChannel server =
                                (ServerSocketChannel) key.channel();
                        SocketChannel channel = server.accept();
                        registerChannel(selector, channel,
                                SelectionKey.OP_READ);
                        sayHello(channel);
                    }
                    // Is there data to read on this channel?
                    if (key.isReadable()) {
                        readDataFromSocket(key);
                    }
                    // Remove key from selected set; it's been handled
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void registerChannel(Selector selector,
                                   SelectableChannel channel, int ops) throws Exception {
        if (channel == null) {
            return; // could happen
        }
        // Set the new channel nonblocking
        channel.configureBlocking(false);
        // Register it with the selector
        channel.register(selector, ops);
    }

    protected void readDataFromSocket(SelectionKey key) throws Exception {
        requestQueue.add(key);
        consumer.notifyOne();
    }

    private void sayHello(SocketChannel channel) throws Exception {
        buffer.clear();
        buffer.put("Hi there!\r\n".getBytes());
        buffer.flip();
        channel.write(buffer);
    }

}
