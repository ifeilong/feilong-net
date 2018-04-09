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
package com.feilong.netpay.util;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import com.feilong.net.HttpMethodType;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;
import com.feilong.net.httpclient4.builder.HttpClientBuilder;
import com.feilong.net.httpclient4.builder.HttpResponseUtil;
import com.feilong.net.httpclient4.builder.HttpUriRequestBuilder;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:36.
 *
 * @see <a href="https://blog.csdn.net/fenghuibian/article/details/52459699">https://blog.csdn.net/fenghuibian/article/details/52459699</a>
 */
public class WechatRefundRequest{

    /**
     * 通过Https往API post xml数据.
     *
     * @param url
     *            API地址
     * @param xmlObj
     *            要提交的XML数据对象
     * @param signCertPath
     *            当前目录，用于加载证书
     * @param signCertPassword
     *            the sign cert password
     * @param signCertType
     *            the sign cert type
     * @return the string
     */
    public static String httpsRequest(String url,String xmlObj,String signCertPath,String signCertPassword,String signCertType)
                    throws IOException,KeyStoreException,UnrecoverableKeyException,NoSuchAlgorithmException,KeyManagementException,
                    CertificateException{
        HttpClient httpClient = buildHttpClient(signCertPath, signCertPassword, signCertType);

        HttpResponse httpResponse = httpClient.execute(buildHttpPost(url, xmlObj));
        return HttpResponseUtil.getResultString(httpResponse);
    }

    protected static HttpClient buildHttpClient(String signCertPath,String signCertPassword,String signCertType) throws KeyStoreException,
                    IOException,NoSuchAlgorithmException,CertificateException,KeyManagementException,UnrecoverableKeyException{
        //加载证书
        SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder
                        .build(signCertPath, signCertPassword, signCertType);

        return HttpClientBuilder.build(null, sslConnectionSocketFactory);
    }

    protected static HttpUriRequest buildHttpPost(String url,String xmlObj) throws UnsupportedCharsetException{
        HttpRequest httpRequest = new HttpRequest(url, HttpMethodType.POST);
        //httpRequest.setHeaderMap(toMap("Content-Type", "text/xml"));
        httpRequest.setRequestBody(xmlObj);

        ConnectionConfig useConnectionConfig = new ConnectionConfig();
        useConnectionConfig.setReadTimeout(1000);
        useConnectionConfig.setConnectTimeout(3000);

        return HttpUriRequestBuilder.build(httpRequest, useConnectionConfig);
    }

}