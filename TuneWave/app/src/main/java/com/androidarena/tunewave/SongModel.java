package com.androidarena.tunewave;

import android.graphics.Bitmap;

public class SongModel {
    private int songImg;
    private String songName;
    private String filePath; // Add a filePath field

    public SongModel(int songImg, String songName, String filePath) {
        this.songImg = songImg;
        this.songName = songName;
        this.filePath = filePath;
    }

    public int getSongImg() {
        return songImg;
    }

    public String getSongName() {
        return songName;
    }

    public String getFilePath() {
        return filePath; // Add a getter for filePath
    }
}
