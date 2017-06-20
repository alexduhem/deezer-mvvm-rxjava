package com.bc.alex.model.api;

import com.bc.alex.model.bean.PlaylistResponse;
import com.bc.alex.model.bean.PlaylistTracksResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by alex on 19/06/17.
 */

public interface PlaylistService {

    @GET("user/{userId}/playlists")
    Observable<PlaylistResponse> getPlaylists(@Path("userId") long userId);

    @GET("playlist/{playlistId}/tracks")
    Observable<PlaylistTracksResponse> getTrackForPlaylist(@Path("playlistId") long playlistId, @Query("index") int index);
}
