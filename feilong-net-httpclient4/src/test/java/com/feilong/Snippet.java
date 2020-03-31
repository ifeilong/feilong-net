/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;

public class Snippet{

    private static CookieStore cookieStore = new BasicCookieStore();

    //    在该代码中分别设置了网络代理，重试处理，对于请求的keepalive时间，指定cookiestore用于保存cookie。
    //    
    //    retryHandler:代码里给了两种方式。第一个是简便的用于设置重试，第一个参数为最大重试次数，第二个参数为请求在幂等情况下是否重试。第二种方式详细的规定了在发生了什么exception个下重试，以及幂等和重试次数下的重试情况。
    //    routePlanner:httpClient支持代理。新建一个httphost对象传给一个routeplanner对象即可。httphost的构造方法中可以指定代理ip和端口
    //    CookieStore:需要预先新建一个cookieStore对象。初始化方式如下：
    //    CookieStore cookieStore = new BasicCookieStore();
    private static CloseableHttpClient getInstanceClient(){

        //retryHandler:代码里给了两种方式。
        //第一个是简便的用于设置重试，第一个参数为最大重试次数，第二个参数为请求在幂等情况下是否重试。

        //第二种方式详细的规定了在发生了什么exception个下重试，以及幂等和重试次数下的重试情况。
        StandardHttpRequestRetryHandler standardHandler = new StandardHttpRequestRetryHandler(5, true);
        HttpRequestRetryHandler httpRequestRetryHandler = build();

        //routePlanner:httpClient支持代理。新建一个httphost对象传给一个routeplanner对象即可。httphost的构造方法中可以指定代理ip和端口
        HttpHost proxy = new HttpHost("127.0.0.1", 80);// 设置代理ip
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        return HttpClients.custom()//
                        .setRoutePlanner(routePlanner)//
                        .setRetryHandler(httpRequestRetryHandler)//
                        .setConnectionTimeToLive(1, TimeUnit.DAYS)//
                        .setDefaultCookieStore(cookieStore)//
                        .build();
    }

    /**
     * 第二种方式详细的规定了在发生了什么exception个下重试，以及幂等和重试次数下的重试情况。
     * 
     * @return
     * @since 1.10.6
     */
    private static HttpRequestRetryHandler build(){
        return new HttpRequestRetryHandler(){

            @Override
            public boolean retryRequest(IOException exception,int retryTimes,HttpContext context){
                if (exception instanceof UnknownHostException || exception instanceof ConnectTimeoutException
                                || !(exception instanceof SSLException) || exception instanceof NoHttpResponseException){
                    return true;
                }

                if (retryTimes > 5){
                    return false;
                }

                //---------------------------------------------------------------------------------------

                HttpClientContext clientContext = HttpClientContext.adapt(context);

                HttpRequest httpRequest = clientContext.getRequest();
                boolean idempotent = !(httpRequest instanceof HttpEntityEnclosingRequest);
                return idempotent; // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
            }
        };
    }
}
