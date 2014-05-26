package com.zhaijiong.stock;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import org.jsoup.Connection;
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
public class SimpleFetcher{
    private static final String BASEDIR = "e:/stock/";
    private static final String BASEURL = "http://basic.10jqka.com.cn/%s/xls/%s";
    private static final String[] files =
            {"debtreport.xls",  //资产负债表
                    "benefitreport.xls", //利润表
                    "cashreport.xls"}; // 现金流量表

    private ExecutorService executor;
    private ConcurrentLinkedQueue<String> failTasks;
    private CountDownLatch latch;
    private AtomicInteger successed = new AtomicInteger(0);

    public void init(){
        File file = new File(BASEDIR);
        if(file.exists()){
            file.delete();
        }
        try {
            file.mkdir();
        } catch (Exception e) {
            System.out.println("创建文件失败");
        }

        executor = Executors.newFixedThreadPool(50);
        failTasks = Queues.newConcurrentLinkedQueue();

    }

    public void downloadStockFile(){
        try {
            Map<String,String> stocks = stockList();
            latch = new CountDownLatch(stocks.size());

            Collection<String> values = stocks.values();
            Iterator<String> iterator = values.iterator();

            while(iterator.hasNext()){
                String stock = iterator.next();
                download(stock);
            }
            latch.await();
            retryFailedStock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void download(final String stock) throws MalformedURLException {
        Runnable downloader = new Runnable() {
            @Override
            public void run() {
                int byteread = 0;
                try {
                    for (String fileName : files) {
                        URL url = new URL(String.format(BASEURL, stock, fileName));
                        String filePath = Joiner.on("_").join(BASEDIR, stock, fileName);

                        URLConnection conn = url.openConnection();
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
                    successed.incrementAndGet();
                } catch (Exception e) {
                    failTasks.add(stock);
                    System.out.println("股票资料获取失败:" + stock);
                } finally {
                    latch.countDown();
                }
            }
        };
        executor.execute(downloader);
    }

    private void retryFailedStock(){
        System.out.println("totel success="+successed);
        latch = new CountDownLatch(failTasks.toArray().length);
        while(!failTasks.isEmpty()){
            String stock = failTasks.poll();
            System.out.println("尝试重新获取股票资料:"+stock);
            try {
                download(stock);
            } catch (MalformedURLException e) {
                failTasks.add(stock);
                System.out.println("股票资料获取失败:" + stock);
            }
        }
    }

    private Map<String, String> stockList() throws IOException {
        //k=股票名称 v=股票代码
        Map<String, String> stocks = Maps.newHashMapWithExpectedSize(4000);
        int count = 0;
        String url = "http://bbs.10jqka.com.cn/codelist.html";
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("div[class=bbsilst_wei3] ul a[href~=.(sh|sz).]");
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element e = (Element) iter.next();
            String text = e.html();
            count++;
            stocks.put(text.substring(0, text.lastIndexOf(" ") - 1), text.substring(text.lastIndexOf(" ")));
        }
        System.out.println("沪深股票总数="+count);
        return stocks;
    }

    public void close() throws InterruptedException {
        System.out.println("程序停止中");
        while(executor.isShutdown()){
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
