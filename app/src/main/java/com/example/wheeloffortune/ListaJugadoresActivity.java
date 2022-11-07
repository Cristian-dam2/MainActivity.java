package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
    private Button boton_xd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jugadores);
        guardarPuntuacionJugador2();
        pruebaText = (TextView) findViewById(R.id.textViewPrueba);
        boton_xd = (Button) findViewById(R.id.boton_volver);
        boton_xd.setText(recuperarInformacion());
//        recuperarInformacion();
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
        String st1  = "nada12";
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

    public String  recuperarInformacion(){
        FileInputStream fos = null;
        try {
            fos = openFileInput(nombrefichero);
            InputStreamReader inputStreamReader = new InputStreamReader(fos);
            BufferedReader bw = new BufferedReader(inputStreamReader);
            String linea;

            StringBuilder sB = new StringBuilder();
            while((linea = bw.readLine()) != null){
                sB.append(linea).append("\n");

            }

            return sB.toString();



        } catch (IOException e) {
            e.printStackTrace();
        } finally{

            cerrarFlujo(fos);
        }

     return "nada";
    }
}