package com.example.albergue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RescatadoDetalle extends AppCompatActivity {

    private String nombre, peso, raza, adicional, fecha, idRescatado;
    private int tipo1, tipo2;
    private ImageView imageRescatado;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescatado_detalle);

        nombre = getIntent().getStringExtra("nombre");
        peso = getIntent().getStringExtra("peso");
        raza = getIntent().getStringExtra("raza");
        adicional = getIntent().getStringExtra("adicional");
        fecha = getIntent().getStringExtra("fecha");
        idRescatado = getIntent().getStringExtra("idRescatado");
        tipo1 = getIntent().getIntExtra("tipo1", 0); //perro:5
        tipo2 = getIntent().getIntExtra("tipo2", 0); //gato:10
        imageRescatado = findViewById(R.id.imageViewRescatadoDetalle);

        verDetalle();

    }

    private void verDetalle() {
        if (tipo1 < 7){
            detallePerro();
        } else if (tipo2 > 7){
            detalleGato();
        }

    }

    private void detalleGato() {
        storageReference =
                FirebaseStorage.getInstance().getReference().child("Gatos").child(idRescatado + ".jpg");
        Glide.with(this).load(storageReference).into(imageRescatado);

        TextView nombreText = findViewById(R.id.nombreDetalleAdmin);
        TextView pesoText = findViewById(R.id.pesoDetalleAdmin);
        TextView razaText = findViewById(R.id.razaDetalleAdmin);
        TextView adicionalText = findViewById(R.id.historiaDetalleAdmin);
        TextView fechaText = findViewById(R.id.fechaDetalleAdmin);

        nombreText.setText(nombre);
        pesoText.setText(peso);
        razaText.setText(raza);
        adicionalText.setText(adicional);
        fechaText.setText(fecha);
    }

    private void detallePerro() {
        storageReference =
                FirebaseStorage.getInstance().getReference().child("Perros").child(idRescatado + ".jpg");
        Glide.with(this).load(storageReference).into(imageRescatado);

        TextView nombreText = findViewById(R.id.nombreDetalleAdmin);
        TextView pesoText = findViewById(R.id.pesoDetalleAdmin);
        TextView razaText = findViewById(R.id.razaDetalleAdmin);
        TextView adicionalText = findViewById(R.id.historiaDetalleAdmin);
        TextView fechaText = findViewById(R.id.fechaDetalleAdmin);

        nombreText.setText(nombre);
        pesoText.setText(peso);
        razaText.setText(raza);
        adicionalText.setText(adicional);
        fechaText.setText(fecha);
    }


}