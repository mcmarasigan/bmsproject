package com.groupten.bmsproject.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class AdminService {
    @Autowired
    private Adminrepository adminrepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewAdmin (@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        Adminentity n = new Adminentity();
        n.setuserName(username);
        n.setEmail(email);
        n.setPassword(password);
        adminrepository.save(n);
        return "Saved";
    }
}

