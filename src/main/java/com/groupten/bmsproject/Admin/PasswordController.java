package com.groupten.bmsproject.Admin;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class PasswordController {
    
    @Autowired PasswordService passwordService;

    @PostMapping(path = "/updatePassword")
    public String updatePassword(@RequestParam String email, @RequestParam String password) {
        //TODO: process POST request
        
        return passwordService.updatePassword(email, password);
    }
    
}
