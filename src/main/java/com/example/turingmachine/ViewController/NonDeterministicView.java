package com.example.turingmachine.ViewController;

import com.example.turingmachine.Library.MethodLibrary;
import com.example.turingmachine.Library.ParameterLibrary;
import com.example.turingmachine.SceneNavigator;
import com.example.turingmachine.TupelClasses.*;
import com.example.turingmachine.TupelClasses.NonDeterministicTupel.InputTupel;
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

public class NonDeterministicView implements Initializable {
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

    int checkCountInPut=0;
    boolean foundAcceptState=false;
    String input="";
    String[] lines;
    String name="Untitled";
    String init="";
    String accept="";
    int currentCell1= ParameterLibrary.initializedCell;
    int currentCell2= ParameterLibrary.initializedCell;
    int currentCell3= ParameterLibrary.initializedCell;
    int readArrayLength;
    TextField tapeArray1[]= new TextField[100];
    TextField tapeArray2[]= new TextField[100];
    TextField tapeArray3[]= new TextField[100];
    InputTupel readArray[] = new InputTupel[100];
    TupelWriteCode[][] writeArray = new TupelWriteCode[100][];

    String pathArray[][]= new String[100][100];
    int pathNumbers=0;
    int pathCount=1;

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

    private void showResult(boolean state){
        if (state){
            finalState.setText("Accepted");
            finalState.setTextFill(Color.GREEN);

        }else{
            finalState.setText("Rejected");
            finalState.setTextFill(Color.RED);
        }
    }

    public void loadInput(ActionEvent event) {
        MethodLibrary.resetTape(tapeArray1,arrow1,tape1);
        MethodLibrary.resetTape(tapeArray2,arrow2,tape2);
        MethodLibrary.resetTape(tapeArray3,arrow3,tape3);
        currentCell1=ParameterLibrary.initializedCell;
        currentCell2=ParameterLibrary.initializedCell;
        currentCell3=ParameterLibrary.initializedCell;
        input = inputField.getText();
        MethodLibrary.loadInput(input,tapeArray1,currentCell1);
    }

    public void resetTape2(){
        MethodLibrary.resetTape(tapeArray2,arrow2,tape2);
        currentCell2=ParameterLibrary.initializedCell;
        MethodLibrary.loadInput(input,tapeArray2,ParameterLibrary.initializedCell);
    }


    public void resetTape3(){
        for (int i = 0; i < 100; i++) {
            tapeArray3[i].setText("");
        }
    }

    private void resetTape1(){
        currentCell1=ParameterLibrary.initializedCell;
        MethodLibrary.loadInput(input,tapeArray1,ParameterLibrary.initializedCell);
    }

    public void playBtn(ActionEvent event) {
        if (input!=""){
            findThePath(init, init,30,30);
            resetTape1();
            runTheMachine(pathCount);
        }else{
            MethodLibrary.showAlert("Input required", Alert.AlertType.ERROR);
        }
    }

    private void goBack1Step(String lastState, String currentState, int currentCell1){
        boolean foundTheCellToGoBack=false;
        for (int i = 0; i < readArrayLength; i++) {
            if (!(foundTheCellToGoBack)&&(lastState.equals(readArray[i].state))&&(currentState.equals(readArray[i].endstate))&&(readArray[i].write.equals(tapeArray1[currentCell1].getText()))){
                int cellToGoBack=currentCell1;
                switch (readArray[i].direction){
                    case "l":
                        cellToGoBack++;
                        break;
                    case "r":
                        cellToGoBack--;
                        break;
                    default:
                        break;
                }
                tapeArray1[cellToGoBack].setText(readArray[i].read);
                foundTheCellToGoBack=true;
            }
        }
    }

    private void findThePath(String lastState, String currentState, int currentCell1, int currentCell3){
        boolean cantGoFurther =true;

        String toRead=tapeArray1[currentCell1].getText();
        tapeArray3[currentCell3].setText(currentState);

        for (int i = 0; i < readArrayLength; i++) {
            int inLoopCurrentCell1 = currentCell1;
            int inLoopCurrentCell3 = currentCell3;

            if ((currentState.equals(readArray[i].state))&&(toRead.equals(readArray[i].read))){
                cantGoFurther = false;
                inLoopCurrentCell3++;
                tapeArray1[inLoopCurrentCell1].setText(readArray[i].write);
                switch (readArray[i].direction){
                    case "l":
                        inLoopCurrentCell1--;
                        break;
                    case "r":
                        inLoopCurrentCell1++;
                        break;
                    default:
                        break;
                }

                findThePath(currentState ,readArray[i].endstate,inLoopCurrentCell1,inLoopCurrentCell3);
            }
        }
        goBack1Step(lastState, currentState, currentCell1);
        if (cantGoFurther){
            int pathLength=currentCell3- ParameterLibrary.initializedCell+1;
            if (pathLength>=2) {
                pathNumbers++;
                pathArray[pathNumbers][0] = String.valueOf((currentCell3 - ParameterLibrary.initializedCell + 1));
                for (int i = 1; i <= pathLength; i++) {
                    pathArray[pathNumbers][i] = tapeArray3[ParameterLibrary.initializedCell + i - 1].getText();
                }
            }
        }
    }

