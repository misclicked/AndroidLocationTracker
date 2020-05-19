package com.misclicked.monitor;

import java.util.TimerTask;

public class TrackerTask extends TimerTask {
    public AsyncResponse delegate;

    @Override
    public void run() {
        HttpRequest req = new HttpRequest();
        req.delegate = delegate;
        req.execute("http://[2001:288:7001:270a:5d01:9f26:e46b:97a9]/track");
    }
}
