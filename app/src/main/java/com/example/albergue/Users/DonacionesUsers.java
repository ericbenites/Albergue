package com.example.albergue.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.albergue.Dto.Donaciones;
import com.example.albergue.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonacionesUsers extends AppCompatActivity {

    private String yapename, yapenumber, lukitaname, lukitanumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donaciones_users);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Donaciones").child("W0pfxu2AwPd51rytX7Lb1jVhXKF2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Donaciones donaciones = snapshot.getValue(Donaciones.class);
                    yapename = donaciones.getNombreyape();
                    yapenumber = donaciones.getNumeroyape();
                    lukitaname = donaciones.getNombrelukita();
                    lukitanumber = donaciones.getNumerolukita();
                    mostrarDonacion( yapename, yapenumber,  lukitaname, lukitanumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void mostrarDonacion(String yapename, String yapenumber, String lukitaname, String lukitanumber) {

        TextView yapenombre = findViewById(R.id.yapeNombreUsuario);
        TextView yapenumero = findViewById(R.id.numeroYapeUsuario);
        TextView lukanombre = findViewById(R.id.lukitaNombreUsuario);
        TextView lukanumero = findViewById(R.id.lukitoNumeroUsuario);

        yapenombre.setText(yapename);
        yapenumero.setText(yapenumber);
        lukanombre.setText(lukitaname);
        lukanumero.setText(lukitanumber);

    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        Intent intent3 = new Intent(DonacionesUsers.this, MainUser.class);
        startActivity(intent3);
        finish();
    }*/



}