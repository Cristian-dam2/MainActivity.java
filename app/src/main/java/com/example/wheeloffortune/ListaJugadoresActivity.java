package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaJugadoresActivity extends AppCompatActivity {
    private ArrayList<Jugador> jugadores;
    private RecyclerView tablaNombres;
    private TextView pruebaText;
    private Button boton_volver;
    private Fichero fichero = new Fichero(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jugadores);
        pruebaText = (TextView) findViewById(R.id.textViewPrueba);
        boton_volver = (Button) findViewById(R.id.boton_volver);
        tablaNombres = (RecyclerView) findViewById(R.id.myRecycler);
        tablaNombres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        jugadores = fichero.leerJugador();
        Datos adapter = new Datos(jugadores);
        tablaNombres.setAdapter(adapter);
        boton_volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


}