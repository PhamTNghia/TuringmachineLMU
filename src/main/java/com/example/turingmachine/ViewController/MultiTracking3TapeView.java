package com.example.turingmachine.ViewController;

import com.example.turingmachine.Library.MethodLibrary;
import com.example.turingmachine.Library.MultiTrackingLibrary.MultiTrackMethodLibrary;
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
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MultiTracking3TapeView implements Initializable {
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
    Text step, state;
    @FXML
    ChoiceBox <String> typeOfTMBox,exampleBox;
    @FXML
    Slider speedSlider;

    String input="";
    int currentCell= ParameterLibrary.initializedCell;
    TextField tapeArray1[]= new TextField[100];
    TextField tapeArray2[]= new TextField[100];
    TextField tapeArray3[]= new TextField[100];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTheBoard();
        MethodLibrary.setUpTheMachine(tape1,arrow1,tapeArray1);
        MethodLibrary.setUpTheMachine(tape2,arrow2,tapeArray2);
        MethodLibrary.setUpTheMachine(tape3,arrow3,tapeArray3);
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

    private void moveTheTape( HBox tape1, HBox tape2, HBox tape3, String direction, int currentCell){
        MethodLibrary.moveTheTape(tape1,direction,speedSlider.getValue()*9);
        MethodLibrary.moveTheTape(tape2,direction,speedSlider.getValue()*9);

        TranslateTransition translation3 = MethodLibrary.createTransition(tape3,direction);
        translation3.setDuration(Duration.millis(ParameterLibrary.baseSpeed-speedSlider.getValue()*9));

        int finalDistanceX3 = (int) translation3.getToX();

        translation3.setOnFinished((event -> {
            tape3.setLayoutX(tape3.getLayoutX()+ finalDistanceX3);
            tape3.setTranslateX(0);
            runTuringMachine(currentCell);
        }));
        translation3.play();
    }

    private void showResult(boolean state,int currentCell){
        if (state){
            finalState.setText("Accepted");
            String resultString= tapeArray3[currentCell].getText();
            result.setText(resultString);
            finalState.setTextFill(Color.GREEN);
        }else{
            finalState.setText("Rejected");
            finalState.setTextFill(Color.RED);
            result.setText("");
        }
    }

    public void loadInput(ActionEvent event) {
        MethodLibrary.resetTape(tapeArray1,arrow1,tape1);
        MethodLibrary.resetTape(tapeArray2,arrow2,tape2);
        MethodLibrary.resetTape(tapeArray3,arrow3,tape3);
        currentCell=ParameterLibrary.initializedCell;
        input = inputField.getText();
        MultiTrackMethodLibrary.loadInput(exampleBox.getValue(),input,tapeArray1,tapeArray2,tapeArray3,currentCell);
    }

    public void playBtn(ActionEvent event) {
        if (input!=""){
            MethodLibrary.resetSteps(step,state);
            runTuringMachine(ParameterLibrary.initializedCell);
        }else{
            MethodLibrary.showAlert("Input required", Alert.AlertType.ERROR);
        }
    }

    private void runTuringMachine(int currentCell){
        ParameterLibrary.stepCounter++;
    switch (exampleBox.getValue()){
        case "Multiplication":
            if (tapeArray2[currentCell].getText().equals("0")){
                showResult(true,currentCell);
            }else{
                currentCell++;
                int a=Integer.parseInt(tapeArray1[currentCell-1].getText());
                int b=Integer.parseInt(tapeArray2[currentCell-1].getText())-1;
                int c=Integer.parseInt(tapeArray3[currentCell-1].getText());
                tapeArray2[currentCell].setText(String.valueOf(b));
                tapeArray3[currentCell].setText(String.valueOf(c+a));

                MethodLibrary.showSteps(state,"",step,ParameterLibrary.stepCounter);
                moveTheTape(tape1,tape2,tape3,"right",currentCell);
            }

        default:
            break;
    }


    }

    public void pauseBtn(ActionEvent event) {

    }
}
