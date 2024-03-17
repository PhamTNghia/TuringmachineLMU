package com.example.turingmachine;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneNavigator {
    private SceneNavigator() {
    }

    public static void navigateTo(Location location) {
        String fullResourceString = location.getFileName() + ".fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(SceneNavigator.class.getResource(fullResourceString));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1200, 800);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getPrimaryStage().setTitle("Turing machine");
        HelloApplication.getPrimaryStage().setScene(scene);
    }


    public enum Location {
        START_MENU("startMenuView"),
        INSTRUCTION("instructionView"),
        CREDIT("creditView"),
        ONE_TAPE("oneTapeView"),
        TWO_TAPE("twoTapeView"),
        THREE_TAPE("threeTapeView"),
        MULTI_TRACKING_3_TAPE("multiTracking3TapeView"),
        NON_DETERMINISTIC("Non_DeterministicView"),
        MULTIPLE_HEAD("multipleHeadView"),
        MULTI_DIMENSIONAL("multiDimensionalView"),
        SANDBOX("sandBoxView"),
        SANDBOX_RUN("sandBoxRunView"),
        UNIVERSAL("universalView");

        private final String fileName;

        Location(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

}
