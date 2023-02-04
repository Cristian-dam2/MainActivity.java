package com.example.wheeloffortune.Actividades;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wheeloffortune.Auxiliares.Jugador;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaJugadoresActivity extends AppCompatActivity {
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private Button boton_volver;
    private FirebaseFirestore db;
    private StorageReference myStorage;
    private StorageReference storageReference;
    private StorageReference photoReference;
    private HashMap<String,String> informacion = new HashMap<>();
    private Bitmap bmp ;
    private String nombre;
    private int puntuacion;
    private ArrayList<JugadorModelo> jugadorModelos = new ArrayList<>();
    private Bitmap bitmap;
    private ImageView i;
    private TextView p;
    private TextView n;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelo_lista_jugadores);
        db = FirebaseFirestore.getInstance();
        //boton_volver = (Button) findViewById(R.id.boton_volver);
        myStorage = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        i = findViewById(R.id.image_view);
        p = findViewById(R.id.player_score);
        n = findViewById(R.id.player_name);

//        boton_volver.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                finish();
//            }
//        });
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

                for (String key : informacion.keySet()) {
                    JugadorModelo a = new JugadorModelo(getBitmapFromFirebase(key),informacion.get(key));

                    n.setText(a.getNombre());
                    p.setText(a.getPuntuacion());
                    jugadorModelos.add(a);

                }
            }
        });

    }


    public Bitmap getBitmapFromFirebase(String userID) {
        bitmap = null;
        StorageReference imageRef = myStorage.child("usuarios/" + userID + "/imagen_perfil.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.e("OK", "Imagen cargada con exito");
                i.setImageBitmap(bitmap);
                return;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Error", "No se pudo obtener la imagen");
            }
        });
        return bitmap;
    }

}