package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText nombre;
    private Button verHistorial;
    private Button botonIniciarJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        nombre = (EditText) findViewById(R.id.login_campo_nombre);
        verHistorial = (Button) findViewById(R.id.btnVerHistorial);
        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarJuego();
            }
        });

        botonIniciarJuego = (Button) findViewById(R.id.login_boton_iniciar_sesion);
        botonIniciarJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarJuego();
            }
        });

    }


    public void iniciarJuego() {
        if(nombre.getText().toString().equals("") || !Character.isLetter(nombre.getText().toString().charAt(0))){
            Toast.makeText(this,"Introduce un nombre antes de empezar", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("nombre_usuario", nombre.getText().toString());
        startActivity(myIntent);
        nombre.setText("");


    }
    public void verListaJugadores(View view){
        Intent myIntent = new Intent(this,ListaJugadoresActivity.class);
        nombre.setText("");
        startActivity(myIntent);
    }
}