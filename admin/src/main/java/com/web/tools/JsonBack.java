package com.web.tools;

/**
 * Created by xinz on 2017/1/10.
 */
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuhao.zx on 15-9-14.
 */
public class JsonBack {
    private HttpServletResponse response;
    private Map<String,Object> data = new HashMap<String, Object>();
    private Boolean isSuccess = true;
    private String errorMsg = "";
    private final String TITLE = "data";
    private final String SUCCESS = "suc";
    private final String MSG = "msg";
    private PrintWriter printWriter;
    public static void sendError(String errorMsg,HttpServletResponse response){
        new JsonBack(response).
                setIsSuccess(false).setErrorMsg(errorMsg).
                send();
    }
    public JsonBack(HttpServletResponse response){
        this.response =response;
        try {
            printWriter = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    public JsonBack put(String key,Object value){
        data.put(key,value);
        return this;
    }

    public JsonBack setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
        return this;
    }

    public JsonBack setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public void close(){
        printWriter.close();
    }

    public void send(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put(SUCCESS,isSuccess);
        map.put(MSG,errorMsg);
        map.put(TITLE,data);
        printWriter.print(JSONObject.toJSON(map));
        printWriter.flush();
        printWriter.close();
    }

    public void sendSyn(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put(SUCCESS,isSuccess);
        map.put(MSG,errorMsg);
        map.put(TITLE,data);
        printWriter.print(JSONObject.toJSON(map));
        printWriter.flush();
    }
}
