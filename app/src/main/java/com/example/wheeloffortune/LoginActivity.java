package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        nombre = (EditText) findViewById(R.id.login_campo_nombre);


    }

    public void iniciarJuego(View view) {
    }

    public void enviar(View view){
        Intent myIntent = new Intent(this,MainActivity.class);
        myIntent.putExtra("nombreusuario",nombre.getText().toString());
        startActivity(myIntent);

    }

}