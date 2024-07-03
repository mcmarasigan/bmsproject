package com.groupten.bmsproject.FXMLControllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.JasperReport.ReportService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@Component
public class InventoryFXMLController {

private ReportService reportService;
    @FXML
    private void proceedtoProduct() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayProducts.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void proceedtoIngredient() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayIngredients.fxml"));
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


      @FXML
    private void handleGenerateReportIngredient() {
        generateReport("IngredientReport", "Ingredient_Report.jrxml");
    }

    @FXML
    private void handleGenerateReportSales() {
        generateReport("SalesReport", "Sales_Report.jrxml");
    }

    @FXML
    private void handleGenerateSecurityLogReport() {
        generateReport("SecurityLogReport", "Security_Report.jrxml");
    }
    @FXML
    private void handleGenerateReportProduction() {
        generateReport("ProductionReport", "Production_Report.jrxml");
    }
    @FXML
    private void handleGenerateReportProductionIngredientUsed() {
        generateReport("IngredientUsedReport", "IngredientUsed_Report.jrxml");
    }


    private void generateReport(String reportPrefix, String reportTemplate) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

        // Set default file name
        String timestamp = new SimpleDateFormat("dd-MM-yyyy_hh-mm a").format(new Date()).replace(" ", "_");
        String defaultFileName = String.format("%s_%s.pdf", reportPrefix, timestamp);
        fileChooser.setInitialFileName(defaultFileName);

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                String result = reportService.exportReport(reportTemplate, file.getParent());
                showAlert(AlertType.INFORMATION, "Report Generation", result);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Report Generation Error", e.getMessage());
            }
        }
    }


    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
