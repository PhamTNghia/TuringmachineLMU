package com.example.turingmachine.ViewController;

import com.example.turingmachine.Library.MethodLibrary;
import com.example.turingmachine.Library.ParameterLibrary;
import com.example.turingmachine.SceneNavigator;
import com.example.turingmachine.TupelClasses.TupelReadCode;
import com.example.turingmachine.TupelClasses.TupelWriteCode;
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

public class MultipleHeadView implements Initializable {
    @FXML
    TextField inputField;
    @FXML
    HBox tape;
    @FXML
    ImageView arrow1,arrow2,arrow3,arrow;
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
    int currentCell=ParameterLibrary.initializedCell;
    int currentCell1,currentCell2,currentCell3;
    int remain;
    public static TextField tapeArray[]= new TextField[100];
    TupelReadCode readArray[] = new TupelReadCode[100];
    TupelWriteCode writeArray[] = new TupelWriteCode[100];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MethodLibrary.setUpTheBoard(typeOfTMBox,this.getClass().getSimpleName());
        MethodLibrary.setUpTheMachine(tape,arrow,tapeArray);
        MethodLibrary.setUpExampleBox(exampleBox,this.getClass().getSimpleName());
        typeOfTMBox.setOnAction(this::getScene);
        exampleBox.setOnAction(this::getExample);
    }

    public void getScene(ActionEvent event){
        MethodLibrary.getScene(typeOfTMBox);
    }

    public void getExample(ActionEvent event){ MethodLibrary.getExample(exampleBox, codeArea);}

    private void setUpTheArrow(){

        arrow3.setLayoutX(arrow.getLayoutX()-50*2);
        arrow3.setLayoutY(arrow.getLayoutY());

        arrow1.setLayoutX(arrow.getLayoutX()+50*(input.indexOf("#")-1));
        arrow1.setLayoutY(arrow.getLayoutY());

        arrow2.setLayoutX(arrow.getLayoutX()+50*(input.length()-1));
        arrow2.setLayoutY(arrow.getLayoutY());
    }

    public void loadInput(ActionEvent event) {
        MethodLibrary.resetTape(tapeArray,arrow,tape);
        tapeArray[ParameterLibrary.initializedCell-1].setText("#");
        currentCell=ParameterLibrary.initializedCell;
        input = inputField.getText();
        MethodLibrary.loadInput(input,tapeArray,currentCell);
        setUpTheArrow();
    }

    public void playBtn(ActionEvent event) {
        if (input!=""){
            currentCell3=ParameterLibrary.initializedCell -2;
            currentCell1=ParameterLibrary.initializedCell+input.indexOf("#")-1;
            currentCell2=ParameterLibrary.initializedCell+input.length()-1;
            remain=0;
            runTuringMachine(remain);
        }else{
            MethodLibrary.showAlert("Input required", Alert.AlertType.ERROR);
        }
    }

    private void moveTheArrows(int remain, int toMoveArrow1, int toMoveArrow2){
        if (toMoveArrow1==1) {
            MethodLibrary.moveTheTape(arrow1, "right",speedSlider.getValue()*9);
        }
        if (toMoveArrow2==1) {
            MethodLibrary.moveTheTape(arrow2, "right",speedSlider.getValue()*9);
        }
        TranslateTransition translation3 = MethodLibrary.createTransition(arrow3,"right");
        translation3.setDuration(Duration.millis(ParameterLibrary.baseSpeed-speedSlider.getValue()*9));

        int finalDistanceX3 = (int) translation3.getToX();

        translation3.setOnFinished((event -> {
            arrow3.setLayoutX(arrow3.getLayoutX()+ finalDistanceX3);
            arrow3.setTranslateX(0);
            currentCell3--;
            runTuringMachine(remain);
        }));
        translation3.play();
    }

    private void runTuringMachine(int remain){
        int a=0;
        int b=0;
        int toMoveArrow1=0;
        int toMoveArrow2=0;
        if (!tapeArray[currentCell1].getText().equals("#")){
            a=Integer.valueOf(tapeArray[currentCell1].getText());
            toMoveArrow1=1;
            currentCell1--;
        }
        if (!tapeArray[currentCell2].getText().equals("#")){
            b=Integer.valueOf(tapeArray[currentCell2].getText());
            toMoveArrow2=1;
            currentCell2--;
        }
        if ((toMoveArrow1==0)&&(toMoveArrow2==0)){
            tapeArray[currentCell3].setText(String.valueOf(remain));
            MethodLibrary.showResult(true, finalState, result, tapeArray);
        }else {
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
            tapeArray[currentCell3].setText(String.valueOf(c));
            moveTheArrows(remain, toMoveArrow1, toMoveArrow2);
        }
    }

    public void pauseBtn(ActionEvent event) {
        System.out.println(this.getClass().getSimpleName());
    }

    public void addCodes(ActionEvent event) {
    }
}
