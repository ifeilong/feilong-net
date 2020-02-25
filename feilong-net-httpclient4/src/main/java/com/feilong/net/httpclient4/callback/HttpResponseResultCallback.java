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
package com.feilong.net.httpclient4.callback;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateExtensionUtil.getIntervalTime;
import static com.feilong.core.date.DateUtil.now;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JavaToJsonConfig;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.json.jsonlib.processor.StringOverLengthJsonValueProcessor;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;
import com.feilong.net.httpclient4.builder.HttpHeaderMapBuilder;
import com.feilong.net.httpclient4.builder.HttpResponseUtil;

import net.sf.json.processors.JsonValueProcessor;

/**
 * 用来解析 全数据 {@link com.feilong.net.entity.HttpResponse} 的 {@link ResultCallback}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.0.3
 */
public class HttpResponseResultCallback implements ResultCallback<com.feilong.net.entity.HttpResponse>{

    /** The Constant LOGGER. */
    private static final Logger                    LOGGER   = LoggerFactory.getLogger(HttpResponseResultCallback.class);

    /** Static instance. */
    // the static instance works for all types
    public static final HttpResponseResultCallback INSTANCE = new HttpResponseResultCallback();

    //---------------------------------------------------------------

    /**
     * On.
     *
     * @param httpRequest
     *            the http request
     * @param httpUriRequest
     *            the http uri request
     * @param httpResponse
     *            the http response
     * @param useConnectionConfig
     *            the use connection config
     * @param beginDate
     *            the begin date
     * @return the com.feilong.net.entity. http response
     */
    @Override
    public com.feilong.net.entity.HttpResponse on(
                    HttpRequest httpRequest,
                    HttpUriRequest httpUriRequest,
                    HttpResponse httpResponse,
                    ConnectionConfig useConnectionConfig,
                    Date beginDate){

        com.feilong.net.entity.HttpResponse resultResponse = build(beginDate, httpResponse);

        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            String pattern = "request:[{}],useConnectionConfig:[{}],response:[{}]";
            String response = JsonUtil.format(
                            resultResponse,
                            new JavaToJsonConfig(toMap("resultString", (JsonValueProcessor) new StringOverLengthJsonValueProcessor())));

            LOGGER.info(pattern, JsonUtil.format(httpRequest), JsonUtil.format(useConnectionConfig), response);
        }

        return resultResponse;
    }

    /**
     * Builds the.
     *
     * @param beginDate
     *            the begin date
     * @param httpResponse
     *            the http response
     * @return the com.feilong.net.entity. http response
     * @since 1.10.6
     */
    private static com.feilong.net.entity.HttpResponse build(Date beginDate,HttpResponse httpResponse){
        StatusLine statusLine = httpResponse.getStatusLine();

        //---------------------------------------------------------------
        com.feilong.net.entity.HttpResponse result = new com.feilong.net.entity.HttpResponse();
        result.setStatusCode(statusLine.getStatusCode());
        //since 1.12.5
        result.setHeaderMap(HttpHeaderMapBuilder.build(httpResponse.getAllHeaders()));
        result.setResultString(HttpResponseUtil.getResultString(httpResponse));
        result.setUseTime(getIntervalTime(beginDate, now()));

        return result;
    }
}
