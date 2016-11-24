package com.sf.sfmdb;

import android.util.Log;

import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Created by aeppili on 11/23/16.
 */

public class UtilsTest {
    @Test
    public void decode_null() throws Exception {
        String output = Utils.decode(null);
        assertEquals(output, null);
    }

    @Test
    public void encode_space() throws Exception {
        String input = "The Godfather";
        String expectedOutput = "The+Godfather";
        String output = Utils.encode(input);

        assertTrue(expectedOutput.equalsIgnoreCase(output));
    }

    @Test
    public void encode_null() throws Exception {
        String output = Utils.encode(null);
        assertEquals(output, null);
    }

}
