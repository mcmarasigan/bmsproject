package com.groupten.bmsproject.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private String loggedInUser; // Example storage, replace with your actual implementation

    @Autowired
    private Adminrepository adminrepository;

    public String addNewAdmin(String firstname, String lastname, String username, String email, String password) {
        Adminentity existingAdmin = adminrepository.findByEmail(email);
        if (existingAdmin != null) {
            return "Admin with this email already exists!";
        }
        Adminentity newAdmin = new Adminentity();
        newAdmin.setfirstName(firstname);
        newAdmin.setlastName(lastname);
        newAdmin.setuserName(username);
        newAdmin.setEmail(email);
        newAdmin.setPassword(password);
        adminrepository.save(newAdmin);
        return "Saved";
    }

    // Gets the username of the logged in user
    public void setLoggedInUser(String username) {
        this.loggedInUser = username;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }
}
