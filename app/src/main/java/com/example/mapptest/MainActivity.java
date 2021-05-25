package com.example.mapptest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

//https://console.cloud.google.com/home/dashboard?project=mapp-314800

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, AutoPermissionsListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[]grantResult){
        AutoPermissions.Companion.parsePermissions(this, requestCode,
                permissions, this);
    }

    public void onDenied(int requestCode, String permissions[]){
        Toast.makeText(getApplicationContext(), "onDenied", Toast.LENGTH_SHORT).show();
    }

    public void onGranted(int requestCode, String permissions[]){
        Toast.makeText(getApplicationContext(), "onGranted", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    public void startLocationService(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            @SuppressLint("MissingPermission") Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location !=null){
                double latitude = location.getLatitude();
                double longtiude = location.getLongitude();

                Log.i("TTT", "startLocationService() Lat : " + latitude + ", Lng : " + longtiude);
            }else{
                Log.i("TTT", "Error 1 : Location is null");
                Toast.makeText(getApplicationContext(), "Location is null", Toast.LENGTH_SHORT).show();
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            Toast.makeText(getApplicationContext(), "위치 요청됨", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Log.i("TTT", "Error 2 : " + e);
        }
    }

    class GPSListener implements LocationListener{
        public void onLocationChanged(Location location){
            double latitude = location.getLatitude();
            double longtiude = location.getLongitude();

            Log.i("TTT", "GPSListener() Lat : " + latitude + ", Lng : " + longtiude);
        }

        public void onProviderDisabled(String provider){}
        public void onProviderEnabled(String provider){}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    public void onMapReady(final GoogleMap googleMap){
        mMap = googleMap;
        LatLng SEOUL = new LatLng(37.56, 126.97);
        LatLng CACI = new LatLng(34.92737550040938, 127.48878073496748);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);

        MarkerOptions caciM = new MarkerOptions();
        caciM.position(CACI);
        caciM.title("청암대");
        caciM.snippet("바로 지금 여기");
        mMap.addMarker(caciM);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CACI, 15));
    }
}