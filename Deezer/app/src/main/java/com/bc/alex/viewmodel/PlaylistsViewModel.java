package com.bc.alex.viewmodel;

import com.bc.alex.model.api.PlaylistService;
import com.bc.alex.model.client.DeezerClient;
import com.bc.alex.model.bean.Playlist;
import com.bc.alex.model.bean.PlaylistResponse;
import com.bc.alex.viewmodel.util.DurationFormatter;
import com.bc.alex.viewmodel.util.NetworkChecker;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by alex on 19/06/17.
 */

public class PlaylistsViewModel {

    private DeezerClient deezerClient;
    private PlaylistService playlistService;
    private NetworkChecker networkChecker;
    private DurationFormatter formatter;

    private static final int USER_ID = 5;

    private BehaviorSubject<Boolean> loadingSubjects = BehaviorSubject.createDefault(false);
    private BehaviorSubject<ErrorViewHandler> errorSubject = BehaviorSubject.create();

    public PlaylistsViewModel(DeezerClient deezerClient, NetworkChecker networkChecker, DurationFormatter formatter) {
        this.deezerClient = deezerClient;
        this.networkChecker = networkChecker;
        playlistService = deezerClient.provideClient().create(PlaylistService.class);
        this.formatter = formatter;
    }

    public Observable<Boolean> getLoadingSubjects() {
        return loadingSubjects;
    }

    public BehaviorSubject<ErrorViewHandler> getErrorSubject() {
        return errorSubject;
    }

    public Observable<List<Playlist>> loadPlaylists() {
        if (networkChecker.isNetworkAvailable()) {
            loadingSubjects.onNext(true);
            return playlistService.getPlaylists(USER_ID)
                    .map(PlaylistResponse::getPlaylists)
                    .flatMapIterable(list -> list)
                    .map(playlist -> {
                        playlist.setFormattedDuration(formatter.formatDuration(playlist.getDuration()));
                        return playlist;
                    })
                    .toList()
                    .toObservable()
                    .doOnNext(playlists -> {
                        if (playlists.isEmpty()) {
                            errorSubject.onNext(new ErrorViewHandler(ErrorViewHandler.ERROR_EMPTY));
                        }
                    })
                    .doOnError(this::handleError)
                    .doOnComplete(() -> loadingSubjects.onNext(false));
        } else {
            errorSubject.onNext(new ErrorViewHandler(ErrorViewHandler.ERROR_NO_NETWORK));
            return Observable.empty();
        }
    }

    private void handleError(Throwable throwable) {
        errorSubject.onNext(new ErrorViewHandler(throwable));
    }
}
