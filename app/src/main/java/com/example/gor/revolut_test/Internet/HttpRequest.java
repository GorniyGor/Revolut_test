package com.example.gor.revolut_test.Internet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by Gor on 25.07.2017.
 */

public class HttpRequest {

    public static final int REQUEST_OK = 0;
    public static final int REQUEST_ERROR = 2;
    public static boolean REQUEST_SUCCESS = false;

    private final String mURL;
    private String mContent;

    public HttpRequest(String url){ mURL = url; }

    public int makeRequest(){
        try {
            URL url = new URL(mURL);
            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setInstanceFollowRedirects(true);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    InputStream is = connection.getInputStream();
                    mContent = StringUtils.readInputStream(is);
                    is.close();

                    return REQUEST_OK;
                }

            }
            catch (SocketTimeoutException ex){ ex.printStackTrace(); }
            catch (IOException ex){ ex.printStackTrace(); }
        }
        catch (MalformedURLException ex){ ex.printStackTrace(); }
        return REQUEST_ERROR;
    }

    public String getContent() {
        return mContent;
    }
}
