package com.feilong;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;

class TestRunnable implements Runnable{

    private final CloseableHttpClient httpclient;

    public TestRunnable(CloseableHttpClient httpClient){
        this.httpclient = httpClient;
    }

    @Override
    public void run(){
        String id = "444";
        String name = "testName";
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);
        param.put("name", name);

        //        String result = HttpClientUtil.doHttpPost("http://ip:port/testapi", param, httpclient);
        //        System.out.println(result);
    }

}