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
package com.feilong.snippet;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.util.SortUtil;
import com.feilong.formatter.FormatterUtil;
import com.feilong.net.entity.HttpHeader;

final class HttpHeaderListBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHeaderListBuilder.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private HttpHeaderListBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param allHeaders
     *            the all headers
     * @return 如果 <code>allHeaders</code> 是null或者empty,返回 {@link Collections#emptyList()}<br>
     */
    public static List<HttpHeader> build(Header[] allHeaders){
        if (isNullOrEmpty(allHeaders)){
            return emptyList();
        }

        //---------------------------------------------------------------
        List<HttpHeader> list = newArrayList();
        for (Header header : allHeaders){
            list.add(new HttpHeader(header.getName(), header.getValue()));
        }

        //---------------------------------------------------------------

        list = SortUtil.sortListByPropertyNamesValue(list, "name");

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(FormatterUtil.formatToSimpleTable(list));
        }
        return list;
    }
}
