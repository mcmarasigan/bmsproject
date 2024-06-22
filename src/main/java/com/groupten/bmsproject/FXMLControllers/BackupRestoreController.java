package com.groupten.bmsproject.FXMLControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

@Controller
public class BackupRestoreController {

    @FXML
    private Button backupButton;

    @FXML
    private Button restoreButton;

    @FXML
    public void initialize() {
        backupButton.setOnAction(event -> backupDatabase());
        restoreButton.setOnAction(event -> restoreDatabase());
    }

    private void backupDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(
                    "C:\\Program Files\\MySQL\\MySQL Workbench 8.0\\mysqldump", // Full path to mysqldump
                    "-u", "root",
                    "-p" + "rootadmin",
                    "drewbakesbms",
                    "-r", file.getAbsolutePath()
                );
                Process process = processBuilder.start();
                int processComplete = process.waitFor();
                if (processComplete == 0) {
                    showAlert(AlertType.INFORMATION, "Backup Successful", "Backup created successfully.");
                } else {
                    showAlert(AlertType.ERROR, "Backup Failed", "Backup creation failed.");
                    printProcessOutput(process);
                }
            } catch (IOException | InterruptedException e) {
                showAlert(AlertType.ERROR, "Backup Error", "An error occurred during backup: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void restoreDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(
                    "C:\\Program Files\\MySQL\\MySQL Workbench 8.0\\mysql", // Full path to mysql
                    "-u", "root",
                    "-p" + "rootadmin",
                    "drewbakesbms",
                    "-e", "source " + file.getAbsolutePath()
                );
                Process process = processBuilder.start();
                int processComplete = process.waitFor();
                if (processComplete == 0) {
                    showAlert(AlertType.INFORMATION, "Restore Successful", "Restore completed successfully.");
                } else {
                    showAlert(AlertType.ERROR, "Restore Failed", "Restore failed.");
                    printProcessOutput(process);
                }
            } catch (IOException | InterruptedException e) {
                showAlert(AlertType.ERROR, "Restore Error", "An error occurred during restore: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void printProcessOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        
        StringBuilder error = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            error.append(line).append("\n");
        }

        if (output.length() > 0) {
            showAlert(AlertType.INFORMATION, "Process Output", output.toString());
        }
        if (error.length() > 0) {
            showAlert(AlertType.ERROR, "Process Error", error.toString());
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
