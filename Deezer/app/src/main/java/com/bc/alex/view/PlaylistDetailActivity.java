package com.bc.alex.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.SubtitleCollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bc.alex.R;
import com.bc.alex.model.client.DeezerClientImpl;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.model.rest.Track;
import com.bc.alex.view.adapter.TrackAdapter;
import com.bc.alex.viewmodel.ErrorViewHandler;
import com.bc.alex.viewmodel.PlaylistDetailViewModel;
import com.bc.alex.viewmodel.util.AndroidNetworkChecker;
import com.bc.alex.viewmodel.util.PlaylistDurationFormatter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaylistDetailActivity extends BaseActivity {

    PlaylistDetailViewModel viewModel;
    Playlist playlist;

    public static final String KEY_INTENT_PLAYLIST = "playlist";

    public static void show(Context context, Playlist playlist) {
        context.startActivity(new Intent(context, PlaylistDetailActivity.class)
                .putExtra(KEY_INTENT_PLAYLIST, playlist));
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    TextView textViewError;
    @BindView(R.id.collapsing_toolbar)
    SubtitleCollapsingToolbarLayout toolbar;
    @BindView(R.id.imageViewHeader)
    SimpleDraweeView simpleDraweeView;

    TrackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_playlist_detail);
        ButterKnife.bind(this);
        playlist = getIntent().getParcelableExtra(KEY_INTENT_PLAYLIST);
        viewModel = new PlaylistDetailViewModel(new DeezerClientImpl(),
                new AndroidNetworkChecker(this),
                new PlaylistDurationFormatter());
        initBindings();
        initViews();
        loadMoreTracks();
    }

    private void initViews() {
        simpleDraweeView.setImageURI(playlist.getPictureBigUrl());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        toolbar.setTitle(playlist.getTitle());
        toolbar.setSubtitle(String.format("%s (%s)", playlist.getCreator().getName(), playlist.getFormattedDuration()));
    }

    private void initBindings() {
        disposables.add(viewModel.getLoadingSubjects()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .cast(Boolean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayLoading));
        disposables.add(viewModel.getErrorSubject()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .cast(ErrorViewHandler.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayError)
        );
    }

    private void displayError(ErrorViewHandler errorViewHandler) {
        textViewError.setVisibility(View.VISIBLE);
        switch (errorViewHandler.getErrorCode()) {
            case ErrorViewHandler.ERROR_NO_NETWORK:
                textViewError.setText(R.string.Sorry_no_network);
                break;
            case ErrorViewHandler.ERROR_SERVER_ERROR:
                textViewError.setText(R.string.Woops_error_server);
                break;
        }
    }

    private void loadMoreTracks() {
        disposables.add(viewModel.loadTracks(playlist)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleTrack));
    }

    private void handleTrack(List<Track> tracks) {
        if (adapter == null) {
            adapter = new TrackAdapter(tracks);
            disposables.add(adapter.getLastItemDisplayedSubject().subscribe(integer ->
                    loadMoreTracks()
            ));
            recyclerView.setAdapter(adapter);
        } else {
            ((TrackAdapter) recyclerView.getAdapter()).addTracks(tracks);
        }
    }

    private void displayLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }

}
