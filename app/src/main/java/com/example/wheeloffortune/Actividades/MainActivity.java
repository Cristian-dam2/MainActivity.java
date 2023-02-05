package com.example.wheeloffortune.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wheeloffortune.Auxiliares.Audio;
import com.example.wheeloffortune.Auxiliares.C;
import com.example.wheeloffortune.Auxiliares.Esperar;
import com.example.wheeloffortune.Auxiliares.Fichero;
import com.example.wheeloffortune.Auxiliares.Jugador;
import com.example.wheeloffortune.Auxiliares.Palabra;
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
    private static final String[] SECTORES = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final int[] GRADOS_SECTORES = new int [SECTORES.length];
    private static final Random random = new Random();
    private static int valorConseguido = 0;
    private static int completarPalabra = 0;
    private static final int NUMERO_DE_PANELES = 27;

    private static Jugador jugadarGuardar;

    private boolean girando = false;
    private int degree = 0;

    private C cc;
    private EditText introducirLetra;
    private EditText introducirPalabra;
    private ImageView ruleta;

    private TextView puntuacion;
    private Button cartelNombre;
    private ImageView pin;
    private TextView informacion;
    private TextView[] conjuntoTextViews = new TextView[NUMERO_DE_PANELES];
    private TextView resolverPalabra;

    private Palabra palabraAdivinar;
    private Audio audio = new Audio(this);
    private Fichero fichero = new Fichero(this);
    private Esperar Esperando = new Esperar();

    private FirebaseAuth myAuth;
    private FirebaseFirestore myStorage;
    private String idUsuario = "";
    private String nombre = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cc = new C(false);
        introducirLetra = (EditText) findViewById(R.id.editTextColocarLetra);
        introducirPalabra = (EditText) findViewById(R.id.editTextColocarPalabra);
        ruleta = findViewById(R.id.ruleta);

        inicializarListeners();

        generarCuadros();
        mostrarCuadrosLetras();
        obtenerGradosSecciones();
        resolverPalabra = (TextView) findViewById(R.id.textResolverPalabra);
        pin = findViewById(R.id.pin);
        puntuacion = (TextView) findViewById(R.id.puntos);
        cartelNombre = findViewById(R.id.boton_nombre);



        informacion = (TextView) findViewById(R.id.InformacionparaAdivinar);
        palabraAdivinar = new Palabra(conjuntoTextViews);
        informacion.setText(palabraAdivinar.getInformacion());

        myAuth = FirebaseAuth.getInstance();
        idUsuario = myAuth.getCurrentUser().getUid();
        myStorage = FirebaseFirestore.getInstance();
        obtenerNombre(idUsuario);
    }

    /**
     * Inicializa los listeners de esta actividad:
     * <ul>
     *     <li>cc: Se ejecuta al llamar a cc.setBool()</li>
     *     <li>introducirLetra: Se ejecuta al pulsar sobre el campo de introducir letras.</li>
     *     <li>introducirPalabra: Se ejecuta al pulsar sobre el campo de introducir palabras.</li>
     *     <li>ruleta: Se ejecuta al pulsar sobre la ruleta.</li>
     * </ul>
     * @see C
     */
    private void inicializarListeners() {
        cc.setOnBoolChangeListener(new C.BoolChangeListener() {
            @Override
            public void onBoolChange(boolean b) {
                if (b == true) {
                    Esperando.segundos(500);
                    palabraAdivinar.pintarPalabra();
                    finalizarActividad();
                }
            }
        });
        introducirLetra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarLetra();
            }
        });
        introducirPalabra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resolverPalabra();
            }
        });
        ruleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!girando) {
                    girar();
                    girando = true;
                }
            }
        });
    }


    private void girar() {
        ruleta.setEnabled(false);
        degree = random.nextInt(SECTORES.length - 1);
        RotateAnimation rotateAnimacion = new RotateAnimation(0, (360 * SECTORES.length) + GRADOS_SECTORES[degree], RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
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
                valorConseguido = Integer.valueOf(SECTORES[SECTORES.length - (degree + 1)]);
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
        int rangoSeccion = 360 / SECTORES.length;
        for (int i = 0; i < SECTORES.length; i++) {
            GRADOS_SECTORES[i] = (i + 1) * rangoSeccion;
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

    /**
     * Inicializa el array conjuntoTextViews con todos los cuadros del panel en activity_main.xml.<br>
     * Para ello, llama al método generarNombreLetrasPNG(), que devolverá la que debería ser la lista
     * con el ID de cada cuadro del panel.
     */
    private void generarCuadros() {
        int idTextView;
        ArrayList<String> paneles = generadorNombreLetrasPNG();
        for (int i = 0; i < paneles.size(); i++) {
            idTextView = getResources().getIdentifier(paneles.get(i), "id", getPackageName());
            conjuntoTextViews[i] = (TextView)findViewById(idTextView);
        }
    }
    /**
     * Crea una lista con los nombres de los TextViews en activity_main.xml. El nombre de cada
     * elemento será cN donde N es un número entre 0 y el límite establecido en la constante NUMERO_DE_PANELES
     * de la misma clase.
     * @return Lista con los nombres de los cuadros.
     */
    private ArrayList<String> generadorNombreLetrasPNG() {
        // c es el nombre de los TextViews en el MainActivity, se añade los numeros correspondiente al recuadro que pertenecen.
        ArrayList<String> letrasEnumeradas = new ArrayList<>();
        for (int i = 0; i < NUMERO_DE_PANELES; i++) {
            letrasEnumeradas.add("c".concat(String.valueOf(i)));
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
        } else {
            duplicarPuntos();
            Esperando.segundos(500);
           // palabraAdivinar.pintarPalabra();
            ocultarTeclado(this);
            //A lo mejor hay que sacar el audio en la condicion de victoria
            // para que pinte antes de que suene la musica
            //audio.musicaVictoria(palabraAdivinar);
            // Esperando.segundos(500);
            desactivarIntroductores();
            cc.setBool(true);

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
                cc.setBool(true);

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

    /**
     * Vacía los dos campos de entradas (el de introducir letras individuales y el de resolver).
     */
    private void limpiarEntradas(){
        introducirPalabra.setText("");
        introducirLetra.setText("");
    }
    private void desactivarIntroductores() {
        resolverPalabra.setVisibility(View.INVISIBLE);
        introducirLetra.setVisibility(View.INVISIBLE);
        introducirPalabra.setVisibility(View.INVISIBLE);

        resolverPalabra.setEnabled(false);
        ruleta.setEnabled(true);

        limpiarEntradas();
        ocultarTeclado(this);
    }

    public void activarResolverPalabra(){
       introducirLetra.setVisibility(View.INVISIBLE);
       introducirPalabra.setVisibility(View.VISIBLE);
       resolverPalabra.setEnabled(false);
    }
    //REVISAR SI ESTE METODO FUNCIONA
    //No funciona xd
    private void ocultarTeclado(Activity activity) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
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
        Esperar.segundos(1000);
        guardarDatosJugador();
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