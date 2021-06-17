package com.example.note_tech_geeks_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class CreateNoteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // Location Demo with FUSED LOCATION PROVIDER CLIENT
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final int UPDATE_INTERVAL = 5000; // 5 seconds
    private static final int FASTEST_INTERVAL = 3000; // 3 seconds
    private static final int REQUEST_CODE = 1;
    private LatLng userLatLng;
    private Location currentLocation;
    private boolean attachLocation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.create_note_map);
        mapFragment.getMapAsync(this);
        //following is to bind location switch that when it is enable only then location will be
        //shown on map
        bindLocationSwitch();

    }

    @Override
    protected void onStart() {
        super.onStart();
        bindingLocationClient();
    }

    private void bindingLocationClient() {
        //following is to return if google play service is not installed.
        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(getApplicationContext(), "No Services", Toast.LENGTH_SHORT).show();
                        }
                    });
            errorDialog.show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        //following is to get last location;
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location!=null)
                currentLocation = location;
        });
        //following is the location request for call back on fused location client
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationCallback =
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    currentLocation = locationResult.getLastLocation();
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void bindLocationSwitch(){
        // instantiate the fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (!isPermissionGranted()) {
            requestPermission();
        }
        //following switch to show location of user
        ((Switch) findViewById(R.id.add_location_switch)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                buttonView.setChecked(false);
                return;
            }
            if (isChecked) {
                if (!isPermissionGranted()) {
                    buttonView.setChecked(false);
                    requestPermission();
                    return;
                }
                if (currentLocation != null)
                    showLocation(currentLocation);
            } else {
                if (mMap != null)
                    mMap.setMyLocationEnabled(false);
            }
            attachLocation = isChecked;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
    }

    private void showLocation(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Toast.makeText(this,"inside show location",Toast.LENGTH_SHORT).show();
        if (mMap == null)
            return;
        mMap.setMyLocationEnabled(true);
        userLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isPermissionGranted(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }
}