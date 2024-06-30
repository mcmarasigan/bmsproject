package com.groupten.bmsproject.FXMLControllers;

import com.groupten.bmsproject.Admin.Adminentity;
import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Admin.AdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
public class AccountManagementController {

    @FXML
    private TableView<Adminentity> accountTable;

    @FXML
    private TableColumn<Adminentity, String> emailColumn;

    @FXML
    private TableColumn<Adminentity, String> firstNameColumn;

    @FXML
    private TableColumn<Adminentity, String> lastNameColumn;

    @FXML
    private TableColumn<Adminentity, String> usernameColumn;

    @FXML
    private TableColumn<Adminentity, String> statusColumn;

    @FXML
    private Button backToDashboardButton;

    @FXML
    private Button deactivateButton;

    @FXML
    private Button reactivateButton;

    @FXML
    private Button editButton;

    @FXML
    private Text accountManagementText;

    private final AdminService adminService;
    private ObservableList<Adminentity> accountData = FXCollections.observableArrayList();

    @Autowired
    public AccountManagementController(AdminService adminService) {
        this.adminService = adminService;
    }

    @FXML
    private void initialize() {
        initializeTableColumns();
        loadAccountData();
    }

    private void initializeTableColumns() {
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAccountData() {
        List<Adminentity> accounts = adminService.getAllAccounts();
        accountData.setAll(accounts);
        accountTable.setItems(accountData);
    }

    @FXML
    private void backToDashboard() {
        // Implementation to go back to the dashboard
    }

    @FXML
    private void handleDeactivateButton() {
        Adminentity selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            adminService.deactivateAccount(selectedAccount);
            loadAccountData(); // Refresh the table view
        } else {
            showAlert("No Selection", "Please select an account to deactivate.");
        }
    }

    @FXML
    private void handleReactivateButton() {
        Adminentity selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            adminService.reactivateAccount(selectedAccount);
            loadAccountData(); // Refresh the table view
        } else {
            showAlert("No Selection", "Please select an account to reactivate.");
        }
    }

    @FXML
    private void handleEditButton() {
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
