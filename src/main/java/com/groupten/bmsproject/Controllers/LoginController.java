package com.groupten.bmsproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.Admin.Admincontroller;

@Component
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private TextField unameField;

    // Autowire the Admincontroller directly
    @Autowired
    private Admincontroller adminController;

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
}
