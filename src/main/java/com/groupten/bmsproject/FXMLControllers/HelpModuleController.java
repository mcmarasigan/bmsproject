package com.groupten.bmsproject.FXMLControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

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
}
