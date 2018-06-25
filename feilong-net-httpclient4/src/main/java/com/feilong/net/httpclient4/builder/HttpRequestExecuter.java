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

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLException;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 专门发送请求 <code>httpUriRequest</code> .
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public final class HttpRequestExecuter{

    /** Don't let anyone instantiate this class. */
    private HttpRequestExecuter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * Execute.
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return the http response
     */
    public static HttpResponse execute(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        HttpUriRequest httpUriRequest = HttpUriRequestBuilder.build(httpRequest, useConnectionConfig);

        //---------------------------------------------------------------
        try{
            return HttpRequestExecuter.execute(httpUriRequest, connectionConfig);
        }
        //链接超时
        catch (SocketTimeoutException e){
            StringBuilder sb = new StringBuilder();
            //            sb.append("pls check:").append(lineSeparator());
            //            sb.append("1.wangluo");
            //            sb.append("2.can not ");

            throw new UncheckedHttpException(buildMessage(e, sb.toString(), httpRequest, useConnectionConfig), e);
        }
        //https://www.cnblogs.com/sunny08/p/8038440.html
        catch (SSLException e){
            StringBuilder sb = new StringBuilder();
            //            sb.append("pls check:").append(lineSeparator());
            //            sb.append("1.wangluo");
            //            sb.append("2.can not ");
            throw new UncheckedHttpException(buildMessage(e, sb.toString(), httpRequest, useConnectionConfig), e);
        }catch (Exception e){
            throw new UncheckedHttpException(buildMessage(e, "", httpRequest, useConnectionConfig), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the message.
     *
     * @param e
     *            the e
     * @param httpRequest
     *            the http request
     * @param useConnectionConfig
     *            the use connection config
     * @return the string
     * @since 1.11.4
     */
    private static String buildMessage(Exception e,String handlerMessage,HttpRequest httpRequest,ConnectionConfig useConnectionConfig){
        if (isNullOrEmpty(handlerMessage)){
            String pattern = "[{}],httpRequest:[{}],useConnectionConfig:[{}]";
            return Slf4jUtil.format(pattern, e.getMessage(), JsonUtil.format(httpRequest), JsonUtil.format(useConnectionConfig));
        }

        //---------------------------------------------------------------

        String pattern = "[{}],[{}],httpRequest:[{}],useConnectionConfig:[{}]";
        return Slf4jUtil.format(
                        pattern,
                        e.getMessage(),
                        handlerMessage,
                        JsonUtil.format(httpRequest),
                        JsonUtil.format(useConnectionConfig));
    }

    //---------------------------------------------------------------

    /**
     * Execute.
     *
     * @param httpUriRequest
     *            the http uri request
     * @param connectionConfig
     *            the connection config
     * @return 如果 <code>httpUriRequest</code> 是null,抛出 {@link NullPointerException}<br>
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @since 1.11.0 change method Access Modifiers
     */
    public static HttpResponse execute(HttpUriRequest httpUriRequest,ConnectionConfig connectionConfig) throws IOException{
        Validate.notNull(httpUriRequest, "httpUriRequest can't be null!");

        HttpClient httpClient = HttpClientBuilder.build(connectionConfig);
        return httpClient.execute(httpUriRequest);
    }
}
