package com.example.turingmachine.TupelClasses.NonDeterministicTupel;

public class InputTupel {
    public String state;
    public String read;
    public String write;
    public String direction;
    public String endstate;

    public InputTupel(String state, String read, String write, String direction, String endstate){
        this.read=read;
        this.state=state;
        this.write=write;
        this.direction=direction;
        this.endstate=endstate;
    }
}
