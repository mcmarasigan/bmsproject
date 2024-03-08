package com.groupten.bmsproject.OTP;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OTPGenerator {
    @Autowired
    private OTPRepository otpRepository;
    
    public static String generatedOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
