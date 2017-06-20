package com.bc.alex.viewmodel;

import com.bc.alex.model.api.PlaylistService;
import com.bc.alex.model.client.DeezerClient;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.model.rest.PlaylistResponse;
import com.bc.alex.model.rest.PlaylistTracksResponse;
import com.bc.alex.model.rest.Track;
import com.bc.alex.viewmodel.util.DurationFormatter;
import com.bc.alex.viewmodel.util.NetworkChecker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by alex on 20/06/17.
 */

public class PlaylistViewModel {

    DeezerClient deezerClient;
    NetworkChecker networkChecker;
    PlaylistService service;
    DurationFormatter formatter;

    int offsetIndex;

    private BehaviorSubject<Boolean> loadingSubjects = BehaviorSubject.createDefault(false);
    private BehaviorSubject<ErrorViewHandler> errorSubject = BehaviorSubject.create();

    public PlaylistViewModel(DeezerClient deezerClient,
                             NetworkChecker networkChecker,
                             DurationFormatter formatter) {
        this.deezerClient = deezerClient;
        this.networkChecker = networkChecker;
        this.formatter = formatter;
        service = deezerClient.provideClient().create(PlaylistService.class);
    }

    public BehaviorSubject<Boolean> getLoadingSubjects() {
        return loadingSubjects;
    }

    public BehaviorSubject<ErrorViewHandler> getErrorSubject() {
        return errorSubject;
    }

    public Observable<List<Track>> loadTracks(Playlist playlist){
        if (networkChecker.isNetworkAvailable()) {
            loadingSubjects.onNext(true);
            return service.getTrackForPlaylist(playlist.getId(), offsetIndex)
                    .doOnNext(playlistTracksResponse -> handleNextUrl(playlistTracksResponse.getNext()))
                    .map(PlaylistTracksResponse::getTracks)
                    .flatMapIterable(list -> list)
                    .map(track -> {
                        track.setFormattedDuration(formatter.formatDuration(track.getDuration()));
                        return track;
                    })
                    .toList()
                    .toObservable()
                    .doOnError(this::handleError)
                    .doOnComplete(() -> loadingSubjects.onNext(false));
        } else {
            errorSubject.onNext(new ErrorViewHandler(ErrorViewHandler.ERROR_NO_NETWORK));
            return io.reactivex.Observable.empty();
        }
    }

    private void handleNextUrl(String url){

    }

    private void handleError(Throwable throwable){

    }
}
