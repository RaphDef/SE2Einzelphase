package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.core.Observable;

public class MainActivity extends AppCompatActivity {

    public static final String server = "se2-isys.aau.at";
    public static final int port = 53212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add EventListener to Send-Button
        Button send = findViewById(R.id.btnSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mat_input = findViewById(R.id.matInput);
                String mat = mat_input.getText().toString();
                observer(mat);
            }
        });

        //Add EventListener to CalculateButton (Aufgabe3)
        Button calc = findViewById(R.id.btnCalc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mat_input = findViewById(R.id.matInput);
                String mat = mat_input.getText().toString();
                calculate(mat);
            }
        });
    }

    public void observer(String mat) {
        createConnection(mat)
                //Thread manager, can be used for asynchronously performing blocking IO
                .subscribeOn(Schedulers.io())
                //subscribe on I/O thread and observe on UI thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    /*The Observable will emit items back to the Observer, causing the “onNext”
                     * method to be called with access to the item emitted. If the Observable is
                     * finished emitting objects, “onCompleted” is called. If an exception is
                     * thrown at any time, “onError” is called with that exception, allowing for
                     * easy error handling.
                     */
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull String response) {
                        TextView serverResp = findViewById(R.id.serverResponse);
                        serverResp.setText(response);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Connection-Task completed");
                    }
                });
    }

    public Observable<String> createConnection(String mat) {
        return Observable.defer(() -> {
            //like a promise in JS
            //defer does not create the Observable until the observer subscribes, and create a fresh Observable for each observer
            try {
                Socket con = new Socket(server, port);
                PrintWriter pw = new PrintWriter(con.getOutputStream());
                pw.println(mat);
                pw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response = br.readLine();

                pw.close();
                br.close();
                con.close();

                //just converts the String into an Observable that emits this String
                return Observable.just(response);

            } catch (IOException e) {
                return Observable.just(e.getMessage());
            }
        });
    }

    public void calculate(String mat) {
        int[] matNumbers = new int[mat.length()];

        for (int i = 0; i < matNumbers.length; i++) {
            matNumbers[i] = mat.charAt(i) - 48;
            //char to int: '1' char == 49 int
        }

        StringBuilder finalMat = new StringBuilder();

        for (int i = 0; i < matNumbers.length; i++) {
            if (i % 2 == 0) {
                finalMat.append(matNumbers[i]);
            } else {
                char temp = (char) (matNumbers[i] + 96);
                finalMat.append(temp);
                //1 + 96 = 97: 97 int == 'a' char
            }
        }
        TextView matOutput = findViewById(R.id.calcText);
        matOutput.setText(finalMat);
    }
}