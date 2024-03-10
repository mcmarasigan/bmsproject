package com.groupten.bmsproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.Admin.Admincontroller;
import com.groupten.bmsproject.OTP.EmailService;
import com.groupten.bmsproject.OTP.OTPGenerator;
import com.groupten.bmsproject.OTP.OTPService;

@Component
public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private TextField unameField;

    @FXML
    private TextField otpField;

    // Autowire the Admincontroller directly
    @Autowired
    private Admincontroller adminController;

    @Autowired
    private OTPService otpService;

    @Autowired
    private OTPGenerator otpGenerator;

    @Autowired
    private EmailService eMailSender;
    
    // Method to handle the submit button action
    @FXML
    private void handleSubmitButton() {
        String email = emailField.getText();
        String username = unameField.getText();
        // Call the Admincontroller method to add a new admin
        String result = adminController.addNewAdmin(username, email);

        // You can handle the result as needed, e.g., display a message
        System.out.println(result);
    }

    @FXML
    private void handleOTPButton () {
        String email = emailField.getText();
        String otp = otpGenerator.generatedOTP();
        LocalDateTime time = LocalDateTime.now();

        String result = otpService.addOTP(email, otp, time);

        eMailSender.sendVerificationEmail("","OTP Verification Code","",otp);

        System.out.println(result);
    }
}
