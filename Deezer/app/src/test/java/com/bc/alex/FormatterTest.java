package com.bc.alex;

import com.bc.alex.viewmodel.util.PlaylistDurationFormatter;

import org.junit.Test;

import static org.junit.Assert.*;


public class FormatterTest {

    @Test
    public void testDurationPlaylist(){
        PlaylistDurationFormatter formatter = new PlaylistDurationFormatter();
        assertEquals("00:01:00", formatter.formatDuration(60));
        assertEquals("01:01:40", formatter.formatDuration(3700));
        assertEquals("01:00:00", formatter.formatDuration(3600));
        assertEquals("100:00:01", formatter.formatDuration(360001));
    }

}