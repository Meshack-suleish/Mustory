package com.mustory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView bottomSheetTitle, currentTime, totalDuration;
    private MediaPlayer mediaPlayer;

    ArrayList<Song> songs;
    private int currentPosition;
    Song currentSong;

    private ImageButton playPauseButton, previousButton, nextButton;
    private SeekBar musicSeekBar;
    private TextView songTitle, songDescription,artistName;
    private Handler seekBarHandler = new Handler();
    private Runnable updateSeekBar;
    private View bottomSheet;
    private ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Initialize views
        playPauseButton = findViewById(R.id.btn_play_pause);
        previousButton = findViewById(R.id.btn_previous);
        nextButton = findViewById(R.id.btn_next);
        musicSeekBar = findViewById(R.id.music_seekbar);
        currentTime = findViewById(R.id.current_time);
        totalDuration = findViewById(R.id.total_duration);
        songTitle = findViewById(R.id.song_title);
        songDescription = findViewById(R.id.song_description);
        artistName = findViewById(R.id.artist_name);
        progressBar=findViewById(R.id.progressBar);


        // Receive Parcelable list of songs
        songs = getIntent().getParcelableArrayListExtra("songs");

        int currentPosition = getIntent().getIntExtra("position", 0);
        assert songs != null;
        currentSong = songs.get(currentPosition);



        // Set song title and description
        songTitle.setText(currentSong.getName());
        songDescription.setText(currentSong.getDescription());
        artistName.setText(currentSong.getArtist());


        // Initialize bottom sheet behavior
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetTitle = findViewById(R.id.bottom_sheet_title);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        setupBottomSheet();

        // Initialize MediaPlayer and controls
        initializeMediaPlayer(currentSong);
        setupControls();
    }

    private void setupBottomSheet() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;

        int collapsedHeight = (int) (screenHeight * 0.1); // 10% of screen height
        int expandedHeight = (int) (screenHeight * 0.8); // 80% of screen height

        bottomSheetBehavior.setPeekHeight(collapsedHeight);
        bottomSheet.getLayoutParams().height = expandedHeight;
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetTitle.setOnClickListener(v -> toggleBottomSheetState());
    }



    private void initializeMediaPlayer(Song currentSong) {

        if (currentSong.getPath() != null) {
            mediaPlayer = new MediaPlayer();
            try {
                Uri uri = Uri.parse(currentSong.getPath());
                mediaPlayer.setDataSource(this, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.baseline_pause_24);
                totalDuration.setText(formatTime(mediaPlayer.getDuration()));
                setupSeekBar();
            } catch (IOException e) {
                Toast.makeText(this, "Error loading song", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No song path provided", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupControls() {
        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24);
                } else {
                    mediaPlayer.start();
                    playPauseButton.setImageResource(R.drawable.baseline_pause_24);
                }
            }
        });

        previousButton.setOnClickListener(v -> {
            playPreviousSong();
        });

        nextButton.setOnClickListener(v -> {
            playNextSong();
        });


    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    // Method to hide ProgressBar
    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void playPreviousSong() {
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.reset();

            currentPosition = (currentPosition-1 + songs.size())% songs.size();
            currentSong = songs.get(currentPosition);

            initializeMediaPlayer(currentSong);
            songTitle.setText(currentSong.getName());
            songDescription.setText(currentSong.getDescription());
            artistName.setText(currentSong.getArtist());

        }
    }

    private void playNextSong() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();

            // Move to next song index, loop if at end
            currentPosition = (currentPosition + 1) % songs.size();

            // Get the next song object
            currentSong = songs.get(currentPosition);

            // Initialize MediaPlayer with the next song path
            initializeMediaPlayer(currentSong);

            // Update UI
            songTitle.setText(currentSong.getName());
            songDescription.setText(currentSong.getDescription());
            artistName.setText(currentSong.getArtist());
        }
    }

    private void setupSeekBar() {
        musicSeekBar.setMax(mediaPlayer.getDuration());

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPos = mediaPlayer.getCurrentPosition();
                    musicSeekBar.setProgress(currentPos);
                    currentTime.setText(formatTime(currentPos));
                    seekBarHandler.postDelayed(this, 1000);
                }
            }
        };
        seekBarHandler.post(updateSeekBar);

        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarHandler.removeCallbacks(updateSeekBar); // Stop updating while seeking
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    if (mediaPlayer.isPlaying()) {
                        seekBarHandler.post(updateSeekBar); // Resume updating
                    }
                }
            }
        });
    }

    private void toggleBottomSheetState() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @NonNull
    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            seekBarHandler.removeCallbacks(updateSeekBar); // Stop SeekBar updates
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
