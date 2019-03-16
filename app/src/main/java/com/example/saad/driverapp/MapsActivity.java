package com.example.saad.driverapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

@SuppressLint("MissingPermission")
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button buttonStartDriving;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        buttonStartDriving = findViewById(R.id.buttonStartRiding);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Starting the background service

        buttonStartDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonStartDriving.getText().equals("Start Ride")) {
                    Intent intent = new Intent(getApplicationContext(), AndroidLocationServices.class);
                    startService(intent);
                    buttonStartDriving.setText("Stop");
                    Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("Stopping Service")
                            .setMessage("Are you sure")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent myService = new Intent(getApplicationContext(), AndroidLocationServices.class);
                                    stopService(myService);
                                    buttonStartDriving.setText("Start Ride");
                                    Toast.makeText(getApplicationContext(),"Service Stopped",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Cancel", null).setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Exit")
                .setMessage("Are you sure")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myService = new Intent(getApplicationContext(), AndroidLocationServices.class);
                        stopService(myService);
                        buttonStartDriving.setText("Start Ride");
                        MapsActivity.super.onBackPressed();
                    }
                }).setNegativeButton("Cancel", null).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.20453, 97.34466),2));


        //Adding GoogleMaps icons on the map for user interference
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Location update will only be registered after the map is ready
        registerLocationUpdates();
    }


    private LocationManager locationManager;
    private ArrayList<LatLng> locationPoints;
    private Polyline polyline;


    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            Log.e("Google", "Location Changed");

            locationPoints.add(new LatLng(location.getLatitude(),location.getLongitude()));
            appendCurrentPointOnLine();

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    private void appendCurrentPointOnLine() {


        mMap.clear();

        PolylineOptions polylineOptions=new PolylineOptions();
        polylineOptions.width(12);
        polylineOptions.color(Color.rgb(76,171,234));

        for (int i = 0; i < locationPoints.size(); i++) {
            polylineOptions.add(locationPoints.get(i));
        }

        polyline=mMap.addPolyline(polylineOptions);

//        Toast.makeText(this, "Drawing Line", Toast.LENGTH_SHORT).show();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationPoints.get(locationPoints.size()-1),17));


    }

    //Suppressing MissingPermission because user will definitely have permission at the start of the app. During Login Activity

    @SuppressLint("MissingPermission")
    private void registerLocationUpdates() {


        //Initialise PolylineArrayList
        locationPoints=new ArrayList<>();


        //This method will register location manager for MapsActivity
        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 1, listener);
    }


}
