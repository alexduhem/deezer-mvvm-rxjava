package com.bc.alex.model.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by alex on 19/06/17.
 */

public class PlaylistResponse {

    @SerializedName("data")
    ArrayList<Playlist> playlists;
    int total;
    String checksum;

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
}
