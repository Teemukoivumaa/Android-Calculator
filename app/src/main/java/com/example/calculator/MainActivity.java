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
    }

    public void addNumber(View view) {
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
            case "C":
                newCalculation = "";
                if (calculation.equals("")) {
                    for (int i=0; i<7; i++) {
                        ((TextView) findViewById(answers[i])).setText(newCalculation);
                    }
                }
                inputValues.setText(newCalculation);
                break;
            case "Erase":
                if (!calculation.isEmpty()) { newCalculation = removeLastChar(calculation); inputValues.setText(newCalculation); }
                break;
            case "%":
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
                    calculation = removeLastChar(calculation);
                }
                newCalculation = calculation + buttonText;
                inputValues.setText(newCalculation);
                break;
            default:
                Toast.makeText(MainActivity.this, "Unknown value.", Toast.LENGTH_LONG).show();
        }
    }

    private String percentageOf(String calculation) {
        float percent;
        int stopLocation = 0;
        int length = calculation.length();
        String newCalculation;
        StringBuilder percentageCalculation = new StringBuilder();

        Log.i("Calculation", calculation);
        Log.i("Calculation lenght", String.valueOf(calculation.length()));

        for (int i = length - 1; i >= 0; i--) {
            if (Character.isDigit(calculation.charAt(i))) {
                Log.i("Numerot", String.valueOf(calculation.charAt(i)));
                percentageCalculation.insert(0, calculation.charAt(i));
            } else { stopLocation= i;  i = 0; }
        }

        float nums = Integer.parseInt(String.valueOf(percentageCalculation));
        percent = nums / 100;

        if (stopLocation != 0) {
            Log.i("Calculation", calculation);
            newCalculation = calculation.substring(0, calculation.length() - percentageCalculation.length());
            Log.i("New", newCalculation);
            newCalculation = newCalculation + percent;
        } else { newCalculation = String.valueOf(percent); }

        return newCalculation;
    }

    private void swiftAnswers(String answer, String calculation) {
        for (int i=6; i>0; i--) {
            int nextValue = i-1;
            ((TextView) findViewById(answers[i])).setText(((TextView) findViewById(answers[nextValue])).getText());
        }

        answer1.setText(String.format("%s = %s", calculation, answer));
    }

    private Boolean isPreviousMarkSpecial(String calculation) {
        Boolean previousSpecial = false;
        String lastChar = calculation.substring(calculation.length() - 1);
        if (lastChar.equals("+") || lastChar.equals("-") || lastChar.equals("÷") || lastChar.equals("x")) {
            previousSpecial = true;
        }
        return previousSpecial;
    }

    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

     /*
        for (int i = 0; i < calculation.length(); i++) {
            System.out.println(calculation.charAt(i));
            if (Character.isLetter(calculation.charAt(i))) {
                lastLetterLocation = i;
            }
        }
        */
}