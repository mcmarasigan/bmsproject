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
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Admin.AdminService;
import com.groupten.bmsproject.OTP.EmailService;
import com.groupten.bmsproject.OTP.OTPGenerator;
import com.groupten.bmsproject.OTP.OTPService;

@Component
public class RegisterController {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z]).{8,}$";

    private final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmpassField;
    @FXML
    private TextField unameField;
    @FXML
    private TextField fnameField;
    @FXML
    private TextField lnameField;
    @FXML
    private TextField otpField;
    @FXML
    private Button otpButton;
    @FXML
    private Button submitButton;
    @FXML
    private ToggleButton showpassBtn;
    @FXML
    private TextField passtxtField;
    @FXML
    private TextField passtxtField2;

    @Autowired
    private AdminService adminService;
    @Autowired
    private OTPService otpService;
    @Autowired
    private OTPGenerator otpGenerator;
    @Autowired
    private EmailService emailSender;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @FXML
    private void initialize() {
        // Bind the TextField's text property to the PasswordField's text property
        passtxtField.textProperty().bindBidirectional(passwordField.textProperty());
        passtxtField2.textProperty().bindBidirectional(confirmpassField.textProperty());
    }

    @FXML
    private void handleSubmitButton() {
        String email = emailField.getText();
        String username = unameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmpassField.getText();
        String otp = otpField.getText();
        String firstName = fnameField.getText();
        String lastName = lnameField.getText();

        if (!validateInput(email, username, password, confirmPassword, otp, firstName, lastName)) return;

        if (!verifyOtp(email, otp)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid OTP.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String result = adminService.addNewAdmin(firstName, lastName, username, email, hashedPassword);
        showAlert(Alert.AlertType.INFORMATION, "Success", result);
    }

    private boolean validateInput(String email, String username, String password, String confirmPassword, String otp, String firstName, String lastName) {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || otp.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill the corresponding fields.");
            return false;
        }

        if (!isEmailValid(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid E-mail format.");
            return false;
        }

        if (!isPasswordValid(password)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password should have at least eight characters and one uppercase letter.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return false;
        }

        return true;
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
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
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
        String username = unameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (!validateOtpRequest(username, email, password)) return;

        String otp = otpGenerator.generatedOTP();
        LocalDateTime time = LocalDateTime.now();

        String result = otpService.addOTP(email, otp, time);
        emailSender.sendVerificationEmail(email, "OTP Verification Code", "", otp);
        showAlert(Alert.AlertType.INFORMATION, "Success", "OTP sent to your email.");
    }

    private boolean validateOtpRequest(String username, String email, String password) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill the corresponding fields.");
            return false;
        }
        return true;
    }

    @FXML
    private void showPassword () {
        if (showpassBtn.isSelected()) {
            passtxtField.setVisible(true);
            passtxtField2.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passtxtField.setVisible(false);
            passtxtField2.setVisible(false);
            passwordField.setVisible(true);
        }
    }

    @FXML
    private void handleBackbtn(ActionEvent event) throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
