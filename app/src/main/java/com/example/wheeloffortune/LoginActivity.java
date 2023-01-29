package com.example.wheeloffortune;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText contraseña;
    private Button verHistorial;
    private Button botonIniciarJuego;
    private Fichero file = new Fichero(this);
    private FirebaseAuth mAuth;
    private Button boton_registro;


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


//        nombre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iniciarJuego();
//            }
//        });

       botonIniciarJuego = (Button) findViewById(R.id.login_boton_iniciar_sesion);
//        botonIniciarJuego.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iniciarJuego();
//            }
//        });
        if (file.existeArchivo() == false){
            file.crearFichero();
        }
    }


//    public void iniciarJuego() {
//        if(nombre.getText().toString().equals("") || !Character.isLetter(nombre.getText().toString().charAt(0))){
//            Toast.makeText(this,"Introduce un nombre antes de empezar", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent myIntent = new Intent(this, MainActivity.class);
//        myIntent.putExtra("nombre_usuario", nombre.getText().toString());
//        startActivity(myIntent);
//        nombre.setText("");
//
//
//    }
    public void verListaJugadores(View view){
        Intent myIntent = new Intent(this,ListaJugadoresActivity.class);
        email.setText("");
        contraseña.setText("");
        startActivity(myIntent);
    }


    public void iniciarSesion(View view) {
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
        String nombre = "";
                    for (int i = 0; i < correo.length(); i++) {
                        if(correo.charAt(i) != '@'){
                            nombre = nombre + correo.charAt(i);
                        }else{
                            break;
                        }

                    }
        intent.putExtra("nombre" , nombre.toUpperCase());
        System.out.println("XXXXXXXXXXXXXXXXX      -     " +  nombre);

        botonIniciarJuego.setEnabled(false);
        //barra_progreso.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //barra_progreso.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Credenciales correctas, bienvenido.", Toast.LENGTH_SHORT).show();

                    startActivity(intent);
                    return;
                }

                //barra_progreso.setVisibility(View.INVISIBLE);
                botonIniciarJuego.setEnabled(true);
                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
 });
}
//    public void iniciarRegistro(View view) {
//
//        Intent myIntent = new Intent(this, registroActivity.class);
//        startActivity(myIntent);
//
//
//    }
}