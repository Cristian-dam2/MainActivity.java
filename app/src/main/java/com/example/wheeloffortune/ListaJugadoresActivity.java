package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ListaJugadoresActivity extends AppCompatActivity {
    private Fichero fichero;
    private ArrayList<Jugador> jugadores;
    private RecyclerView tablaNombre;
    private RecyclerView tablaPuntos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jugadores);
        fichero = new Fichero();
        jugadores = fichero.recuperarJugadores();
        tablaNombre = (RecyclerView) findViewById(R.id.TablaNombre);
        tablaNombre.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        tablaPuntos = (RecyclerView) findViewById(R.id.TablePuntos);


    }private void setJugadoresTablas(){
        int contador = 0;
        for (int i = 0; i < jugadores.size(); i++) {

        }

    }
}