package com.bc.alex;

import com.bc.alex.viewmodel.util.PlaylistDurationFormatter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FormatterTest {


    @Test
    public void formatMinTest(){
        PlaylistDurationFormatter formatter = new PlaylistDurationFormatter();
        assertEquals("00:01:00", formatter.formatDuration(60));
        assertEquals("01:01:40", formatter.formatDuration(3700));
        assertEquals("01:00:00", formatter.formatDuration(3600));
        assertEquals("100:00:01", formatter.formatDuration(360001));
    }

}