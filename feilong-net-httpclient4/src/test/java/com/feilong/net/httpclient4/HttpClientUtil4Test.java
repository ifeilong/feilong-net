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
package com.feilong.net.httpclient4;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.2.1 2015年6月6日 下午11:04:42
 * @since 1.2.1
 */
public class HttpClientUtil4Test{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil4Test.class);

    @Test
    public void testGetResponseBodyAsString(){
        String uri = "https://stage.adidas.com.cn/item/BA8900";
        //uri = "https://www.adidas.com.cn/error-traffic-control";
        uri = "http://127.0.0.1:8084";

        LOGGER.debug(HttpClientUtil.getResponseBodyAsString(uri));
    }

    @Test
    public void testHttpClientUtilTest(){
        // 执行一个get方法,设置超时时间,并且将结果变成字符串
        //Request.Get("http://www.yeetrack.com/").connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();
    }

    /**
     * TestHttpClientUtilTest.
     * 
     * @throws IOException
     * @throws ClientProtocolException
     */
    @Test
    public void testHttpClientUtilTest1() throws ClientProtocolException,IOException{
        String uri = "https://pts.adidas.com.cn/login.htm";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);

        HttpResponse httpResponse = httpclient.execute(httpGet);
        Header[] headers = httpResponse.getHeaders("Set-Cookie");

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("{},{}", HttpClientUtil.getResponseBodyAsString(uri), JsonUtil.format(headers));
        }

        if (isNotNullOrEmpty(headers)){
            Header header = headers[0];
            String cookie = header.getValue();
            httpGet.releaseConnection();

            if (LOGGER.isInfoEnabled()){
                LOGGER.info(cookie);
            }
        }
    }

}
