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
package com.feilong.net.handler;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.net.HttpMethodType.POST;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.Validate;

import com.feilong.net.HttpMethodType;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;

/**
 * URLConnectionHelper辅助类,目前主要是buildURLConnection.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.9.6
 */
public final class URLConnectionBuilder{

    /** Don't let anyone instantiate this class. */
    private URLConnectionBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 构建 {@link java.net.HttpURLConnection}.
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return {@link java.net.HttpURLConnection} <br>
     *         如果 <code>httpRequest</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>httpRequest uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>httpRequest uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static URLConnection build(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        Validate.notNull(httpRequest, "httpRequest can't be null!");
        Validate.notBlank(httpRequest.getUri(), "httpRequest.getUri() can't be blank!");
        Validate.notNull(httpRequest.getHttpMethodType(), "httpRequest.getHttpMethodType() can't be null!");

        //---------------------------------------------------------------
        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, new ConnectionConfig());
        try{
            HttpURLConnection httpURLConnection = HttpURLConnectionOpener.open(httpRequest, useConnectionConfig);

            prepareConnection(httpURLConnection, httpRequest, useConnectionConfig);

            return httpURLConnection;
        }catch (IOException e){
            throw new UncheckedHttpException(e);
        }
    }

    /**
     * Prepare connection.
     *
     * @param httpURLConnection
     *            the http url connection
     * @param httpRequest
     *            the http request
     * @param useConnectionConfig
     *            the use connection config
     * @throws IOException
     *             the IO exception
     * @see "org.springframework.http.client.SimpleClientHttpRequestFactory#prepareConnection(HttpURLConnection, String)"
     */
    private static void prepareConnection(HttpURLConnection httpURLConnection,HttpRequest httpRequest,ConnectionConfig useConnectionConfig)
                    throws IOException{
        Validate.notNull(httpURLConnection, "httpURLConnection can't be null!");

        //如果是https  
        if (httpURLConnection instanceof HttpsURLConnection){
            //HttpsURLConnectionHelper.doWithHttps((HttpsURLConnection) httpURLConnection);
            //FIXME
        }

        //---------------------------------------------------------------
        TimeoutPacker.packer(httpURLConnection, useConnectionConfig);

        //---------------------------------------------------------------
        HttpMethodType httpMethodType = httpRequest.getHttpMethodType();
        httpURLConnection.setRequestMethod(RequestMethodBuilder.build(httpMethodType));

        //设置是否向httpUrlConnection输出,如果是post请求,参数要放在http正文内,因此需要设为true,默认是false
        httpURLConnection.setDoOutput(POST == httpMethodType);

        //---------------------------------------------------------------
        RequestHeadersPacker.pack(httpURLConnection, httpRequest.getHeaderMap());

        doWithRequestBody(httpURLConnection, httpRequest.getRequestBody());
    }

    /**
     * Do with request body.
     *
     * @param httpURLConnection
     *            the http URL connection
     * @param requestBody
     *            the request body
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     * @since 1.10.4
     */
    private static void doWithRequestBody(HttpURLConnection httpURLConnection,String requestBody)
                    throws IOException,UnsupportedEncodingException{
        if (isNullOrEmpty(requestBody)){
            return;
        }

        //---------------------------------------------------------------
        //since 1.10.0
        //do with RequestBody
        OutputStream outputStream = httpURLConnection.getOutputStream();

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, UTF8);
        outputStreamWriter.write(requestBody);
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

}
