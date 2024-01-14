package com.example.turingmachine.ViewController;

import com.example.turingmachine.Library.MethodLibrary;
import com.example.turingmachine.Library.ParameterLibrary;
import com.example.turingmachine.SceneNavigator;
import com.example.turingmachine.TupelClasses.TupelRead2Code;
import com.example.turingmachine.TupelClasses.TupelReadCode;
import com.example.turingmachine.TupelClasses.TupelWrite2Code;
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
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TwoTapeView implements Initializable {
    @FXML
    TextField inputField;
    @FXML
    HBox tape1,tape2;
    @FXML
    ImageView arrow1,arrow2;
    @FXML
    TextArea codeArea;
    @FXML
    Label finalState, result;
    @FXML
    Text step, state;
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
    int currentCell2=ParameterLibrary.initializedCell;
    int countForRead=0;
    int countForWrite=0;
    TextField tapeArray1[]= new TextField[100];
    TextField tapeArray2[]= new TextField[100];
    TupelRead2Code readArray[] = new TupelRead2Code[100];
    TupelWrite2Code writeArray[] = new TupelWrite2Code[100];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTheBoard();
        MethodLibrary.setUpTheMachine(tape1,arrow1,tapeArray1);
        MethodLibrary.setUpTheMachine(tape2,arrow2,tapeArray2);
        MethodLibrary.setUpExampleBox(exampleBox,this.getClass().getSimpleName());
        exampleBox.setOnAction(this::getExample);

        MethodLibrary.resetSteps(step,state);
    }

    private void setUpTheBoard(){
        MethodLibrary.setUpTheBoard(typeOfTMBox,this.getClass().getSimpleName());
        typeOfTMBox.setOnAction(this::getScene);
    }

    public void getScene(ActionEvent event){
        MethodLibrary.getScene(typeOfTMBox);
    }

    public void getExample(ActionEvent event){ MethodLibrary.getExample(exampleBox, codeArea);}

    private void moveTheTape( HBox tape1, HBox tape2, String direction1, String direction2, String currentState, int currentCell1, int currentCell2 ){
        MethodLibrary.moveTheTape(tape1,direction1,speedSlider.getValue()*9);

        TranslateTransition translation2 = MethodLibrary.createTransition(tape2,direction2);
        translation2.setDuration(Duration.millis(ParameterLibrary.baseSpeed-speedSlider.getValue()*9));

        int finalDistanceX2 = (int) translation2.getToX();

        translation2.setOnFinished((event -> {
            tape2.setLayoutX(tape2.getLayoutX()+ finalDistanceX2);
            tape2.setTranslateX(0);
            runTuringMachine(currentState,currentCell1,currentCell2);
        }));
        translation2.play();
    }

    public void loadInput(ActionEvent event) {
        MethodLibrary.resetTape(tapeArray1,arrow1,tape1);
        MethodLibrary.resetTape(tapeArray2,arrow2,tape2);
        currentCell1=30;
        currentCell2=30;
        input = inputField.getText();
        MethodLibrary.loadInput(input,tapeArray1,currentCell1);
    }

    public void playBtn(ActionEvent event) {
        if (input!=""){
            MethodLibrary.resetSteps(step,state);
            runTuringMachine(init,30,30);
        }else{
            MethodLibrary.showAlert("Input required", Alert.AlertType.ERROR);
        }
    }

    private void runTuringMachine(String currentState, int currentCell1, int currentCell2){
        ParameterLibrary.stepCounter++;
        int i=-1;
        for (int j = 0; j < countForRead; j++) {
            if ((Objects.equals(readArray[j].state, currentState)) && (Objects.equals(readArray[j].read1, tapeArray1[currentCell1].getText())) &&(Objects.equals(readArray[j].read2, tapeArray2[currentCell2].getText()))) {
                i = j;
            }
        }
        if (i==-1){
            MethodLibrary.showResult(false, finalState, result, tapeArray2);
        }else{
            currentState=writeArray[i].state;
            tapeArray1[currentCell1].setText(writeArray[i].write1);
            tapeArray2[currentCell2].setText(writeArray[i].write2);
            String direction1=MethodLibrary.translateDirection(writeArray[i].move1);
            String direction2=MethodLibrary.translateDirection(writeArray[i].move2);
            currentCell1=MethodLibrary.translateCurrentCell(writeArray[i].move1, currentCell1);
            currentCell2=MethodLibrary.translateCurrentCell(writeArray[i].move2, currentCell2);

            MethodLibrary.showSteps(state,currentState,step,ParameterLibrary.stepCounter);
            moveTheTape(tape1,tape2,direction1,direction2,currentState,currentCell1,currentCell2);
        }
        if (Objects.equals(currentState, accept)){
            MethodLibrary.showResult(true, finalState, result, tapeArray2);
        };
    }

    public void addCodes(ActionEvent event) {
        lines = codeArea.getText().split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith("name:")){
                name=lines[i].substring(6);
            }
            if (lines[i].startsWith("init:")){
                init=lines[i].substring(6);
            }
            if (lines[i].startsWith("accept:")){
                accept=lines[i].substring(8);
            }
        }
        if (init==""||accept==""){
            MethodLibrary.showAlert("The rule requires initial state and accept state:", Alert.AlertType.ERROR);
        }else{
            countForRead=0;
            countForWrite=0;
            for (int i = 0; i < lines.length; i++) {
                if ((!lines[i].startsWith("name"))&&(!lines[i].startsWith("init:"))&&(!lines[i].startsWith("accept:"))&&(!lines[i].startsWith("//"))){
                    String parts[]=lines[i].split(",");
                    if (parts.length==3){
                        readArray[countForRead]=new TupelRead2Code(null,null,null);
                        readArray[countForRead].state=parts[0];
                        readArray[countForRead].read1=parts[1];
                        readArray[countForRead].read2=parts[2];
                        readArray[countForRead].read1=MethodLibrary.translateBlank(readArray[countForRead].read1);
                        readArray[countForRead].read2=MethodLibrary.translateBlank(readArray[countForRead].read2);
                        countForRead++;
                    }else if (parts.length==5){
                        writeArray[countForWrite]=new TupelWrite2Code(null,null,null,null,null);
                        writeArray[countForWrite].state=parts[0];
                        writeArray[countForWrite].write1=parts[1];
                        writeArray[countForWrite].write2=parts[2];
                        writeArray[countForWrite].move1=parts[3];
                        writeArray[countForWrite].move2=parts[4];
                        writeArray[countForWrite].write1=MethodLibrary.translateBlank(writeArray[countForWrite].write1);
                        writeArray[countForWrite].write2=MethodLibrary.translateBlank(writeArray[countForWrite].write2);
                        countForWrite++;
                    }
                }
            }
        }
    }

    public void pauseBtn(ActionEvent event) {
        System.out.println(Thread.activeCount());
    }
}
