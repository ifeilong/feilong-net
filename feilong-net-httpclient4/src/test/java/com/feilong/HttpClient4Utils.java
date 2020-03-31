/*
 * @(#) HttpClientUtils.java 2016年2月3日
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package com.feilong;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.feilong.test.AbstractTest;

/**
 * HttpClient工具类
 *
 * @author captcha_dev
 * @version 2016年2月3日
 */
public class HttpClient4Utils extends AbstractTest{

    private static HttpClient defaultClient = createHttpClient(20, 20, 5000, 5000, 3000);

    /**
     * 实例化HttpClient
     *
     * @param maxTotal
     * @param maxPerRoute
     * @param socketTimeout
     * @param connectTimeout
     * @param connectionRequestTimeout
     * @return
     */
    public static HttpClient createHttpClient(
                    int maxTotal,
                    int maxPerRoute,

                    int socketTimeout,
                    int connectTimeout,
                    int connectionRequestTimeout){
        RequestConfig defaultRequestConfig = RequestConfig.custom()//
                        .setSocketTimeout(socketTimeout)//
                        .setConnectTimeout(connectTimeout)//
                        .setConnectionRequestTimeout(connectionRequestTimeout)//
                        .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        cm.setValidateAfterInactivity(200); // 一个连接idle超过200ms,再次被使用之前,需要先做validation

        CloseableHttpClient httpClient = HttpClients.custom()//
                        .setConnectionManager(cm)//
                        .setConnectionTimeToLive(30, TimeUnit.SECONDS)//
                        .setRetryHandler(new StandardHttpRequestRetryHandler(3, true)) // 配置出错重试
                        .setDefaultRequestConfig(defaultRequestConfig)//
                        .build();

        startMonitorThread(cm);

        return httpClient;
    }

    /**
     * 增加定时任务, 每隔一段时间清理连接
     *
     * @param poolingHttpClientConnectionManager
     */
    private static void startMonitorThread(final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager){
        start(new Runnable(){

            @Override
            public void run(){
                while (true){
                    try{
                        poolingHttpClientConnectionManager.closeExpiredConnections();
                        poolingHttpClientConnectionManager.closeIdleConnections(30, TimeUnit.SECONDS);

                        // log.info("closing expired & idle connections, stat={}", cm.getTotalStats());
                        TimeUnit.SECONDS.sleep(10);
                    }catch (Exception e){
                        // ignore exceptoin
                    }
                }
            }
        });
    }

    private static void start(Runnable runnable){
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

}