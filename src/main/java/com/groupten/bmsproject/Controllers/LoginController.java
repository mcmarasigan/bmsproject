package com.groupten.bmsproject.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

@Component
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        } else {
            showAlert("Invalid email or password");
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
        String sql = "SELECT * FROM adminentity WHERE email = ? AND password = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, email, password);
        return !rows.isEmpty();
    }

    @FXML
    private void handleForgotpassbtn(ActionEvent event) throws IOException {
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forgotpassword.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Set the new scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) throws IOException {
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Register.fxml"));
        Parent root = loader.load();

        // Get the current stage
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Set the new scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
