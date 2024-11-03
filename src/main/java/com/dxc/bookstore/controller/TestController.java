package com.dxc.bookstore.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {
    @GetMapping("/")
    public String defaultMessage() {
        return "Server setup successful!";
    }
    
}
