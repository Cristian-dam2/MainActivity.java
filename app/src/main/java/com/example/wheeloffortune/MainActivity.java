package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] sectors = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",};
    private static final int[] sectorDegress = new int[sectors.length];
    private static final Random random = new Random();
    private int degree = 0;
    private boolean isSpinning = false;
    private ImageView ruleta;
    private Button botongirar;
    private TextView score;
    private Button cartelnombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ruleta = findViewById(R.id.ruleta);
        botongirar = findViewById(R.id.botongirar);
        score = (TextView) findViewById(R.id.puntos);
        cartelnombre = findViewById(R.id.boton_nombre);
        cartelnombre.setText(getIntent().getStringExtra("nombreusuario"));

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
                Toast.makeText(MainActivity.this, "MUCHA SUERTE", Toast.LENGTH_SHORT).show();
                botongirar.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int valor = Integer.valueOf(sectors[sectors.length - (degree + 1)]);
                Toast.makeText(MainActivity.this, "Tienes " + valor + " puntos.", Toast.LENGTH_LONG).show();
                sumarPuntos(valor);
                isSpinning = false;
                botongirar.setEnabled(true);
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

    private void sumarPuntos(int valornuevo) {
        String valorviejo = score.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        int suma = valornuevo + numeroviejo;
        score.setText(String.valueOf(suma));
    }
}