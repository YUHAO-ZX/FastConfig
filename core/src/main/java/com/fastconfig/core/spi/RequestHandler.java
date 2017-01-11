package com.fastconfig.core.spi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by xinz on 2017/1/11.
 */
public class RequestHandler {
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    static int count1 = 0;
    public void readDataFromSocket(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int count;
        buffer.clear();
        /** socketChannel.read(buffer) 多次write可能合并一起读入到buffer中**/
        while ((count = socketChannel.read(buffer)) > 0) {
            buffer.flip();
            byte[] continer = new byte[buffer.limit()];

            int i=0;
            while (buffer.hasRemaining()) {
                continer[i++] = buffer.get();
            }
            if(count1 ++ % 100 == 0){
                System.out.println("recieve finish:"+new String(continer)+" count:"+count1);
            }

            // WARNING: the above loop is evil. Because
            // it's writing back to the same nonblocking
            // channel it read the data from, this code can
            // potentially spin in a busy loop. In real life
            // you'd do something more useful than this.
            buffer.clear(); // Empty buffer
        }
        if (count < 0) {
        // Close channel on EOF, invalidates the key
            socketChannel.close();
        }

        //TODO
        // Resume interest in OP_READ
        key.interestOps(key.interestOps() | SelectionKey.OP_READ);
        // Cycle the selector so this key is active again
        key.selector().wakeup();
    }
}
