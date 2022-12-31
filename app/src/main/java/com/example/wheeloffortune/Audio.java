package com.example.wheeloffortune;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

public class Audio extends AppCompatActivity {
    private Context context;
    private final int segundoExtra = 1500;
    private Esperar esperando = new Esperar();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Audio(Context context) {
        this.context = context;
    }


    public Audio (){

    }
    public void Victoria() {
        MediaPlayer win = MediaPlayer.create(getContext(), R.raw.victoriasound);
        win.start();
        //SE AGREGA UN SEGUNDO AL PARA FINALIZAR LA MUSICA.
        esperando.segundos(win.getDuration()  + segundoExtra);

    }

    public void Correcto() {
        MediaPlayer correct = MediaPlayer.create(getContext(), R.raw.acierto);
        correct.start();
    }

    public void Incorrecto() {
        MediaPlayer lose = MediaPlayer.create(getContext(), R.raw.losesound);
        lose.start();
    }

    public void Giro() {

        MediaPlayer giro = MediaPlayer.create(getContext(),R.raw.spinningeffect);
        giro.start();

    }


    public int getRawId(String name) {
        Class<?> c = R.raw.class;
        Field f = null;
        int id = 0;

        try {
            f = R.raw.class.getField(name);
            id = f.getInt(null);
        } catch (NoSuchFieldException e) {
            Log.i("Reflection", "Missing raw " + name);
        } catch (IllegalAccessException e) {
            Log.i("Reflection", "Illegal access to field " + name);
        }

        return id;
    }

    public void musicaVictoria(Palabra palabra){
        if (palabra.isMusicapersonalizada()) {
            int id = getRawId(palabra.getPalabra().toLowerCase());
            MediaPlayer music = MediaPlayer.create(getContext(), id);
            music.start();
            //SE AGREGA UN SEGUNDO AL PARA FINALIZAR LA MUSICA.
            esperando.segundos(music.getDuration()  + segundoExtra);

        } else {
            Victoria();
        }
    }
}
