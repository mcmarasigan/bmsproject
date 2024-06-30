package com.groupten.bmsproject.Admin;

import java.util.List;

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

    public String getLoggedInUserEmail() {
        Adminentity admin = adminrepository.findByUsername(loggedInUser);
        return admin != null ? admin.getEmail() : "unknown";
    }

    // Add this method to get an account by username
    public Adminentity getAccountByUsername(String username) {
        return adminrepository.findByUsername(username);
    }

    // Retrieves data for the table
    public List<Adminentity> getAllAccounts() {
        return adminrepository.findAll();
    }

    // Activate and Deactivate accounts
    public void deactivateAccount(Adminentity admin) {
        admin.setStatus("deactivated");
        adminrepository.save(admin);
    }

    public void reactivateAccount(Adminentity admin) {
        admin.setStatus("active");
        adminrepository.save(admin);
    }
}
