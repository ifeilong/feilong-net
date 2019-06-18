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

import static com.feilong.core.date.DateExtensionUtil.getIntervalTime;
import static com.feilong.core.date.DateUtil.now;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

/**
 * The Class HttpResponseBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public final class HttpResponseBuilder{

    /** Don't let anyone instantiate this class. */
    private HttpResponseBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

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
    public static com.feilong.net.entity.HttpResponse build(Date beginDate,HttpResponse httpResponse){
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
