package com.jiaochuan.hazakura.api.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/user")
public class UserController {

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createUser(){
        return ResponseEntity.ok().build();
    }
}
