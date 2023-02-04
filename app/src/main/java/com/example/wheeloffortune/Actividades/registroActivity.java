package com.example.wheeloffortune.Actividades;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wheeloffortune.Auxiliares.Esperar;
import com.example.wheeloffortune.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class registroActivity extends AppCompatActivity {
    TextView quitarAvatartexto;
    EditText intro_nombre, intro_mail,  intro_pwd, intro_pwd_conf;
    Button boton_registro;
    TextView link_volver;
    ImageView avatar;
    FirebaseAuth myAuth;
    FirebaseFirestore myStore;
    String idUsuario;
    StorageReference myStorage;
    StorageReference storageReference;
    StorageReference photoReference;
    Bitmap imageBitmap = null;
    Uri selectedImageUri= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        intro_nombre = findViewById(R.id.editTextTextPersonNameR);
        intro_mail = findViewById(R.id.editTextTextPersonName2R);
        intro_pwd = findViewById(R.id.editTextTextPasswordR);
        intro_pwd_conf = findViewById(R.id.editTextTextPassword2r);
        boton_registro = findViewById(R.id.buttonR);
        link_volver = (TextView) findViewById(R.id.textView5r);
        avatar = (ImageView) findViewById(R.id.Imagen);
        avatar.setImageResource(R.drawable.avatar);
        quitarAvatartexto = (TextView) findViewById(R.id.textviewCambiarAvatar);
        myStore = FirebaseFirestore.getInstance();
        myAuth = FirebaseAuth.getInstance();
        myStorage = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

//    private void imageChooser() {
//            // create an instance of the
//            // intent of the type image
//            Intent i = new Intent();
//            i.setType("image/*");
//            i.setAction(Intent.ACTION_GET_CONTENT);
//
//            // pass the constant to compare it
//            // with the returned requestCode
//            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
//        }
//
//        // this function is triggered when user
//        // selects the image from the imageChooser
//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//
//            if (resultCode == RESULT_OK) {
//
//                // compare the resultCode with the
//                // SELECT_PICTURE constant
//                if (requestCode == SELECT_PICTURE) {
//                    // Get the url of the image from data
//                    Uri selectedImageUri = data.getData();
//                    if (null != selectedImageUri) {
//                        // update the preview image in the layout
//                        avatar.setImageURI(selectedImageUri);
//                    }
//                }
//            }
//        }

    private void selectImage() {
        final CharSequence[] options = { "Tomar una foto", "Abrir la galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(registroActivity.this);
        builder.setTitle("Agregar foto!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Tomar una foto"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Abrir la galeria"))
                {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);

                    // pass the constant to compare it
                    // with the returned requestCode
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), 2);
                }
                else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                 imageBitmap = (Bitmap) extras.get("data");
                  avatar.setImageBitmap(imageBitmap);
                  selectedImageUri = null;
            } else if (requestCode == 2) {
                // Get the url of the image from data
                    selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // update the preview image in the layout
                        avatar.setImageURI(selectedImageUri);
                        imageBitmap = null;
                    }
            }
        }

        quitarAvatartexto.setVisibility(View.VISIBLE);
    }


//    public void registrar(){
//        String email = intro_mail.getText().toString();
//        String password = intro_pwd.getText().toString();
//
//
//        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                //Si no ha habido problemas (la tarea de registrar el usuario ha sido exitosa: Feedback y redireccion a la MainActivity
//                if (task.isSuccessful()) {
//                    Toast.makeText(registroActivity.this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), myLoginActivity.class);
//                    startActivity(intent);
//                } else {
//
//                    //Toast.makeText(Register.this,"Se ha producido une error en el proceso de registro.", Toast.LENGTH_SHORT ).show();
//                    Toast.makeText(registroActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//
//    }

    public void mandarLogin(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);

    }

    public void mandarRegistro2(View view) {

        String email = intro_mail.getText().toString();
        String password = intro_pwd.getText().toString();
        String name = intro_nombre.getText().toString();
        String password2 = intro_pwd_conf.getText().toString();

        if (!password.equals(password2)) {
            intro_pwd.setError("Contraseña no coincide");
            Toast.makeText(registroActivity.this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || name == null) {
            intro_nombre.setError("Nombre no valido");
            Toast.makeText(registroActivity.this, "Nombre no valido", Toast.LENGTH_SHORT).show();
            return;
        }
        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Si no ha habido problemas (la tarea de registrar el usuario ha sido exitosa: Feedback y redireccion a la MainActivity
                if (task.isSuccessful()) {
                    Log.d("OK", "Se guardo el perfil");

                    //HE CAMBIADO EL ORDEN DE LA INICIALIZACION DEL IDUSUARIO, PARECE QUE FUNCIONA CORRECTAMENTE...
                    idUsuario = myAuth.getCurrentUser().getUid();
                    photoReference = storageReference.child("usuarios/" + idUsuario + "/imagen_perfil.jpg");
                    DocumentReference docRef = myStore.collection("Usuarios").document(idUsuario);
                    HashMap<String, String> infoUsuario = new HashMap<>();
                    infoUsuario.put("Nombre", name);
                    infoUsuario.put("Puntuacion", "0");
                    docRef.set(infoUsuario);
                    if (imageBitmap == null && selectedImageUri != null) {
                        subirImagenCloudStorageFirebase(selectedImageUri);
                    }
                    if (imageBitmap != null && selectedImageUri == null) {
                        subirImagenCloudStorageFirebase(getImageUri(imageBitmap));
                    }
                    if (imageBitmap == null && selectedImageUri == null) {
                        subirImagenCloudStorageFirebase(transformarDrawabletoUri());
                    }
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    Esperar.segundos(2500);
                    startActivity(intent);
                } else {
                    Log.d("ER", "No se guardo el perfil");

                }
            }
        });

    }

    public void quitarAvatar(View view) {
        quitarAvatartexto.setVisibility(View.INVISIBLE);
        imageBitmap = null;
        selectedImageUri =null;
        avatar.setImageResource(R.drawable.avatar);
    }


    private void subirImagenCloudStorageFirebase(Uri imagen) {
        StorageReference referenciaFichero = myStorage.child("usuarios/" + idUsuario + "/imagen_perfil.jpg");
        referenciaFichero.putFile(imagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("OK", "Se cargo la imagen en el Storage");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ER", "No se cargo la imagen en el Storage");

            }
        });
    }


    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Uri transformarDrawabletoUri(){
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.avatar)
                + '/' + getResources().getResourceTypeName(R.drawable.avatar)
                + '/' + getResources().getResourceEntryName(R.drawable.avatar));
        return imageUri;
    }
}