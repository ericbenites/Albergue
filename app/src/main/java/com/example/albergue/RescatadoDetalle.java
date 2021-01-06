package com.example.albergue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.albergue.Admin.AdminPerrosGatos;
import com.example.albergue.Admin.PrincipalAdminActivity;
import com.example.albergue.Admin.RescatadoEditar;
import com.example.albergue.Users.MainUser;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RescatadoDetalle extends AppCompatActivity {

    private String nombre, peso, raza, adicional, fecha, idRescatado, tipoRescatado;
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
        tipoRescatado = getIntent().getStringExtra("tiporescatado"); //Perro o Gato
        tipo1 = getIntent().getIntExtra("tipo1", 0); //perro:5
        tipo2 = getIntent().getIntExtra("tipo2", 0); //gato:10
        imageRescatado = findViewById(R.id.imageViewRescatadoDetalle);

        Log.d("infoApp", "id: " +  idRescatado);
        verDetalle();

    }

    private void verDetalle() {
        if (tipo1 > 3){
            detallePerro();
        } else if (tipo2 > 7){
            detalleGato();
        }

    }

    private void detalleGato() {
        StorageReference storageReference1 =
                FirebaseStorage.getInstance().getReference().child("Gatos").child(idRescatado + ".jpg");
        Glide.with(this).load(storageReference1).into(imageRescatado);
        Log.d("infoApp", "estoy en detalle gato:");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalleeliminar, menu);
        return true;
    }

    public void cerrarSesion(MenuItem item){
        AuthUI instancia = AuthUI.getInstance();
        instancia.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(RescatadoDetalle.this, MainActivity.class));
                finish();
            }
        });
    }

    public void eliminarRegistro (MenuItem item){
        if (tipo1 > 3){
            eliminarPerro();
        } else if (tipo2 > 7){
            eliminarGato();
        }
    }

    private void eliminarPerro() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Perros").child(idRescatado).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(RescatadoDetalle.this, AdminPerrosGatos.class);
                        intent.putExtra("admin", 10);
                        intent.putExtra("perros", 8);
                        Toast.makeText(RescatadoDetalle.this, "Registro eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void eliminarGato() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Gatos").child(idRescatado).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(RescatadoDetalle.this, AdminPerrosGatos.class);
                        intent.putExtra("admin", 10);
                        intent.putExtra("gatos", 4);
                        Toast.makeText(RescatadoDetalle.this, "Registro eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void editarRescatado(View view){

        Intent intent = new Intent(RescatadoDetalle.this, RescatadoEditar.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("peso", peso);
        intent.putExtra("raza", raza);
        intent.putExtra("adicional", adicional);
        intent.putExtra("fecha", fecha);
        intent.putExtra("idRescatado", idRescatado);
        intent.putExtra("tiporescatado", tipoRescatado);//Perro o Gato
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        if(tipoRescatado.equals("Perro")){
            Intent in = new Intent(RescatadoDetalle.this, AdminPerrosGatos.class);
            in.putExtra("admin", 10);
            in.putExtra("perros", 8);
            startActivity(in);
            finish();
        } else  if (tipoRescatado.equals("Gato")){
            Intent in = new Intent(RescatadoDetalle.this, AdminPerrosGatos.class);
            in.putExtra("admin", 10);
            in.putExtra("gatos", 4);
            startActivity(in);
            finish();
        }

    }


}