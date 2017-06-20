package com.bc.alex.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by alex on 20/06/17.
 */

public class PlaylistTracksResponse {

    @SerializedName("data")
    ArrayList<Track> tracks;
    String checksum;
    String next;
    int total;

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public String getChecksum() {
        return checksum;
    }

    public String getNext() {
        return next;
    }

    public int getTotal() {
        return total;
    }
}
