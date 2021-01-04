package com.example.albergue.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.albergue.Adaptadores.ListaGatosAdapter;
import com.example.albergue.Adaptadores.ListaPerrosAdapter;
import com.example.albergue.Dto.MascotasRegistro;
import com.example.albergue.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminPerros extends AppCompatActivity {

    ArrayList<MascotasRegistro> listaPerrosGatos = new ArrayList<>();
    RecyclerView perroRecycler;
    ListaPerrosAdapter listaPerrosAdapter;
    ListaGatosAdapter listaGatosAdapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_perros);

        int rescatado1 = getIntent().getIntExtra("perros", 0);
        int rescatado2 = getIntent().getIntExtra("gatos", 0);
        //String rescatado2 = getIntent().getStringExtra("gatos");
        Log.d("infoApp", "llegue a AdminPerros");
        Log.d("infoApp", String.valueOf(rescatado1));

        String rr = "perros";

        if (rescatado1 > 5){
            listarPerros();
        } else if (rescatado2 <5){
            listarGatos();
        } else {
            Toast.makeText(AdminPerros.this, "no ingresó a ninguno", Toast.LENGTH_SHORT).show();
        }

    }

    private void listarGatos() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Gatos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPerrosGatos.clear();

                for (DataSnapshot children : snapshot.getChildren()){
                    MascotasRegistro mascotasRegistro = children.getValue(MascotasRegistro.class);
                    mascotasRegistro.setIdRescatado(children.getKey());
                    listaPerrosGatos.add(mascotasRegistro);
                }

                perroRecycler = findViewById(R.id.rvPerros);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminPerros.this);
                perroRecycler.setLayoutManager(linearLayoutManager);

                listaGatosAdapter = new ListaGatosAdapter(listaPerrosGatos, AdminPerros.this);
                perroRecycler.setAdapter(listaGatosAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listarPerros() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Perros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPerrosGatos.clear();

                for (DataSnapshot children : snapshot.getChildren()){
                    MascotasRegistro mascotasRegistro = children.getValue(MascotasRegistro.class);
                    mascotasRegistro.setIdRescatado(children.getKey());
                    listaPerrosGatos.add(mascotasRegistro);
                }

                perroRecycler = findViewById(R.id.rvPerros);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminPerros.this);
                perroRecycler.setLayoutManager(linearLayoutManager);

                listaPerrosAdapter = new ListaPerrosAdapter(listaPerrosGatos, AdminPerros.this);
                perroRecycler.setAdapter(listaPerrosAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}