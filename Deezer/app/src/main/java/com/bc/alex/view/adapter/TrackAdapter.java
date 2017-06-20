package com.bc.alex.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bc.alex.R;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.model.rest.Track;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by alex on 20/06/17.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHolder> {

    List<Track> tracks;

    BehaviorSubject<Integer> lastItemDisplayedSubject = BehaviorSubject.create();

    public TrackAdapter(List<Track> tracks) {
        this.tracks = tracks;
    }

    public BehaviorSubject<Integer> getLastItemDisplayedSubject() {
        return lastItemDisplayedSubject;
    }

    @Override
    public TrackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrackHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TrackHolder holder, int position) {
        holder.setTrack(tracks.get(position));
        if (position == tracks.size()-1){
            lastItemDisplayedSubject.onNext(position);
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void addTracks(List<Track> newTracks){
        tracks.addAll(newTracks);
        notifyDataSetChanged();
    }

    class TrackHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView textViewTitle;
        @BindView(R.id.textDuration)
        TextView textViewDuration;

        public TrackHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTrack(Track track) {
            textViewTitle.setText(track.getTitle());
            textViewDuration.setText(track.getFormattedDuration());
        }
    }
}