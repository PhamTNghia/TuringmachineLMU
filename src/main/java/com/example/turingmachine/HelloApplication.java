package com.example.turingmachine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    private static Stage pStage;

    @Override
    public void start(Stage stage) throws IOException {
        setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("startMenuView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        pStage.setResizable(false);
        pStage.setTitle("Turing machine");
        pStage.setScene(scene);


        pStage.show();
    }

    public static Stage getPrimaryStage(){
        return pStage;
    }

    public void setPrimaryStage(Stage stage){
        HelloApplication.pStage=stage;
    }

    public static void main(String[] args) {
        launch();
    }
}