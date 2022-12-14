package com.example.wheeloffortune;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Fichero extends AppCompatActivity {
    private static final String nombre_fichero = "Jugadores.txt";
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


    public void guardarJugador(Jugador jugador) {
        FileOutputStream fos = null;
        String texto = jugador.toStringJugador();
        Log.d("INFO PARA ESCRIBIR", texto);

        try {
            fos = getContext().openFileOutput(nombre_fichero, MODE_APPEND);
            fos.write(texto.getBytes());
            Log.d("TAG", "Datos guardados en: " + getContext().getFilesDir() + "/" + nombre_fichero);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CerrarFlujos.cerrarStream(fos);
        }
    }


    public boolean existeArchivo(){
        String ruta = getContext().getFilesDir() + "/" + nombre_fichero;
        return new File(ruta).exists();
    }

    public void crearFichero() {
        FileOutputStream fos = null;

        try {
            fos = getContext().openFileOutput(nombre_fichero, MODE_APPEND);
            fos.write("".getBytes());
            System.out.println("SE CREO EL FICHERO POR PRIMERA VEZ");
            Log.d("ARCHIVO NUEVO", "SE CREO EL ARCHIVO" + getContext().getFilesDir() + File.separator + nombre_fichero);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CerrarFlujos.cerrarStream(fos);
        }
    }


    public ArrayList<Jugador> leerJugador() {
        FileInputStream fos = null;
        ArrayList<Jugador> jugadores = new ArrayList<>();
        StringBuilder sB = new StringBuilder();
        try {
            fos = getContext().openFileInput(nombre_fichero);

            BufferedReader br = new BufferedReader(new InputStreamReader(fos));
            String linea;
            Jugador jugador;

            while (fos.available() > 0) {
                linea = br.readLine();
                sB.append(linea);
                System.out.println(sB);
                jugador = Jugador.recuperarJugador(sB.toString());
                jugadores.add(jugador);
                sB = new StringBuilder();
            }
            Collections.sort(jugadores);

        } catch (IOException e) {
            Log.d("TAG", "NO PUDO RECUPERAR JUGADORES");
        } finally {
            CerrarFlujos.cerrarStream(fos);
        }

        return mejoresJugadores(jugadores, 10);
    }

    private ArrayList<Jugador> mejoresJugadores(ArrayList<Jugador> entrada, int maximo) {
        ArrayList<Jugador> mejoresJugadores = new ArrayList<>();
        for (int i = 0; i < entrada.size() && i < maximo; i++) {
            mejoresJugadores.add(entrada.get(i));
        }
        return mejoresJugadores;
    }


}
