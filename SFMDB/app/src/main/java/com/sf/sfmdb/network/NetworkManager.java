package com.sf.sfmdb.network;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sf.sfmdb.SFMDBApplication;

/**
 * Created by aeppili on 11/23/16.
 * Network Manager will be responsible for all network calls within App
 *  - Initially Added an AsyncTask and some logic to handle tasks
 *  - Later added Volley and using Volley for network calls (referred some Vollye Tutorials online)
 */

 public class NetworkManager {
        static final String TAG = "NetworkingManager";
        private static final Deque<WeakReference<NetworkTask>> mNetworkTasks =
                new ArrayDeque<>();


    /**
     * Method to make json object request where json response starts wtih {
     * */
    public static void volleyRequest(final String rIdentifier, String urlStr, final NetworkResponseHandler adResponseHandler) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlStr, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object
                    Log.d(TAG, response.toString());
                    if(adResponseHandler != null) {
                        adResponseHandler.onResponseReceived(rIdentifier, new NetworkResponse(response.toString(), response));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                if(adResponseHandler != null) {
                    adResponseHandler.onResponseReceived(rIdentifier, new NetworkResponse(null));
                }
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        SFMDBApplication.getInstance().addToRequestQueue(jsonObjReq);
    }


    /**
         * @param weakNetworkTask Weak reference to an NetworkTask in progress
         * @return  <tt>false</tt> if weakNetworkTask is null, has a null referent, or if the task has
         *          already been completed
         *          <tt>true</tt> otherwise
         */
        private static boolean cancelOneTask(
                @Nullable final WeakReference<NetworkTask> weakNetworkTask) {
            if (weakNetworkTask == null) {
                return false;
            }

            final NetworkTask downloaderTask = weakNetworkTask.get();
            if (downloaderTask == null) {
                return false;
            }

            return downloaderTask.cancel(true);
        }

        public static void loadNetworkTask(String rIdentifier, String urlStr, NetworkResponseHandler adResponseHandler) {
            final NetworkTask networkRequest = new NetworkTask(rIdentifier, adResponseHandler);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    networkRequest.executeOnExecutor(null);
                } else {
                    networkRequest.execute();
                }
            } catch (Exception e) {
                Log.i(TAG, "Failed to parse Ad response, " + e.toString());
            }
        }

        private static class NetworkTask extends AsyncTask<String, Integer, NetworkResponse> {

            private final String TAG = "Networking";
            private final int REQUEST_TIMEOUT = 20000;
            private final NetworkResponseHandler mResponseHandler;
            private String mTaskIdentifier;
            @NonNull
            private final WeakReference<NetworkTask> mWeakSelf;

            NetworkTask(String rIndetifier, NetworkResponseHandler responseHandler) {
                mTaskIdentifier = rIndetifier;
                mResponseHandler = responseHandler;

                mWeakSelf = new WeakReference<>(this);
            }

            protected NetworkResponse doInBackground(String... params) {
                try {
                    if(params == null || params.length == 0) {
                        if(mResponseHandler != null) {
                            mResponseHandler.onResponseReceived(mTaskIdentifier, null);
                        }
                    }
                    URL url = new URL(params[0]);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("GET");
                    conn.setChunkedStreamingMode(0);
                    conn.setConnectTimeout(REQUEST_TIMEOUT);
                    conn.connect();

                    int httpResult = conn.getResponseCode();
                    StringBuilder builder = new StringBuilder();

                    if (httpResult != 200) {
                        return new NetworkResponse(null); //NetworkResponse(null, ResponseCode.SERVER_ERROR);
                    } else {
                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        reader.close();
                        is.close();
                    }

                    return new NetworkResponse(builder.toString()); //, ResponseCode.SUCCESS);
                } catch (MalformedURLException murlexp) {
                    Log.w(TAG, "MalformedURLException: " + murlexp.getMessage());
                    return new NetworkResponse(null) ;//, ResponseCode.INVALID_REQUEST);
                } catch (IOException ioexp) {
                    Log.w(TAG, "IOException" + ioexp.getMessage());
                    return new NetworkResponse(null); //, ResponseCode.NETWORK_ERROR);
                }
            }

            protected void onPostExecute(NetworkResponse response) {
                if (isCancelled()) {
                    onCancelled();
                    return;
                }

                mNetworkTasks.remove(mWeakSelf);

                if (mResponseHandler != null) {
                    mResponseHandler.onResponseReceived(mTaskIdentifier, response);
                }
            }

            @Override
            protected void onCancelled() {
                Log.d(TAG, "Network task was cancelled.");
                mNetworkTasks.remove(mWeakSelf);
                if(mResponseHandler != null) {
                    mResponseHandler.onResponseReceived(mTaskIdentifier, null);
                }
            }
        }
    }

