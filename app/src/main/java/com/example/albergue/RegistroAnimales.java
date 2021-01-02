package com.example.albergue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.albergue.Admin.PrincipalAdminActivity;
import com.example.albergue.Dto.MascotasRegistro;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroAnimales extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_animales);
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

        if (!raza1.isEmpty() && !donde.isEmpty() && !fecha1.isEmpty() && !peso1.isEmpty()){
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
                                Intent intent = new Intent(RegistroAnimales.this, PrincipalAdminActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegistroAnimales.this, "Rescatado registrado exitosamente", Toast.LENGTH_LONG).show();
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
                                Intent intent = new Intent(RegistroAnimales.this, PrincipalAdminActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegistroAnimales.this, "Rescatado registrado exitosamente", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Toast.makeText(RegistroAnimales.this, "No se registró", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        }else {
            Toast.makeText(RegistroAnimales.this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        }




    }
}