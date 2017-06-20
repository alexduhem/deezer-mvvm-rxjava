package com.bc.alex.viewmodel.util;

import android.annotation.SuppressLint;

/**
 * Created by alex on 20/06/17.
 */

public class PlaylistDurationFormatter implements DurationFormatter{


    @SuppressLint("DefaultLocale")
    @Override
    public String formatDuration(int seconds) {
        int hour = seconds / 3600;
        int min = (seconds % 3600) / 60;
        int sec = (seconds % 60);
        return String.format("%02d:%02d:%02d", hour, min, sec);
    }
}
