package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;

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
    private ForgotpassController forgotpassController;

    @FXML
    private void initialize() {
        // Bind the TextField's text property to the PasswordField's text property
        passtxtField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    @FXML
    private void handleLoginButton(){
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()){
            showAlert("Please fill the corresponding fields.");
            return;
        } 
        
        if (isValidCredentials(email, password)){
            System.out.println("Login Succeed");
            try {
                failedLoginAttempts = 0; // Reset the counter on successful login
                proceedtoDashboard();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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

    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
    }

    private boolean isValidCredentials(String email, String password){
        String sql = "SELECT password FROM adminentity WHERE email = ?";
        try {
            String storedHashedPassword = jdbcTemplate.queryForObject(sql, new Object[]{email}, String.class);
            return BCrypt.checkpw(password, storedHashedPassword);
        } catch (Exception e) {
            // Handle exception, e.g., user not found
            return false;
        }
    }

    @FXML
    private void showPassword () {
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
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
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
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
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
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
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
                    ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forgotpassword.fxml"));
                    loader.setControllerFactory(context::getBean);

                    Parent root = loader.load();
                    Stage stage = BmsprojectApplication.getPrimaryStage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    failedLoginAttempts = 0;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    
}
