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
package org.apache.http.auth;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class CredentialsTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialsTest.class);

    /**
     * TestHttpClientUtilTest.
     */
    @Test
    public void testHttpClientUtilTest12(){

        //任何用户认证的过程，都需要一系列的凭证来确定用户的身份。最简单的用户凭证可以是用户名和密码这种形式。UsernamePasswordCredentials这个类可以用来表示这种情况，这种凭据包含明文的用户名和密码。
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user", "pwd");
        System.out.println(creds.getUserPrincipal().getName());
        System.out.println(creds.getPassword());

        //NTCredentials是微软的windows系统使用的一种凭据，包含username、password，还包括一系列其他的属性，比如用户所在的域名。在Microsoft Windows的网络环境中，同一个用户可以属于不同的域，所以他也就有不同的凭据。
        NTCredentials creds1 = new NTCredentials("user", "pwd", "workstation", "domain");
        System.out.println(creds1.getUserPrincipal().getName());
        System.out.println(creds1.getPassword());
    }

    /**
     * TestCredentialsTest.
     */
    @Test
    public void testCredentialsTest(){
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope("somehost", AuthScope.ANY_PORT), new UsernamePasswordCredentials("u1", "p1"));
        credentialsProvider.setCredentials(new AuthScope("somehost", 8080), new UsernamePasswordCredentials("u2", "p2"));
        credentialsProvider.setCredentials(
                        new AuthScope("otherhost", 8080, AuthScope.ANY_REALM, "ntlm"),
                        new UsernamePasswordCredentials("u3", "p3"));

        System.out.println(credentialsProvider.getCredentials(new AuthScope("somehost", 80, "realm", "basic")));
        System.out.println(credentialsProvider.getCredentials(new AuthScope("somehost", 8080, "realm", "basic")));
        System.out.println(credentialsProvider.getCredentials(new AuthScope("otherhost", 8080, "realm", "basic")));
        System.out.println(credentialsProvider.getCredentials(new AuthScope("otherhost", 8080, null, "ntlm")));

    }

    /**
     * TestCredentialsTest.
     * 
     * @throws IOException
     * @throws ClientProtocolException
     */
    @Test
    public void testCredentialsTest5() throws ClientProtocolException,IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpHost targetHost = new HttpHost("pts.adidas.com.cn", 443, "https");
        HttpPost httpPost = new HttpPost("/j_spring_security_check");

        List<NameValuePair> nameValuePairList = newArrayList();
        nameValuePairList.add(new BasicNameValuePair("j_username", "xin.jin"));
        nameValuePairList.add(new BasicNameValuePair("j_password", "Aa123456"));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));

        HttpClientContext httpClientContext = buildHttpClientContext(targetHost);
        CloseableHttpResponse closeableHttpResponse = httpclient.execute(targetHost, httpPost, httpClientContext);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(EntityUtils.toString(closeableHttpResponse.getEntity(), UTF8));
        }
    }

    /**
     * @return
     * @since 1.10.6
     */
    public HttpClientContext buildHttpClientContext(HttpHost targetHost){

        CredentialsProvider credentialsProvider = buildCredentialsProvider(targetHost);
        // Lookup<AuthSchemeProvider> authRegistry = <...>
        //    // 创建 AuthCache 对象
        AuthCache authCache = new BasicAuthCache();
        //    //创建 BasicScheme，并把它添加到 auth cache中
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // 把AutoCache添加到上下文中
        HttpClientContext httpClientContext = HttpClientContext.create();
        httpClientContext.setCredentialsProvider(credentialsProvider);
        // context.setAuthSchemeRegistry(authRegistry);
        // context.setAuthCache(authCache);

        return httpClientContext;
    }

    /**
     * @param targetHost
     * @return
     * @since 1.10.6
     */
    public CredentialsProvider buildCredentialsProvider(HttpHost targetHost){
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials("adidas", "ad20170731");
        credentialsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()), usernamePasswordCredentials);

        return credentialsProvider;
    }
}
