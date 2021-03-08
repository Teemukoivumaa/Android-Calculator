package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// https://github.com/Teemukoivumaa/bankingApplication/blob/master/app/src/main/java/com/example/bankingapplication/CurrencyConverter.java

public class MainActivity extends AppCompatActivity {

    private TextView inputValues;
    private TextView answer1;

    private double prevValue, nextValue;
    private int prevValueLocation, nextValueLocation;

    private final int[] answers = {
            R.id.answer1,
            R.id.answer2,
            R.id.answer3,
            R.id.answer4,
            R.id.answer5,
            R.id.answer6,
            R.id.answer7
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputValues = findViewById(R.id.calculation);
        answer1 = findViewById(R.id.answer1); // only answer1 is needed because we set it manually instead of for loop
    }

    public void addNumber(View view) { // Adds the number that was pressed, could be added to specialMarkers() but it's simpler this way
        String valueText = inputValues.getText().toString();

        Button b = (Button)view;
        String buttonText = b.getText().toString();

        valueText = valueText + buttonText;
        inputValues.setText(valueText);
    }

    public void specialMarkers(View view) {
        String calculation = inputValues.getText().toString();
        String newCalculation = "", answer = "";

        Button b = (Button)view;
        String buttonText = b.getText().toString();

        switch (buttonText) {
            case "C": // Clear calculation, if calculation is empty clear answers
                newCalculation = "";
                if (calculation.equals("")) {
                    for (int i=0; i<7; i++) {
                        ((TextView) findViewById(answers[i])).setText(newCalculation);
                    }
                }
                inputValues.setText(newCalculation);
                break;

            case "Erase": // Erase the last mark in calculation
                if (!calculation.isEmpty()) { newCalculation = removeLastChar(calculation); inputValues.setText(newCalculation); }
                break;

            case "%": // percentage calculation
                if (!calculation.isEmpty()) {
                    if (!isLastMarkSpecial(calculation)) {
                        newCalculation = percentageOf(calculation);
                        inputValues.setText(newCalculation);
                        break;
                    }
                }

                Toast.makeText(MainActivity.this, "Calculation is empty", Toast.LENGTH_LONG).show();
                break;

            case "=":
                answer = mathCalculation(calculation);
                if (!answer.equals("null")) {
                    swiftAnswers(answer, calculation);
                    inputValues.setText(answer);
                }
                break;

            case "+":
            case "-":
            case "÷":
            case "x":
                if (!calculation.isEmpty()) {
                    if (isLastMarkSpecial(calculation)) {
                        // If last mark is special replace it with the new one
                        calculation = removeLastChar(calculation);
                    } // else just add the button text to calculation
                    newCalculation = calculation + buttonText;
                    inputValues.setText(newCalculation);
                    break;
                }

                Toast.makeText(MainActivity.this, "Calculation is empty", Toast.LENGTH_LONG).show();
                break;

            case ".":
                newCalculation = calculation;
                if (isLastMarkSpecial(calculation) || calculation.isEmpty()) {
                    newCalculation += "0";
                }
                newCalculation += buttonText;
                inputValues.setText(newCalculation);

                break;
            default: // Default value, should never happen.
                Toast.makeText(MainActivity.this, "Unknown value", Toast.LENGTH_LONG).show();
        }
    }

