package com.androidarena.tunewave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity implements RecyclerSongInterface {
    RecyclerView recyclerView;
    ArrayList<SongModel> arraySongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising and setting the layout manager for Recycler view
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // List to add songs
        arraySongs = new ArrayList<>();

        // Granting External storage access
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_MEDIA_AUDIO)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<String> songFilePaths = fetchSongs(Environment.getExternalStorageDirectory());

                        for (String filePath : songFilePaths) {
                            String fileName = new File(filePath).getName().replace(".mp3", "");
                            arraySongs.add(new SongModel(R.drawable.songimg, fileName, filePath));
                        }


                        RecyclerSongAdapter recyclerSongAdapter = new RecyclerSongAdapter(MainActivity.this, arraySongs, MainActivity.this);
//                        recyclerView.setAdapter(recyclerSongAdapter);

//                        Adding Animation (Scaling)
                        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(recyclerSongAdapter);
                        scaleInAnimationAdapter.setDuration(1000);
                        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
                        scaleInAnimationAdapter.setFirstOnly(false);
                        recyclerView.setAdapter(scaleInAnimationAdapter);

                        SongDataSingleton.getInstance().setSongsList(arraySongs);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

        }

        public ArrayList<String> fetchSongs(File file){
            ArrayList<String> list = new ArrayList<>();
            File[] mySongs = file.listFiles();

            if(mySongs != null){
                for (File files : mySongs){
                    if(!files.isHidden() && files.isDirectory()){
                        list.addAll(fetchSongs(files));
                    }
                    else if(files.getName().endsWith(".mp3")){
                        list.add(files.getAbsolutePath());
                    }
                }
            }
            return list;
        }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, PlaySong.class);
        String currSong = arraySongs.get(position).getSongName();
        intent.putExtra("currsong", currSong);
//        intent.putExtra("arrsongs",arraySongs);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
