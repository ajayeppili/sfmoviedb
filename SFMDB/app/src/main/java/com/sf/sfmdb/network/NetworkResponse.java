package com.sf.sfmdb.network;

import org.json.JSONObject;

/**
 * Created by aeppili on 11/23/16.
 */

public class NetworkResponse {
    private String mResponse;
    private JSONObject mJSONResponse;
    //private final ResponseCode mResponseCode;

    public NetworkResponse(String response) {
        mResponse = response;
    }

    public NetworkResponse(String response, JSONObject jsonResponse) {
        mResponse = response;
        mJSONResponse = jsonResponse;
    }
//    public NetworkResponse(String response, ResponseCode responseCode) {
//        mResponseCode = responseCode;
//        mResponse = response;
//    }
//    public ResponseCode getStatus() {
//        return mResponseCode;
//    }

    public String getResponseAsString() {
        return mResponse;
    }
    public JSONObject getResponseAsJSON() {
        return mJSONResponse;
    }
}