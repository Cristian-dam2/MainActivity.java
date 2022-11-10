package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String nombrefichero = "Jugadores.txt";
    private static final String[] sectors = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",};
    private static final int[] sectorDegress = new int[sectors.length];
    private static final Random random = new Random();
    private int degree = 0;
    private boolean isSpinning = false;
    private ImageView ruleta;
    private Button botongirar;
    private TextView score;
    private Button cartelnombre;
    private Button botonSalidaActividad;
    private ImageView pin;

    private int reproducir_sonido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pin = findViewById(R.id.pin);
        ruleta = findViewById(R.id.ruleta);
        botongirar = findViewById(R.id.botongirar);
        score = (TextView) findViewById(R.id.puntos);
        cartelnombre = findViewById(R.id.boton_nombre);
        cartelnombre.setText(getIntent().getStringExtra("nombre_usuario"));
        botonSalidaActividad = findViewById(R.id.boton_Salir);



        getDegreeForSectors();
        botongirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSpinning) {
                    spin();
                    isSpinning = true;
                }
            }
        });

        botonSalidaActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Jugador jugadarGuardar = new Jugador(cartelnombre.getText().toString(), Integer.valueOf(score.getText().toString()));
                guardarPuntuacionJugadorMain(jugadarGuardar);
                finish();

            }
        });
    }


    private void spin() {
        degree = random.nextInt(sectors.length - 1);
        RotateAnimation rotateAnimacion = new RotateAnimation(0, (360 * sectors.length) + sectorDegress[degree],
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimacion.setDuration(4800);
        rotateAnimacion.setFillAfter(true);
        rotateAnimacion.setInterpolator(new DecelerateInterpolator());
        rotateAnimacion.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                botongirar.setEnabled(false);
                botonSalidaActividad.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int valor = Integer.valueOf(sectors[sectors.length - (degree + 1)]);
                Toast.makeText(MainActivity.this, "Haz ganado " + valor + " puntos.", Toast.LENGTH_LONG).show();
                sumarPuntos(valor);
                isSpinning = false;
                botongirar.setEnabled(true);
                botonSalidaActividad.setEnabled(true);
                audioVictoria();

//                pin.setVisibility(View.GONE);
//                ruleta.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ruleta.startAnimation(rotateAnimacion);
    }

    private void getDegreeForSectors() {
        int sectorDegree = 360 / sectors.length;
        for (int i = 0; i < sectors.length; i++) {
            sectorDegress[i] = (i + 1) * sectorDegree;
        }
    }

    private void sumarPuntos(int numeronuevo) {
        String valorviejo = score.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        int suma = numeronuevo + numeroviejo;
        score.setText(String.valueOf(suma));

//        boolean flag = true;
//        while(flag){
//            numeroviejo++;
//            score.setText(String.valueOf(numeroviejo));
//            if(suma == numeroviejo){
//                flag = false;
//            }
//            try {
//                Thread.sleep(350);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
    }


    public void guardarPuntuacionJugadorMain(Jugador jugador) {
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

        }
    }

    private void audioVictoria(){
        MediaPlayer mp = MediaPlayer.create(this,R.raw.victoriasound);
        mp.start();

    }
}