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

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.feilong.net.UncheckedHttpException;

/**
 * 专门发送请求 <code>httpUriRequest</code> ,得到返回的 {@link HttpEntity}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class HttpEntityBuilder{

    /**
     * 获得 response entity.
     *
     * @param httpUriRequest
     *            the http uri request
     * @return the response entity
     * @see org.apache.http.impl.client.HttpClients#createDefault()
     */
    public static HttpEntity execute(HttpUriRequest httpUriRequest){
        try{
            HttpClient httpClient = buildHttpClient();

            HttpResponse httpResponse = httpClient.execute(httpUriRequest);
            return httpResponse.getEntity();
        }catch (IOException e){
            throw new UncheckedHttpException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 创造 {@link HttpClient}.
     *
     * @return the closeable http client
     */
    private static CloseableHttpClient buildHttpClient(){

        //TODO 处理 https
        return HttpClients.createDefault();
    }
}
