package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class ListaJugadoresActivity extends AppCompatActivity {
    private static final String nombrefichero = "Jugadores.txt";
    private ArrayList<Jugador> jugadores;
    private RecyclerView tablaNombres;
    private TextView pruebaText;
    private Button boton_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jugadores);
        pruebaText = (TextView) findViewById(R.id.textViewPrueba);
        boton_volver = (Button) findViewById(R.id.boton_volver);
        tablaNombres =(RecyclerView) findViewById(R.id.myRecycler);
        tablaNombres.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        jugadores = recuperarInformacion();
        Datos adapter = new Datos(jugadores);
        tablaNombres.setAdapter(adapter);
        boton_volver.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { finish(); } });
    }


    public  ArrayList<Jugador> recuperarInformacion() {
        FileInputStream fos = null;
        ArrayList<Jugador> collectorJugadores = new ArrayList<>();
        try {
            fos = openFileInput(nombrefichero);
            InputStreamReader inputStreamReader = new InputStreamReader(fos);
            BufferedReader bw = new BufferedReader(inputStreamReader);
            String linea;
            Jugador jugador;
            StringBuilder sB = new StringBuilder();
            while ((linea = bw.readLine()) != null) {
                sB.append(linea);
                jugador = Fichero.formateartxtToJugador(sB.toString());
                collectorJugadores.add(jugador);
                Collections.sort(collectorJugadores);
                jugador = null;
                linea = "";
                sB = new StringBuilder();
            }
            Collections.sort(collectorJugadores);

        } catch (IOException e) {
            Log.d("TAG1", "NO PUDO RECUPERAR JUGADORES");
        } finally {
            Fichero.cerrarFlujo(fos);
        }

        return best10Players(collectorJugadores);
    }

    private ArrayList<Jugador> best10Players(ArrayList<Jugador> entrada){
        ArrayList<Jugador> mejoresJugadores = new ArrayList<>();
        int contador = 0;
        for (int i = 0; i < entrada.size(); i++) {
            mejoresJugadores.add(entrada.get(contador));
            contador++;
            if(contador ==10){
                return mejoresJugadores;
            }
        }
        return mejoresJugadores;
    }
}