package com.bc.alex.viewmodel;

import com.bc.alex.model.api.PlaylistService;
import com.bc.alex.model.client.DeezerClient;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.model.rest.PlaylistResponse;
import com.bc.alex.viewmodel.util.NetworkChecker;

import java.util.ArrayList;
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

    private BehaviorSubject<Boolean> loadingSubjects = BehaviorSubject.createDefault(false);
    private BehaviorSubject<List<Playlist>> playlistSubjects = BehaviorSubject.createDefault(new ArrayList<>());
    private BehaviorSubject<ErrorViewHandler> errorSubject = BehaviorSubject.create();

    public PlaylistsViewModel(DeezerClient deezerClient, NetworkChecker networkChecker) {
        this.deezerClient = deezerClient;
        this.networkChecker = networkChecker;
        playlistService = deezerClient.provideClient().create(PlaylistService.class);
    }

    public Observable<Boolean> getLoadingSubjects() {
        return loadingSubjects;
    }

    public BehaviorSubject<ErrorViewHandler> getErrorSubject() {
        return errorSubject;
    }

    public Observable<ArrayList<Playlist>> loadPlaylists() {
        if (networkChecker.isNetworkAvailable()) {
            loadingSubjects.onNext(true);
            return playlistService.getPlaylists(1839834)
                    .map(PlaylistResponse::getPlaylists)
                    .doOnNext(playlists -> {
                        playlistSubjects.onNext(playlists);
                    })
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
