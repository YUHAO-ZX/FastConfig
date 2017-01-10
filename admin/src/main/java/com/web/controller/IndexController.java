package com.web.controller;

import com.web.tools.JsonBack;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;
/**
 * Created by xinz on 2017/1/10.
 */
@Controller
@RequestMapping("/index1")
public class IndexController implements InitializingBean{
    @RequestMapping("/hello")
    @ResponseBody
    public void printHello(HttpServletRequest request, HttpServletResponse response) {
        new JsonBack(response).put("hello","world").send();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("finish init()");
    }
}