    private void animation(int pathNr, String direction, int currentPathCellToHandle, int currentPathLength){
        TranslateTransition translation2 = MethodLibrary.createTransition(tape2, direction);
        translation2.setDuration(Duration.millis(ParameterLibrary.baseSpeed-speedSlider.getValue()*9));

        int finalDistanceX2 = (int) translation2.getToX();
        tapeArray3[ParameterLibrary.initializedCell+currentPathCellToHandle-1].setStyle("-fx-text-fill: red");
        translation2.setOnFinished((event -> {
            tape2.setLayoutX(tape2.getLayoutX() + finalDistanceX2);
            tape2.setTranslateX(0);
            tapeArray3[ParameterLibrary.initializedCell+currentPathCellToHandle-1].setStyle("-fx-text-fill: black");
            if (tapeArray3[ParameterLibrary.initializedCell+currentPathCellToHandle-1].getText().equals(accept)){
                showResult(true);
                foundAcceptState=true;
            }else {
                if (currentPathCellToHandle >= currentPathLength) {
                    runTheMachine(pathNr + 1);
                } else {
                    moveTheTape(pathNr, currentPathCellToHandle + 1, currentPathLength);
                }
            }
        }));
        translation2.play();
    }

    private void moveTheTape(int pathNr, int currentPathCellToHandle, int currentPathLength){
        if (currentPathCellToHandle==1){
            animation(pathNr, "", currentPathCellToHandle, currentPathLength);
        }else {

            int j = currentPathCellToHandle;
            if (currentPathCellToHandle <= currentPathLength) {
                int k = currentPathCellToHandle;
                String direction = "";
                String lastState = pathArray[pathNr][j - 1];
                String currentState = pathArray[pathNr][j];
                boolean foundCellToMove = false;
                int i = 0;
                while ((!foundCellToMove) && (i < readArrayLength)) {
                    if ((!foundCellToMove) && (readArray[i].state.equals(lastState)) && (readArray[i].endstate.equals(currentState)) && tapeArray2[currentCell2].getText().equals(readArray[i].read)) {
                        foundCellToMove = true;
                        tapeArray2[currentCell2].setText(readArray[i].write);
                        switch (readArray[i].direction) {
                            case "l":
                                direction = "left";
                                currentCell2--;
                                break;
                            case "r":
                                direction = "right";
                                currentCell2++;
                                break;
                            default:
                                break;
                        }
                    }
                    i++;
                }
                animation(pathNr, direction, currentPathCellToHandle, currentPathLength);
            }
        }
    }

    private void runTheMachine(int pathNr){
        if (pathNr<=pathNumbers) {
            printTape3(pathNr);
            resetTape2();
            int currentPathCellToHandle =1;
            moveTheTape(pathNr,currentPathCellToHandle, Integer.valueOf(pathArray[pathNr][0]));

        }else {
            showResult(false);
        }
    }

    private void printTape3(int pathNr){
        resetTape3();
        for (int i = 1; i <= Integer.valueOf(pathArray[pathNr][0]); i++) {
            tapeArray3[ParameterLibrary.initializedCell+i-1].setText(pathArray[pathNr][i]);
        }
    }

    public void addCodes(ActionEvent event) {
        lines=codeArea.getText().split("\n");
        int i=0;
        readArrayLength=0;
        while (i<lines.length){
            if (lines[i].startsWith("name:")){
                name=lines[i].substring(6);
            }else
            if (lines[i].startsWith("init:")){
                init=lines[i].substring(6);
            }else
            if (lines[i].startsWith("accept:")){
                accept=lines[i].substring(8);
            }else
            if (!Objects.equals(lines[i], "")){
                String[] parts =lines[i].split(" ");
                readArray[readArrayLength]=new InputTupel(null,null,null,null,null);
                readArray[readArrayLength].state=parts[0];
                readArray[readArrayLength].read=parts[1];
                readArray[readArrayLength].write=parts[2];
                readArray[readArrayLength].direction=parts[3];
                readArray[readArrayLength].endstate=parts[4];
                readArray[readArrayLength].read=MethodLibrary.translateBlank(readArray[readArrayLength].read);
                readArray[readArrayLength].write=MethodLibrary.translateBlank(readArray[readArrayLength].write);

                readArrayLength++;
            }
            i++;
        }
        readArrayLength--;
    }

    public void pauseBtn(ActionEvent event) {

    }
}
