package com.fastconfig.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by xinz on 2017/1/10.
 */
public class NIOClient {
    public SocketChannel init(String host,int port) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host,port));
        socketChannel.configureBlocking(false);
        System.out.println("connect finish");
        return socketChannel;
    }
    ByteBuffer buffer = ByteBuffer.allocate(20);
    public void sendMsg(String msg,SocketChannel channel){
        byte[] source = msg.getBytes();
        int i = 0;
        while (true){
            if(i >= source.length){
                return;
            }
            buffer.clear();
            while(buffer.hasRemaining()){
                if(i >= source.length){
                    break;
                }
                buffer.put(source[i++]);
            }
            try {

                if(buffer.position() == 0){
                    return;
                }
                buffer.flip();
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        NIOClient client = new NIOClient();
        try {
            SocketChannel channel = client.init("127.0.0.1",8908);
            while(true){
                if(!channel.isConnected()){
                    System.out.println("is not connected");
                    break;
                }
                client.sendMsg("hello world{}{}{}{}{{}}{}}{}{sdfsdfsdf阿士大夫阿士大夫阿士大夫",channel);
                Thread.sleep(5000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
