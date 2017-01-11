package com.fastconfig.core.spi;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by xinz on 2017/1/11.
 */
public class ThreadWorker extends Thread {
    private RequestHandler requestHandler;
    private SelectionKey key;
    private ThreadPool threadPool;
    public ThreadWorker(RequestHandler requestHandler,ThreadPool threadPool){
        this.requestHandler = requestHandler;
        this.threadPool = threadPool;
    }

    public synchronized void run() {
            while(true){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(key == null){
                    continue;
                }
//                System.out.println(this.getName() + " has been awakened");

                try {
                    requestHandler.readDataFromSocket(key);
                } catch (IOException e) {
                    try {
                        key.channel().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                key = null;
                threadPool.returnWorker(this);
            }
    }

    public synchronized void server(SelectionKey selectionKey){
        this.key = selectionKey;
        key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
        this.notify(); // Awaken the thread
    }
}
