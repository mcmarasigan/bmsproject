package com.groupten.bmsproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import org.springframework.jdbc.core.JdbcTemplate;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // Method to handle the submit button action
    @FXML
    private void handleSubmitButton() {
        String email = emailField.getText();
        String username = unameField.getText();
        String password = passwordField.getText();
        String otp = otpField.getText();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || otp.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the corresponding fields.");
            alert.showAndWait();
        }

        // Call a method to verify the OTP
        boolean isOtpValid = verifyOtp(email, otp);

        if (!isOtpValid){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid OTP.");
            alert.showAndWait();
        }
        
        else {
        // Call the Admincontroller method to add a new admin
        String result = adminController.addNewAdmin(username, email, password);
        // You can handle the result as needed, e.g., display a message
        System.out.println(result);
        }
    }

    private boolean verifyOtp(String email, String otp) {
        // Implement code to verify OTP against the email in your database (MySQL)
        // Return true if OTP is valid for the email, otherwise return false
        String sql = "SELECT otp FROM otpentity WHERE email = ?";
        String storedOtp = jdbcTemplate.queryForObject(sql, String.class, email);
        return otp.equals(storedOtp);
    }

    @FXML
    private void handleOTPButton () {
        String username = unameField.getText();
        String email = emailField.getText();
        String pasword = passwordField.getText();
        String otp = otpGenerator.generatedOTP();
        LocalDateTime time = LocalDateTime.now();

        if (username.isEmpty() || email.isEmpty() || pasword.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the corresponding fields.");
            alert.showAndWait();
        } else {

        String result = otpService.addOTP(email, otp, time);
        eMailSender.sendVerificationEmail(email,"OTP Verification Code","",otp);

        System.out.println(result);
        }
    }
}
