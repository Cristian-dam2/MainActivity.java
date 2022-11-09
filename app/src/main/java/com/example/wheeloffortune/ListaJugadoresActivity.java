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
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class ListaJugadoresActivity extends AppCompatActivity {
    private static final String nombrefichero = "Jugadores.txt";
    private ArrayList<Jugador> jugadores;
    private RecyclerView tablaPuntos;
    private TextView pruebaText;
    private Button boton_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jugadores);
        pruebaText = (TextView) findViewById(R.id.textViewPrueba);
        boton_volver = (Button) findViewById(R.id.boton_volver);
        tablaPuntos =(RecyclerView) findViewById(R.id.myRecycler);

        tablaPuntos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));



      // Jugador gx = new Jugador("BOLSONARO",250);
       // guardarPuntuacionJugador(gx);
        jugadores = recuperarInformacion();
        Datos adapter = new Datos(jugadores);
        tablaPuntos.setAdapter(adapter);
        boton_volver.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { finish(); } });




    }




    public void guardarPuntuacionJugador(Jugador jugador) {
        FileOutputStream fos = null;
        String jugadorString = Fichero.formatearJugadorToTxt(jugador);
        try {
            fos = openFileOutput(nombrefichero, MODE_APPEND);
            fos.write(jugadorString.getBytes());
            Log.d("TAG1", "Fichero guardado en: " + getFilesDir() + "/" + nombrefichero);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cerrarFlujo(fos);
        }

    }

    private void cerrarFlujo(Closeable c1) {
        try {
            if (c1 != null) {
                c1.close();
            }
        } catch (IOException ex) {
            Log.d("TAG2", "NO SE PUDO CERRAR EL FLUJO");
        }
    }

    public  ArrayList<Jugador> recuperarInformacion() {
        FileInputStream fos = null;
        ArrayList<Jugador> collectorJugadores = new ArrayList<>();
        int contador = 0;
        try {
            fos = openFileInput(nombrefichero);
            InputStreamReader inputStreamReader = new InputStreamReader(fos);
            BufferedReader bw = new BufferedReader(inputStreamReader);
            String linea;
            Jugador jugador1;

            StringBuilder sB = new StringBuilder();
            while ((linea = bw.readLine()) != null) {
                sB.append(linea);
                jugador1 = Fichero.formateartxtToJugador(sB.toString());
                collectorJugadores.add(jugador1);
                Collections.sort(collectorJugadores);

                jugador1 = null;
                linea = "";
                sB = new StringBuilder();



            }
            Collections.sort(collectorJugadores);

        } catch (IOException e) {
            Log.d("TAG1", "NO PUDO RECUPERAR JUGADORES");
        } finally {
            cerrarFlujo(fos);
        }

        return reservarTOP10(collectorJugadores);
    }

    private ArrayList<Jugador>  reservarTOP10(ArrayList<Jugador> entrada){
        ArrayList<Jugador> auxiliar = new ArrayList<>();
        int contador = 0;
        for (int i = 0; i < entrada.size(); i++) {
            auxiliar.add(entrada.get(contador));
            contador++;
            if(contador ==10){
                return auxiliar;
            }
        }
        return auxiliar;
    }
}