package com.example.note_tech_geeks_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import android.media.AudioManager;
import android.media.MediaRecorder;

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

import java.io.IOException;

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

    private int REQUEST_CODE_CAMERA = 100;
    private int REQUEST_CODE_GALLERY = 110;
    private int REQUEST_CODE_AUDIO = 120;

    private ImageView imageView;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private AudioManager audioManager;

    private String pathForAudio = "";

    private boolean isRecording = false;
    private boolean isRecordingPlaying = false;

    final private static String RECORDED_FILE = "/audio.3gp";

    private ImageButton btnRecord;
    private ImageButton btnPlay;

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

        imageView = findViewById(R.id.image_note_create);

        findViewById(R.id.camera_button_create).setOnClickListener(v -> {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,REQUEST_CODE_CAMERA);
        });

        findViewById(R.id.gallery_button_create).setOnClickListener(v ->{

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_CODE_GALLERY);
        });

        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume (AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);

        btnRecord = findViewById(R.id.record_button_create);
        btnPlay = findViewById(R.id.play_button_create);

        enableOrDisableRecording();
        playOrPauseRecording();
    }

    private void enableOrDisableRecording(){

        if (!checkPermissionDevice())
            requestPermissionForAudio();

            btnRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkPermissionDevice()) {

                        if(!isRecording){

                            isRecording = true;
                            pathForAudio = getExternalCacheDir().getAbsolutePath()
                                    + RECORDED_FILE;

                            Log.d("path", "onClick: " + pathForAudio);

                            setUpMediaRecorder();

                            try {
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                            } catch (IllegalStateException ise) {

                                ise.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(CreateNoteActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                        }

                        else{

                            isRecording = false;
                            mediaRecorder.stop();
                            Toast.makeText(CreateNoteActivity.this, "Recording stop ...", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        requestPermission();
                }
            });
        }

    private void playOrPauseRecording(){

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRecordingPlaying){

                    isRecordingPlaying = true;
                    btnRecord.setEnabled(false);

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(pathForAudio);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            btnRecord.setEnabled(true);
                            Toast.makeText(CreateNoteActivity.this, "Recording is Completed...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    mediaPlayer.start();
                    Toast.makeText(CreateNoteActivity.this, "Recording is playing...", Toast.LENGTH_SHORT).show();
                }
                else{

                    isRecordingPlaying = false;
                    btnPlay.setEnabled(true);
                    btnRecord.setEnabled(true);

                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setUpMediaRecorder();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent backIntent) {
        super.onActivityResult(requestCode, resultCode, backIntent);

        if (requestCode == REQUEST_CODE_CAMERA)  //back from camera
        {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) (backIntent.getExtras().get("data"));
                imageView.setImageBitmap(bmp);
            }
        } else if (requestCode == REQUEST_CODE_GALLERY)  //back from gallery
            {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = backIntent.getData();
                        //following to convert from uri to bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        //following is to get and show image through URI in image view
                        //imv1.setImageURI(uri);
                        imageView.setImageBitmap(bitmap);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
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

        if (requestCode == REQUEST_CODE_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPermissionGranted(){

        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void setUpMediaRecorder() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathForAudio);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void requestPermissionForAudio() {

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_CODE_AUDIO);
    }

    private boolean checkPermissionDevice() {

        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}