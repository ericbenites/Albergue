package com.example.albergue.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.albergue.Dto.Donaciones;
import com.example.albergue.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonacionesAdmin extends AppCompatActivity {

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donaciones_admin);

        EditText nombreluki = findViewById(R.id.lukitaNombreAdmin);
        EditText numeroluki = findViewById(R.id.lukitaNumeroAdmin);
        EditText nombreyape = findViewById(R.id.yapeNombreAdmin);
        EditText numeroyape = findViewById(R.id.yapeNumeroAdmin);

        String lukiname = nombreluki.getText().toString();
        String lukinumber = numeroluki.getText().toString();
        String yapename = nombreyape.getText().toString();
        String yapenumber =numeroyape.getText().toString();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        Log.d("infoApp", "uid: " + uid);

    }

    public void  registrarDonaciones(View view){

        EditText nombreluki = findViewById(R.id.lukitaNombreAdmin);
        EditText numeroluki = findViewById(R.id.lukitaNumeroAdmin);
        EditText nombreyape = findViewById(R.id.yapeNombreAdmin);
        EditText numeroyape = findViewById(R.id.yapeNumeroAdmin);

        String lukiname = nombreluki.getText().toString();
        String lukinumber = numeroluki.getText().toString();
        String yapename = nombreyape.getText().toString();
        String yapenumber =numeroyape.getText().toString();

        Donaciones donaciones = new Donaciones();
        donaciones.setNombrelukita(lukiname);
        donaciones.setNombreyape(yapename);
        donaciones.setNumerolukita(lukinumber);
        donaciones.setNumeroyape(yapenumber);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Donaciones").child(uid).setValue(donaciones).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(DonacionesAdmin.this, PrincipalAdminActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


}