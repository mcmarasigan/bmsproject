package com.groupten.bmsproject.FXMLControllers;

import com.groupten.bmsproject.ProductionSchedule.ProductionIngredient;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleEntity;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleService;
import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientService;
import com.groupten.bmsproject.Product.ProductEntity;
import com.groupten.bmsproject.Product.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class ProductionScheduleController {

    @FXML
    private ChoiceBox<String> productChoiceBox;

    @FXML
    private TextField quantityField;

    @FXML
    private DatePicker dateofproductionPicker;

    @FXML
    private DatePicker expdatePicker;

    @FXML
    private Button savebutton;

    @FXML
    private TableView<IngredientTableRow> RecipeTable;

    @FXML
    private TableColumn<IngredientTableRow, String> IngredientNameColumn;

    @FXML
    private TableColumn<IngredientTableRow, Double> QuantityColumn;

    @FXML
    private TableColumn<IngredientTableRow, String> UnitTypeColumn;

    private final ProductionScheduleService productionScheduleService;
    private final ProductService productService;
    private final IngredientService ingredientService;

    private static ObservableList<IngredientTableRow> ingredientData = FXCollections.observableArrayList();

    @Autowired
    public ProductionScheduleController(ProductionScheduleService productionScheduleService, ProductService productService, IngredientService ingredientService) {
        this.productionScheduleService = productionScheduleService;
        this.productService = productService;
        this.ingredientService = ingredientService;
    }

    @FXML
    private void initialize() {
        populateProductChoiceBox();
        savebutton.setOnAction(event -> handleSaveButton());

        IngredientNameColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        QuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        UnitTypeColumn.setCellValueFactory(new PropertyValueFactory<>("unitType"));

        RecipeTable.setItems(ingredientData);

        expdatePicker.setDayCellFactory(getDateCellFactory());
        dateofproductionPicker.setDayCellFactory(getDateCellFactory());
    }

    private Callback<DatePicker, DateCell> getDateCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d48200;");
                }
            }
        };
    }

    private void populateProductChoiceBox() {
        productService.getActiveProducts().forEach(product -> productChoiceBox.getItems().add(product.getproductName()));
    }

    @FXML
private void handleSaveButton() {
    if (ingredientData.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No Ingredients");
        alert.setHeaderText(null);
        alert.setContentText("Please add at least one ingredient before saving.");
        alert.showAndWait();
        return;
    }

    String productName = productChoiceBox.getValue();
    if (productName == null || productName.trim().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText("Please select a product.");
        alert.showAndWait();
        return;
    }

    Double quantity = null;
    try {
        quantity = Double.parseDouble(quantityField.getText());
    } catch (NumberFormatException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText("Quantity must be a valid number.");
        alert.showAndWait();
        return;
    }

    LocalDate dateOfProduction = dateofproductionPicker.getValue();
    LocalDate expDate = expdatePicker.getValue();
    if (dateOfProduction == null || expDate == null || dateOfProduction.isAfter(expDate)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Dates");
        alert.setHeaderText(null);
        alert.setContentText("Please select valid production and expiry dates.");
        alert.showAndWait();
        return;
    }

    long numberOfDays = ChronoUnit.DAYS.between(dateOfProduction, expDate);

    ProductionScheduleEntity productionSchedule = new ProductionScheduleEntity();
    productionSchedule.setproductName(productName);
    productionSchedule.setproductschedQuantity(quantity);
    productionSchedule.setdateofProduction(dateOfProduction);
    productionSchedule.setexpDate(expDate);
    productionSchedule.setnumberofdaysexp((int) numberOfDays);
    productionSchedule.setlvlofstock(quantity < 10 ? "Low" : "Sufficient");
    if (numberOfDays <= 0) {
        productionSchedule.setExpiryStatus("Expired");
    } else {
        productionSchedule.setExpiryStatus("Valid");
    }

    // Fetch and set the product entity
    ProductEntity productEntity = productService.getProductByName(productName);
    if (productEntity != null) {
        productionSchedule.setProductId(productEntity.getID());
    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Product Not Found");
        alert.setHeaderText(null);
        alert.setContentText("Selected product does not exist.");
        alert.showAndWait();
        return;
    }

    productionScheduleService.save(productionSchedule);

    RecipeTable.getItems().forEach(row -> {
        ProductionIngredient productionIngredient = new ProductionIngredient();
        productionIngredient.setProductionschedule(productionSchedule);

        IngredientEntity ingredient = ingredientService.findByName(row.getIngredientName());
        productionIngredient.setIngredientid(ingredient);
        productionIngredient.setQuantity(row.getQuantity());
        productionIngredient.setUnitType(row.getUnitType());

        productionScheduleService.saveProductionIngredient(productionIngredient);
    });

    // Show success dialog
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Production Schedule Saved");
    alert.setHeaderText(null);
    alert.setContentText("Production schedule and ingredients saved successfully!");
    alert.showAndWait();
}

    
    @FXML
    private void openAddIngredientDialog() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddIngredientDialog.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public static void addIngredientToTable(String ingredientName, Double quantity, String unitType) {
        IngredientTableRow row = new IngredientTableRow(ingredientName, quantity, unitType);
        ingredientData.add(row);
    }

    @FXML
    private void handleRemoveButton() {
        IngredientTableRow selectedRow = RecipeTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            ingredientData.remove(selectedRow);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an ingredient to remove.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void goBackProductionSched() throws IOException {
        // Clear the ingredient data
        ingredientData.clear();

        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayProductionSchedule.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
