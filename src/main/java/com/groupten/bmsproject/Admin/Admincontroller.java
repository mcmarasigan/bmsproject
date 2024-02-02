package com.groupten.bmsproject.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class Admincontroller {
    @Autowired
    private Adminrepository adminrepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewAdmin (@RequestParam String name, @RequestParam String email) {
        Adminentity n = new Adminentity();
        n.setName(name);
        n.setEmail(email);
        adminrepository.save(n);
        return "Saved";
    }
}
