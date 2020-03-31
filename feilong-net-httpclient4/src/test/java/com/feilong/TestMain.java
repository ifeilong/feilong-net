package com.feilong;

import java.util.concurrent.TimeoutException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class TestMain{

    public static void main(String[] args) throws TimeoutException{
        // http请求
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(800);// 设置每个路由基础的连接
        poolingHttpClientConnectionManager.setMaxTotal(1000);//设置最大连接数
        //cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);// 设置目标主机的最大连接数

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).build();
        for (int i = 0; i < 1000; i++){
            TestRunnable tmp = new TestRunnable(httpClient);
            new Thread(tmp).start();
        }

    }
}