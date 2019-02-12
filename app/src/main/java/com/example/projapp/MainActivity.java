package com.example.projapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;




public class MainActivity extends AppCompatActivity {

    private TextView comando;
    private String resultat; //el que he parlat
    private String IP = "192.168.0.148";
    private int PORT = 5500;
    private TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayip(IP);
        displayport(PORT);
        comando = (TextView) findViewById(R.id.command);
         response = (TextView) findViewById(R.id.response);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }
    public void setIPPORT (View view) {
    response.setText("Valors de IP i PORT actualitzats correctament.");
    EditText ipdec = (EditText) findViewById(R.id.ip);
    EditText portdec = (EditText) findViewById(R.id.port);
    IP = ipdec.getText().toString();
    PORT = Integer.parseInt(portdec.getText().toString());


    }

    public void displayip(String ip){
        EditText find_ip = (EditText) findViewById(R.id.ip);
        find_ip.setText(String.valueOf(ip));

    }

    public void displayport(int port){
        EditText find_port = (EditText) findViewById(R.id.port);
        find_port.setText(String.valueOf(port));

    }

    public void getSpeechInput(View view) {
        setIPPORT(view);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "No li agrada l'input", Toast.LENGTH_LONG).show();
        }

    }



    public void envia_reb (){   //https://www.youtube.com/watch?v=6kPOhWqHqZc   per rebre
        log(" socket " + IP + " " + PORT);
        try {
            Socket sk = new Socket(IP, PORT);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(sk.getInputStream()));
            PrintWriter salida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()));
            log("enviando... ");
            salida.println(resultat);
            log("recibiendo ... " + entrada.readLine());
            sk.close();
        } catch (Exception e) {
            log("error: " + e.toString());
        }
    }

    private void log(String string) {
        response.append(string + "\n");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case 10:
                    if(resultCode == RESULT_OK && data != null){
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        resultat = result.get(0);
                        envia_reb();
                    }

                    break;
            }
    }
}
