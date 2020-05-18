package com.misclicked.monitor;

import java.util.TimerTask;

public class TrackerTask extends TimerTask {
    public AsyncResponse delegate;
    @Override
    public void run() {
        HttpRequest req = new HttpRequest();
        req.delegate = delegate;
        req.execute("http://192.168.55.4/track");
    }
}
