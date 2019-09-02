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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.2.1 2015年6月6日 下午11:04:42
 * @since 1.2.1
 */
public class GetResponseStatusCodeTest2{

    /** The Constant log. */
    private static final Logger LOGGER             = LoggerFactory.getLogger(GetResponseStatusCodeTest2.class);

    //---------------------------------------------------------------
    private static final String trustStorePath     = "/Users/feilong/workspace/feilong/feilong-net/feilong-net-httpclient4/src/test/resources/amiuat.keystore";

    private static final String trustStorePassword = "amiuat";

    @Test
    public void test(){
        //
        setHttpsCertificates(trustStorePath, trustStorePassword);

        testGetResponseBodyAsString1();
    }

    @Test
    public void test222(){
        //
        setHttpsCertificates(trustStorePath, trustStorePassword);

        System.clearProperty("javax.net.ssl.trustStore");
        System.clearProperty("javax.net.ssl.trustStorePassword");
        // System.clearProperty("java.protocol.handler.pkgs");

        testGetResponseBodyAsString1();
    }

    public void testGetResponseBodyAsString1(){
        String uri = "https://cps.wecommerce.com.cn/cps/broker/getToken?storeId=9&secret=991d110bc99aa4c9f151525f49eb6934";

        LOGGER.debug(HttpClientUtil.get(uri));
    }

    @Test
    public void testGetResponseBodyAsString122(){
        String uri = "https://40.73.75.129/api/oms/mall/in";
        LOGGER.debug(HttpClientUtil.post(uri));
    }

    @Test
    public void testGetResponseBodyAsString12222(){
        String uri = "https://40.73.75.129/api/oms/mall/in";
        LOGGER.debug(HttpClientUtil.getResponseBodyAsString(new HttpRequest(uri), new ConnectionConfig(false)));
    }

    /**
     * 为指定的HTTPS域名或者IP设置本地证书
     * 
     * @param trustStorePath
     *            .keystore 文件所在目录
     * @param trustStorePassword
     *            生成 .keystore 文件设置的密码
     */
    public static void setHttpsCertificates(String trustStorePath,String trustStorePassword){
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    }

}
