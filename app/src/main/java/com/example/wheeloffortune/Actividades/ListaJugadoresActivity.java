package com.example.wheeloffortune.Actividades;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wheeloffortune.Auxiliares.Esperar;
import com.example.wheeloffortune.Auxiliares.Jugador;
import com.example.wheeloffortune.Auxiliares.JugadorAdaptador;
import com.example.wheeloffortune.Auxiliares.JugadorModelo;
import com.example.wheeloffortune.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaJugadoresActivity extends AppCompatActivity {
    private Button boton_volver;
    private FirebaseFirestore db;
    private StorageReference myStorage;
    private StorageReference storageReference;
    private StorageReference photoReference;
    private HashMap<String,String> informacion = new HashMap<>();
    private Bitmap[] bitmap = new Bitmap[1];
    private List<JugadorModelo> mJugadorList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private JugadorAdaptador mJugadorAdaptador;
    private TextView cartel;
    final long ONE_MEGABYTE = 1024 * 1024;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jugadores);
        db = FirebaseFirestore.getInstance();
        cartel = findViewById(R.id.textViewPrueba);
        cartel.bringToFront();
        boton_volver = (Button) findViewById(R.id.boton_volver);
        boton_volver.bringToFront();
        myStorage = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mRecyclerView = findViewById(R.id.myRecycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        boton_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolverMainActivity();
            }
        });

        CollectionReference collectionReference = db.collection("Usuarios");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        String nombre = document.getData().get("Nombre").toString();
                        String puntuacion = (document.getData().get("Puntuacion").toString());
                        informacion.put(document.getId(),nombre+"$"+puntuacion);
                    }
                } else {
                    Log.w("TAG", "Error al leer la colección.", task.getException());
                }
                loadBitmaps(informacion);
            }
        });

    }


    private void loadBitmaps( Map<String, String> informacion) {
        for (String key : informacion.keySet()) {
            System.out.println(key);
            String jugadorInfo = informacion.get(key);
            StorageReference imageRef = myStorage.child("usuarios/" + key + "/imagen_perfil.jpg");

            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.e("OK", "Imagen cargada con éxito");
                    JugadorModelo a = new JugadorModelo(bitmap, jugadorInfo);
                    mJugadorList.add(a);
                    Collections.sort(mJugadorList, new Comparator<JugadorModelo>() {
                        @Override
                        public int compare(JugadorModelo o1, JugadorModelo o2) {
                            return Integer.valueOf(o2.getPuntuacion()).compareTo(Integer.valueOf(o1.getPuntuacion()));
                        }
                    });
                    if (mJugadorList.size() == informacion.size()) {
                        mJugadorAdaptador = new JugadorAdaptador(getTop10(mJugadorList));
                        mRecyclerView.setAdapter(mJugadorAdaptador);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("Error", "No se pudo obtener la imagen");
                }
            });
        }
    }


    public List<JugadorModelo> getTop10(List<JugadorModelo> jugadores) {
        List<JugadorModelo> top10 = new ArrayList<>();

        // Ordenamos la lista de jugadores por puntuación de mayor a menor
        Collections.sort(jugadores, new Comparator<JugadorModelo>() {
            @Override
            public int compare(JugadorModelo o1, JugadorModelo o2) {
                return Integer.valueOf(o2.getPuntuacion()).compareTo(Integer.valueOf(o1.getPuntuacion()));
            }
        });

        // Tomamos los primeros 10 jugadores o menos si no hay suficientes
        int max = Math.min(10, jugadores.size());
        for (int i = 0; i < max; i++) {
            top10.add(jugadores.get(i));
        }

        return top10;
    }


    public void VolverMainActivity() {
//        Intent myintent = new Intent(this,LoginActivity.class);
//        startActivity(myintent);
        finish();
    }
}