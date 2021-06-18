package com.example.note_tech_geeks_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note_tech_geeks_android.db.NoteAppDatabase;
import com.example.note_tech_geeks_android.models.Note;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class EditNoteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private int noteId;
    private Note note;
    private EditText titleEditText, detailEditText;
    private String audioPath;
    private double latidude,longitude;
    private GoogleMap mMap;
    private SeekBar seekBarAudio;
    private ImageButton playButton;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private boolean isRecordingPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.edit_note_map);
        mapFragment.getMapAsync(this);

        //to get id of note from previous activity and edit note using that.
        noteId = getIntent().getIntExtra("noteId",-1);
        note = NoteAppDatabase.getInstance(this).noteDao().getNoteWithId(noteId);
        if (note == null) {
            Toast.makeText(getApplicationContext(),"Error while getting note",Toast.LENGTH_LONG).show();
            this.finish();
        }
        titleEditText = findViewById(R.id.note_title_edit);
        titleEditText.setText(note.getNoteTitle());
        detailEditText = findViewById(R.id.note_detail_edit);
        detailEditText.setText(note.getNoteContent());
        byte[] bytes = note.getImageData();
        if (bytes!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ((ImageView)findViewById(R.id.imageview_edit)).setImageBitmap(bitmap);
        }
        if (note.getLocation()!=null){
            latidude = note.getLatitude();
            longitude = note.getLongitude();
        }else
            ((TextView)findViewById(R.id.textview_map_edit)).setText("No Location for this note.");
        if (note.getVoiceURL()!=null){
            audioPath = note.getVoiceURL();
        }else
            ((TextView)findViewById(R.id.textview_audio_edit)).setText("No Recording for this note.");
        //following is cancel button
        findViewById(R.id.cancel_button_edit_note).setOnClickListener(v -> {this.finish();});
        //following is save button
        findViewById(R.id.save_button_edit_note).setOnClickListener(v -> {
            saveThisNote();
        });

        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        playButton = findViewById(R.id.play_button_edit);
        playButton.setOnClickListener(v -> {
            playAudio();
        });
    }

    //implement logic to play audio here
    private void playAudio(){
        if (audioPath==null) {
            Toast.makeText(this, "No audio available", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isRecordingPlaying){

            isRecordingPlaying = true;
            playButton.setImageResource(android.R.drawable.ic_media_pause);

            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            setSeekBar();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isRecordingPlaying = false;
                    seekBarAudio.setProgress(0);
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                    Toast.makeText(EditNoteActivity.this, "Audio is Completed...", Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(EditNoteActivity.this, "Audio is playing...", Toast.LENGTH_SHORT).show();
        }
        else{

            isRecordingPlaying = false;
            playButton.setImageResource(android.R.drawable.ic_media_play);
            mediaPlayer.pause();
        }
    }

    private void setSeekBar(){

        seekBarAudio = findViewById(R.id.seek_bar_edit);
        seekBarAudio.setMax(mediaPlayer.getDuration());

        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBarAudio.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 300);
    }

    private void saveThisNote(){
        String title = titleEditText.getText().toString().trim();
        String detail = detailEditText.getText().toString().trim();
        if (title.isEmpty()) {
            titleEditText.setError("First Name cannot be empty");
            titleEditText.requestFocus();
            return;
        }
        if (detail.isEmpty()) {
            detailEditText.setError("Last name cannot be empty");
            detailEditText.requestFocus();
            return;
        }
        note.setNoteTitle(title);
        note.setNoteContent(detail);
        NoteAppDatabase.getInstance(this).noteDao().updateNote(note);
        this.finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (note.getLocation() == null)
            return;
        LatLng userLatLng = new LatLng(latidude,longitude);
        Marker marker = mMap.addMarker(new MarkerOptions().position(userLatLng).title("Note location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,15));
    }
}