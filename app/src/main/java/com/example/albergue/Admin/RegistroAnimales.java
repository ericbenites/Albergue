package com.example.albergue.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.albergue.Dto.MascotasRegistro;
import com.example.albergue.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class RegistroAnimales extends AppCompatActivity {

    private Button tomarFoto, elegirFoto;
    private ImageView mImageView;
    private  Uri uri2;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    public String [] permissions ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_animales);

        mImageView = findViewById(R.id.imageViewRegistrar);
        tomarFoto = findViewById(R.id.buttonTomarFoto);
        elegirFoto = findViewById(R.id.buttonElegirFoto);

        elegirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    Log.d("infoApp", "primera entrada de galeria");
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                        Log.d("infoApp", "pedir permiso");
                    }
                    else {
                        pickImageFromGalley();
                    }
                }
            }
        });

        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisosCamara();
            }
        });

    }

    private void permisosCamara() {
        //Request for camera permission
        if (ContextCompat.checkSelfPermission(RegistroAnimales.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RegistroAnimales.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }
    }

    public void pickImageFromGalley (){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGalley();
                    break;
                }
                else {
                    Toast.makeText(this, "Permiso denegado para la galeria", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            case CAMERA_PERM_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    dispatchTakePictureIntent();
                    break;
                }else {
                    Toast.makeText(RegistroAnimales.this, "Se requiere permisos de cámara", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mImageView.setImageURI(data.getData());
            uri2 = data.getData();
            Log.d("infoApp", data.getData().toString());
        } else if (requestCode == CAMERA_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                File f = new File(rutaFoto);
                mImageView.setImageURI(Uri.fromFile(f));
                Log.d("infoApp", "nuevo uri: " + Uri.fromFile(f).toString());
                uri2 =  Uri.fromFile(f);

            }
        }

        else {
            Toast.makeText(this, "no se pudo", Toast.LENGTH_SHORT).show();
            Log.d("infoApp", String.valueOf(requestCode));
            Log.d("infoApp", String.valueOf(requestCode));

        }


    }

    String rutaFoto;

    private File createImageFile() throws IOException {
        //create an image file name
        String imagiFileName = "prueba_1";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imagiFileName, ".jpg", storageDir);
        //save a file:path for use with ACTION_VIEW intents
        rutaFoto = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent(){
        Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there's a camera activity to handle the intent
        if (tomarFotoIntent.resolveActivity(getPackageManager()) != null){
            //Create the file where the photo should go
            File photoFIle = null;
            try {
                photoFIle = createImageFile();
            } catch (IOException ex) {

            }
            //COntinue only if the file was successfully created
            if (photoFIle != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFIle);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(tomarFotoIntent, CAMERA_REQUEST_CODE);
            }

        }
    }


    public void registrarRescatado(View view) {

        EditText nombre = findViewById(R.id.editTextNombreRescatado);
        EditText peso = findViewById(R.id.editTextNumberPeso);
        EditText raza = findViewById(R.id.editTextRaza);
        EditText adici = findViewById(R.id.editTextAdicional);
        Spinner spinner = findViewById(R.id.spinnerRegistro);
        EditText fecha = findViewById(R.id.editTextDateRegistro);


        String peso1 = (peso.getText().toString());
        String raza1 = raza.getText().toString();
        String donde = adici.getText().toString();
        String nombre1 = nombre.getText().toString();
        String fecha1 = fecha.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if (uri2 != null ){
            if (!raza1.isEmpty() && !donde.isEmpty() && !fecha1.isEmpty() && !peso1.isEmpty() && !nombre1.isEmpty()){
                if (spinner.getSelectedItem().toString().equals("Perro")) {

                    MascotasRegistro mascotasRegistro = new MascotasRegistro();
                    mascotasRegistro.setNombre(nombre.getText().toString());
                    mascotasRegistro.setPeso(peso1);
                    mascotasRegistro.setRaza(raza.getText().toString());
                    mascotasRegistro.setAdicional(adici.getText().toString());
                    mascotasRegistro.setFecha(fecha.getText().toString());

                    databaseReference.child("Perros").push().setValue(mascotasRegistro)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child("Perros").orderByKey().limitToLast(1)
                                            .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if (snapshot.getValue() != null){
                                                String iddd = snapshot.getKey();
                                                Log.d("infoApp", "id: " + iddd);
                                                subirArchivoPerro(uri2, iddd);
                                                Log.d("infoApp", "uri: " + uri2.toString());
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    /*Intent intent = new Intent(RegistroAnimales.this, PrincipalAdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(RegistroAnimales.this, "Rescatado registrado exitosamente", Toast.LENGTH_LONG).show();*/
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(RegistroAnimales.this, "No se registró", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else if (spinner.getSelectedItem().toString().equals("Gato")) {

                    MascotasRegistro mascotasRegistro = new MascotasRegistro();
                    mascotasRegistro.setNombre(nombre.getText().toString());
                    //mascotasRegistro.setPeso(Double.valueOf(peso.getText().toString()));
                    mascotasRegistro.setRaza(raza.getText().toString());
                    mascotasRegistro.setAdicional(adici.getText().toString());
                    mascotasRegistro.setFecha(fecha.getText().toString());

                    databaseReference.child("Gatos").push().setValue(mascotasRegistro)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child("Gatos").orderByKey().limitToLast(1)
                                            .addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if (snapshot.getValue() != null){
                                                        String iddd = snapshot.getKey();
                                                        Log.d("infoApp", "id: " + iddd);
                                                        subirArchivoGato(uri2, iddd);
                                                        Log.d("infoApp", "uri: " + uri2.toString());
                                                    }
                                                }

                                                @Override
                                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                }

                                                @Override
                                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });


                }
            }else {
                Toast.makeText(RegistroAnimales.this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(RegistroAnimales.this, "Debe tomar o elegir una foto", Toast.LENGTH_SHORT).show();
        }




    }
    StorageReference storageReference;
    private void subirArchivoPerro(Uri data, String iddd) {
        storageReference = FirebaseStorage.getInstance().getReference();

        storageReference.child("Perros").child( iddd + ".jpg").putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getTask().isComplete()){
                            Intent intent = new Intent(RegistroAnimales.this, PrincipalAdminActivity.class);
                            Toast.makeText(RegistroAnimales.this, "Rescatado registrado exitosamente", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Intent intent = new Intent(RegistroAnimales.this, PrincipalAdminActivity.class);
                Toast.makeText(RegistroAnimales.this, "no se subió la fot", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }

    private void subirArchivoGato(Uri data, String iddd) {
        storageReference = FirebaseStorage.getInstance().getReference();

        storageReference.child("Gatos").child( iddd + ".jpg").putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getTask().isComplete()){
                            Intent intent = new Intent(RegistroAnimales.this, PrincipalAdminActivity.class);
                            Toast.makeText(RegistroAnimales.this, "Rescatado registrado exitosamente", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Intent intent = new Intent(RegistroAnimales.this, PrincipalAdminActivity.class);
                Toast.makeText(RegistroAnimales.this, "no se subió la fot", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        Intent intent = new Intent(RegistroAnimales.this, PrincipalAdminActivity.class);
        startActivity(intent);
        finish();
    }

}