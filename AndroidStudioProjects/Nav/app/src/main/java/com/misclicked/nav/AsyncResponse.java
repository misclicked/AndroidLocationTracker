package com.misclicked.nav;

import org.json.JSONException;

public interface AsyncResponse {
    void processFinish(String output) throws JSONException;
}
