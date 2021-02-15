package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// https://github.com/Teemukoivumaa/bankingApplication/blob/master/app/src/main/java/com/example/bankingapplication/CurrencyConverter.java

public class MainActivity extends AppCompatActivity {

    private TextView inputValues;
    private TextView answer1;

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
        answer1 = findViewById(R.id.answer1);
        // only answer1 is needed because we set it manually instead of for loop
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
        String newCalculation, answer = "";

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
                if (!isPreviousMarkSpecial(calculation)) {
                    newCalculation = percentageOf(calculation);
                    inputValues.setText(newCalculation);
                }
                break;

            case "=":
                answer = "0";
                swiftAnswers(answer, calculation);
                inputValues.setText(answer);
                break;

            case "+":
            case "-":
            case "÷":
            case "x":
            case ".":
                if (isPreviousMarkSpecial(calculation)) {
                    // If last mark is special replace it with the new one
                    calculation = removeLastChar(calculation);
                } // else just add the button text to calculation
                newCalculation = calculation + buttonText;
                inputValues.setText(newCalculation);
                break;

            default: // Default value, should never happen.
                Toast.makeText(MainActivity.this, "Unknown value.", Toast.LENGTH_LONG).show();
        }
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

    private void swiftAnswers(String answer, String calculation) { // Swifts answers up. Example: Answer1 = 22 so after next calculation Answer2 = 22
        for (int i=6; i>0; i--) {
            int nextValue = i-1;
            ((TextView) findViewById(answers[i])).setText(((TextView) findViewById(answers[nextValue])).getText());
        }
        answer1.setText(String.format("%s = %s", calculation, answer));
    }

    private boolean isPreviousMarkSpecial(String calculation) { // Checks if last mark is special
        boolean previousSpecial = false;
        String lastChar = calculation.substring(calculation.length() - 1);
        if (lastChar.equals("+") || lastChar.equals("-") || lastChar.equals("÷") || lastChar.equals("x")) {
            previousSpecial = true;
        }
        return previousSpecial;
    }

    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

}