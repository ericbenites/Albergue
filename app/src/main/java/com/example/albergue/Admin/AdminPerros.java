package com.example.albergue.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.albergue.Dto.MascotasRegistro;
import com.example.albergue.R;

import java.util.ArrayList;

public class AdminPerros extends AppCompatActivity {

    ArrayList<MascotasRegistro> listaPerros = new ArrayList<>();
    RecyclerView perroRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_perros);
    }
}