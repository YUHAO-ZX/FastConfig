package com.fastconfig.core;

/**
 * Created by xinz on 2017/1/10.
 */
public interface NIOServerHandler {
    public void connect();
    public void recieveMsg();
    public void sendMsg();
    public void recieveHeartBeat();
    public void closeConnect();
}
