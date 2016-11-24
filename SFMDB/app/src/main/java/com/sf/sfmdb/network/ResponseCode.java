package com.sf.sfmdb.network;

/**
 * Created by aeppili on 11/23/16.
 **/

public enum ResponseCode {
        SUCCESS("Success"),
        SERVER_ERROR("Server Error"),
        INVALID_REQUEST("Invalid Request"),
        NO_CONNECTION("No Network Connection"),
        UNSPECIFIED("Unspecified Error");

        private final String mMessage;

        ResponseCode(String message) {
            mMessage = message;
        }

        @Override
        public String toString() {
            return mMessage;
        }
}
