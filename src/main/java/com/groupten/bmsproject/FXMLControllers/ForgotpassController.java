package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Admin.AdminService;
import com.groupten.bmsproject.Admin.Adminrepository;
import com.groupten.bmsproject.Admin.PasswordService;
import com.groupten.bmsproject.OTP.EmailService;
import com.groupten.bmsproject.OTP.OTPGenerator;
import com.groupten.bmsproject.OTP.OTPService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

@Component
public class ForgotpassController {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private final Pattern emailpattern = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z]).{8,}$";
    private final Pattern passwordpattern = Pattern.compile(PASSWORD_REGEX);

    @FXML
    private TextField emailField;

    @FXML
    private TextField otpField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmpassField;

    @FXML
    private Button otpButton;

    @FXML
    private Button submitButton;

    @FXML
    private Button backButton;

    @FXML
    private ToggleButton showpassBtn;

    @FXML
    private TextField passtxtField1;
    
    @FXML
    private TextField passtxtField2;

    @Autowired
    private AdminService adminService;

    @Autowired
    private Adminrepository adminRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private OTPGenerator otpGenerator;

    @Autowired
    private EmailService eMailSender;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @FXML
    private void initialize() {
        passtxtField1.textProperty().bindBidirectional(passwordField.textProperty());
        passtxtField2.textProperty().bindBidirectional(confirmpassField.textProperty());
    }

    @FXML
    private void handleSubmitButton() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmedpass = confirmpassField.getText();
        String otp = otpField.getText();

        if (email.isEmpty() || password.isEmpty() || confirmedpass.isEmpty() || otp.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all the fields.");
            return;
        }

        if (!isEmailValid(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid E-mail format.");
            return;
        }

        if (!isPasswordValid(password)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password should have at least eight characters and one uppercase letter.");
            return;
        }

        if (!isPasswordSame(password, confirmedpass)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }

        boolean isOtpValid = verifyOtp(email, otp);

        if (!isOtpValid) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid OTP.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String result = passwordService.updatePassword(email, hashedPassword);
        showAlert(Alert.AlertType.INFORMATION, "Success", result);
    }

    private boolean verifyOtp(String email, String otp) {
        String sql = "SELECT otp FROM otpentity WHERE email = ?";
        try {
            String storedOtp = jdbcTemplate.queryForObject(sql, String.class, email);
            return otp.equals(storedOtp);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isEmailValid(String email) {
        Matcher matcher = emailpattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        Matcher matcher = passwordpattern.matcher(password);
        return matcher.matches();
    }

    private boolean isPasswordSame(String password, String confirmedPassword) {
        return password.equals(confirmedPassword);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleOTPButton() {
        String email = emailField.getText();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter your email.");
            return;
        }

        String otp = otpGenerator.generatedOTP();
        LocalDateTime time = LocalDateTime.now();

        String result = otpService.addOTP(email, otp, time);
        eMailSender.sendVerificationEmail(email, "OTP Verification Code", "", otp);

        showAlert(Alert.AlertType.INFORMATION, "Success", "OTP sent to your email.");
    }

    @FXML
    private void showPassword() {
        if (showpassBtn.isSelected()) {
            passtxtField1.setVisible(true);
            passtxtField2.setVisible(true);
            passwordField.setVisible(false);
            confirmpassField.setVisible(false);
        } else {
            passtxtField1.setVisible(false);
            passtxtField2.setVisible(false);
            passwordField.setVisible(true);
            confirmpassField.setVisible(true);
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
