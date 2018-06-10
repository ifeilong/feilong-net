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

import static com.feilong.core.bean.ConvertUtil.toMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PostTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.2.1 2015年6月6日 下午11:04:42
 * @since 1.2.1
 */
public class PostTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PostTest.class);

    /**
     * Test get response body as string.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testGetResponseBodyAsString(){
        String uri = "http://127.0.0.1:8084/post";
        LOGGER.debug(HttpClientUtil.get(uri));
    }

    /**
     * Test get response body as string 1.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testGetResponseBodyAsString1(){
        String uri = "http://127.0.0.1:8084/post1";
        LOGGER.debug(HttpClientUtil.post(uri, toMap("name", "金鑫", "age", "18")));
    }

    /**
     * Test get response body as string 122.
     */
    @Test
    public void testGetResponseBodyAsString122(){

        //        "requestURL": "http://staging.mapemall.com/pay/redirect/doku", 
        //        "requestMethod": "POST", 
        //         "parameters": { 
        //               "PAYMENTCHANNEL": ["01"]
        //               (其他参数略去......)
        //            }

        String uri = "http://test.mapemall.com/pay/redirect/doku";
        LOGGER.debug(HttpClientUtil.post(uri, toMap("PAYMENTCHANNEL", "01")));
    }
}
