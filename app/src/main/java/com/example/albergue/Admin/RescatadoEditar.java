package com.example.albergue.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.albergue.Dto.MascotasRegistro;
import com.example.albergue.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RescatadoEditar extends AppCompatActivity {

    private String nombre, peso, raza, fecha, tipo, adicional, idRescatado;
    private String tipoFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescatado_editar);

        EditText nombreEditar = findViewById(R.id.editTextNombreRescatadoEditar);
        EditText pesoEditar = findViewById(R.id.editTextNumberPesoEditar);
        EditText razaEditar = findViewById(R.id.editTextRazaEditar);
        TextView fechaEditar = findViewById(R.id.textViewfechaRegistroEditar);
        EditText adicionalEditar = findViewById(R.id.editTextAdicionalEditar);
        Spinner spinner = findViewById(R.id.spinnerRegistroEditar);


        nombre = getIntent().getStringExtra("nombre");
        peso = getIntent().getStringExtra("peso");
        raza = getIntent().getStringExtra("raza");
        adicional = getIntent().getStringExtra("adicional");
        fecha = getIntent().getStringExtra("fecha");
        idRescatado = getIntent().getStringExtra("idRescatado");
        tipo = getIntent().getStringExtra("tiporescatado");//Perro o Gato

        if (tipo.equals("Perro")){
            tipoFirebase = "Perros";
        }else  if (tipo.equals("Gatos")){
            tipoFirebase = "Gatos";
        }

        nombreEditar.setText(nombre);
        pesoEditar.setText(peso);
        razaEditar.setText(raza);
        fechaEditar.setText(fecha);
        adicionalEditar.setText(adicional);


    }

    public void guardarCambios(View view){

        EditText nombreEditar = findViewById(R.id.editTextNombreRescatadoEditar);
        EditText pesoEditar = findViewById(R.id.editTextNumberPesoEditar);
        EditText razaEditar = findViewById(R.id.editTextRazaEditar);
        TextView fechaEditar = findViewById(R.id.textViewfechaRegistroEditar);
        EditText adicionalEditar = findViewById(R.id.editTextAdicionalEditar);
        Spinner spinner = findViewById(R.id.spinnerRegistroEditar);

        String nombreEdi = nombreEditar.getText().toString();
        String pesoEdi = pesoEditar.getText().toString();
        String razaEdi = razaEditar.getText().toString();
        String fechaEdi = fechaEditar.getText().toString();
        String adicionalEdi = adicionalEditar.getText().toString();

        MascotasRegistro mascotasRegistro = new MascotasRegistro();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //databaseReference.child(tipoFirebase).setValue()

    }
}