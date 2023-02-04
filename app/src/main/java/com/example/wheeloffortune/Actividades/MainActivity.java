package com.example.wheeloffortune.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.wheeloffortune.Audio;
import com.example.wheeloffortune.Auxiliares.Esperar;
import com.example.wheeloffortune.C;
import com.example.wheeloffortune.Fichero;
import com.example.wheeloffortune.Jugador;
import com.example.wheeloffortune.Palabra;
import com.example.wheeloffortune.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String[] secciones = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",};
    private static final int[] gradosSecciones = new int[secciones.length];
    private static final Random random = new Random();
    private static int valorConseguido = 0;
    private static int completarPalabra = 0;
    private static Jugador jugadarGuardar;
    private boolean girando = false;
    private int degree = 0;
    private ImageView ruleta;
    private TextView puntuacion;
    private Button cartelNombre;
    private ImageView pin;
    private EditText introducirLetra;
    private EditText introducirPalabra;
    private TextView informacion;
    private TextView[] conjuntoTextViews = new TextView[27];
    private Palabra palabraAdivinar;
    private Audio audio = new Audio(this);
    private Fichero fichero = new Fichero(this);
    private TextView resolverPalabra;
    private Esperar Esperando = new Esperar();
    private C cc = new C();
    private FirebaseAuth myAuth;
    private String idUsuario = "";
    private FirebaseFirestore myStorage;
    private String nombre = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cc.setBoo(false);
        cc.setOnBooChangeListener(new C.BooChangeListener() {
            @Override
            public void OnBooChange(boolean Boo) {
            }
            @Override
            public void onBooChange(boolean b) {
                Esperando.segundos(500);
                palabraAdivinar.pintarPalabra();
                audio.Victoria(palabraAdivinar);
                finalizarActividad();
            }
        });
        setContentView(R.layout.activity_main);

        generarCuadros();
        mostrarCuadrosLetras();
        obtenerGradosSecciones();
        resolverPalabra = (TextView) findViewById(R.id.textResolverPalabra);
        pin = findViewById(R.id.pin);
        ruleta = findViewById(R.id.ruleta);
        puntuacion = (TextView) findViewById(R.id.puntos);
        cartelNombre = findViewById(R.id.boton_nombre);

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

         myAuth = FirebaseAuth.getInstance();
         idUsuario = myAuth.getCurrentUser().getUid();
         myStorage = FirebaseFirestore.getInstance();
        obtenerNombre(idUsuario);


    }


    private void girar() {
        ruleta.setEnabled(false);
        degree = random.nextInt(secciones.length - 1);
        RotateAnimation rotateAnimacion = new RotateAnimation(0, (360 * secciones.length) + gradosSecciones[degree], RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimacion.setDuration(8870);  //8870
        //SI COLOCO TRUE EN EL METODO SETFILLAFTER NO PUEDO HACER INVISIBLE MI IMAGEN
        rotateAnimacion.setFillAfter(true);
        rotateAnimacion.setInterpolator(new DecelerateInterpolator());
        rotateAnimacion.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                audio.Giro();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                valorConseguido = Integer.valueOf(secciones[secciones.length - (degree + 1)]);
                Toast.makeText(MainActivity.this, "Puedes ganar " + valorConseguido + " puntos, si aciertas!!!", Toast.LENGTH_LONG).show();
                girando = false;
                Esperando.segundos(2500);
                activarIntroductores();

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


    private void sumarPuntos(int numeronuevo) {
        String valorviejo = puntuacion.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        int suma = (Palabra.encuentros*numeronuevo) + numeroviejo;
        puntuacion.setText(String.valueOf(suma));


    }

    private void restar() {
        //SI EL USUARIO SE EQUIVOCA DE LETRA, SE RESTA 5 PUNTOS
        String valorviejo = puntuacion.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        int resta = numeroviejo - 5;
        puntuacion.setText(String.valueOf(resta));
        Toast.makeText(MainActivity.this, "Acabas de perder " + 5 + " puntos!!", Toast.LENGTH_SHORT).show();
    }

    private void quitarPuntos(){
        puntuacion.setText(String.valueOf(0));
        Toast.makeText(MainActivity.this, "Acabas de perder todos tus puntos!!", Toast.LENGTH_SHORT).show();
    }

    private void duplicarPuntos(){
        String valorviejo = puntuacion.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        puntuacion.setText(String.valueOf(numeroviejo*2));
        Toast.makeText(MainActivity.this, "Acabas de duplicar tus puntos!!", Toast.LENGTH_SHORT).show();

    }






    private void generarCuadros() {
        int temp;
        ArrayList<String> id = generadorNombreLetrasPNG();
        for (int i = 0; i < id.size(); i++) {
            temp = getResources().getIdentifier(id.get(i), "id", getPackageName());
            conjuntoTextViews[i] = (TextView) findViewById(temp);
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
            Esperando.segundos(500);
        if(flag == false){
            audio.Incorrecto();
            Esperando.segundos(500);
            quitarPuntos();
            Esperando.segundos(500);
            desactivarIntroductores();
        }else{
            duplicarPuntos();
            Esperando.segundos(500);
           // palabraAdivinar.pintarPalabra();
            ocultarTeclado();
            //A lo mejor hay que sacar el audio en la condicion de victoria
            // para que pinte antes de que suene la musica
            //audio.musicaVictoria(palabraAdivinar);
            // Esperando.segundos(500);
            desactivarIntroductores();
            cc.setBoo(true);

           // finalizarActividad();

        }
    }

    public void enviarLetra() {
        int aciertos = contarLetras(palabraAdivinar.getPalabra());
        String letra = introducirLetra.getText().toString().toLowerCase();
        if (palabraAdivinar.analizarLetra(letra)) {
            palabraAdivinar.pintarLetra(letra);
            Esperando.segundos(500);
            audio.Correcto();
            sumarPuntos(valorConseguido);
            completarPalabra++;
            if (aciertos == completarPalabra) {
                Esperando.segundos(1500);
                palabraAdivinar.pintarLetra(letra);
                audio.Victoria(palabraAdivinar);
                cc.setBoo(true);

            }
        } else {
            audio.Incorrecto();
            restar();
        }
        desactivarIntroductores();
    }


    private void activarIntroductores() {
        introducirLetra.setVisibility(View.VISIBLE);
        resolverPalabra.setVisibility(View.VISIBLE);
        resolverPalabra.setEnabled(true);


    }

    private void LimpiarEntradas(){
        introducirPalabra.setText("");
        introducirLetra.setText("");
    }

    private void desactivarIntroductores() {
        resolverPalabra.setVisibility(View.INVISIBLE);
        resolverPalabra.setEnabled(false);
        ruleta.setEnabled(true);
        introducirLetra.setVisibility(View.INVISIBLE);
        introducirPalabra.setVisibility(View.INVISIBLE);
        LimpiarEntradas();
        ocultarTeclado();
    }

    public void activarResolverPalabra(){
       introducirLetra.setVisibility(View.INVISIBLE);
       introducirPalabra.setVisibility(View.VISIBLE);
       resolverPalabra.setEnabled(false);
    }
    //REVISAR SI ESTE METODO FUNCIONA
    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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


    public  void finalizarActividad() {
        Esperando.segundos(1000);
        guardarDatosJugador();
        // jugadarGuardar = new Jugador(cartelNombre.getText().toString(), Integer.valueOf(puntuacion.getText().toString()));
        //fichero.guardarJugador(jugadarGuardar);
        palabraAdivinar.limpiarValoreStaticos();
        finish();
    }


    public void activarResolverPalabra(View view) {
        activarResolverPalabra();
    }





    public void guardarDatosJugador() {
        String usuario = cartelNombre.getText().toString();
        String puntuacionX = puntuacion.getText().toString();

        DocumentReference doc_ref = myStorage.collection("Usuarios").document(idUsuario);
        HashMap<String, String> info_usuario = new HashMap<>();
        info_usuario.put("Nombre", usuario);
        info_usuario.put("Puntuacion", puntuacionX);
        doc_ref.set(info_usuario);
        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);


    }


    private void obtenerNombre(String perfil){
        CollectionReference collectionReference = myStorage.collection("Usuarios");
        // Leer todos los documentos en la colección
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("TAG", perfil);
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        if(document.getId().equals(perfil)){
                            Log.d("TAG", "si entro");
                            nombre = document.getData().get("Nombre").toString();
                            puntuacion.setText(document.getData().get("Puntuacion").toString());


                        }


                    }
                    Log.d("TAG", nombre);
                    cartelNombre.setText(nombre.toUpperCase());
                } else {
                    Log.w("TAG", "Error al leer la colección.", task.getException());
                }
            }
        });

    }
}