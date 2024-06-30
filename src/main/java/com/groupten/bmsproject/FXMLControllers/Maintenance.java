package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class Maintenance {
    @FXML
    private void proceedtoAccountManagement() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccountManagement.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void proceedtoSystemConfiguration() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SystemConfiguration.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void proceedtoSecurityLogs() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SecurityLogs.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void backtoDashboard() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
