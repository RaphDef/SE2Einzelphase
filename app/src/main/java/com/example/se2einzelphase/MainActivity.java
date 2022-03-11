package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add EventListener to CalculateButton (Aufgabe3)
        Button calc = (Button) findViewById(R.id.btnCalc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mat_input = findViewById(R.id.matInput);
                String mat = mat_input.getText().toString();
                calculate(mat);
            }
        });
    }

    public void calculate(String mat) {
        int[] matNumbers = new int[mat.length()];

        for (int i = 0; i < matNumbers.length; i++) {
            matNumbers[i] = mat.charAt(i) - 48;
        }

        StringBuilder finalMat = new StringBuilder();

        for (int i = 0; i < matNumbers.length; i++) {
            if (i % 2 == 0) {
                finalMat.append(matNumbers[i]);
            } else {
                char temp = (char) (matNumbers[i] + 96);
                finalMat.append(temp);
            }
        }
        TextView matOutput = findViewById(R.id.calcText);
        matOutput.setText(finalMat);
    }
}