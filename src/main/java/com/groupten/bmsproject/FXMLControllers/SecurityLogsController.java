package com.groupten.bmsproject.FXMLControllers;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.SecurityLogs.SecurityLogs;
import com.groupten.bmsproject.SecurityLogs.SecurityLogsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityLogsController {

    @FXML
    private TableView<SecurityLogs> securityLogsTable;

    @FXML
    private TableColumn<SecurityLogs, String> usernameColumn;

    @FXML
    private TableColumn<SecurityLogs, String> activitylogColumn;

    @FXML
    private TableColumn<SecurityLogs, String> activitytimestampColumn;

    private final SecurityLogsService securityLogsService;
    private final ObservableList<SecurityLogs> securityLogsList = FXCollections.observableArrayList();

    @Autowired
    public SecurityLogsController(SecurityLogsService securityLogsService) {
        this.securityLogsService = securityLogsService;
    }

    @FXML
    private void initialize() {
        // Initialize the table columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        activitylogColumn.setCellValueFactory(new PropertyValueFactory<>("activityLog"));
        activitytimestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        // Load data from the database
        loadSecurityLogs();
    }

    private void loadSecurityLogs() {
        securityLogsList.clear();
        List<SecurityLogs> logs = securityLogsService.getAllSecurityLogs();
        securityLogsList.addAll(logs);
        securityLogsTable.setItems(securityLogsList);
    }

    @FXML
    private void handleBackbtn(ActionEvent event) throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Maintenance.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
