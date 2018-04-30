package com.majavrella.bloodinformer.api;


import org.json.JSONObject;

/**
 * Created by lim on 3/30/16.
 */
public abstract class APIResponse {

    public APIResponse() {

    }

    public abstract void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json);
}
