package com.bc.alex.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bc.alex.R;
import com.bc.alex.model.client.DeezerClientImpl;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.view.adapter.PlaylistsAdapter;
import com.bc.alex.viewmodel.ErrorViewHandler;
import com.bc.alex.viewmodel.PlaylistsViewModel;
import com.bc.alex.viewmodel.util.AndroidNetworkChecker;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerViewAdapter;
import com.jakewharton.rxbinding2.widget.RxAdapter;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {

    PlaylistsViewModel playlistsViewModel;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textViewError)
    TextView textViewError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        playlistsViewModel = new PlaylistsViewModel(new DeezerClientImpl(), new AndroidNetworkChecker(this));
        disposables.add(playlistsViewModel.getLoadingSubjects()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .cast(Boolean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayLoading));
        disposables.add(playlistsViewModel.getErrorSubject()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .cast(ErrorViewHandler.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayError)
        );
        loadPlaylist();
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_column_number)));

    }

    private void loadPlaylist() {
        disposables.add(playlistsViewModel.loadPlaylists().observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(playlists -> {
                    PlaylistsAdapter adapter = new PlaylistsAdapter(playlists);
                    disposables.add(adapter.getClickObservable().subscribe(playlist -> {
                        PlaylistActivity.show(this, playlist);
                    }));
                    recyclerView.setAdapter(adapter);
                }));
    }

    private void displayLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }

    private void displayError(ErrorViewHandler viewHandler) {
        if (viewHandler != null) {
            textViewError.setVisibility(View.VISIBLE);
            switch (viewHandler.getErrorCode()) {
                case ErrorViewHandler.ERROR_NO_NETWORK:
                    textViewError.setText(R.string.Sorry_no_network);
                    break;
                case ErrorViewHandler.ERROR_SERVER_ERROR:
                    textViewError.setText(R.string.Woops_error_server);
                    break;
            }
        } else {
            textViewError.setVisibility(View.GONE);
        }
    }


}
