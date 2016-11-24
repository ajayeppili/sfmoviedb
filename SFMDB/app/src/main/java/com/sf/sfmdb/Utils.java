package com.sf.sfmdb;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by aeppili on 11/23/16.
 */

public class Utils {

    public static String decode(String input) throws Exception{
        if(input == null) {
            return null;
        }
        return URLDecoder.decode(input, "UTF-8");
    }

    public static String encode(String input) throws Exception {
        if(input == null) {
            return null;
        }
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (Exception e){
            return null;
        }
    }
}
