package com.fastconfig.core.spi;

import java.nio.channels.SelectionKey;
import java.util.concurrent.BlockingDeque;

/**
 * Created by xinz on 2017/1/11.
 */
public class RequestsConsumer {
    private ConsumerThread consumerThread;
    public RequestsConsumer(BlockingDeque<SelectionKey> requestQueue){
        consumerThread = new ConsumerThread(requestQueue);
    }
    public void notifyOne(){
        synchronized (consumerThread){
            consumerThread.notify();
        }
    }
    class ConsumerThread extends Thread{
        public BlockingDeque<SelectionKey> requestQueue;
        private ThreadPool pool = new ThreadPool(100);
        public ConsumerThread(BlockingDeque<SelectionKey> requestQueue){
            this.requestQueue = requestQueue;
        }
        @Override
        public synchronized void run() {
            while(true){
                ThreadWorker worker = null;
                try {
                    SelectionKey key = requestQueue.poll();
                    if(null == key){
                        this.wait();
                        continue;
                    }
                    worker = pool.getWorker();
                    worker.server(key);

                }catch (Throwable throwable){
                    throwable.printStackTrace();
                }finally {
                    if(null != worker){
                        pool.returnWorker(worker);
                    }
                }
            }
        }
    }
    public void start(){
        consumerThread.start();
    }
}
