package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Admin.AdminService;
import com.groupten.bmsproject.Admin.Adminentity;
import com.groupten.bmsproject.SecurityLogs.SecurityLogs;
import com.groupten.bmsproject.SecurityLogs.SecurityLogsRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

@Component
public class LoginController {
    @Autowired
    private AdminService adminService;

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private int failedLoginAttempts = 0;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ToggleButton showpassBtn;

    @FXML
    private TextField passtxtField;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SecurityLogsRepository securityLogsRepository;

    @Autowired
    private ForgotpassController forgotpassController;

    @FXML
    private void initialize() {
        passtxtField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    @FXML
    private void handleLoginButton() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Please fill the corresponding fields.");
            return;
        } 
        
        if (isValidCredentials(email, password)) {
            String username = getUsernameByEmail(email);
            if (username != null) {
                adminService.setLoggedInUser(username); // Store the username
                logSecurityEvent(username, username + " has logged in the system");
                System.out.println("Login Succeed");
                try {
                    failedLoginAttempts = 0; // Reset the counter on successful login
                    proceedtoDashboard();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Unable to retrieve username.");
            }
        } else {
            failedLoginAttempts++;
            if (failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
                suggestForgotPassword();
            } else {
                showAlert("Invalid email or password");
            }
        }
    }

    private void logSecurityEvent(String username, String activityLog) {
        SecurityLogs securityLog = new SecurityLogs(username, activityLog, LocalDateTime.now());
        securityLogsRepository.save(securityLog);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidCredentials(String email, String password) {
        String sql = "SELECT password, status FROM adminentity WHERE email = ?";
        try {
            Adminentity admin = jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                Adminentity a = new Adminentity();
                a.setPassword(rs.getString("password"));
                a.setStatus(rs.getString("status"));
                return a;
            });

            if (admin != null) {
                if ("deactivated".equals(admin.getStatus())) {
                    showAlert("Your account is deactivated. Please contact support.");
                    return false;
                }
                return BCrypt.checkpw(password, admin.getPassword());
            }
        } catch (Exception e) {
            // Handle exception, e.g., user not found
            return false;
        }
        return false;
    }

    private String getUsernameByEmail(String email) {
        String sql = "SELECT username FROM adminentity WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, String.class);
        } catch (Exception e) {
            // Handle exception, e.g., user not found
            return null;
        }
    }

    @FXML
    private void showPassword() {
        if (showpassBtn.isSelected()) {
            passtxtField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passtxtField.setVisible(false);
            passwordField.setVisible(true);
        }
    }

    @FXML
    private void handleForgotpassbtn(ActionEvent event) throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forgotpassword.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRegisterbtn(ActionEvent event) throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminRegister.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void proceedtoDashboard() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void suggestForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Login Failed");
        alert.setHeaderText(null);
        alert.setContentText("You have failed to login 3 times. Would you like to reset your password?");

        ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                try {
                    ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forgotpassword.fxml"));
                    loader.setControllerFactory(context::getBean);

                    Parent root = loader.load();
                    Stage stage = BmsprojectApplication.getPrimaryStage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    failedLoginAttempts = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
