package com.example.wheeloffortune;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String[] secciones = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",};
    private static final int[] gradosSecciones = new int[secciones.length];
    private static final Random random = new Random();
    private static int valorConseguido = 0;
    private static int completarPalabra = 0;
    private int degree = 0;
    private boolean girando = false;
    private ImageView ruleta;
    private TextView score;
    private Button cartelNombre;
    private Button botonFinalizar;
    private ImageView pin;
    private EditText introducirLetra;
    private EditText introducirPalabra;
    private TextView informacion;
    private TextView[] conjuntoTextViews = new TextView[27];
    private Palabra palabraAdivinar;
    private static Jugador jugadarGuardar;
    private Audio audio = new Audio(this);
    private Fichero fichero = new Fichero(this);
    private boolean acabado = false;
    private TextView resolverPalabra;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generarCuadros();
        obtenerGradosSecciones();
        resolverPalabra = (TextView) findViewById(R.id.textResolverPalabra);

        pin = findViewById(R.id.pin);
        ruleta = findViewById(R.id.ruleta);
        score = (TextView) findViewById(R.id.puntos);
        cartelNombre = findViewById(R.id.boton_nombre);
        cartelNombre.setText(getIntent().getStringExtra("nombre_usuario"));
        botonFinalizar = findViewById(R.id.boton_Salir);
        introducirLetra = (EditText) findViewById(R.id.editTextColocarLetra);
        introducirLetra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarLetra();
            }
        });


        introducirPalabra = (EditText) findViewById(R.id.editTextColocarPalabra);
        introducirPalabra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resolverPalabra();
            }
        });


        informacion = (TextView) findViewById(R.id.InformacionparaAdivinar);
        palabraAdivinar = new Palabra(conjuntoTextViews);
