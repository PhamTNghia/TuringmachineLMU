package com.example.turingmachine.ViewController;

import com.example.turingmachine.SceneNavigator;
import javafx.event.ActionEvent;

public class InstructionView {
    public void backClicked(ActionEvent event) {
        SceneNavigator.navigateTo(SceneNavigator.Location.START_MENU);
    }
}
