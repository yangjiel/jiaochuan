package com.jiaochuan.hazakura.api.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/user")
public class UserController {

    @GetMapping("/")
    public void createUser(){
    }
}
