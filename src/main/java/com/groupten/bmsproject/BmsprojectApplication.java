package com.groupten.bmsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
@ComponentScan
@EntityScan
public class BmsprojectApplication extends Application {

    private static ConfigurableApplicationContext applicationContext;
    private static Stage primaryStage;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(BmsprojectApplication.class, args);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayIngredients.fxml"));

        // Set the Spring context for the FXMLLoader
        loader.setControllerFactory(applicationContext::getBean);

        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Make sure to close the application context when the JavaFX stage is closed
    @Override
    public void stop() throws Exception {
        super.stop();
        applicationContext.close();
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
