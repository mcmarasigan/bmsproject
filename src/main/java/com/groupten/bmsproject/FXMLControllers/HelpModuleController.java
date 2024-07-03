package com.groupten.bmsproject.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;

import java.io.File;
import java.io.IOException;

@Component
public class HelpModuleController {

    @FXML
    private void openUserManual(ActionEvent event) {
        // Path to the UserManual.pdf
        String pdfFilePath = "src/main/resources/UserManual.pdf";

        // Get the file
        File pdfFile = new File(pdfFilePath);

        if (pdfFile.exists()) {
            try {
                // Open the PDF file using the default system viewer
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    new ProcessBuilder("cmd", "/c", pdfFile.getAbsolutePath()).start();
                } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    new ProcessBuilder("open", pdfFile.getAbsolutePath()).start();
                } else if (System.getProperty("os.name").toLowerCase().contains("nix") ||
                           System.getProperty("os.name").toLowerCase().contains("nux")) {
                    new ProcessBuilder("xdg-open", pdfFile.getAbsolutePath()).start();
                } else {
                    System.out.println("Unsupported operating system.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist: " + pdfFilePath);
        }
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
    @FXML
    private void proceedtoFAQs() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FAQ.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
