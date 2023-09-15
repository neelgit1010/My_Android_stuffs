package com.androidarena.tunewave;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

    ImageView play, previous, next;
    private MediaPlayer player;
    SeekBar seekBar;
//    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        seekBar = findViewById(R.id.seekBar);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        Intent intent = getIntent();
        final int[] position = {intent.getIntExtra("position", -1)};

        if (position[0] != -1) {
            // Retrieve the selected song from the arraySongs list
            ArrayList<SongModel> arraySongs = SongDataSingleton.getInstance().getSongsList();
            SongModel selectedSong = arraySongs.get(position[0]);

            // Display the selected song name
            TextView songNameTextView = findViewById(R.id.songName);
            songNameTextView.setText(selectedSong.getSongName());
            songNameTextView.setSelected(true);

            Handler handler = new Handler();

            // Create a new MediaPlayer instance and set the data source
            player = new MediaPlayer();

            try {
                player.setDataSource(selectedSong.getFilePath());
                player.prepare(); // Synchronous preparation
                player.start(); // Start playback
                seekBar.setMax(player.getDuration());

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        player.seekTo(seekBar.getProgress());
                    }
                });

//                updateSeek = new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        int currPos = 0;
//                        try {
//                            while (currPos < player.getDuration()){
//                                currPos = player.getCurrentPosition();
//                                seekBar.setProgress(currPos);
//                                sleep(700);
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                };
//
//                updateSeek.start();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null && player.isPlaying()) {
                            int currPos = player.getCurrentPosition();
                            seekBar.setProgress(currPos);
                        }
                        // Schedule the next update after a delay (e.g., 100 milliseconds)
                        handler.postDelayed(this, 700);
                    }
                }, 700);

                // Changing the seekbar color
                seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
                seekBar.getThumb().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);

                play.setOnClickListener(view -> {
                    if(player.isPlaying()){
                        play.setImageResource(R.drawable.btnplay);
                        player.pause();
                    }else{
                        play.setImageResource(R.drawable.pause);
                        player.start();
                    }
//                    play.setImageResource(R.drawable.pause);
                    // Display the selected song name
                    TextView songNameTextView1 = findViewById(R.id.songName);
                    songNameTextView1.setText(selectedSong.getSongName());
                    songNameTextView1.setSelected(true);
                    seekBar.setMax(player.getDuration());
                });

                previous.setOnClickListener(view -> {
                    player.stop();
//                        releaseMediaPlayer();
                    player.release();

                   if(position[0] != 0) position[0]--;
                   else position[0] = arraySongs.size() - 1;

                    SongModel selectedSong12 = arraySongs.get(position[0]);
                    // Create a new MediaPlayer instance and set the data source
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(selectedSong12.getFilePath());
                        player.prepare(); // Synchronous preparation
                        player.start(); // Start playback
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    play.setImageResource(R.drawable.pause);
                    // Display the selected song name
                    TextView songNameTextView2 = findViewById(R.id.songName);
                    songNameTextView2.setText(selectedSong.getSongName());
                    songNameTextView2.setSelected(true);
                    seekBar.setMax(player.getDuration());
                    seekBar.setProgress(0);
                });

                player.setOnCompletionListener(mediaPlayer -> next.performClick());

                next.setOnClickListener(view -> {
                    player.stop();
//                        releaseMediaPlayer();
                    player.release();

                    if(position[0] != arraySongs.size() - 1) position[0]++;
                    else position[0] = 0;

                    SongModel selectedSong1 = arraySongs.get(position[0]);
                    // Create a new MediaPlayer instance and set the data source
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(selectedSong1.getFilePath());
                        player.prepare(); // Synchronous preparation
                        player.start(); // Start playback
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    play.setImageResource(R.drawable.pause);
                    // Display the selected song name
//                    TextView songNameTextView1 = findViewById(R.id.songName);
                    songNameTextView.setText(selectedSong1.getSongName());
                    songNameTextView.setSelected(true);
                    seekBar.setMax(player.getDuration());
                    seekBar.setProgress(0);
                });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error playing the song.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity on error
            }
        } else {
            // Handle the case where the data is missing or invalid
            Toast.makeText(this, "Invalid data received.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity or perform some other action
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (player != null) {
            player.release();
            player = null;
//            updateSeek.interrupt();
        }
    }
}



