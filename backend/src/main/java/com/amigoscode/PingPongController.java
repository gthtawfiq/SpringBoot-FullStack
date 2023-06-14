package com.amigoscode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    record Pingpong(String result){}

    @GetMapping("/ping")
    public Pingpong getPingpong(){
        return new Pingpong("Pong");
    }



}