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
package com.feilong.net.jsoup;

import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * Jsoup 的工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.6
 */
public final class JsoupUtil{

    /** <code>{@value}</code>. */
    public static final int     DEFAULT_TIMEOUT_MILLIS = 20 * MILLISECOND_PER_SECONDS;

    /**
     * 伪造的 useragent.
     * 
     * @since 1.8.1
     */
    private static final String DEFAULT_USER_AGENT     = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private JsoupUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 通过url 获得文档.
     * 
     * @param urlString
     *            the url string
     * @return the document
     * @throws JsoupUtilException
     *             if Exception
     */
    public static Document getDocument(String urlString){
        return getDocument(urlString, DEFAULT_USER_AGENT);
    }

    /**
     * 通过url 获得文档.
     * 
     * @param urlString
     *            the url
     * @param userAgent
     *            the user agent
     * @return the document
     * @throws JsoupUtilException
     *             if Exception
     * @see org.jsoup.Jsoup#connect(String)
     * @see org.jsoup.Connection#userAgent(String)
     * @see org.jsoup.Connection#timeout(int)
     * @see org.jsoup.Connection#get()
     */
    public static Document getDocument(String urlString,String userAgent){
        try{
            return Jsoup.connect(urlString).userAgent(userAgent).timeout(DEFAULT_TIMEOUT_MILLIS).get();
        }catch (IOException e){
            throw new JsoupUtilException(Slf4jUtil.format("urlString:[{}],userAgent:[{}]", urlString, userAgent), e);
        }
    }

    /**
     * Gets the elements by select.
     * 
     * @param url
     *            the url
     * @param selectQuery
     *            the select query
     * @return the elements by select
     * @throws JsoupUtilException
     *             the jsoup util exception
     * @see #getDocument(String)
     * @see org.jsoup.nodes.Element#select(String)
     */
    public static Elements getElementsBySelect(String url,String selectQuery){
        Validate.notEmpty(url);
        Validate.notEmpty(selectQuery);
        Document document = getDocument(url);
        return document.select(selectQuery);
    }

    /**
     * getElementById.
     * 
     * @param url
     *            the url
     * @param id
     *            the id
     * @return getElementById
     * @throws JsoupUtilException
     *             the jsoup util exception
     * @see #getDocument(String)
     * @see org.jsoup.nodes.Element#getElementById(String)
     */
    public static Element getElementById(String url,String id){
        Validate.notEmpty(url);
        Validate.notEmpty(id);
        Document document = getDocument(url);
        return document.getElementById(id);
    }
}
