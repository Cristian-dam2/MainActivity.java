package com.example.wheeloffortune;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaJugadoresActivity extends AppCompatActivity {
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private RecyclerView tablaNombres;
    private TextView pruebaText;
    private Button boton_volver;
    private Fichero fichero = new Fichero(this);
    private FirebaseFirestore db;
    private Datos adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_jugadores);
        db = FirebaseFirestore.getInstance();
        pruebaText = (TextView) findViewById(R.id.textViewPrueba);
        boton_volver = (Button) findViewById(R.id.boton_volver);
        tablaNombres = (RecyclerView) findViewById(R.id.myRecycler);
        tablaNombres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        jugadores = fichero.leerJugador();
//        Datos adapter = new Datos(jugadores);
//        tablaNombres.setAdapter(adapter);

        boton_volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        // Crea una referencia a la colección
        CollectionReference collectionReference = db.collection("Usuarios");

        // Leer todos los documentos en la colección
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        colocarJugadores(document.getData());
                    }
                    adapter = new Datos(jugadores);
                    tablaNombres.setAdapter(adapter);
                } else {
                    Log.w("TAG", "Error al leer la colección.", task.getException());
                }
            }
        });



    }

    private void colocarJugadores(Map a){
        String nombre = a.get("Nombre").toString();
        int puntuacion = Integer.valueOf(a.get("Puntuacion").toString());
        jugadores.add(new Jugador(nombre,puntuacion));
    }

}