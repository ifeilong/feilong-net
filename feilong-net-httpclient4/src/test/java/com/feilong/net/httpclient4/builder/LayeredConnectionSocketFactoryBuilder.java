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
package com.feilong.net.httpclient4.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class LayeredConnectionSocketFactoryBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(LayeredConnectionSocketFactoryBuilder.class);

    private static SSLConnectionSocketFactory build(String signCertPath,String signCertPassword,String signCertType)
                    throws KeyStoreException,FileNotFoundException,IOException,NoSuchAlgorithmException,CertificateException,
                    KeyManagementException,UnrecoverableKeyException{
        KeyStore keyStore = buildKeyStore(signCertPath, signCertPassword, signCertType);

        // Trust own CA and all self-signed certs
        // 加载证书密码，默认为商户ID
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, signCertPassword.toCharArray()).build();
        // Allow TLSv1 protocol only
        return new SSLConnectionSocketFactory(
                        sslcontext,
                        new String[] { "TLSv1" },
                        null,
                        SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    private static KeyStore buildKeyStore(String signCertPath,String signCertPassword,String signCertType)
                    throws KeyStoreException,FileNotFoundException,IOException,NoSuchAlgorithmException,CertificateException{
        //拼接证书的路径
        KeyStore keyStore = KeyStore.getInstance(signCertType);

        //加载本地的证书进行https加密传输
        InputStream inputStream = new FileInputStream(new File(signCertPath));
        keyStore.load(inputStream, signCertPassword.toCharArray()); //加载证书密码，默认为商户ID
        return keyStore;
    }
}
