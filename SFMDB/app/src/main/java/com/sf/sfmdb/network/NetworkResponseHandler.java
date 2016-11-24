package com.sf.sfmdb.network;

/**
 * Created by aeppili on 11/23/16.
 * Callback for networks calls
 */

public interface NetworkResponseHandler {
     void onResponseReceived(String taskId, NetworkResponse response);
}
