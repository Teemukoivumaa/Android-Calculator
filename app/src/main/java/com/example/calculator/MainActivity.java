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

        Log.i("Test", buttonText);
        valueText = valueText + buttonText;
        inputValues.setText(valueText);
    }

    public void specialMarkers(View view) {
        String valueText = inputValues.getText().toString();

        Button b = (Button)view;
        String buttonText = b.getText().toString();

        switch (buttonText) {
            case "C":
                valueText = ""; inputValues.setText(valueText);
                break;
            case "Erase":
                if (!valueText.isEmpty()) { valueText = removeLastChar(valueText); inputValues.setText(valueText); }
                break;
            default:
                Toast.makeText(MainActivity.this, "Unknown value.", Toast.LENGTH_LONG).show();
        }

        inputValues.setText(valueText);
    }

    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}