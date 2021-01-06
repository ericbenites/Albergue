package com.example.albergue.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.albergue.Admin.AdminPerrosGatos;
import com.example.albergue.Admin.PrincipalAdminActivity;
import com.example.albergue.Dto.UbicacionAlbergue;
import com.example.albergue.MainActivity;
import com.example.albergue.R;
import com.example.albergue.Ubicacion.VerUbicacion;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainUser extends AppCompatActivity {

    private Double lat1, long1;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
    }


    public void listarPerrosUser (View view){
        Intent intent = new Intent(MainUser.this, AdminPerrosGatos.class);
        String resca = "perros";
        int valor = 8;
        intent.putExtra("perros", valor);
        intent.putExtra("usuario", 20);
        startActivity(intent);
    }

    public void listarGatosUser (View view){
        Intent intent = new Intent(MainUser.this, AdminPerrosGatos.class);
        String resca2 = "gatos";
        int valor = 4;
        intent.putExtra("gatos", valor);
        intent.putExtra("usuario", 20);
        startActivity(intent);
    }

    public void verUbicacion(View view){

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Ubicacion").orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null){
                    UbicacionAlbergue ubicacionAlbergue = snapshot.getValue(UbicacionAlbergue.class);
                    lat1 = ubicacionAlbergue.getLatitud();
                    long1 = ubicacionAlbergue.getLongitud();
                    verMapa (lat1, long1);
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

    private void verMapa(Double lat1, Double long1) {
        Intent intent = new Intent(MainUser.this, VerUbicacion.class);
        intent.putExtra("latitud", lat1);
        intent.putExtra("longitud", long1);
        intent.putExtra("usuario", 4);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusuario, menu);
        return true;
    }

    public void cerrarSesion(MenuItem item){
        AuthUI instancia = AuthUI.getInstance();
        instancia.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(MainUser.this, MainActivity.class));
                finish();
            }
        });
    }

}