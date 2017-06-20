package com.bc.alex.model.api;

import com.bc.alex.model.rest.PlaylistResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alex on 19/06/17.
 */

public interface PlaylistService {

    @GET("user/{userId}/playlists")
    Observable<PlaylistResponse> getPlaylists(@Path("userId") long userId);
}
