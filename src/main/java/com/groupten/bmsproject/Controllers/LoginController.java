package com.groupten.bmsproject.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
}
