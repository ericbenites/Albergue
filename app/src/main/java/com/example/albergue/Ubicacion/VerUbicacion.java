package com.example.albergue.Ubicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.albergue.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VerUbicacion extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private Double latitud1;
    private Double longitud1;
    private int usuario, admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ubicacion);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map1);
        supportMapFragment.getMapAsync(this);
        Toast.makeText(VerUbicacion.this, "Tocar la pantalla para ver la ubicación", Toast.LENGTH_LONG).show();

        latitud1 = getIntent().getDoubleExtra("latitud",0);
        longitud1 = getIntent().getDoubleExtra("longitud", 0);
        usuario = getIntent().getIntExtra("usuario", 0);//4
        admin = getIntent().getIntExtra("admin", 0);//20
        Log.d("infoApp", "ver ubicacion latuitud:  " + latitud1.toString());
        Log.d("infoApp", "ver ubicacion longitu:  " + longitud1.toString());


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //creating marker
                MarkerOptions markerOptions = new MarkerOptions();
                //set marker position
                markerOptions.position(latLng);
                //set latitude and loca

                latLng = new LatLng(latitud1, longitud1);

                //clear the previously click
                gMap.clear();
                //zoom
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
                //add marker on map
                gMap.addMarker(markerOptions.position(latLng).title("Esta es la ubicación del Albergue :D"));
                Log.d("infoApp", latLng.latitude +"/" + latLng.longitude);

                if (latLng.latitude != 0){
                    if (admin > 10) {
                        Toast.makeText(VerUbicacion.this, "Tenemos registrada su ubicación", Toast.LENGTH_LONG).show();
                    }else if (usuario > 2) {
                        Toast.makeText(VerUbicacion.this, "Esta es la ubicación del Albergue", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}