package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ListaJugadoresActivity extends AppCompatActivity {
    private static final String nombrefichero = "Lista de puntuacion.txt";
    private ArrayList<Jugador> jugadores;
    private RecyclerView tablaNombre;
    private RecyclerView tablaPuntos;
    private TextView pruebaText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jugadores);
        guardarPuntuacionJugador2();
        pruebaText = (TextView) findViewById(R.id.textView);
        recuperarInformacion();
//        System.out.println(fichero.getFichero().exists());
//        tablaNombre = (RecyclerView) findViewById(R.id.TablaNombre);
//        tablaNombre.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        jugadores = fichero.recuperarJugadores();
//        //tablaNombre.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
//        //tablaPuntos = (RecyclerView) findViewById(R.id.TablePuntos);
//        Datos datos = new Datos(jugadores);
//        tablaNombre.setAdapter(datos);
    }


    private void guardarPuntuacionJugador2(){
        FileOutputStream fos = null;
        String st1  = "hola";
        try {
            fos = openFileOutput(nombrefichero,MODE_PRIVATE);
            fos.write(st1.getBytes());
            Log.d("TAG1","Fichero guardado en: " + getFilesDir() + "/" + nombrefichero);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            cerrarFlujo(fos);
        }

    }

    private void cerrarFlujo(Closeable c1) {
        try {
            if (c1 != null) {
                c1.close();
            }
        } catch (IOException ex) {

        }
    }

    private void recuperarInformacion(){
        FileInputStream fos = null;

        try {
            fos = openFileInput(nombrefichero);
            InputStreamReader inputStreamReader = new InputStreamReader(fos);
            BufferedReader bw = new BufferedReader(inputStreamReader);
            String linea;
            pruebaText.setText("MARICA");
            StringBuilder sB = new StringBuilder();
            while((linea = bw.readLine()) != null){
                sB.append(linea).append("\n");

            }

            pruebaText.setText(linea);

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            cerrarFlujo(fos);
        }

    }
}