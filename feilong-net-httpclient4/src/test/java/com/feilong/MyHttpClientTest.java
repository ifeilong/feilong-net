package com.feilong;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import com.feilong.test.AbstractTest;

/**
 * @Author: zgd
 * @Date: 2019/8/15 00:04
 * @Description: 测试httpclient
 */
public class MyHttpClientTest extends AbstractTest{

    private static HttpClient client;

    static{
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(3000)
                        .setSocketTimeout(5000).build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }

    @Test
    public void run(){
        for (int i = 0; i < 10; i++){
            String url = "";
            if (i < 5){
                url = "https://www.baidu.com";
            }else{
                url = "https://www.hao123.com";
            }
            HttpGet httpGet = new HttpGet(url);
            HttpResponse res = null;
            try{
                res = client.execute(httpGet);
            }catch (IOException e){
                LOGGER.error("异常： ", e);
                return;
            }
            int code = res.getStatusLine().getStatusCode();
            LOGGER.info("---url:{}\tcode:{}\ti:{}", url, code, i + 1);

        }

    }

}