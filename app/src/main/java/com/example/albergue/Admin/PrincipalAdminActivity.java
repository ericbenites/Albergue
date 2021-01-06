package com.example.albergue.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.albergue.Dto.UbicacionAlbergue;
import com.example.albergue.MainActivity;
import com.example.albergue.R;
import com.example.albergue.Ubicacion.VerUbicacion;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PrincipalAdminActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    private Double lat1, long1;
    private UbicacionAlbergue ubicacionAlbergue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_admin);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_4main, menu);
        return true;
    }

    public void registrar (View view){
        Intent intent = new Intent(this, RegistroAnimales.class);
        startActivity(intent);
    }

    public void listarPerros (View view){
        Intent intent = new Intent(PrincipalAdminActivity.this, AdminPerrosGatos.class);
        String resca = "perros";
        int valor = 8;
        intent.putExtra("perros", valor);
        intent.putExtra("admin", 10);
        startActivity(intent);
    }

    public void listarGatos (View view){
        Intent intent = new Intent(PrincipalAdminActivity.this, AdminPerrosGatos.class);
        String resca2 = "gatos";
        int valor = 4;
        intent.putExtra("gatos", valor);
        intent.putExtra("admin", 10);
        startActivity(intent);
    }

    public void escogerUbicacion(MenuItem item){
        Log.d("infoApp", "estoy en permisosUbicacion");
        if (ActivityCompat.checkSelfPermission(PrincipalAdminActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            Log.d("infoApp", "tienes los permisos");
            getLocation();
        } else {

            ActivityCompat.requestPermissions(PrincipalAdminActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED ){
                //when permission grated
                //call method
                getLocation();
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    if (location != null) {

                        try {
                            //Initialize geoCoder
                            Geocoder geocoder = new Geocoder(PrincipalAdminActivity.this,
                                    Locale.getDefault());
                            //Initialize address list
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );
                            //Set Latitude on TextVIew
                            //textView1.setText("latitud: " + addresses.get(0).getLatitude());
                            lat1 = addresses.get(0).getLatitude();
                            Log.d("infoApp", "latituuud" + lat1.toString());
                            //set Longitude
                            //textView2.setText("longitud : " + addresses.get(0).getLongitude());
                            long1 = addresses.get(0).getLongitude();
                            Log.d("infoApp", "longituuud" + long1.toString());
                            verUbicacionEnMapa();
                            //Set country name
                            //textView3.setText("country name : " + addresses.get(0).getCountryName());
                            //set locality
                            //textView4.setText("locality: " + addresses.get(0).getLocality());
                            //set address
                            //textView5.setText("dirección: " + addresses.get(0).getAddressLine(0));


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }

    }

    private void verUbicacionEnMapa() {

        ubicacionAlbergue = new UbicacionAlbergue();
        ubicacionAlbergue.setLatitud(lat1);
        ubicacionAlbergue.setLongitud(long1);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Ubicacion").push().setValue(ubicacionAlbergue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(PrincipalAdminActivity.this, VerUbicacion.class);
                intent.putExtra("latitud", lat1);
                intent.putExtra("longitud", long1);
                intent.putExtra("admin", 20);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("infoApp", "no se guardó la ubicación en database");
            }
        });


    }


    public void logout(MenuItem item){
        AuthUI instancia = AuthUI.getInstance();
        instancia.signOut(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(PrincipalAdminActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void irDonacionesAdmin(View view){
        Intent intent = new Intent(PrincipalAdminActivity.this, DonacionesAdmin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }
}