package com.bc.alex.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.alex.R;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.view.itemview.PlaylistItemView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by alex on 19/06/17.
 */

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistHolder> {

    List<Playlist> playlists;
    BehaviorSubject<Playlist> itemClickSubject = BehaviorSubject.create();


    public PlaylistsAdapter(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public BehaviorSubject<Playlist> getClickObservable(){
        return itemClickSubject;
    }

    @Override
    public PlaylistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PlaylistHolder holder = new PlaylistHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item_view, parent, false));
        RxView.clicks(holder.itemView)
                .takeUntil(RxView.detaches(parent))
                .map(aVoid -> holder.itemView)
                .cast(PlaylistItemView.class)
                .map(PlaylistItemView::getPlaylist)
                .subscribe(itemClickSubject);
        return holder;
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
