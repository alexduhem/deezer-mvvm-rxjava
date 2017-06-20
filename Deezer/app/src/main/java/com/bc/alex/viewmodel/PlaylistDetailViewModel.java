package com.bc.alex.viewmodel;

import com.bc.alex.model.api.PlaylistService;
import com.bc.alex.model.client.DeezerClient;
import com.bc.alex.model.rest.Playlist;
import com.bc.alex.model.rest.PlaylistResponse;
import com.bc.alex.model.rest.PlaylistTracksResponse;
import com.bc.alex.model.rest.Track;
import com.bc.alex.viewmodel.util.DurationFormatter;
import com.bc.alex.viewmodel.util.NetworkChecker;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static java.util.stream.Collectors.toList;

/**
 * Created by alex on 20/06/17.
 */

public class PlaylistDetailViewModel {

    private DeezerClient deezerClient;
    private NetworkChecker networkChecker;
    private PlaylistService service;
    private DurationFormatter formatter;

    private int offsetIndex;
    private int total = -1;

    private BehaviorSubject<Boolean> loadingSubjects = BehaviorSubject.createDefault(false);
    private BehaviorSubject<ErrorViewHandler> errorSubject = BehaviorSubject.create();

    public PlaylistDetailViewModel(DeezerClient deezerClient,
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

    public Observable<List<Track>> loadTracks(Playlist playlist) {
        //if there's nothing more to download well... we don't call the api
        if (offsetIndex >= total && total >= 0){
            return Observable.empty();
        }
        if (networkChecker.isNetworkAvailable()) {
            if (offsetIndex == 0) {
                //we just display the progress bar for the first download
                loadingSubjects.onNext(true);
            }
            return service.getTrackForPlaylist(playlist.getId(), offsetIndex)
                    .doOnNext(playlistTracksResponse -> total = playlistTracksResponse.getTotal())
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

    private void handleNextUrl(String query) {
        if (query == null){
            //at the end of the playlist, we have no "next url"
            offsetIndex = total;
            return;
        }
        Map<String, String> split = splitQuery(query);
        offsetIndex = split.get("index") == null ? 0 : Integer.parseInt(split.get("index"));

    }

    /**
     * I don't like this code, but I don't like the "next" url in the output of Deezer API.
     * I just take this code on stackoverflow quickly to parse an api and retrieve the index
     * @param url
     * @return
     */
    private Map<String, String> splitQuery(String url)  {
        Map<String, String> queryPairs = new LinkedHashMap<String, String>();
        String[] pairs = url.split("\\?")[1].split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return queryPairs;
    }

    private void handleError(Throwable throwable) {
        if (offsetIndex > 0){
            //same as loading, we dont display the error if we already got a list
            errorSubject.onNext(new ErrorViewHandler(throwable));
        }
    }
}
