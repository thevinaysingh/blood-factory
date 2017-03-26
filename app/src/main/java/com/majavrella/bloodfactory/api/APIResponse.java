package com.majavrella.bloodfactory.api;


import org.json.JSONObject;

import java.util.List;

/**
 * Created by lim on 3/30/16.
 */
public abstract class APIResponse {

    public APIResponse() {

    }

    public abstract void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json);
}