//        if(palabraAdivinar != null){
//
//
//            palabraAdivinar.limpiarValoreStaticos();
//            palabraAdivinar = null;
//            palabraAdivinar = new Palabra(conjuntoTextViews);
//            informacion.setText(palabraAdivinar.getInformacion());
//
//        }

        informacion.setText(palabraAdivinar.getInformacion());
        ruleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!girando) {
                    girar();
                    girando = true;
                }
            }
        });

        mostrarTablero();
        botonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jugadarGuardar = new Jugador(cartelNombre.getText().toString(), Integer.valueOf(score.getText().toString()));
                finalizarActividad();
                finish();


            }
        });
    }


    private void girar() {
        ruleta.setEnabled(false);
        resolverPalabra.setEnabled(false);
        degree = random.nextInt(secciones.length - 1);
        RotateAnimation rotateAnimacion = new RotateAnimation(0, (360 * secciones.length) + gradosSecciones[degree],
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimacion.setDuration(8870);  //8870
        //SI COLOCO TRUE EN EL METODO SETFILLAFTER NO PUEDO HACER INVISIBLE MI IMAGEN
        rotateAnimacion.setFillAfter(true);
        rotateAnimacion.setInterpolator(new DecelerateInterpolator());
        rotateAnimacion.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bloquearBotonesGiro();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                valorConseguido = Integer.valueOf(secciones[secciones.length - (degree + 1)]);
                Toast.makeText(MainActivity.this, "Puedes ganar " + valorConseguido + " puntos, si aciertas!!!", Toast.LENGTH_LONG).show();
                girando = false;
                resolverPalabra.setEnabled(true);
                desbloquearBotonesGiro();

                try {
                    Thread.sleep(2500);
                  //  ocultarRuleta();
                    introducirLetra.setVisibility(View.VISIBLE);
                  //  mostrarTablero();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ruleta.startAnimation(rotateAnimacion);
    }

    private void obtenerGradosSecciones() {
        int rangoSeccion = 360 / secciones.length;
        for (int i = 0; i < secciones.length; i++) {
            gradosSecciones[i] = (i + 1) * rangoSeccion;
        }
    }

    public void touchRuleta(View view) {
        girar();
        ruleta.setEnabled(false);
    }

    private void sumarPuntos(int numeronuevo) {
        String valorviejo = score.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        int suma = numeronuevo + numeroviejo;
        score.setText(String.valueOf(suma));


    }

    private void restar() {
        //SI EL USUARIO SE EQUIVOCA DE LETRA, SE RESTA 5 PUNTOS
        String valorviejo = score.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        int resta = numeroviejo - 5;
        score.setText(String.valueOf(resta));
        Toast.makeText(MainActivity.this, "Acabas de perder " + 5 + " puntos!!", Toast.LENGTH_SHORT).show();
    }


    private void bloquearBotonesGiro() {
        //botonGirar.setVisibility(View.INVISIBLE);
        botonFinalizar.setEnabled(false);
       audio.Giro();
    }

    private void desbloquearBotonesGiro() {
        //botonGirar.setEnabled(true);
        botonFinalizar.setEnabled(true);
    }

    private void ocultarRuleta() {
        pin.setVisibility(View.INVISIBLE);
        // ruleta.clearAnimation(); SI NO COLOCO TRUE EN EL SETFILLAFTER DE LA ANIMACION
        ruleta.setVisibility(View.INVISIBLE);
      //  botonGirar.setVisibility(View.INVISIBLE);
    }

    private void mostrarRuleta() {
        pin.setVisibility(View.VISIBLE);
        ruleta.setVisibility(View.VISIBLE);
       // botonGirar.setVisibility(View.VISIBLE);

    }

    private void mostrarTablero() {
        informacion.setVisibility(View.VISIBLE);
        //introducirLetra.setVisibility(View.VISIBLE);
        mostrarCuadrosLetras();

    }

    private void ocultarTablero() {
        // informacion.setVisibility(View.INVISIBLE);
        introducirLetra.setVisibility(View.INVISIBLE);
       // mostrarRuleta();
        //ocultarCuadrosLetras();
    }


    private void generarCuadros() {
        int temp;
        ArrayList<String> id = generadorNombreLetrasPNG();
        for (int i = 0; i < id.size(); i++) {
            temp = getResources().getIdentifier(id.get(i), "id", getPackageName());
            conjuntoTextViews[i] = (TextView) findViewById(temp);
        }
    }

    private void resetearCuadros() {
        for (int i = 0; i < conjuntoTextViews.length; i++) {
            conjuntoTextViews[i].setBackgroundResource(R.drawable.panelespacio);

        }
    }

    // C es el nombre de los TextViews en el MainActivity, se añade los numeros correspondiente al recuadro que pertenecen.
    private ArrayList<String> generadorNombreLetrasPNG() {
        String letra = "c";
        ArrayList<String> letrasEnumeradas = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            letrasEnumeradas.add(letra.concat(String.valueOf(i)));
        }
        return letrasEnumeradas;

    }


    private void mostrarCuadrosLetras() {
        for (int i = 0; i < conjuntoTextViews.length; i++) {
            conjuntoTextViews[i].setVisibility(View.VISIBLE);
        }

    }

    private void ocultarCuadrosLetras() {
        for (int i = 0; i < conjuntoTextViews.length; i++) {
            conjuntoTextViews[i].setVisibility(View.INVISIBLE);
        }
    }

    public void resolverPalabra(){
        String palabraIntroducida = introducirPalabra.getText().toString().toLowerCase();
        String palabraAadivinar = palabraAdivinar.getPalabra().toLowerCase();
        boolean flag = true;

        for (int i = 0; i < palabraAadivinar.length(); i++) {
            if(palabraAadivinar.charAt(i) != palabraIntroducida.charAt(i)){
                flag = false;
                break;
            }
        }

        if(flag == false){
            audio.Incorrecto();
        }
        palabraAdivinar.pintarPalabra();
        ocultarTeclado();
        audio.musicaVictoria(palabraAdivinar);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finalizarActividad();
    }


    public void enviarLetra() {
        int aciertos = contarLetras(palabraAdivinar.getPalabra());
        String letra = introducirLetra.getText().toString().toLowerCase();
        ocultarTeclado();
        if (palabraAdivinar.analizarLetra(letra)) {
            palabraAdivinar.pintarLetra(letra);

            audio.Correcto();
            sumarPuntos(valorConseguido);
            completarPalabra++;
            if (aciertos == completarPalabra) {
                audio.musicaVictoria(palabraAdivinar);

                try {
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                acabado = true;

//                jugadarGuardar = new Jugador(cartelNombre.getText().toString(), Integer.valueOf(score.getText().toString()));
//                fichero.guardarPuntuacion(jugadarGuardar);
//                palabraAdivinar.limpiarValoreStaticos();
//                palabraAdivinar = new Palabra(conjuntoTextViews);
//                informacion.setText(palabraAdivinar.getInformacion());
//                completarPalabra = 0;

              //  finalizarActividad();
            }



        } else {
            audio.Incorrecto();
            restar();


        }
        ocultarTablero();
       // botonGirar.setVisibility(View.VISIBLE);
        introducirLetra.setText("");
        ruleta.setEnabled(true);
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }




    public void activarResolverPalabra(){
       introducirLetra.setVisibility(View.INVISIBLE);
       introducirPalabra.setVisibility(View.VISIBLE);
    }

    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private int contarLetras(String e) {
        StringBuilder sinRepetir = new StringBuilder();
        for (int i = 0; i < e.length(); i++) {
            String si = e.substring(i, i + 1);
            if (sinRepetir.indexOf(si) == -1) {
                sinRepetir.append(si);
            }
        }
        return sinRepetir.toString().length();
    }


    private void finalizarActividad() {
        jugadarGuardar = new Jugador(cartelNombre.getText().toString(), Integer.valueOf(score.getText().toString()));
        fichero.guardarJugador(jugadarGuardar);
        palabraAdivinar.limpiarValoreStaticos();
       // palabraAdivinar = null;
      //  palabraAdivinar = new Palabra(conjuntoTextViews);
        // informacion.setText(palabraAdivinar.getInformacion());
        finish();

//        palabraAdivinar.limpiarValoreStaticos();
//        palabraAdivinar = null;
//        palabraAdivinar = new Palabra(conjuntoTextViews);
//        informacion.setText(palabraAdivinar.getInformacion());


    }


    public void activarResolverPalabra(View view) {
        activarResolverPalabra();
    }
}