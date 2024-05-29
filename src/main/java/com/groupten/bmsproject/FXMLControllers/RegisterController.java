package com.groupten.bmsproject.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Admin.AdminService;
import com.groupten.bmsproject.OTP.EmailService;
import com.groupten.bmsproject.OTP.OTPGenerator;
import com.groupten.bmsproject.OTP.OTPService;

@Component
public class RegisterController {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private final Pattern emailpattern = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z]).{8,}$";
    private final Pattern passwordpattern = Pattern.compile(PASSWORD_REGEX);

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private TextField unameField;

    @FXML
    private TextField otpField;

    @FXML
    private Button otpButton;

    // Autowire the adminService directly
    @Autowired
    private AdminService adminService;

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

        if (!isEmailValid(email)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid E-mail format.");
            alert.showAndWait();
        }

        if (!isPasswordValid(password)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Password should have atleast eight characters and one uppercase letter.");
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
        // Call the adminService method to add a new admin
        String result = adminService.addNewAdmin(username, email, password);
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

    private boolean isEmailValid(String email){
        Matcher matcher = emailpattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password){
        Matcher matcher = passwordpattern.matcher(password);
        return matcher.matches();
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

    @FXML
    private void handleBackbtn(ActionEvent event) throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    
}
