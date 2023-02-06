package com.example.wheeloffortune.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wheeloffortune.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText contraseña;
    private Button verHistorial;
    private Button botonIniciarJuego;
    //private Fichero file = new Fichero(this);
    private FirebaseAuth mAuth;
    private Button boton_registro;
    private FirebaseFirestore db;
    private String nombre = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = (EditText) findViewById(R.id.login_campo_nombre);
        contraseña = (EditText) findViewById(R.id.editcontraseña);
        verHistorial = (Button) findViewById(R.id.btnVerHistorial);
        mAuth = FirebaseAuth.getInstance();
        boton_registro = (Button) findViewById(R.id.boton_registro);
        db = FirebaseFirestore.getInstance();
        botonIniciarJuego = (Button) findViewById(R.id.login_boton_iniciar_sesion);
        botonIniciarJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });
//        if (file.existeArchivo() == false){
//            file.crearFichero();
//        }
    }

    public void verListaJugadores(View view){
        Intent myIntent = new Intent(this,ListaJugadoresActivity.class);
        startActivity(myIntent);
        limpiarInformacion();
    }


    public void iniciarSesion() {
        String correo = email.getText().toString();
        String password= contraseña.getText().toString();

        if (correo.isEmpty()) {
            email.setError("Debe introducir un email");
            return;
        }
        if (password.isEmpty()) {
            contraseña.setError("Debe introducir una contraseña");
            return;
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        botonIniciarJuego.setEnabled(false);
        mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    botonIniciarJuego.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Credenciales correctas, bienvenido.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    limpiarInformacion();

                } else {
                    botonIniciarJuego.setEnabled(true);
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void iniciarRegistro(View view) {

        Intent myIntent = new Intent(this, registroActivity.class);
        startActivity(myIntent);
    }


    private void limpiarInformacion(){
        email.setText("");
        contraseña.setText("");
    }



}