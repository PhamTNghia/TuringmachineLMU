package com.example.turingmachine.Library;

public class ParameterLibrary {
    public static int initializedCell =30;
    public static int baseSpeed=1000;
    public static int stepCounter=0;

    public static String[] choiceOfType={"1 Tape", "2 Tape","3 Tape","MultiTracking3Tape", "Non_Deterministic","MultipleHead","MultiDimensional","Universal"};

    public enum ExampleList{
        ONE_TAPE(new String[]{"Decimal To Binary"}),
        TWO_TAPE(new String[]{"Binary Palindrome"}),
        THREE_TAPE(new String[]{"Binary Addition"}),
        MULTI_TRACKING_3_TAPE(new String[]{"Multiplication"}),
        NON_DETERMINISTIC(new String[]{"Mirror"}),
        UNIVERSAL(new String[]{"Even Amount Of 0"});

        private final String[] exampleList;

        ExampleList(String[] exampleList) {
            this.exampleList = exampleList;
        }

        public String[] getExampleList() {
            return exampleList;
        }
    }

    public static String ruleSaveForUniversal="";
}
