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
package com.feilong.temp.netpay.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class WechatRefundRequestTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatRefundRequestTest.class);

    @Test
    public void test() throws UnrecoverableKeyException,KeyManagementException,KeyStoreException,NoSuchAlgorithmException,
                    CertificateException,IOException{
        String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
        String xmlObj = "<obj>111</obj>";

        //签名证书路径，必须使用绝对路径，如果不想使用绝对路径，可以自行实现相对路径获取证书的方法；测试证书所有商户共用开发包中的测试签名证书，生产环境请从cfca下载得到
        String signCertPath = "/Users/feilong/workspace/feilong/feilong-net/feilong-net-httpclient4/src/test/resources/apiclient_cert.p12";
        //签名证书密码，默认为商户ID
        String signCertPassword = "1236387202";
        //签名证书类型，固定不需要修改
        String signCertType = "PKCS12";

        //---------------------------------------------------------------

        LOGGER.info(WechatRefundRequest.httpsRequest(url, xmlObj, signCertPath, signCertPassword, signCertType));

    }
}
