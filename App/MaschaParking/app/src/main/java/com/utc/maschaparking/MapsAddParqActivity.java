package com.utc.maschaparking;

import androidx.fragment.app.FragmentActivity;

import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Fragments.ParqAddFragment;
import com.utc.maschaparking.Fragments.ParqEditFragment;
import com.utc.maschaparking.FragmentsUserAdmin.EditParqUserAdminFragment;

import java.net.InterfaceAddress;

public class MapsAddParqActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    Geocoder geocoder = null;

    String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_add_parq);


        type = getIntent().getExtras().getString("typeLocate");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Debe mantener precionado el mapa para marcar..!", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light )).setDuration(4500);
        snackbar.show();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(googleMap.MAP_TYPE_NORMAL);
        LatLng sydney = null;

        if (type.equals("update")) {

            sydney = new LatLng( Double.parseDouble( getIntent().getExtras().getString("lat")), Double.parseDouble( getIntent().getExtras().getString("long")));
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmap)).anchor(0.5f, 1.0f)
                    .position(sydney).title("Lugar Actual")).
                    showInfoWindow();

        }

        if (type.equals("updateUserAdmin")) {

            sydney = new LatLng( Double.parseDouble( getIntent().getExtras().getString("lat")), Double.parseDouble( getIntent().getExtras().getString("long")));
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmap)).anchor(0.5f, 1.0f)
                    .position(sydney).title("Lugar Actual"))
                    .showInfoWindow();

        } else {

            sydney = new LatLng(-0.933772, -78.615021);
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            //mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource
            //       (R.drawable.common_full_open_on_phone)).anchor(0.0f,1.0f).position(sydney).title("d"));
        }
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        mMap.clear();
        String label = "Nuevo Sitio?";
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(label).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconmap)).anchor(0.5f, 1.0f);
        mMap.addMarker(markerOptions).showInfoWindow();

        if (type.equals("update")) {
            ParqEditFragment.latitudEdit = latLng.latitude;
            ParqEditFragment.longitudEdit = latLng.longitude;

        } if (type.equals("updateUserAdmin")) {
            EditParqUserAdminFragment.latitudEdit = latLng.latitude;
            EditParqUserAdminFragment.longitudEdit = latLng.longitude;

        }else{
            ParqAddFragment.latitud = latLng.latitude;
            ParqAddFragment.longitud = latLng.longitude;
        }



    }
}
