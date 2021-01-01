package com.example.albergue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.albergue.Dto.UsersRegistroAuten;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    UsersRegistroAuten usersRegistroAuten;
    FirebaseAuth mAuth;

    public void registro(View view){

        EditText correo = findViewById(R.id.editTextCorreo);
        EditText contra1 = findViewById(R.id.editTextContra);
        EditText contra2 = findViewById(R.id.editTextVerificarContra);

        String email = correo.getText().toString();
        String contrase1 = contra1.getText().toString();
        String contrase2 = contra2.getText().toString();

        if (!email.isEmpty() && !contrase1.isEmpty() && !contrase2.isEmpty()){
            boolean validar = validarContrasena(contrase1, contrase2);
            if (validar){
                usersRegistroAuten = new UsersRegistroAuten();
                usersRegistroAuten.setMail(email);
                usersRegistroAuten.setPassword(contrase1);
                registro(usersRegistroAuten);
            }
        }else {
            Toast.makeText(RegistroActivity.this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void registro(UsersRegistroAuten usersRegistroAuten) {

        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(usersRegistroAuten.getMail(), usersRegistroAuten.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                            usuario.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }else {
                            Toast.makeText(RegistroActivity.this, "No se logró registrar",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "No se logró registrar correctamente",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    public boolean validarContrasena(String contra1, String contra2 ){
        boolean boole = false;
        if (contra1.equals(contra2)){
            if (contra1.length() >7){
                boole = true;
                return boole;
            }else {
                Toast.makeText(RegistroActivity.this, "La contraseña debe ser de al menos" +
                                "8 caracteres",
                        Toast.LENGTH_LONG).show();
                boole = false;
                return boole;
            }
        }else {
            Toast.makeText(RegistroActivity.this, "Las contraseñas no coindicen",
                    Toast.LENGTH_LONG).show();
            Log.d("infoApp", contra1 + " / " + contra2);
            boole = false;
            return boole;
        }
    }

}