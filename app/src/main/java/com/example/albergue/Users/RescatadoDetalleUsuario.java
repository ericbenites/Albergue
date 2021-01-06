package com.example.albergue.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.albergue.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RescatadoDetalleUsuario extends AppCompatActivity {

    private String nombre, peso, raza, adicional, fecha, idRescatado;
    private int tipo1, tipo2;
    private ImageView imageRescatado1;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescatado_detalle_usuario);

        nombre = getIntent().getStringExtra("nombre");
        peso = getIntent().getStringExtra("peso");
        raza = getIntent().getStringExtra("raza");
        adicional = getIntent().getStringExtra("adicional");
        fecha = getIntent().getStringExtra("fecha");
        idRescatado = getIntent().getStringExtra("idRescatado");
        tipo1 = getIntent().getIntExtra("tipo1", 0); //perro:5
        tipo2 = getIntent().getIntExtra("tipo2", 0); //gato:20
        imageRescatado1 = findViewById(R.id.imageRescatadoDetalleUsuario);

        Log.d("infoApp", nombre);
        Log.d("infoApp", String.valueOf(tipo1));
        Log.d("infoApp", String.valueOf(tipo2));
        verDetalle1(tipo1, tipo2);

    }

    private void verDetalle1(int tipo1, int tipo2) {
        if (tipo1 > 3){
            Log.d("infoApp", nombre);
            detallePerro1();
        } else if (tipo2 > 7){
            detalleGato1();
        }
    }

    private void detalleGato1() {
        storageReference =
                FirebaseStorage.getInstance().getReference().child("Gatos").child(idRescatado + ".jpg");
        Glide.with(this).load(storageReference).into(imageRescatado1);

        TextView nombreText1 = findViewById(R.id.nombreDetalleUsuario);
        TextView pesoText1 = findViewById(R.id.pesoDetalleUsuario);
        TextView razaText1 = findViewById(R.id.razaDetalleUsuario);
        TextView adicionalText1 = findViewById(R.id.historiaDetalleUsuario);
        TextView fechaText1 = findViewById(R.id.fechaDetalleUsuario);

        Log.d("infoApp", nombre);
        nombreText1.setText(nombre);
        pesoText1.setText(peso);
        razaText1.setText(raza);
        adicionalText1.setText(adicional);
        fechaText1.setText(fecha);
    }

    private void detallePerro1() {
        storageReference =
                FirebaseStorage.getInstance().getReference().child("Perros").child(idRescatado + ".jpg");
        Glide.with(this).load(storageReference).into(imageRescatado1);

        TextView nombreText1 = findViewById(R.id.nombreDetalleUsuario);
        TextView pesoText1 = findViewById(R.id.pesoDetalleUsuario);
        TextView razaText1 = findViewById(R.id.razaDetalleUsuario);
        TextView adicionalText1 = findViewById(R.id.historiaDetalleUsuario);
        TextView fechaText1 = findViewById(R.id.fechaDetalleUsuario);

        nombreText1.setText(nombre);
        pesoText1.setText(peso);
        razaText1.setText(raza);
        adicionalText1.setText(adicional);
        fechaText1.setText(fecha);
    }
}