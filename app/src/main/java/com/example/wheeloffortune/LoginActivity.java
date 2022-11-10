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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        nombre = (EditText) findViewById(R.id.login_campo_nombre);
        verHistorial = (Button) findViewById(R.id.btnVerHistorial);
    }


    public void iniciarJuego(View view) {
        if(nombre.getText().toString().equals("") || !Character.isLetter(nombre.getText().toString().charAt(0))){
            Toast.makeText(this,"Introduce un nombre antes de empezar", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("nombre_usuario", nombre.getText().toString());
        nombre.setText("");
        startActivity(myIntent);
    }
    public void verHistorial(View view){
        Intent myIntent = new Intent(this,ListaJugadoresActivity.class);
        //myIntent.putExtra("nombre_usuario", nombre.getText().toString());
        nombre.setText("");
        startActivity(myIntent);
    }
}