package com.androidarena.tunewave;

import java.util.ArrayList;

public class SongDataSingleton {
    private static final SongDataSingleton instance = new SongDataSingleton();
    private ArrayList<SongModel> songsList;

    private SongDataSingleton() {
        // Private constructor to prevent instantiation
    }

    public static SongDataSingleton getInstance() {
        return instance;
    }

    public ArrayList<SongModel> getSongsList() {
        return songsList;
    }

    public void setSongsList(ArrayList<SongModel> songsList) {
        this.songsList = songsList;
    }
}
