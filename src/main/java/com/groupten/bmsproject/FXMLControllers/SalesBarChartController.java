package com.groupten.bmsproject.FXMLControllers;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.JasperReport.ReportService;
import com.groupten.bmsproject.Sales.SalesEntity;
import com.groupten.bmsproject.Sales.SalesService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SalesBarChartController {

    @FXML
    private BarChart<String, Number> salesBarChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private ComboBox<String> viewTypeComboBox;

    @FXML
    private ComboBox<Month> monthComboBox;

    @FXML
    private ComboBox<Integer> yearComboBox;

    @FXML
    private DatePicker weekStartDatePicker;

    @FXML
    private Text mostsoldProducttxt;

    @FXML
    private Text totalSalestxt;

    private final SalesService salesService;

    @Autowired
    private ReportService reportService;

    @Autowired
    public SalesBarChartController(SalesService salesService) {
        this.salesService = salesService;
    }

    private boolean isWeekly = false;

    public void initialize() {
        populateComboBoxes();
        viewTypeComboBox.setValue("Monthly");
        weekStartDatePicker.setVisible(false);
        populateBarChartMonthly(LocalDate.now().getMonth(), LocalDate.now().getYear());
    }

    private void populateComboBoxes() {
        viewTypeComboBox.setItems(FXCollections.observableArrayList("Monthly", "Weekly"));
        viewTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> toggleViewType(newVal));
        
        monthComboBox.setItems(FXCollections.observableArrayList(Month.values()));
        yearComboBox.setItems(FXCollections.observableArrayList(
            LocalDate.now().getYear() - 2,
            LocalDate.now().getYear() - 1,
            LocalDate.now().getYear(),
            LocalDate.now().getYear() + 1
        ));
        
        monthComboBox.setValue(LocalDate.now().getMonth());
        yearComboBox.setValue(LocalDate.now().getYear());
    }

    private void toggleViewType(String viewType) {
        if ("Weekly".equals(viewType)) {
            isWeekly = true;
            monthComboBox.setVisible(false);
            yearComboBox.setVisible(false);
            weekStartDatePicker.setVisible(true);
            totalSalestxt.setText("Total Sales for the week:");
        } else {
            isWeekly = false;
            monthComboBox.setVisible(true);
            yearComboBox.setVisible(true);
            weekStartDatePicker.setVisible(false);
            totalSalestxt.setText("Total Sales for the month:");
        }
    }

    @FXML
    private void updateBarChart() {
        if (isWeekly) {
            LocalDate startDate = weekStartDatePicker.getValue();
            if (startDate != null) {
                populateBarChartWeekly(startDate);
            }
        } else {
            Month selectedMonth = monthComboBox.getValue();
            Integer selectedYear = yearComboBox.getValue();
            populateBarChartMonthly(selectedMonth, selectedYear);
        }
    }

    private void populateBarChartMonthly(Month month, int year) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Most Sold Product:");

        List<SalesEntity> filteredSales = salesService.getPaidAndDeliveredOrders().stream()
            .filter(sales -> sales.getDatePurchased().getMonth() == month && sales.getDatePurchased().getYear() == year)
            .collect(Collectors.toList());

        String mostSoldProduct = findMostSoldProduct(filteredSales);
        mostsoldProducttxt.setText(mostSoldProduct);

        double totalSales = filteredSales.stream()
            .mapToDouble(SalesEntity::getTotalAmount)
            .sum();
        totalSalestxt.setText(String.format("Total Sales for the month: ₱%.2f", totalSales));

        filteredSales.stream()
            .collect(Collectors.groupingBy(SalesEntity::getProductOrder,
                    Collectors.summingDouble(SalesEntity::getTotalAmount)))
            .forEach((product, totalAmount) -> {
                series.getData().add(new XYChart.Data<>(product, totalAmount));
            });

        salesBarChart.getData().clear();
        salesBarChart.getData().add(series);
    }

    private void populateBarChartWeekly(LocalDate startDate) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Most Sold Product:");

        LocalDate endDate = startDate.plusWeeks(1);

        List<SalesEntity> filteredSales = salesService.getPaidAndDeliveredOrders().stream()
            .filter(sales -> !sales.getDatePurchased().isBefore(startDate) && sales.getDatePurchased().isBefore(endDate))
            .collect(Collectors.toList());

        String mostSoldProduct = findMostSoldProduct(filteredSales);
        mostsoldProducttxt.setText(mostSoldProduct);

        double totalSales = filteredSales.stream()
            .mapToDouble(SalesEntity::getTotalAmount)
            .sum();
        totalSalestxt.setText(String.format("Total Sales for the week: ₱%.2f", totalSales));

        filteredSales.stream()
            .collect(Collectors.groupingBy(SalesEntity::getProductOrder,
                    Collectors.summingDouble(SalesEntity::getTotalAmount)))
            .forEach((product, totalAmount) -> {
                series.getData().add(new XYChart.Data<>(product, totalAmount));
            });

        salesBarChart.getData().clear();
        salesBarChart.getData().add(series);
    }

    private String findMostSoldProduct(List<SalesEntity> salesEntities) {
        Map<String, Double> productSales = salesEntities.stream()
            .collect(Collectors.groupingBy(SalesEntity::getProductOrder,
                    Collectors.summingDouble(SalesEntity::getTotalAmount)));

        // Find the product with the highest total sales
        String mostSoldProduct = productSales.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("No sales");

        return mostSoldProduct;
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
