package com.misclicked.monitor;

import java.util.TimerTask;

import android.content.Context;


public class TrackerTask extends TimerTask {
    public AsyncResponse delegate;
    public String trackUrl;

    @Override
    public void run() {
        HttpRequest req = new HttpRequest();
        req.delegate = delegate;
        req.execute(trackUrl);
    }
}
