package com.example.turingmachine.ViewController;

import com.example.turingmachine.SceneNavigator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;

public class StartMenuView {
    @FXML
    Text name;

    public void initialize(){
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
        name.setFill(Color.rgb(41, 128, 185)); // Text color
        name.setEffect(new DropShadow(10, 3, 3, Color.rgb(0, 0, 0, 0.4)));
    }

    public void startClicked(ActionEvent event) {
        SceneNavigator.navigateTo(SceneNavigator.Location.ONE_TAPE);
    }

    public void quitClicked(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void sandboxClicked(ActionEvent event) {
        SceneNavigator.navigateTo(SceneNavigator.Location.SANDBOX);
    }

    public void instructionClicked(ActionEvent event) {
        SceneNavigator.navigateTo(SceneNavigator.Location.INSTRUCTION);
    }

    public void creditClicked(ActionEvent event) {
        SceneNavigator.navigateTo(SceneNavigator.Location.CREDIT);
    }
}
