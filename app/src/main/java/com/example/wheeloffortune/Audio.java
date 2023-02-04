package com.example.wheeloffortune;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wheeloffortune.Auxiliares.Esperar;

import java.lang.reflect.Field;

/**
 * Clase para cargar sonido. Para usar sus métodos, hay que instanciar un objeto con el Context de
 * la aplicación.
 */
public class Audio extends AppCompatActivity {
    private final int SEGUNDO_EXTRA = 1500;

    private Context context;
    private MediaPlayer reproducir;

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
     * Reproduce el archivo /app/src/main/res/raw/victoriasound.mp3 u otro archivo según el atributo
     * isMusicaPersonalizada de la entrada.
     * @param palabra Usado para determinar si la palabra usa música personalizada o no. Si la tiene,
     *                averigua el nombre del archivo usando la "palabra" dentro de la instancia.
     *                (Ej.: Palabra (yatra), si musicaPersonalizada = true, entonces cargar: yatra.mp3)
     * @see Palabra
     */
    public void Victoria(Palabra palabra) {
        int id;
        if (palabra.isMusicaPersonalizada()) {
            id = Audio.getRawId(palabra.getPalabra().toLowerCase());
        } else {
            id = R.raw.victoriasound;
        }

        reproducir = MediaPlayer.create(this.getContext(), id);
        reproducir.start();

        //Esto equivale a Thread.sleep()
        Esperar.segundos(reproducir.getDuration() + SEGUNDO_EXTRA);
    }

    /**
     * Obtiene el ID del archivo usando el nombre que recibe de entrada. Este método lo usa musicaVictoria(Palabra)
     * cuando ha determinado que la música es personalizada y no sabe el ID del archivo para cargar.
     * @param name Nombre del archivo a buscar
     * @return ID con el archivo o 0 si no se pudo encontrar el archivo
     */
    public static int getRawId(String name) {
        try {
            Field f = R.raw.class.getField(name);
            return f.getInt(null);
        } catch (NoSuchFieldException e) {
            Log.i("Reflection", "No se ha encontrado la pista de musical. " + name);
        } catch (IllegalAccessException e) {
            Log.i("Reflection", "Contenido no accesible " + name);
        }

        return 0;
    }
}
