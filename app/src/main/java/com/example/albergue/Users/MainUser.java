package com.example.albergue.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.albergue.Admin.AdminPerrosGatos;
import com.example.albergue.Admin.PrincipalAdminActivity;
import com.example.albergue.MainActivity;
import com.example.albergue.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainUser extends AppCompatActivity {

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