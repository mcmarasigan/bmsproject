package com.groupten.bmsproject.OTP;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OTPEntity {
    @Id
    private String email;
    private String otp;
    private LocalDateTime expiryTime;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOTP() {
        return otp;
    }

    public void setOTP(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpirytime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}
