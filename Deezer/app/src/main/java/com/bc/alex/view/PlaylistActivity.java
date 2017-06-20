package com.bc.alex.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bc.alex.R;
import com.bc.alex.model.client.DeezerClientImpl;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.viewmodel.PlaylistViewModel;
import com.bc.alex.viewmodel.util.AndroidNetworkChecker;
import com.bc.alex.viewmodel.util.PlaylistDurationFormatter;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaylistActivity extends BaseActivity {

    PlaylistViewModel viewModel;
    Playlist playlist;

    public static final String KEY_INTENT_PLAYLIST = "playlist";

    public static void show(Context context, Playlist playlist){
        context.startActivity(new Intent(context, PlaylistActivity.class)
                .putExtra(KEY_INTENT_PLAYLIST, playlist));
    }

    RecyclerView recyclerView;

    ProgressBar progressBar;

    TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        ButterKnife.bind(this);
        playlist = getIntent().getParcelableExtra(KEY_INTENT_PLAYLIST);
        viewModel = new PlaylistViewModel(new DeezerClientImpl(),
                new AndroidNetworkChecker(this),
                new PlaylistDurationFormatter());
        loadMoreTracks();

    }

    private void loadMoreTracks() {
        disposables.add(viewModel.loadTracks(playlist)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                tracks -> {
                    Log.e("tracks", "tracks "+tracks);
                }
        ));
    }


}
