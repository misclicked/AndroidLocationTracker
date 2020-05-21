package com.misclicked.nav;

import java.util.TimerTask;


public class UpdaterTask extends TimerTask {
    public AsyncResponse delegate;
    public String trackUrl;

    @Override
    public void run() {
        HttpRequest req = new HttpRequest();
        req.delegate = delegate;
        req.execute(trackUrl);
    }
}
