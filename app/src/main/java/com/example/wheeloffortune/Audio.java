package com.example.wheeloffortune;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

/**
 * Clase para cargar sonido. Hay que instanciar un objeto y luego llamar sus métodos para cargar lo música.
 */
public class Audio extends AppCompatActivity {
    private final int SEGUNDO_EXTRA = 1500;

    private Context context;
    private MediaPlayer reproducir;
    private Esperar esperando = new Esperar();

    // Getter y Setter
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    // Constructores
    public Audio(Context context) {
        this.context = context;
    }


    public void Victoria() {
        reproducir = MediaPlayer.create(getContext(), R.raw.victoriasound);
        reproducir.start();

        //SE AGREGA UN SEGUNDO AL PARA FINALIZAR LA MUSICA.
        esperando.segundos(reproducir.getDuration()  + SEGUNDO_EXTRA);
    }
    /**
     * Reproduce el archivo /app/src/main/res/raw/acierto.mp3
     */
    public void Correcto() {
        reproducir = MediaPlayer.create(getContext(), R.raw.acierto);
        reproducir.start();
    }
    /**
     * Reproduce el archivo /app/src/main/res/raw/losesound.mp3
     */
    public void Incorrecto() {
        reproducir = MediaPlayer.create(getContext(), R.raw.losesound);
        reproducir.start();
    }
    /**
     * Reproduce el archivo /app/src/main/res/raw/spinningeffect.mp3
     */
    public void Giro() {
        reproducir = MediaPlayer.create(getContext(), R.raw.spinningeffect);
        reproducir.start();
    }
    /**
     * Reproduce el archivo /app/src/main/res/raw/acierto.mp3
     * @param palabra La instancia de palabra tiene un atributo que indica si tiene música personalizada.
     *                Si la tiene, buscará el nombre del archivo según la palabra guardada en el objeto,
     *                la cual será leída en minúsculas.
     *                (Ej.: Si palabra.musicaPersonalizada = true entonces Audio cargado: yatra.mp3)
     */
    public void musicaVictoria(Palabra palabra){
        if (palabra.isMusicaPersonalizada()) {
            int id = getRawId(palabra.getPalabra().toLowerCase());

            MediaPlayer music = MediaPlayer.create(this.getContext(), id);
            music.start();

            //SE AGREGA UN SEGUNDO AL PARA FINALIZAR LA MUSICA.
            esperando.segundos(music.getDuration() + SEGUNDO_EXTRA);
        } else {
            this.Victoria();
        }
    }

    /**
     * Obtiene el ID del archivo usando el nombre que recibe de entrada. Este método lo usa musicaVictoria(Palabra)
     * cuando ha determinado que la música es personalizada y no sabe el ID del archivo para cargar.
     * @param name Nombre del archivo a buscar
     * @return ID con el archivo o 0 si no se pudo encontrar el archivo
     */
    public int getRawId(String name) {
        try {
            Field f = R.raw.class.getField(name);
            return f.getInt(null);
        } catch (NoSuchFieldException e) {
            Log.i("Reflection", "Missing raw " + name);
        } catch (IllegalAccessException e) {
            Log.i("Reflection", "Illegal access to field " + name);
        }

        return 0;
    }
}
