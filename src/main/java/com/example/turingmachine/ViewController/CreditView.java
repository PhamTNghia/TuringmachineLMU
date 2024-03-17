package com.example.turingmachine.ViewController;

import com.example.turingmachine.Library.ParameterLibrary;
import com.example.turingmachine.SceneNavigator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CreditView {
    @FXML
    VBox text;

    public void initialize(){
        moveTheText();
    }

    private void moveTheText(){
        TranslateTransition translation = new TranslateTransition();
        translation.setDuration(Duration.seconds(8));
        translation.setNode(text);
        translation.setToY(-2000);
        translation.setOnFinished((event -> {
            SceneNavigator.navigateTo(SceneNavigator.Location.START_MENU);
        }));
        translation.play();
    }

}
