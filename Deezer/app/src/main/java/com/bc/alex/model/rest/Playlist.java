package com.bc.alex.model.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 19/06/17.
 */

public class Playlist {
    int id;
    String title;
    String description;
    int duration;
    String formattedDuration;
    boolean isPublic;
    @SerializedName("nb_tracks")
    int trackNumber;
    @SerializedName("picture")
    String pictureUrl;
    @SerializedName("picture_small")
    String pictureSmallUrl;
    @SerializedName("picture_medium")
    String pictureMediumUrl;
    @SerializedName("picture_big")
    String pictureBigUrl;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPictureSmallUrl() {
        return pictureSmallUrl;
    }

    public String getPictureMediumUrl() {
        return pictureMediumUrl;
    }

    public String getPictureBigUrl() {
        return pictureBigUrl;
    }
}
