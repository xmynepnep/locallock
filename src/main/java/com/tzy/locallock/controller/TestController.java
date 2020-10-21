package com.tzy.locallock.controller;

import com.tzy.locallock.annotation.LocalLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${server.port}")
    String port;

    @RequestMapping(value="test")
    @LocalLock
    public String test(){
        return " i am " + port;
    }

}