    private String mathCalculation(String calculation) {
        String answer = "null";
        boolean isValid = false;

        if (!isLastMarkSpecial(calculation)) { isValid = true; }

        List<String> calculationList;
        calculationList = new ArrayList<>();



        if (isValid) {
            Log.i("Calculation", "mathCalculation: Is valid");
            boolean calculating = true;
            while (calculating) {
                Log.i("Calculation", calculation);
                double result = 0;

                List<String> operators;
                List<String> operatorLocations;
                operators = new ArrayList<>();
                operatorLocations = new ArrayList<>();

                for (int i=0; i < calculation.length(); i++) {
                    String charAt = "" + calculation.charAt(i);
                    boolean isNum = charAt.matches("\\d+(?:\\.\\d+)?");
                    if (!isNum && !charAt.equals(".")) {
                        operators.add(charAt);
                        operatorLocations.add(String.valueOf(i));
                    }
                }

                if (operators.isEmpty()) { answer = calculation; break;}

                Log.i("Operators", String.valueOf(operators));
                Log.i("OperatorsLocations", String.valueOf(operatorLocations));

                String mult = "x"; String div = "÷"; String plus = "+"; String min = "-";

                for (int i=0; i < operators.size(); i++) {
                    String operator = operators.get(i);
                    int operatorLocation = Integer.parseInt(operatorLocations.get(i));
                    getLocations(calculation, operatorLocation);
                    if (operator.equals(mult) || operator.equals(div)) {
                        result = basicCalculations(nextValue, prevValue, operator);
                        calculation = createNewCalculation(calculation, prevValueLocation, operatorLocation, nextValueLocation, result);
                        break;
                    } else if (operator.equals(plus) || operator.equals(min)) {
                        result = basicCalculations(nextValue, prevValue, operator);
                        calculation = createNewCalculation(calculation, prevValueLocation, operatorLocation, nextValueLocation, result);
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong with the calculation.", Toast.LENGTH_LONG).show(); break;
                    }
                }
            }
        } else {
            Log.i("Calculation", "mathCalculation: Is not valid");
        }

        return answer;
    }

    private String percentageOf(String calculation) { // Calculate percentageOf X
        int stopLocation = 0;
        int length = calculation.length();
        StringBuilder percentageCalculation = new StringBuilder();

        for (int i = length - 1; i >= 0; i--) { // Get the number we want to convert to percentage
            if (Character.isDigit(calculation.charAt(i))) { // If char is a number add it to calculation
                percentageCalculation.insert(0, calculation.charAt(i));
            } else { stopLocation = i;  i = 0; } // Else stop and mark the stop location
        }

        float number = Integer.parseInt(String.valueOf(percentageCalculation));
        float percent = number / 100; // Parse to float and calculate percentage
        String newCalculation;

        if (stopLocation != 0) { // If stop location not 0, snip the end of calculation and add the percentage to it.
            newCalculation = calculation.substring(0, calculation.length() - percentageCalculation.length());
            newCalculation = newCalculation + percent;
        } else { newCalculation = String.valueOf(percent); } // If stop location is 0, just set the percentage to calculation.

        return newCalculation;
    }

    private String createNewCalculation(String calculation, int prevValueLocation, int operatorLocation, int nextValueLocation, double result) {
        String newCalculation = "";
        for (int i=0; i < calculation.length(); i++) {
            if (i == prevValueLocation) { // Add calculation result
                newCalculation += String.valueOf(result);
            } else if (i != operatorLocation && i != nextValueLocation) { // If we aren't on previous- or nextValue location add number/operator to newCalculation
                newCalculation += calculation.charAt(i);
            }
        }

        Log.i("new calc", "New calculation: " + newCalculation);
        return newCalculation;
    }

    private double basicCalculations(double first, double second, String operator) {
        double resultValue = 0;
        switch (operator) { // Calculate with different operators
            case "+": resultValue = first + second; break;
            case "-": resultValue = first - second; break;
            case "x": resultValue = first * second; break;
            case "÷": resultValue = first / second; break;
        }
        return resultValue;
    }

    private void getLocations(String calculation, int operatorLocation) {
        prevValueLocation = operatorLocation - 1;
        nextValueLocation = operatorLocation + 1;

        prevValue = Double.parseDouble(String.valueOf(calculation.charAt(prevValueLocation)));
        nextValue = Double.parseDouble(String.valueOf(calculation.charAt(nextValueLocation)));
    }

    private void swiftAnswers(String answer, String calculation) { // Swifts answers up. Example: Answer1 = 22 so after next calculation Answer2 = 22
        for (int i=6; i>0; i--) {
            int nextValue = i-1;
            ((TextView) findViewById(answers[i])).setText(((TextView) findViewById(answers[nextValue])).getText());
        }
        answer1.setText(String.format("%s = %s", calculation, answer));
    }

    private boolean isLastMarkSpecial(String calculation) { // Checks if last mark is special
        boolean lastIsSpecial = false;

        if (!calculation.isEmpty()) {
            String lastChar = calculation.substring(calculation.length() - 1);
            if (lastChar.equals("+") || lastChar.equals("-") || lastChar.equals("÷") || lastChar.equals("x")) {
                lastIsSpecial = true;
            }
        }

        return lastIsSpecial;
    }

    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

}