package com.majavrella.bloodfactory.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIManager {

    APIResponse responseHandler;
    Context ctx;

    public static APIManager getInstance() {
        return new APIManager();
    }

    public void callApiListener(final String url, Context ctx, final APIResponse responseHandler) {
        this.responseHandler= responseHandler;
        this.ctx = ctx;
        if(isNetworkAvailable()){
            new JsonTask().execute(url);
        } else{
            responseHandler.resultWithJSON(APIConstant.ApiLoginResponse.API_NETWORK_FAIL, new JSONObject());
        }
    }

    private class JsonTask extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progress;

        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ctx);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        protected JSONObject doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                JSONObject jsonObject = new JSONObject(buffer.toString());
                return jsonObject;

            } catch (MalformedURLException e) {
                responseHandler.resultWithJSON(APIConstant.ApiLoginResponse.API_FAIL, new JSONObject());
                e.printStackTrace();
            } catch (IOException e) {
                responseHandler.resultWithJSON(APIConstant.ApiLoginResponse.API_FAIL, new JSONObject());
                e.printStackTrace();
            } catch (JSONException e) {
                responseHandler.resultWithJSON(APIConstant.ApiLoginResponse.API_FAIL, new JSONObject());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            progress.dismiss();
            responseHandler.resultWithJSON(APIConstant.ApiLoginResponse.API_SUCCESS, result );
        }
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}