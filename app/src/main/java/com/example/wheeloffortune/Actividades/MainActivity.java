package com.example.wheeloffortune.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
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

import com.example.wheeloffortune.Auxiliares.Audio;
import com.example.wheeloffortune.Auxiliares.BoolTrigger;
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
import java.util.Timer;
import java.util.TimerTask;

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

    private BoolTrigger cc;
    private EditText introducirLetra;
    private EditText introducirPalabra;
    private ImageView ruleta;

    private TextView puntos;
    private Button cartelNombre;
    private ImageView pin;
    private TextView textoPista;
    private TextView[] conjuntoTextViews = new TextView[NUMERO_DE_PANELES];
    private TextView botonResolverPalabra;

    private Palabra palabraAdivinar;
    private Audio audio = new Audio(this);
    private Fichero fichero = new Fichero(this);

    private FirebaseAuth myAuth;
    private FirebaseFirestore myStorage;
    private String idUsuario = "";
    private String nombre = "";
    private Timer time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cc = new BoolTrigger(false);

        inicializarElementosDeLaActividad();
        inicializarComponentesDeFirebase();
        inicializarListeners();
        inicializarPanel();
        obtenerGradosSecciones();
        inicializarPalabra();
        obtenerNombre(idUsuario);

        time = new Timer();
    }

    private void inicializarElementosDeLaActividad() {
        introducirLetra = (EditText) findViewById(R.id.campo_colocar_letra);
        introducirPalabra = (EditText) findViewById(R.id.campo_colocar_palabra);
        botonResolverPalabra = (TextView) findViewById(R.id.boton_resolver_palabra);
        textoPista = (TextView) findViewById(R.id.pista);
        puntos = (TextView) findViewById(R.id.texto_puntos);
        cartelNombre = findViewById(R.id.boton_nombre);
        ruleta = findViewById(R.id.ruleta);
        pin = findViewById(R.id.pin);
    }
    private void inicializarComponentesDeFirebase() {
        myAuth = FirebaseAuth.getInstance();
        idUsuario = myAuth.getCurrentUser().getUid();
        myStorage = FirebaseFirestore.getInstance();
    }
    /**
     * Inicializa los listeners de esta actividad:
     * <ul>
     *     <li>cc: Se ejecuta al llamar a cc.setBool()</li>
     *     <li>introducirLetra: Se ejecuta al pulsar sobre el campo de introducir letras.</li>
     *     <li>introducirPalabra: Se ejecuta al pulsar sobre el campo de introducir palabras.</li>
     *     <li>ruleta: Se ejecuta al pulsar sobre la ruleta.</li>
     * </ul>
     * @see BoolTrigger
     */
    private void inicializarListeners() {
        cc.setOnBoolChangeListener(new BoolTrigger.BoolChangeListener() {
            @Override
            public void onBoolChange(boolean b) {
                if (b == true) {
                    Esperar.segundos(500);
                    ruleta.setEnabled(false);
                }
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        palabraAdivinar.pintarPalabra();
                        audio.Victoria(palabraAdivinar);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                alert.setTitle("Información").setMessage("Se va a cerrar la aplicación").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finalizarActividad();
                                    }
                                });
                                alert.create().show();
                            }
                        });
                    }
                },1000);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.campo_colocar_letra:
                        enviarLetra();
                        break;
                    case R.id.campo_colocar_palabra:
                        resolverPalabra();
                        break;
                    case R.id.ruleta:
                        if (!girando) {
                            girar();
                            girando = true;
                        }
                        break;
                }
            }
        };
        introducirLetra.setOnClickListener(listener);
        introducirPalabra.setOnClickListener(listener);
        ruleta.setOnClickListener(listener);
    }
    /**
     * Inicializa el array de TextViews con todos los cuadros del panel en activity_main.xml.<br>
     * Para ello, llama al método generarNombreLetrasPNG(), que devolverá la que debería ser la lista
     * con el ID de cada cuadro del panel.
     */
    private void inicializarPanel() {
        int idTextView;
        ArrayList<String> paneles = generadorNombreLetrasPNG();
        for (int i = 0; i < paneles.size(); i++) {
            idTextView = getResources().getIdentifier(paneles.get(i), "id", getPackageName());
            conjuntoTextViews[i] = (TextView)findViewById(idTextView);
        }
    }
    /**
     * Crea la palabra oculta y escribe la pista en la actividad.
     */
    private void inicializarPalabra() {
        palabraAdivinar = new Palabra(conjuntoTextViews);
        textoPista.setText(palabraAdivinar.getInformacion());
    }
    /**
     * Recupera la información de usuario de una de las colecciones de Firestore
     * @param perfil Nombre de usuario para buscar
     */
    private void obtenerNombre(String perfil) {
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
                            puntos.setText(document.getData().get("Puntuacion").toString());
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

    /**
     * Crea una lista con los nombres de los TextViews en activity_main.xml. El nombre de cada elemento
     * será cN donde N es un número entre 0 y el límite establecido en la constante NUMERO_DE_PANELES
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
    /**
     * Vuelve visibles todos los cuadros del panel
     */
    private void mostrarCuadrosLetras() {
        for (int i = 0; i < conjuntoTextViews.length; i++) {
            conjuntoTextViews[i].setVisibility(View.VISIBLE);
        }
    }

    /**
     * Vuelve invisibles todos los cuadros del panel
     */
    private void ocultarCuadrosLetras() {
        for (int i = 0; i < conjuntoTextViews.length; i++) {
            conjuntoTextViews[i].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Averigua cuántos grados mide cada sector de la ruleta y luego, con un índice, determina la
     * posisión inicial de cada uno. El resultado se guarda en el array GRADOS_SECTORES.<br>
     * Ejemplo: Si un círculo tiene 4 sectores, en la división se determina que cada sector mide 90.
     * Con ello, multiplica 90 por su posición en el array de sectores (Resultado: 90, 180, 270 y 360).
     */
    private void obtenerGradosSecciones() {
        int rangoSeccion = 360 / SECTORES.length;
        for (int i = 0; i < SECTORES.length; i++) {
            GRADOS_SECTORES[i] = (i + 1) * rangoSeccion;
        }
    }

    /**
     * Lee la letra introducida en el campo de texto de esscribir letras y suma o resta puntos dependiendo
     * de si lo leído es correcto o no. Si entonces todas las letras están descubiertas, el juego finaliza.
     * @see Palabra
     */
    public void enviarLetra() {
        int aciertos = contarLetras(palabraAdivinar.getPalabra());
        String letra = introducirLetra.getText().toString().toLowerCase();

        if (palabraAdivinar.analizarLetra(letra)) {
            palabraAdivinar.pintarLetra(letra);

            Esperar.segundos(500);
            audio.Correcto();
            sumarPuntos(valorConseguido);
            completarPalabra++;

            if (aciertos == completarPalabra) {
                Esperar.segundos(1500);
                cc.setBool(true);
            }
        } else {
            audio.Incorrecto();
            restar();
        }

        desactivarIntroductores();
    }

    /**
     * Compara la palabra oculta con la introducida en el campo de resolver palabra.
     */
    public void resolverPalabra() {
        String palabraIntroducida = introducirPalabra.getText().toString().toLowerCase();
        String palabraAAdivinar = palabraAdivinar.getPalabra().toLowerCase();
        boolean palabra_adivinada = true;

        for (int i = 0; i < palabraAAdivinar.length(); i++) {
            if (palabraAAdivinar.charAt(i) != palabraIntroducida.charAt(i)){
                palabra_adivinada = false;
                break;
            }
        }

        Esperar.segundos(500);
        desactivarIntroductores();
        if (palabra_adivinada == false) {
            audio.Incorrecto();
            Esperar.segundos(500);
            quitarPuntos();
            Esperar.segundos(500);
        } else {
            duplicarPuntos();
            Esperar.segundos(500);
            cc.setBool(true);
        }
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
                Esperar.segundos(2500);
                activarIntroductores();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ruleta.startAnimation(rotateAnimacion);
    }



    private void sumarPuntos(int numeronuevo) {
        String valorviejo = puntos.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        int suma = (Palabra.encuentros*numeronuevo) + numeroviejo;
        puntos.setText(String.valueOf(suma));


    }

    private void restar() {
        //SI EL USUARIO SE EQUIVOCA DE LETRA, SE RESTA 5 PUNTOS
        String valorviejo = puntos.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        int resta = numeroviejo - 5;
        puntos.setText(String.valueOf(resta));
        Toast.makeText(MainActivity.this, "Acabas de perder " + 5 + " puntos!!", Toast.LENGTH_SHORT).show();
    }

    private void quitarPuntos(){
        puntos.setText(String.valueOf(0));
        Toast.makeText(MainActivity.this, "Acabas de perder todos tus puntos!!", Toast.LENGTH_SHORT).show();
    }

    private void duplicarPuntos(){
        String valorviejo = puntos.getText().toString();
        int numeroviejo = Integer.valueOf(valorviejo);
        puntos.setText(String.valueOf(numeroviejo*2));
        Toast.makeText(MainActivity.this, "Acabas de duplicar tus puntos!!", Toast.LENGTH_SHORT).show();
    }


    private void activarIntroductores() {
        introducirLetra.setVisibility(View.VISIBLE);
        botonResolverPalabra.setVisibility(View.VISIBLE);
        botonResolverPalabra.setEnabled(true);


    }

    /**
     * Vacía los dos campos de entradas (el de introducir letras individuales y el de resolver).
     */
    private void limpiarEntradas(){
        introducirPalabra.setText("");
        introducirLetra.setText("");
    }

    /**
     * Oculta el teclado, vacía desactiva los campos para introducir palabras y el botón de resolver
     * y vuelve a habilitar la ruleta.
     */
    private void desactivarIntroductores() {
        botonResolverPalabra.setVisibility(View.INVISIBLE);
        introducirLetra.setVisibility(View.INVISIBLE);
        introducirPalabra.setVisibility(View.INVISIBLE);

        botonResolverPalabra.setEnabled(false);
        ruleta.setEnabled(true);

        limpiarEntradas();
        ocultarTeclado(this);
    }
    public void activarResolverPalabra(){
       introducirLetra.setVisibility(View.INVISIBLE);
       introducirPalabra.setVisibility(View.VISIBLE);
       botonResolverPalabra.setEnabled(false);
    }

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



    public void activarResolverPalabra(View view) {
        activarResolverPalabra();
    }

    public void guardarDatosJugador() {
        String usuario = cartelNombre.getText().toString();
        String puntuacionX = puntos.getText().toString();

        DocumentReference doc_ref = myStorage.collection("Usuarios").document(idUsuario);
        HashMap<String, String> info_usuario = new HashMap<>();
        info_usuario.put("Nombre", usuario);
        info_usuario.put("Puntuacion", puntuacionX);
        doc_ref.set(info_usuario);
        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void finalizarActividad() {
        Esperar.segundos(1000);
        guardarDatosJugador();
        palabraAdivinar.limpiarValoreStaticos();
        finish();
    }
}