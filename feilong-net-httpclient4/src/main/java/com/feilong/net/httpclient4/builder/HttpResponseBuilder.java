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

import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import com.feilong.net.UncheckedHttpException;
import com.feilong.net.entity.HttpHeader;

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
     * @throws UncheckedHttpException
     *             the unchecked http exception
     * @since 1.10.6
     */
    public static com.feilong.net.entity.HttpResponse build(Date beginDate,HttpResponse httpResponse) throws UncheckedHttpException{
        long useTime = getIntervalTime(beginDate, new Date());

        StatusLine statusLine = httpResponse.getStatusLine();

        Header[] allHeaders = httpResponse.getAllHeaders();
        List<HttpHeader> httpHeaderList = HttpHeaderListBuilder.build(allHeaders);

        //---------------------------------------------------------------

        com.feilong.net.entity.HttpResponse httpResponse2 = new com.feilong.net.entity.HttpResponse();
        httpResponse2.setStatusCode(statusLine.getStatusCode());
        httpResponse2.setHeaderList(httpHeaderList);
        httpResponse2.setResultString(HttpResponseUtil.getResultString(httpResponse));
        httpResponse2.setUseTime(useTime);

        return httpResponse2;
    }
}
