package com.bc.alex.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.alex.R;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.view.itemview.PlaylistItemView;

import java.util.ArrayList;

/**
 * Created by alex on 19/06/17.
 */

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistHolder> {

    ArrayList<Playlist> playlists;

    public PlaylistsAdapter(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public PlaylistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaylistHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(PlaylistHolder holder, int position) {
        holder.displayPlaylist(playlists.get(position));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class PlaylistHolder extends RecyclerView.ViewHolder {

        public PlaylistHolder(View itemView) {
            super(itemView);
        }

        public void displayPlaylist(Playlist playlist){
            ((PlaylistItemView)itemView).setPlaylist(playlist);
        }
    }

}
