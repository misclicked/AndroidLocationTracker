package com.misclicked.monitor;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpRequest extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... urls) {
        return GET(urls[0]);
    }

    private String GET(String url_) {
        String result = "";
        HttpURLConnection connection;
        try {
            URL url = new URL(url_);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            InputStream inputStream = connection.getInputStream();
            if(inputStream != null){
                InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader in = new BufferedReader(reader);

                String line="";
                while ((line = in.readLine()) != null) {
                    result += (line+"\n");
                }
            } else{
                result = "Did not work!";
            }
            return  result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);
    }
}
