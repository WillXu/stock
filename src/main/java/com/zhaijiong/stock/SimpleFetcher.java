package com.zhaijiong.stock;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuqi.xq on 2014/5/26.
 */
public class SimpleFetcher {
    private static final String BASEDIR = "e:/stock/";
    private static final String BASEURL = "http://basic.10jqka.com.cn/%s/xls/%s";
    private static final String[] files =
            {"debtreport.xls",  //资产负债表
                    "benefitreport.xls", //利润表
                    "cashreport.xls"}; // 现金流量表

    private ExecutorService executor;
    private ConcurrentLinkedQueue<String> tasks;
    private AtomicInteger successed = new AtomicInteger(0);
    private Integer totalTask = 0;

    public void init() {
        File file = new File(BASEDIR);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.mkdir();
        } catch (Exception e) {
            System.out.println("创建文件失败");
        }

        executor = Executors.newFixedThreadPool(50);
        tasks = Queues.newConcurrentLinkedQueue();
    }

    public void downloadStockFile() {
        try {
            Collection<String> values = stockList().values();
            totalTask = values.size();
            tasks.addAll(values);

            while (totalTask != successed.get()) {
                String stock = tasks.poll();
                if(!Strings.isNullOrEmpty(stock)){
                    download(stock);
                }
                Thread.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download(final String stock) throws MalformedURLException {
        Runnable downloader = new Runnable() {
            @Override
            public void run() {
                int byteread;
                try {
                    for (String fileName : files) {
                        URL url = new URL(String.format(BASEURL, stock, fileName));
                        String filePath = Joiner.on("_").join(BASEDIR, stock, fileName);

                        URLConnection conn = url.openConnection();
                        conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36");
                        conn.setRequestProperty("Referer","http://stockpage.10jqka.com.cn/"+stock+"/finance/");
//                        conn.setRequestProperty("Referer","http://stockpage.10jqka.com.cn/"+stock);
                        conn.setRequestProperty("Cookies","uid=2ktMyVODByARVh0iA2zcAg==; historystock="+stock+"; spversion=20130314");
//                        conn.setRequestProperty("Cookies","BAIDU_CLB_REFER=http%3A%2F%2Fwww.baidu.com%2Fs%3Fie%3DUTF-8%26wd%3D%25E5%2590%258C%25E8%258A%25B1%25E9%25A1%25BA; spversion=20130314; __utma=156575163.1007340261.1401095968.1401107726.1402399647.3; __utmb=156575163.1.10.1402399647; __utmc=156575163; __utmz=156575163.1401095968.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic|utmctr=%E5%90%8C%E8%8A%B1%E9%A1%BA; historystock="+ stock +"%7C*%7C002152%7C*%7C002409");
//                        conn.setRequestProperty("X-Requested-With","XMLHttpRequest");
//                        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//                        conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
//                        conn.setRequestProperty("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.6,en;q=0.4");
//                        conn.setRequestProperty("Cache-Control", "max-age=0");
//                        conn.setRequestProperty("Connection", "keep-alive");
//                        conn.setRequestProperty("Host", "stockpage.10jqka.com.cn");
                        conn.setConnectTimeout(30000);
                        conn.setReadTimeout(30000);
                        InputStream inStream = conn.getInputStream();
                        FileOutputStream fs = new FileOutputStream(filePath);

                        byte[] buffer = new byte[1024 * 1024];
                        while ((byteread = inStream.read(buffer)) != -1) {
                            fs.write(buffer, 0, byteread);
                        }
                        fs.close();
                    }
                    System.out.println("download success:" + stock);
                    successed.incrementAndGet();
                } catch (Exception e) {
                    tasks.add(stock);
                    System.out.println("股票资料获取失败:" + stock);
                    e.printStackTrace();
                }
            }
        };
        executor.execute(downloader);
    }

    private Map<String, String> stockList() throws IOException {
        //k=股票名称 v=股票代码
        Map<String, String> stocks = Maps.newHashMapWithExpectedSize(4000);
        int count = 0;
        String url = "http://bbs.10jqka.com.cn/codelist.html";
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("div[class=bbsilst_wei3] ul a[href~=.(sh).]");
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element e = (Element) iter.next();
            String text = e.html();
            count++;
            stocks.put(text.substring(0, text.lastIndexOf(" ") - 1), text.substring(text.lastIndexOf(" ")));
        }
        System.out.println("沪深股票总数=" + count);
        return stocks;
    }

    public void close() throws InterruptedException {
        System.out.println("程序停止中");
        while (!executor.isShutdown()) {
            executor.shutdown();
            Thread.sleep(1000);
            System.out.print(".");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Stopwatch watch = Stopwatch.createStarted();
        SimpleFetcher fetcher = new SimpleFetcher();
        fetcher.init();
        fetcher.downloadStockFile();
        fetcher.close();
        System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
    }
}
