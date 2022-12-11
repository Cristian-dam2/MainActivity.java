package com.example.wheeloffortune;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Fichero extends AppCompatActivity {
    private static final String nombrefichero = "Jugadores.txt";
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Fichero(Context context) {
        //SI NO TENGO EL CONTEXT DE LA ACTIVIDAD DE DONDE LA ESTOY LLAMANDO, ME GENERARÁ UN ERROR.
        this.context = context;
    }


    public void guardarPuntuacion(Jugador jugador) {
        FileOutputStream fos = null;
        System.out.println(jugador.toStringJugador());
        try {
            fos = getContext().openFileOutput(nombrefichero, MODE_APPEND);
            fos.write(jugador.toStringJugador().getBytes());
            Log.d("TAG", "Fichero guardado en: " + getContext().getFilesDir() + "/" + nombrefichero);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cerrarFlujo(fos);
        }

    }


    public static void cerrarFlujo(Closeable c1) {
        try {
            if (c1 != null) {
                c1.close();
            }
        } catch (IOException ex) {
            Log.d("404", "Error cerrando un stream");
        }
    }



    public ArrayList<Jugador> recuperarInformacion() {
        FileInputStream fos = null;
        ArrayList<Jugador> jugadores = new ArrayList<>();
        StringBuilder sB = new StringBuilder();
        try {
            fos = getContext().openFileInput(nombrefichero);
            InputStreamReader inputStreamReader = new InputStreamReader(fos);
            BufferedReader bw = new BufferedReader(inputStreamReader);
            String linea;
            Jugador jugador;
            while ((linea = bw.readLine()) != null) {
                sB.append(linea);
                System.out.println(sB);
                jugador = Jugador.recuperarJugador(sB.toString());
                jugadores.add(jugador);
                sB = new StringBuilder();
                linea = "";
            }
            Collections.sort(jugadores);

        } catch (IOException e) {
            Log.d("TAG", "NO PUDO RECUPERAR JUGADORES");
        } finally {
           cerrarFlujo(fos);
        }

        return mejoresJugadore(jugadores);
    }

    private ArrayList<Jugador> mejoresJugadore(ArrayList<Jugador> entrada){
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
