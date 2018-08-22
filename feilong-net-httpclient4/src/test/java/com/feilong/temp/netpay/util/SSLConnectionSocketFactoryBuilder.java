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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;

import com.feilong.net.KeyStoreBuilder;
import com.feilong.net.ssl.SSLProtocol;

/**
 * The Class LayeredConnectionSocketFactoryBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class SSLConnectionSocketFactoryBuilder{

    /**
     * Builds the.
     *
     * @param signCertPath
     *            the sign cert path
     * @param signCertPassword
     *            the sign cert password
     * @param signCertType
     *            the sign cert type
     * @return the SSL connection socket factory
     * @throws KeyStoreException
     *             the key store exception
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     * @throws CertificateException
     *             the certificate exception
     * @throws KeyManagementException
     *             the key management exception
     * @throws UnrecoverableKeyException
     *             the unrecoverable key exception
     */
    public static SSLConnectionSocketFactory build(String signCertPath,String signCertPassword,String signCertType)
                    throws KeyStoreException,IOException,NoSuchAlgorithmException,CertificateException,KeyManagementException,
                    UnrecoverableKeyException{
        SSLContext sslContext = buildSSLContext(signCertPath, signCertPassword, signCertType);
        // Allow TLSv1 protocol only
        return new SSLConnectionSocketFactory(
                        sslContext,
                        new String[] { SSLProtocol.TLSv1 },
                        null,
                        SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    //---------------------------------------------------------------

    private static SSLContext buildSSLContext(String signCertPath,String signCertPassword,String signCertType) throws KeyStoreException,
                    IOException,NoSuchAlgorithmException,CertificateException,KeyManagementException,UnrecoverableKeyException{
        KeyStore keyStore = KeyStoreBuilder.build(signCertPath, signCertPassword, signCertType);

        // Trust own CA and all self-signed certs
        return SSLContexts.custom().loadKeyMaterial(keyStore, signCertPassword.toCharArray()).build();
    }

}
