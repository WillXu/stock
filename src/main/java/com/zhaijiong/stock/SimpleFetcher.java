package com.zhaijiong.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xuqi.xq on 2014/5/26.
 */
public class SimpleFetcher implements Fetcher {

    @Override
    public String fetch(String url) throws IOException {
        StringBuffer contentBuffer = new StringBuffer();

        URL page = new URL(url);
        URLConnection urlConnection = page.openConnection();
        urlConnection.connect();
        InputStream input= urlConnection.getInputStream();
        InputStreamReader reader = new InputStreamReader(input);
        BufferedReader buffStr = new BufferedReader(reader);

        String str = null;
        while ((str = buffStr.readLine()) != null){
            contentBuffer.append(str).append("\n");
        }
        reader.close();
        return contentBuffer.toString();
    }

    public static void main(String[] args) throws IOException {
        SimpleFetcher fetcher = new SimpleFetcher();
        String fetch = fetcher.fetch("http://stockpage.10jqka.com.cn/002409/finance/");
        System.out.println(fetch);

    }
}
