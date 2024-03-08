package com.groupten.bmsproject.OTP;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class OTPService {
    @Autowired
    private OTPRepository otprepository;

    @PostMapping(path="/add")
    public @ResponseBody String addOTP (@RequestParam String email, @RequestParam String otp, @RequestParam LocalDateTime expiryTime) {
        OTPEntity m = new OTPEntity();
        m.setEmail(email);
        m.setOTP(otp);
        m.setExpirytime(expiryTime);
        otprepository.save(m);
        return "Saved";
    }
}
