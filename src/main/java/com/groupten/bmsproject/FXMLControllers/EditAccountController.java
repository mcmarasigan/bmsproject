package com.groupten.bmsproject.FXMLControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.Admin.Adminentity;
import com.groupten.bmsproject.Admin.AdminService;

@Controller
public class EditAccountController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField unameField;

    @FXML
    private TextField fnameField;

    @FXML
    private TextField lnameField;

    private Adminentity selectedAccount;

    @Autowired
    private AdminService adminService;

    @FXML
    private void initialize() {
        // This will be called when the FXML file is loaded
    }

    public void setSelectedAccount(Adminentity account) {
        this.selectedAccount = account;
        if (account != null) {
            emailField.setText(account.getEmail());
            unameField.setText(account.getuserName());
            fnameField.setText(account.getfirstName());
            lnameField.setText(account.getlastName());
        }
    }

    @FXML
    private void handleGoBackbtn() {
        // Close the edit account stage
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSaveButton() {
        // Save logic here
        if (selectedAccount != null) {
            selectedAccount.setEmail(emailField.getText());
            selectedAccount.setuserName(unameField.getText());
            selectedAccount.setfirstName(fnameField.getText());
            selectedAccount.setlastName(lnameField.getText());
            // Call service to save updated account information
            adminService.save(selectedAccount);
            handleGoBackbtn();
        }
    }
}
