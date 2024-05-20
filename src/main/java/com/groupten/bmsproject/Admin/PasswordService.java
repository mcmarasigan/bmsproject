package com.groupten.bmsproject.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class PasswordService {
    @Autowired
    private Adminrepository adminrepository;

    @PostMapping(path="/updatePassword")
    public @ResponseBody String updatePassword (@RequestParam String email, @RequestParam String password) {
        Adminentity admin = adminrepository.findByEmail(email);
        if (admin == null) {
            return "Admin with the specified email does not exist";
        }
        admin.setPassword(password);
        adminrepository.save(admin);
        return "Password updated successfully";
    }
}
