package com.groupten.bmsproject.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping(path = "/addAdmin")
    public String addNewAdmin(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        return adminService.addNewAdmin(username, email, password);
    }
}
