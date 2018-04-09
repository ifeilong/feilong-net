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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.feilong.net.httpclient4.builder.HttpResponseUtil;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:36.
 *
 * @see <a href="https://blog.csdn.net/fenghuibian/article/details/52459699">https://blog.csdn.net/fenghuibian/article/details/52459699</a>
 */
public class WechatRefundRequest{

    //wechat.refund.url=https://api.mch.weixin.qq.com/secapi/pay/refund

    //    ##签名证书路径，必须使用绝对路径，如果不想使用绝对路径，可以自行实现相对路径获取证书的方法；测试证书所有商户共用开发包中的测试签名证书，生产环境请从cfca下载得到
    //    #windows下
    //    #acpsdk.signCert.path=/service/ssl/wechat/certs/acp_test_sign.pfx
    //    wechat.signCert.path=/Users/fangzhaojun/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/e7f22383bf4542b5ab561b9b17246574/Message/MessageTemp/aad711a61d2cc2471cabe82c0a263f71/File/cert/apiclient_cert.p12

    //    ##签名证书密码，默认为商户ID
    //    wechat.signCert.pwd=1236387202
    //    ##签名证书类型，固定不需要修改
    //    wechat.signCert.type=PKCS12

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
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws KeyStoreException
     *             the key store exception
     * @throws UnrecoverableKeyException
     *             the unrecoverable key exception
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     * @throws KeyManagementException
     *             the key management exception
     * @throws CertificateException
     *             the certificate exception
     */
    public static String httpsRequest(String url,String xmlObj,String signCertPath,String signCertPassword,String signCertType)
                    throws IOException,KeyStoreException,UnrecoverableKeyException,NoSuchAlgorithmException,KeyManagementException,
                    CertificateException{

        //根据默认超时限制初始化requestConfig
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(3000).build();

        //---------------------------------------------------------------

        HttpPost httpPost = new HttpPost(url);

        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        //---------------------------------------------------------------

        //设置请求器的配置
        httpPost.setConfig(requestConfig);

        //---------------------------------------------------------------

        //加载证书
        SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder
                        .build(signCertPath, signCertPassword, signCertType);

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        return HttpResponseUtil.getResultString(httpResponse);
    }

}