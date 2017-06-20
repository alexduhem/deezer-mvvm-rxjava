package com.bc.alex.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 20/06/17.
 */

public class Track {

    String id;
    boolean readable;
    String title;
    @SerializedName("title_short")
    String titleShort;
    int duration;

    String formattedDuration;

    public void setFormattedDuration(String formattedDuration) {
        this.formattedDuration = formattedDuration;
    }

    public String getId() {
        return id;
    }

    public boolean isReadable() {
        return readable;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleShort() {
        return titleShort;
    }

    public int getDuration() {
        return duration;
    }

    public String getFormattedDuration() {
        return formattedDuration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
