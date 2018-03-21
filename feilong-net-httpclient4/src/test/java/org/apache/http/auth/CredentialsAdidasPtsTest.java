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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.httpclient4.builder.CredentialsProviderBuilder;

public class CredentialsAdidasPtsTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialsAdidasPtsTest.class);

    @Test
    public void testCredentialsTest5() throws ClientProtocolException,IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();

        List<NameValuePair> nameValuePairList = newArrayList();
        nameValuePairList.add(new BasicNameValuePair("j_username", "xin.jin"));
        nameValuePairList.add(new BasicNameValuePair("j_password", "Aa123456"));

        HttpPost httpPost = new HttpPost("/j_spring_security_check");
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));

        HttpHost targetHost = new HttpHost("pts.adidas.com.cn", 443, "https");
        HttpClientContext httpClientContext = buildHttpClientContext(targetHost);
        CloseableHttpResponse closeableHttpResponse = httpclient.execute(targetHost, httpPost, httpClientContext);

        LOGGER.debug(EntityUtils.toString(closeableHttpResponse.getEntity(), UTF8));
    }

    //---------------------------------------------------------------

    private HttpClientContext buildHttpClientContext(HttpHost targetHost){
        CredentialsProvider credentialsProvider = CredentialsProviderBuilder.build(targetHost, "adidas", "ad20170731");
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

}