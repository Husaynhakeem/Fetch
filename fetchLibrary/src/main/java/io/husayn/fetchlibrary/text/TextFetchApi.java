package io.husayn.fetchlibrary.text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by husaynhakeem on 8/5/17.
 */

public class TextFetchApi {


    private TextFetchApi() {
    }


    public static TextFetchApi instance() {
        return new TextFetchApi();
    }


    public Single<String> load(final String url) {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getStringResult(url);
            }
        })
                .subscribeOn(Schedulers.io())
                .cache();
    }


    public String getStringResult(String url) {
        try {
            URL urlForJson = new URL(url);
            return getStringResult(urlForJson);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getStringResult(URL url) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            }
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }
}
