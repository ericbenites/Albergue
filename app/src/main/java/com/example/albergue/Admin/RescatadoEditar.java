package com.example.albergue.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.albergue.Dto.MascotasRegistro;
import com.example.albergue.R;
import com.example.albergue.RescatadoDetalle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class RescatadoEditar extends AppCompatActivity {

    private String nombre, peso, raza, fecha, tipo, adicional, idRescatado;
    private String tipoFirebase;
    private StorageReference storageReference;
    private ImageView imageEditar;
    private int fYear, fMes, fDia;
    static final int DATE_ID = 0;
    Calendar C;
    private Button editarFotoGal, editarCamara;
    private String rutaFoto;
    Uri uri2;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public String[] permissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescatado_editar);

        EditText nombreEditar = findViewById(R.id.editTextNombreRescatadoEditar);
        EditText pesoEditar = findViewById(R.id.editTextNumberPesoEditar);
        EditText razaEditar = findViewById(R.id.editTextRazaEditar);
        TextView fechaEditar = findViewById(R.id.textViewfechaRegistroEditar);
        EditText adicionalEditar = findViewById(R.id.editTextAdicionalEditar);
        imageEditar = findViewById(R.id.imageViewEditar);


        nombre = getIntent().getStringExtra("nombre");
        peso = getIntent().getStringExtra("peso");
        raza = getIntent().getStringExtra("raza");
        adicional = getIntent().getStringExtra("adicional");
        fecha = getIntent().getStringExtra("fecha");
        idRescatado = getIntent().getStringExtra("idRescatado");
        tipo = getIntent().getStringExtra("tiporescatado");//Perro o Gato

        if (tipo.equals("Perro")){
            storageReference =
                    FirebaseStorage.getInstance().getReference().child("Perros").child(idRescatado + ".jpg");
            Glide.with(this).load(storageReference).into(imageEditar);

        }else  if (tipo.equals("Gato")){
            storageReference =
                    FirebaseStorage.getInstance().getReference().child("Gatos").child(idRescatado + ".jpg");
            Glide.with(this).load(storageReference).into(imageEditar);
        }

        nombreEditar.setText(nombre);
        pesoEditar.setText(peso);
        razaEditar.setText(raza);
        fechaEditar.setText(fecha);
        adicionalEditar.setText(adicional);
        editarFotoGal = findViewById(R.id.buttonElegirFotoEditar);
        editarCamara = findViewById(R.id.buttonTomarFotoEditar);

        editarFotoGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGalley();
                    }
                }
            }
        });
        editarCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisosCamara();
            }
        });


        TextView fecha1 = findViewById(R.id.textViewfechaRegistroEditar);
        fecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });


    }

    private void permisosCamara() {
        //Request for camera permission
        if (ContextCompat.checkSelfPermission(RescatadoEditar.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RescatadoEditar.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGalley();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            }
            case CAMERA_PERM_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(RescatadoEditar.this, "Se requiere permisos de cámara", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageEditar.setImageURI(data.getData());
            uri2 = data.getData();
            Log.d("infoApp", data.getData().toString());
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(rutaFoto);
                imageEditar.setImageURI(Uri.fromFile(f));
                Log.d("infoApp", "nuevo uri: " + Uri.fromFile(f).toString());
                uri2 = Uri.fromFile(f);

            }
        }

    }
    private File createImageFile() throws IOException {
        //create an image file name
        String imagiFileName = "prueba_1";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imagiFileName, ".jpg", storageDir);
        //save a file:path for use with ACTION_VIEW intents
        rutaFoto = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there's a camera activity to handle the intent
        if (tomarFotoIntent.resolveActivity(getPackageManager()) != null) {
            //Create the file where the photo should go
            File photoFIle = null;
            try {
                photoFIle = createImageFile();
            } catch (IOException ex) {

            }
            //COntinue only if the file was successfully created
            if (photoFIle != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFIle);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(tomarFotoIntent, CAMERA_REQUEST_CODE);
            }

        }
    }

    public void pickImageFromGalley() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void obtenerFecha() {
        C = Calendar.getInstance();
        TextView fechaHoy = findViewById(R.id.textViewfechaRegistroEditar);
        fMes = C.get(Calendar.MONTH);
        fDia = C.get(Calendar.DAY_OF_MONTH);
        fYear = C.get(Calendar.YEAR);

        DatePickerDialog fecha = new DatePickerDialog(RescatadoEditar.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int mes = month + 1;
                        String fechaa = dayOfMonth + "/" + mes + "/" + year;
                        fechaHoy.setText(fechaa);
                    }
                }, fYear, fMes, fDia);
        fecha.show();
    }
    private String nombreEdi, pesoEdi, razaEdi, fechaEdi, adicionalEdi;
    private String tipoRescatado;
    private int valor1 = 0;
    private int valor2 = 0;

    public void guardarCambios(View view){

        EditText nombreEditar = findViewById(R.id.editTextNombreRescatadoEditar);
        EditText pesoEditar = findViewById(R.id.editTextNumberPesoEditar);
        EditText razaEditar = findViewById(R.id.editTextRazaEditar);
        TextView fechaEditar = findViewById(R.id.textViewfechaRegistroEditar);
        EditText adicionalEditar = findViewById(R.id.editTextAdicionalEditar);

        nombreEdi = nombreEditar.getText().toString();
        pesoEdi = pesoEditar.getText().toString();
        razaEdi = razaEditar.getText().toString();
        fechaEdi = fechaEditar.getText().toString();
        adicionalEdi = adicionalEditar.getText().toString();

        MascotasRegistro mascotasRegistro = new MascotasRegistro();
        mascotasRegistro.setNombre(nombreEdi);
        mascotasRegistro.setPeso(pesoEdi);
        mascotasRegistro.setRaza(razaEdi);
        mascotasRegistro.setFecha(fechaEdi);
        mascotasRegistro.setAdicional(adicionalEdi);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if (uri2 != null) {
            Log.d("infoApp", "uri2" + uri2.toString());

            if (!razaEdi.isEmpty() && !adicionalEdi.isEmpty() && !fechaEdi.isEmpty() && !pesoEdi.isEmpty() && !nombreEdi.isEmpty()) {
                int pesovalor = Integer.parseInt(pesoEdi);
                try {
                    int nombrevalor = Integer.parseInt(nombreEdi);
                    Toast.makeText(RescatadoEditar.this, "Debe ingresar un nombre válido", Toast.LENGTH_SHORT).show();
                }catch (NumberFormatException e){
                    try {
                        int razavalor = Integer.parseInt(razaEdi);
                        Toast.makeText(RescatadoEditar.this, "No puede registrar un número como raza", Toast.LENGTH_SHORT).show();
                    }catch (NumberFormatException ed) {
                        if (pesovalor > 0 && pesovalor <= 20){
                        if (tipo.equals("Perro")) {
                            databaseReference.child("Perros").child(idRescatado).setValue(mascotasRegistro)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            valor1 = 5;
                                            actualizarFoto(valor1);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Intent intent = new Intent(RescatadoEditar.this, RescatadoDetalle.class);
                                    Toast.makeText(RescatadoEditar.this, "No se modificó", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else if (tipo.equals("Gato")) {
                            databaseReference.child("Gatos").child(idRescatado).setValue(mascotasRegistro)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            valor1 = 10;
                                            actualizarFoto(valor1);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Intent intent = new Intent(RescatadoEditar.this, RescatadoDetalle.class);
                                    Toast.makeText(RescatadoEditar.this, "No se modificó", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }else {
                            Toast.makeText(RescatadoEditar.this, "Debe ingresar una edad válida", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        }else {
                Toast.makeText(RescatadoEditar.this, "Todos los campos deben estar completos", Toast.LENGTH_LONG).show();
        }

        } else {

            if (!razaEdi.isEmpty() && !adicionalEdi.isEmpty() && !fechaEdi.isEmpty() && !pesoEdi.isEmpty() && !nombreEdi.isEmpty()) {
                int pesovalor123 = Integer.parseInt(pesoEdi);

                try {
                    int nombrevalor123 = Integer.parseInt(nombreEdi);
                    Toast.makeText(RescatadoEditar.this, "Debe ingresar un nombre válido", Toast.LENGTH_SHORT).show();
                }catch (NumberFormatException e){
                    try {
                        int razavalor = Integer.parseInt(razaEdi);
                        Toast.makeText(RescatadoEditar.this, "No puede registrar un número como raza", Toast.LENGTH_SHORT).show();
                    }catch (NumberFormatException ed) {

                        if (pesovalor123 > 0 && pesovalor123 <= 20){

                        if (tipo.equals("Perro")) {
                            databaseReference.child("Perros").child(idRescatado).setValue(mascotasRegistro)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(RescatadoEditar.this, AdminPerrosGatos.class);
                                            intent.putExtra("admin", 10);
                                            intent.putExtra("perros", 8);
                                            //int rescatado1 = getIntent().getIntExtra("perros", 0);//8
                                            //valor11 = getIntent().getIntExtra("admin", 0);//10
                               /* intent.putExtra("nombre", nombreEdi);
                                intent.putExtra("peso", pesoEdi);
                                intent.putExtra("raza", razaEdi);
                                intent.putExtra("adicional", adicionalEdi);
                                intent.putExtra("fecha", fechaEdi);
                                intent.putExtra("idRescatado", idRescatado);
                                intent.putExtra("tiporescatado", "Perro" );
                                intent.putExtra("tipo1", 5);*/
                                            startActivity(intent);
                                            Toast.makeText(RescatadoEditar.this, "Cambios guardados", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Intent intent = new Intent(RescatadoEditar.this, RescatadoDetalle.class);
                                    Toast.makeText(RescatadoEditar.this, "No se modificó", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else if (tipo.equals("Gato")) {
                            databaseReference.child("Gatos").child(idRescatado).setValue(mascotasRegistro)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(RescatadoEditar.this, RescatadoDetalle.class);
                                            intent.putExtra("nombre", nombreEdi);
                                            intent.putExtra("peso", pesoEdi);
                                            intent.putExtra("raza", razaEdi);
                                            intent.putExtra("adicional", adicionalEdi);
                                            intent.putExtra("fecha", fechaEdi);
                                            intent.putExtra("idRescatado", idRescatado);
                                            intent.putExtra("tiporescatado", "Gato");
                                            intent.putExtra("tipo2", 10);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Intent intent = new Intent(RescatadoEditar.this, RescatadoDetalle.class);
                                    Toast.makeText(RescatadoEditar.this, "No se modificó", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }else{
                            Toast.makeText(RescatadoEditar.this, "Debe ingresar una edad válida", Toast.LENGTH_LONG).show();
                    }
                    }
                }


        }else{
                Toast.makeText(RescatadoEditar.this, "Todos los campos deben estar completos", Toast.LENGTH_LONG).show();
        }

        }

    }

    private void actualizarFoto(int valor) {
        storageReference = FirebaseStorage.getInstance().getReference();

        if (valor > 3){
            storageReference.child("Perros/" + idRescatado + ".jpg").putFile(uri2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getTask().isComplete()) {

                                Intent intent = new Intent(RescatadoEditar.this, AdminPerrosGatos.class);
                                intent.putExtra("admin", 10);
                                intent.putExtra("perros", 8);
                                //int rescatado1 = getIntent().getIntExtra("perros", 0);//8
                                //valor11 = getIntent().getIntExtra("admin", 0);//10
                               /* intent.putExtra("nombre", nombreEdi);
                                intent.putExtra("peso", pesoEdi);
                                intent.putExtra("raza", razaEdi);
                                intent.putExtra("adicional", adicionalEdi);
                                intent.putExtra("fecha", fechaEdi);
                                intent.putExtra("idRescatado", idRescatado);
                                intent.putExtra("tiporescatado", "Perro" );
                                intent.putExtra("tipo1", 5);*/
                                startActivity(intent);
                                Toast.makeText(RescatadoEditar.this, "Cambios guardados", Toast.LENGTH_LONG).show();
                                finish();
                                valor1 = 0;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Intent intent = new Intent(RescatadoEditar.this, RescatadoDetalle.class);
                    //Toast.makeText(RescatadoEditar.this, "no se subió la fot", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            });
        }else if (valor > 7){
            storageReference.child("Gatos/" + idRescatado + ".jpg").putFile(uri2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getTask().isComplete()) {

                                Intent intent = new Intent(RescatadoEditar.this, RescatadoDetalle.class);
                                intent.putExtra("nombre", nombreEdi);
                                intent.putExtra("peso", pesoEdi);
                                intent.putExtra("raza", razaEdi);
                                intent.putExtra("adicional", adicionalEdi);
                                intent.putExtra("fecha", fechaEdi);
                                intent.putExtra("idRescatado", idRescatado);
                                intent.putExtra("tiporescatado", "Gato" );
                                intent.putExtra("tipo2", 10);
                                startActivity(intent);
                                finish();
                                valor1 = 0;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Intent intent = new Intent(RescatadoEditar.this, RescatadoDetalle.class);
                    //Toast.makeText(RescatadoEditar.this, "no se subió la fot", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            });
        }

    }
}