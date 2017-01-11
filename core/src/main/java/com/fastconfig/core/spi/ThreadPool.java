package com.fastconfig.core.spi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xinz on 2017/1/11.
 */
public class ThreadPool {
    List<ThreadWorker> workers = new LinkedList<ThreadWorker>();
    public ThreadPool(int size){
        for(int i=0;i<size;i++){
            ThreadWorker worker = new ThreadWorker(new RequestHandler(),this);
            worker.setName("worker "+i);
            workers.add(worker);
            worker.start();
        }
    }

    public ThreadWorker getWorker() throws InterruptedException {
        synchronized (workers){
            ThreadWorker worker = null;
            while(worker == null){
                if(workers.size() > 0){
                    worker = workers.remove(0);
                    break;
                }
                workers.wait();
            }
            return worker;
        }
    }

    public void returnWorker(ThreadWorker worker){
        synchronized (workers){
            workers.add(worker);
            workers.notify();
        }
    }
}
