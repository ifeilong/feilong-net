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
package com.feilong.net;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import static com.feilong.core.bean.ConvertUtil.toArray;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.9.6
 */
class HttpsURLConnectionHelper{

    static void doWithHttps(HttpsURLConnection httpURLConnection){
        try{
            SSLSocketFactory socketFactory = buildSSLSocketFactory();
            httpURLConnection.setSSLSocketFactory(socketFactory);

            // ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(new TrustAnyHostnameVerifier());
        }catch (KeyManagementException | NoSuchAlgorithmException e){
            throw new UncheckedHttpException(e);
        }
    }

    /**
     * The following chart depicts the protocols and algorithms supported in each JDK version:
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * 
     * <tr style="background-color:#ccccff">
     * <th align="left"></th>
     * <th align="left">JDK 8 <br>
     * (March 2014 to present)
     * </th>
     * <th align="left">JDK 7<br>
     * (July 2011 to present)
     * </th>
     * 
     * <th align="left">JDK 6<br>
     * (2006 to end of public updates 2013)
     * </th>
     * </tr>
     * 
     * <tr valign="top">
     * 
     * <td><a href="https://en.wikipedia.org/wiki/Transport_Layer_Security#History_and_development">TLS Protocols</a></td>
     * 
     * <td>
     * <a href="https://blogs.oracle.com/java-platform-group/entry/java_8_will_use_tls">TLSv1.2 (default)</a><br>
     * TLSv1.1<br>
     * TLSv1<br>
     * SSLv3<br>
     * </td>
     * 
     * <td>
     * TLSv1.2<br>
     * TLSv1.1<br>
     * TLSv1 (default)<br>
     * SSLv3<br>
     * </td>
     * 
     * <td>
     * TLS v1.1 (<a href="http://www.oracle.com/technetwork/java/javase/overview-156328.html">JDK 6 update 111</a> and above)<br>
     * TLSv1 (default)<br>
     * SSLv3<br>
     * </td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>JSSE Ciphers:</td>
     * <td><a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#ciphersuites">Ciphers in JDK 8</a>
     * </td>
     * <td><a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#ciphersuites">Ciphers in JDK 7</a>
     * </td>
     * <td><a href="http://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html#ciphersuites">Ciphers in JDK 6</a>
     * </td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>Reference:</td>
     * <td><a href="http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html">JDK 8 JSSE</a></td>
     * <td><a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/jsse/JSSERefGuide.html">JDK 7 JSSE</a></td>
     * <td><a href="http://docs.oracle.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html">JDK 6 JSSE</a></td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>Java Cryptography Extension, Unlimited Strength (explained later)</td>
     * <td><a href="http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html">JCE for JDK 8</a></td>
     * <td><a href="http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html">JCE for JDK 7</a></td>
     * <td><a href="http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html">JCE for JDK 6</a></td>
     * </tr>
     * 
     * </table>
     * </blockquote>
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @see <a href="https://en.wikipedia.org/wiki/Transport_Layer_Security#History_and_development">Transport Layer Security</a>
     * @see <a href="http://stackoverflow.com/questions/30121510/java-httpsurlconnection-and-tls-1-2">java-httpsurlconnection-and-tls-1-2
     *      </a>
     * @see <a href="https://blogs.oracle.com/java-platform-group/entry/diagnosing_tls_ssl_and_https">diagnosing_tls_ssl_and_https</a>
     * 
     * @see "org.apache.http.ssl.SSLContextBuilder.TLS"
     * @see "org.apache.http.conn.ssl.SSLConnectionSocketFactory.TLS"
     * 
     */
    private static SSLSocketFactory buildSSLSocketFactory() throws NoSuchAlgorithmException,KeyManagementException{
        String tlsProtocol = "TLSv1.1";
        //String tlsProtocol = "TLS";
        SSLContext sslContext = SSLContext.getInstance(tlsProtocol);

        KeyManager[] keyManagers = null;
        sslContext.init(keyManagers, toArray(new TrustAnyTrustManager()), new java.security.SecureRandom());

        return sslContext.getSocketFactory();
    }

    private static class TrustAnyTrustManager implements X509TrustManager{

        @Override
        public void checkClientTrusted(X509Certificate[] chain,String authType) throws CertificateException{
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException{
        }

        @Override
        public X509Certificate[] getAcceptedIssuers(){
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier{

        @Override
        public boolean verify(String hostname,SSLSession session){
            return true;// 直接返回true
        }
    }
}
