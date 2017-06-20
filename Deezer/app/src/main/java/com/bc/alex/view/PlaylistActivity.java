package com.bc.alex.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bc.alex.R;
import com.bc.alex.model.client.DeezerClientImpl;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.model.rest.Track;
import com.bc.alex.viewmodel.PlaylistViewModel;
import com.bc.alex.viewmodel.util.AndroidNetworkChecker;
import com.bc.alex.viewmodel.util.PlaylistDurationFormatter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaylistActivity extends BaseActivity {

    PlaylistViewModel viewModel;
    Playlist playlist;

    public static final String KEY_INTENT_PLAYLIST = "playlist";

    public static void show(Context context, Playlist playlist) {
        context.startActivity(new Intent(context, PlaylistActivity.class)
                .putExtra(KEY_INTENT_PLAYLIST, playlist));
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ProgressBar progressBar;

    TextView textViewError;

    @BindView(R.id.imageViewHeader)
    SimpleDraweeView simpleDraweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_playlist);
        ButterKnife.bind(this);
        playlist = getIntent().getParcelableExtra(KEY_INTENT_PLAYLIST);
        viewModel = new PlaylistViewModel(new DeezerClientImpl(),
                new AndroidNetworkChecker(this),
                new PlaylistDurationFormatter());
        loadMoreTracks();
        simpleDraweeView.setImageURI(playlist.getPictureBigUrl());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setTitle(playlist.getTitle());
    }

    private void loadMoreTracks() {
        disposables.add(viewModel.loadTracks(playlist)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tracks -> {
                            Log.e("tracks", "tracks " + tracks);
                            recyclerView.setAdapter(new TrackAdapter(tracks));
                        }
                ));
    }

    class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHolder> {

        List<Track> tracks;

        public TrackAdapter(List<Track> tracks) {
            this.tracks = tracks;
        }

        @Override
        public TrackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TrackHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.track_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(TrackHolder holder, int position) {
            holder.setTrack(tracks.get(position));
        }

        @Override
        public int getItemCount() {
            return tracks.size();
        }

        class TrackHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.text)
            TextView textViewTitle;

            public TrackHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void setTrack(Track track) {
                textViewTitle.setText(track.getTitle());
            }
        }
    }


}
