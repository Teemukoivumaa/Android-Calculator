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
    private TextView outputValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputValues = findViewById(R.id.calculation);
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
        String newCalculation = "";

        Button b = (Button)view;
        String buttonText = b.getText().toString();

        switch (buttonText) {
            case "C":
                newCalculation = ""; inputValues.setText(newCalculation);
                break;
            case "Erase":
                if (!calculation.isEmpty()) { newCalculation = removeLastChar(calculation); inputValues.setText(newCalculation); }
                break;
            case "%":
                if (!isPreviousMarkSpecial(calculation)) {
                    newCalculation = presentageOf(calculation);
                    inputValues.setText(newCalculation);
                }
                break;
            case "+":
            case "-":
            case "÷":
            case "x":
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

    private String presentageOf(String calculation) {
        float percent = 0;
        int stopLocation = 0;
        StringBuilder precentageCalculation = new StringBuilder();

        for (int i = calculation.length() - 1; i >= 0; i--) {
            if (Character.isDigit(calculation.charAt(i))) {
                Log.i("Numerot", String.valueOf(calculation.charAt(i)));
                precentageCalculation.insert(0, calculation.charAt(i));
            } else { stopLocation = i; i = 0; }
        }

        float nums = Integer.parseInt(String.valueOf(precentageCalculation));
        percent = nums / 100;

        String newCalculation = calculation.substring(0, calculation.length() - stopLocation) + percent;

        return newCalculation;
    }

    private Boolean isPreviousMarkSpecial(String calculation) {
        Boolean previousSpecial = false;
        String lastChar = calculation.substring(calculation.length() - 1);
        Toast.makeText(MainActivity.this, lastChar, Toast.LENGTH_LONG).show();
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