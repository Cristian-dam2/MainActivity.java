package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText nombre;
    private Button verHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        nombre = (EditText) findViewById(R.id.login_campo_nombre);
        verHistorial = (Button) findViewById(R.id.btnVerHistorial);


    }

    public void iniciarJuego(View view) {
    }

    public void enviar(View view){
        Intent myIntent = new Intent(this,MainActivity.class);
        myIntent.putExtra("nombreusuario",nombre.getText().toString());
        startActivity(myIntent);

    }
    public void verHistorial(View view){
        Intent myIntent = new Intent(this,ListaJugadoresActivity.class);
        myIntent.putExtra("nombreusuario",nombre.getText().toString());
        startActivity(myIntent);

    }

}