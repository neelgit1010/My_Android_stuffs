package com.androidarena.tunewave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Inheriting the actual properties of RecyclerView's adapter
public class RecyclerSongAdapter extends RecyclerView.Adapter<RecyclerSongAdapter.ViewHolder> {

    private final RecyclerSongInterface recyclerSongInterface;
//    Context needed to set the views in RecyclerSongAdapter from MainActivity
    Context context;
    ArrayList<SongModel> arrSongs;
    public RecyclerSongAdapter(Context context, ArrayList<SongModel> arrSongs, RecyclerSongInterface recyclerSongInterface){
        this.arrSongs = arrSongs;
        this.context = context;
        this.recyclerSongInterface = recyclerSongInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Pass the inflated view to our ViewHolder
        // The view will attach the default layout of the Recycler view which is "parent"
        // Attach to root must be "false" or else the views will be attached to the parent
        // by default and this will destroy the basic characteristics of recycler view
        View view = LayoutInflater.from(context).inflate(R.layout.songname_card, parent, false);
        // Sending the view to ViewHolder
        return new ViewHolder(view);
    }

//    Binding every data with view wrt position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtsongname.setText(arrSongs.get(position).getSongName());
        holder.txtsongname.setSelected(true);
        holder.imgsong.setImageResource(arrSongs.get(position).getSongImg());
    }

    @Override
    public int getItemCount() {
        return arrSongs.size();
    }

    //    This is our ViewHolder Class which will contain the
    //    parsed views while recycling views both from top and bottom and
    //    similarly it must inherit actual properties of ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgsong;
        TextView txtsongname;
        public ViewHolder(View itemview){
            super(itemview);
            imgsong = itemview.findViewById(R.id.imgsongpic);
            txtsongname = itemview.findViewById(R.id.txtname);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerSongInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recyclerSongInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
