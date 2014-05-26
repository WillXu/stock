package com.zhaijiong.stock;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by xuqi.xq on 2014/5/26.
 */
public interface Fetcher {
    String fetch(String url) throws IOException;
}
