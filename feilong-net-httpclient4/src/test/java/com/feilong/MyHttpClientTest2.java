package com.feilong;

import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.net.httpclient4.HttpClientUtil.getResponseStatusCode;

import java.util.Date;

import org.junit.Test;

import com.feilong.net.entity.ConnectionConfig;
import com.feilong.test.AbstractTest;

/**
 * @Author: zgd
 * @Date: 2019/8/15 00:04
 * @Description: 测试httpclient
 */
public class MyHttpClientTest2 extends AbstractTest{

    @Test
    public void run(){
        //System.setProperty("javax.net.debug", "all");

        Date beginDate = new Date();

        ConnectionConfig conconfig = new ConnectionConfig(200, 400);

        String string = "=======================[{}],{}    code:{}===============================";
        for (int i = 0; i < 1; i++){
            String url = "";
            if (i > 25){
                url = "https://www.baidu.com";
            }else{
                url = "https://www.hao123.com";
            }
            LOGGER.info(string, i + 1, url, getResponseStatusCode(url, conconfig));
        }

        //---------------------------------------------------------------

        LOGGER.info("use time: [{}]", formatDuration(beginDate));

    }

}