package com.sf.sfmdb;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by aeppili on 11/23/16.
 */

public class RequestGeneratorTest {
    @Test
    public void generateSearchQuery_null() {
        String output = RequestGenerator.generateSearchQuery(null);
        assertEquals(output, null);
    }

    @Test
    public void generateSearchQuery_sparam() {

        String output = RequestGenerator.generateSearchQuery("Ring");
        boolean flag = output!= null && output.contains(RequestGenerator.API_URL) && output.contains(RequestGenerator.SEARCH_PARAM_KEY);
        assertTrue(flag);
    }

    @Test
    public void generateSearchQuery_space() {

        String output = RequestGenerator.generateSearchQuery("The Ring");
        boolean flag = output!= null && output.contains("+");
        assertTrue(flag);
    }


    @Test
    public void generateSearchByTitleQuery_tparam() {

        String output = RequestGenerator.generateSearchByTitleQuery("Ring");
        boolean flag = output!= null && output.contains(RequestGenerator.API_URL) && output.contains(RequestGenerator.TITLE_SEARCH_PARAM_KEY);
        assertTrue(flag);
    }
}
