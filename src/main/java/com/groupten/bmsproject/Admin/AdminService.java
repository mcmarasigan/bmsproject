package com.groupten.bmsproject.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private Adminrepository adminrepository;

    public String addNewAdmin(String username, String email, String password) {
        Adminentity existingAdmin = adminrepository.findByEmail(email);
        if (existingAdmin != null) {
            return "Admin with this email already exists!";
        }
        Adminentity newAdmin = new Adminentity();
        newAdmin.setuserName(username);
        newAdmin.setEmail(email);
        newAdmin.setPassword(password);
        adminrepository.save(newAdmin);
        return "Saved";
    }
}
