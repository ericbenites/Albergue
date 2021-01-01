package com.example.albergue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonRegistro = findViewById(R.id.buttonRegistro);
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
        validacionUsuario();

    }

    public void Login (View view){
        //lista de proveedeores por donde puede iniciar sesion
        List<AuthUI.IdpConfig> proveedores = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());

        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.activity_main).
                setEmailButtonId(R.id.buttonLogin)
                .build();
        //Una instancia de Authui
        AuthUI authUI = AuthUI.getInstance();
        //createSignInIntentBuilder: indica que se usará el flujo propio de Auth UI
        //setAvailableProviders: se le indica la lista de proveedores
        //build: crea el intent
        Intent intent = authUI.createSignInIntentBuilder()
                .setAvailableProviders(proveedores).build();

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            validacionUsuario();
        }
    }

    public void validacionUsuario() {
        //obtener el logueo del usuario
        final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        //
        if (usuario != null){
            //para que vuelva a recargarl usuario y no se envíe el correo cada vez que se abre la app
            usuario.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (usuario.isEmailVerified()) {

                    }else {
                        Toast.makeText(MainActivity.this, "se le ha enviado un correo de verificación",
                                Toast.LENGTH_SHORT).show();
                        usuario.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("infoApp", "correo enviado");
                            }
                        });
                    }
                }
            });
        }
    }
}