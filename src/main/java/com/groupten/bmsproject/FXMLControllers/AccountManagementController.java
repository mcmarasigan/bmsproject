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
    private void backToDashboard() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void backtoMaintenance() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Maintenance.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void changePassword() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangePassword.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
        Adminentity selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            try {
                ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditAccount.fxml"));
                loader.setControllerFactory(context::getBean);

                Parent root = loader.load();

                EditAccountController controller = loader.getController();
                controller.setSelectedAccount(selectedAccount);

                Stage stage = new Stage();
                stage.setTitle("Edit Account");
                stage.setScene(new Scene(root));
                stage.show();

                // After closing the edit window, refresh the table view
                stage.setOnHidden(event -> loadAccountData());

            } catch (IOException e) {
                showAlert("Error", "Failed to load Edit Account screen.");
            }
        } else {
            showAlert("No Selection", "Please select an account to edit.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
