package com.example.albergue.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.albergue.Admin.PrincipalAdminActivity;
import com.example.albergue.MainActivity;
import com.example.albergue.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_4main, menu);
        return true;
    }

    public void logout(MenuItem item){
        AuthUI instancia = AuthUI.getInstance();
        instancia.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(MainUserActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}