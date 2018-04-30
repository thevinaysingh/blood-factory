package com.majavrella.bloodinformer.api;

/**
 * Created by lim on 3/30/16.
 */
public class APIConstant {

    public enum ApiLoginResponse {
        API_SUCCESS(1),
        API_FAIL(2),
        API_NETWORK_FAIL(3);
        private int value;
        ApiLoginResponse(int value) {
            this.value = value;
        }
    }

    public enum ApiResponseStatus {
        API_SUCCESS(1),
        API_FAIL(2);

        private int value;
        ApiResponseStatus(int value) {
            this.value = value;
        }
    }
}
