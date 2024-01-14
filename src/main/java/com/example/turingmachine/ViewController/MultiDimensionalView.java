package com.example.turingmachine.ViewController;

import com.example.turingmachine.Library.MethodLibrary;
import com.example.turingmachine.Library.ParameterLibrary;
import com.example.turingmachine.SceneNavigator;
import com.example.turingmachine.TupelClasses.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MultiDimensionalView implements Initializable {
    @FXML
    TextField inputField;
    @FXML
    HBox tape1,tape2,tape3;
    @FXML
    ImageView arrow1,arrow2,arrow3;
    @FXML
    TextArea codeArea;
    @FXML
    Label finalState, result;
    @FXML
    ChoiceBox <String> typeOfTMBox,exampleBox;
    @FXML
    Slider speedSlider;

    String input="";
    String[] lines;
    String name="Untitled";
    String init="";
    String accept="";
    int currentCell1= ParameterLibrary.initializedCell;
    int currentCell2= ParameterLibrary.initializedCell;
    int currentCell3= ParameterLibrary.initializedCell;
    int countForRead=0;
    int countForWrite=0;
    TextField tapeArray1[]= new TextField[100];
    TextField tapeArray2[]= new TextField[100];
    TextField tapeArray3[]= new TextField[100];
    TupelRead3Code readArray[] = new TupelRead3Code[100];
    TupelWrite3Code writeArray[] = new TupelWrite3Code[100];

    boolean isGoingDown = true;
    int remain=0;
    int currentHandleCell;
    int currentHandleTape;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTheBoard();
        MethodLibrary.setUpTheMachine(tape1,arrow1,tapeArray1);
        MethodLibrary.setUpTheMachine(tape2,arrow2,tapeArray2);
        MethodLibrary.setUpTheMachine(tape3,arrow3,tapeArray3);
        MethodLibrary.setUpExampleBox(exampleBox,this.getClass().getSimpleName());
        exampleBox.setOnAction(this::getExample);
    }

    private void setUpTheBoard(){
        MethodLibrary.setUpTheBoard(typeOfTMBox,this.getClass().getSimpleName());
        typeOfTMBox.setOnAction(this::getScene);
    }

    public void getScene(ActionEvent event){
        MethodLibrary.getScene(typeOfTMBox);
    }

    public void getExample(ActionEvent event){ MethodLibrary.getExample(exampleBox, codeArea);}

    private void animate(int currentHandleCell, int remain){

        TranslateTransition translation3 = MethodLibrary.createTransition(tape3,"");
        int finalDistanceX3 = (int) translation3.getToX();

        translation3.setOnFinished((event -> {
            tape3.setLayoutX(tape3.getLayoutX()+ finalDistanceX3);
            tape3.setTranslateX(0);
            runTuringMachine(currentHandleCell,remain);
        }));
        translation3.play();
    }

    public void loadInput(ActionEvent event) {
        MethodLibrary.resetTape(tapeArray1,arrow1,tape1);
        MethodLibrary.resetTape(tapeArray2,arrow2,tape2);
        MethodLibrary.resetTape(tapeArray3,arrow3,tape3);
        currentCell1=ParameterLibrary.initializedCell;
        currentCell2=ParameterLibrary.initializedCell;
        currentCell3=ParameterLibrary.initializedCell;
        input = inputField.getText();

        String[] inputParts = input.split("#");
        int a=inputParts[0].length()-inputParts[1].length();
        if (a>=0){
            for (int i = 0; i < a; i++) {
                inputParts[1]=" "+inputParts[1];
            }
        }else {
            for (int i = 0; i < -a; i++) {
                inputParts[0]=" "+inputParts[0];
            }
        }
        for (int i = 0; i < inputParts[0].length(); i++) {
            tapeArray1[ParameterLibrary.initializedCell+i].setText(String.valueOf(inputParts[0].charAt(i)));
        }
        for (int i = 0; i < inputParts[1].length(); i++) {
            tapeArray2[ParameterLibrary.initializedCell+i].setText(String.valueOf(inputParts[1].charAt(i)));
        }
        currentHandleCell=ParameterLibrary.initializedCell+inputParts[0].length()-1;
    }

    public void playBtn(ActionEvent event) {
        if (input!=""){
            remain=0;
            isGoingDown=true;
            currentHandleTape=1;
            runTuringMachine(currentHandleCell,remain);
        }else{
            MethodLibrary.showAlert("Input required", Alert.AlertType.ERROR);
        }
    }

    private void runTuringMachine(int currentHandleCell, int remain){
        if (currentHandleCell>=ParameterLibrary.initializedCell-1) {
            if (isGoingDown) {
                if (currentHandleTape == 1) {
                    changeColorBlack(tapeArray1,currentHandleCell+1);
                    changeColorRed(tapeArray1, currentHandleCell);
                    changeColorBlack(tapeArray2, currentHandleCell);
                    changeColorBlack(tapeArray3, currentHandleCell);
                    currentHandleTape = 2;
                    animate(currentHandleCell,remain);
                } else if (currentHandleTape == 2) {
                    changeColorBlack(tapeArray1, currentHandleCell);
                    changeColorRed(tapeArray2, currentHandleCell);
                    changeColorBlack(tapeArray3, currentHandleCell);
                    currentHandleTape = 3;
                    animate(currentHandleCell,remain);
                } else {
                    changeColorBlack(tapeArray1, currentHandleCell);
                    changeColorBlack(tapeArray2, currentHandleCell);
                    changeColorRed(tapeArray3, currentHandleCell);
                    currentHandleTape = 2;
                    isGoingDown = false;
                    int a,b;
                    if(tapeArray1[currentHandleCell].getText().equals(" ")||tapeArray1[currentHandleCell].getText().equals("")){
                        a=0;
                    }else {
                        a = Integer.valueOf(tapeArray1[currentHandleCell].getText());
                    }
                    if(tapeArray2[currentHandleCell].getText().equals(" ")||tapeArray2[currentHandleCell].getText().equals("")){
                        b=0;
                    }else {
                        b = Integer.valueOf(tapeArray2[currentHandleCell].getText());
                    }
                    int c = a + b + remain;
                    if (c == 2) {
                        c = 0;
                        remain = 1;
                    } else if (c == 3) {
                        c = 1;
                        remain = 1;
                    } else {
                        remain = 0;
                    }
                    tapeArray3[currentHandleCell].setText(String.valueOf(c));
                    animate(currentHandleCell,remain);
                }
            }else{
                if (currentHandleTape==2){
                    changeColorBlack(tapeArray1, currentHandleCell);
                    changeColorRed(tapeArray2, currentHandleCell);
                    changeColorBlack(tapeArray3, currentHandleCell);
                    currentHandleTape = 1;
                    animate(currentHandleCell,remain);
                }else if (currentHandleTape==1){
                    changeColorRed(tapeArray1, currentHandleCell);
                    changeColorBlack(tapeArray2, currentHandleCell);
                    changeColorBlack(tapeArray3, currentHandleCell);
                    currentHandleTape = 1;
                    currentHandleCell--;
                    isGoingDown=true;
                    animate(currentHandleCell,remain);
                }
            }
        }else{
            MethodLibrary.showResult(true, finalState, result, tapeArray3);
        }
    }

    private void changeColorRed(TextField[] tape, int position){
        tape[position].setStyle("-fx-text-fill: red");
    }

    private void changeColorBlack(TextField[] tape, int position){
        tape[position].setStyle("-fx-text-fill: black");
    }

    public void addCodes(ActionEvent event) {
    }

    public void pauseBtn(ActionEvent event) {

    }
}
