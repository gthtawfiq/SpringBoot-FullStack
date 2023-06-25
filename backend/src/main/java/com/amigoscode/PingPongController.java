package com.amigoscode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    private static int counter=0;
    record Pingpong(String result){}

    @GetMapping("/ping")
    public Pingpong getPingpong(){
        return new Pingpong("Pong "+ ++counter);// ++counter this to start with 1 at first then it will follow with 2,3,4...etc
                                                     // if we use counter++ the value of counter will start with 0 at first then increment with 1,2,3...etc
    }



}