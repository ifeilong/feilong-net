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
package com.feilong.net.httpclient4;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JavaToJsonConfig;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.json.jsonlib.processor.StringOverLengthJsonValueProcessor;
import com.feilong.net.HttpMethodType;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;
import com.feilong.net.httpclient4.builder.HttpRequestExecuter;
import com.feilong.net.httpclient4.builder.HttpResponseBuilder;
import com.feilong.net.httpclient4.builder.HttpResponseUtil;

import net.sf.json.processors.JsonValueProcessor;

/**
 * 基于 HttpClient4 的工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.http.client.methods.HttpUriRequest
 * @see "com.feilong.tools.net.httpclient3.HttpClientUtil"
 * @see "org.springframework.http.client.HttpComponentsClientHttpResponse"
 * @see <a href="http://hc.apache.org/index.html">Apache HttpComponents</a>
 * @see <a href="https://wiki.apache.org/HttpComponents/QuickStart">QuickStart</a>
 * @since 1.10.6
 */
public final class HttpClientUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private HttpClientUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------getResponseStatusCode------------------------------------------

    /**
     * 获得请求的响应码.
     * 
     * <p>
     * 默认 {@link HttpMethodType#GET} 请求
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     int responseStatusCode = HttpClientUtil.getResponseStatusCode(urlString);
     *     LOGGER.debug("" + responseStatusCode);
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 200
     * </pre>
     * 
     * </blockquote>
     *
     * @param urlString
     *            the url string
     * @return 如果 <code>urlString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>urlString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static int getResponseStatusCode(String urlString){
        Validate.notBlank(urlString, "urlString can't be blank!");
        return getResponseStatusCode(urlString, null);
    }

    /**
     * 获得请求的响应码.
     * 
     * <p>
     * 默认 {@link HttpMethodType#GET} 请求
     * </p>
     * 
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     int responseStatusCode = HttpClientUtil.getResponseStatusCode(urlString);
     *     LOGGER.debug("" + responseStatusCode);
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 200
     * </pre>
     * 
     * </blockquote>
     *
     *
     * @param urlString
     *            the url string
     * @param connectionConfig
     *            the connection config
     * @return 如果 <code>urlString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>urlString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static int getResponseStatusCode(String urlString,ConnectionConfig connectionConfig){
        Validate.notBlank(urlString, "urlString can't be blank!");
        HttpRequest httpRequest = new HttpRequest(urlString);
        return getResponseStatusCode(httpRequest, connectionConfig);
    }

    /**
     * 获得请求的响应码.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     int responseStatusCode = HttpClientUtil.getResponseStatusCode(urlString);
     *     LOGGER.debug("" + responseStatusCode);
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 200
     * </pre>
     * 
     * </blockquote>
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return 如果 <code>httpRequest</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static int getResponseStatusCode(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        Validate.notNull(httpRequest, "httpRequest can't be null!");

        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        HttpResponse httpResponse = HttpRequestExecuter.execute(httpRequest, useConnectionConfig);
        StatusLine statusLine = httpResponse.getStatusLine();

        int statusCode = statusLine.getStatusCode();

        //---------------------------------------------------------------

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace(
                            "httpRequest:[{}],connectionConfig:[{}],statusCode:[{}]",
                            JsonUtil.format(httpRequest),
                            JsonUtil.format(useConnectionConfig),
                            statusCode);
        }
        return statusCode;
    }

    //---------------------------getHttpResponse------------------------------------

    /**
     * 获得 {@link com.feilong.net.entity.HttpResponse}.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发请求到百度
     * </p>
     * 
     * <pre>
     * 
     * public void testGetResponseBodyAsString1(){
     *     String urlString = "http://localhost:8081/member/login";
     *     urlString = "http://www.baidu.com";
     * 
     *     HttpClientUtil.getHttpResponse(urlString);
     * }
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    {@code
    
    response:[    {
            "statusCode": 200,
            "resultString": "<!DOCTYPE html>\r\n<!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv
    =X-UA-Compatible content=
    IE=Edge> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123<\/a>  <a href=http://ir.baidu.com>About Baidu<\/a> <\/p> <p id=
    cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读<\/a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈<\/a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> <\/p> <\/div> <\/div> <\/div> <\/body> <\/html>\r\n",
            "useTime": 1721,
            "headerList":         [
                            {
                    "name": "Cache-Control",
                    "value": "private, no-cache, no-store, proxy-revalidate, no-transform"
                },
                            {
                    "name": "Connection",
                    "value": "Keep-Alive"
                },
                            {
                    "name": "Content-Type",
                    "value": "text/html"
                },
                            {
                    "name": "Date",
                    "value": "Wed, 29 Nov 2017 14:41:32 GMT"
                },
                            {
                    "name": "Last-Modified",
                    "value": "Mon, 23 Jan 2017 13:28:36 GMT"
                },
                            {
                    "name": "Pragma",
                    "value": "no-cache"
                },
                            {
                    "name": "Server",
                    "value": "bfe/1.0.8.18"
                },
                            {
                    "name": "Set-Cookie",
                    "value": "BDORZ=27315; max-age=86400; domain=.baidu.com; path=/"
                },
                            {
                    "name": "Transfer-Encoding",
                    "value": "chunked"
                }
            ]
        }]
    }
     * </pre>
     * 
     * </blockquote>
     * 
     * @param urlString
     *            the url string
     * @return 如果 <code>urlString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>urlString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static com.feilong.net.entity.HttpResponse getHttpResponse(String urlString){
        Validate.notBlank(urlString, "urlString can't be blank!");
        return getHttpResponse(urlString, null);
    }

    /**
     * 获得 {@link com.feilong.net.entity.HttpResponse}.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发请求到百度
     * </p>
     * 
     * <pre>
     * 
     * public void testGetResponseBodyAsString1(){
     *     String urlString = "http://localhost:8081/member/login";
     *     urlString = "http://www.baidu.com";
     * 
     *     HttpClientUtil.getHttpResponse(urlString);
     * }
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    {@code
    
    response:[    {
            "statusCode": 200,
            "resultString": "<!DOCTYPE html>\r\n<!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv
    =X-UA-Compatible content=
    IE=Edge> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123<\/a>  <a href=http://ir.baidu.com>About Baidu<\/a> <\/p> <p id=
    cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读<\/a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈<\/a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> <\/p> <\/div> <\/div> <\/div> <\/body> <\/html>\r\n",
            "useTime": 1721,
            "headerList":         [
                            {
                    "name": "Cache-Control",
                    "value": "private, no-cache, no-store, proxy-revalidate, no-transform"
                },
                            {
                    "name": "Connection",
                    "value": "Keep-Alive"
                },
                            {
                    "name": "Content-Type",
                    "value": "text/html"
                },
                            {
                    "name": "Date",
                    "value": "Wed, 29 Nov 2017 14:41:32 GMT"
                },
                            {
                    "name": "Last-Modified",
                    "value": "Mon, 23 Jan 2017 13:28:36 GMT"
                },
                            {
                    "name": "Pragma",
                    "value": "no-cache"
                },
                            {
                    "name": "Server",
                    "value": "bfe/1.0.8.18"
                },
                            {
                    "name": "Set-Cookie",
                    "value": "BDORZ=27315; max-age=86400; domain=.baidu.com; path=/"
                },
                            {
                    "name": "Transfer-Encoding",
                    "value": "chunked"
                }
            ]
        }]
    }
     * </pre>
     * 
     * </blockquote>
     * 
     * @param urlString
     *            the url string
     * @param connectionConfig
     *            the connection config
     * @return 如果 <code>urlString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>urlString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static com.feilong.net.entity.HttpResponse getHttpResponse(String urlString,ConnectionConfig connectionConfig){
        Validate.notBlank(urlString, "urlString can't be blank!");
        HttpRequest httpRequest = new HttpRequest(urlString);
        return getHttpResponse(httpRequest, connectionConfig);
    }

    /**
     * 获得 {@link com.feilong.net.entity.HttpResponse}.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发请求到百度
     * </p>
     * 
     * <pre>
     * 
     * public void testGetResponseBodyAsString1(){
     *     String urlString = "http://localhost:8081/member/login";
     *     urlString = "http://www.baidu.com";
     * 
     *     HttpClientUtil.getHttpResponse(urlString);
     * }
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    {@code
    
    response:[    {
            "statusCode": 200,
            "resultString": "<!DOCTYPE html>\r\n<!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv
    =X-UA-Compatible content=
    IE=Edge> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123<\/a>  <a href=http://ir.baidu.com>About Baidu<\/a> <\/p> <p id=
    cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读<\/a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈<\/a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> <\/p> <\/div> <\/div> <\/div> <\/body> <\/html>\r\n",
            "useTime": 1721,
            "headerList":         [
                            {
                    "name": "Cache-Control",
                    "value": "private, no-cache, no-store, proxy-revalidate, no-transform"
                },
                            {
                    "name": "Connection",
                    "value": "Keep-Alive"
                },
                            {
                    "name": "Content-Type",
                    "value": "text/html"
                },
                            {
                    "name": "Date",
                    "value": "Wed, 29 Nov 2017 14:41:32 GMT"
                },
                            {
                    "name": "Last-Modified",
                    "value": "Mon, 23 Jan 2017 13:28:36 GMT"
                },
                            {
                    "name": "Pragma",
                    "value": "no-cache"
                },
                            {
                    "name": "Server",
                    "value": "bfe/1.0.8.18"
                },
                            {
                    "name": "Set-Cookie",
                    "value": "BDORZ=27315; max-age=86400; domain=.baidu.com; path=/"
                },
                            {
                    "name": "Transfer-Encoding",
                    "value": "chunked"
                }
            ]
        }]
    }
     * </pre>
     * 
     * </blockquote>
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return 如果 <code>httpRequest</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static com.feilong.net.entity.HttpResponse getHttpResponse(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        Validate.notNull(httpRequest, "httpRequest can't be null!");

        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        //---------------------------------------------------------------
        Date beginDate = new Date();
        HttpResponse httpResponse = HttpRequestExecuter.execute(httpRequest, useConnectionConfig);
        com.feilong.net.entity.HttpResponse resultResponse = HttpResponseBuilder.build(beginDate, httpResponse);

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

    //----------------------getResponseBodyAsString-----------------------------------------

    /**
     * 发送get请求,获得请求的响应内容.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发信息给百度,并得到相应字符串
     * </p>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     LOGGER.debug(HttpClientUtil.get(urlString));
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    {@code
    <!DOCTYPE html>
    <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=
    IE=Edge><meta content=always name=
    referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link
    =#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div id=
    u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id
    =ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=
    cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
    
    }
     * </pre>
     * 
     * </blockquote>
     * 
     * @param uri
     *            请求的uri地址
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.10.7
     */
    public static String get(String uri){
        return get(uri, null);
    }

    /**
     * 发送 get 请求,获得请求的响应内容.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发信息给百度,并得到相应字符串
     * </p>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     LOGGER.debug(HttpClientUtil.get(urlString, null));
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
     *     {@code
     *     <!DOCTYPE html>
     *     <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=
    X-UA-Compatible content=
     *     IE=Edge><meta content=always name=
     *     referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link
     *     =#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div id=
     *     u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id
     *     =ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=
     *     cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
     *     
     *     }
     * </pre>
     * 
     * </blockquote>
     *
     * @param uri
     *            请求的uri地址
     * @param requestParamMap
     *            the request param map
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.10.7
     */
    public static String get(String uri,Map<String, String> requestParamMap){
        Validate.notBlank(uri, "uri can't be blank!");

        return getResponseBodyAsString(new HttpRequest(uri, requestParamMap, HttpMethodType.GET));
    }

    //---------------------------------------------------------------

    /**
     * 发送put请求,获得请求的响应内容.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发信息给百度,并得到相应字符串
     * </p>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     LOGGER.debug(HttpClientUtil.put(urlString));
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    {@code
    <!DOCTYPE html>
    <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=
    IE=Edge><meta content=always name=
    referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link
    =#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div id=
    u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id
    =ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=
    cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
    
    }
     * </pre>
     * 
     * </blockquote>
     * 
     * @param uri
     *            请求的uri地址
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.12.5
     */
    public static String put(String uri){
        return put(uri, null);
    }

    /**
     * 发送 put 请求,获得请求的响应内容.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发信息给百度,并得到相应字符串
     * </p>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     LOGGER.debug(HttpClientUtil.put(urlString, null));
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
     *     {@code
     *     <!DOCTYPE html>
     *     <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=
    X-UA-Compatible content=
     *     IE=Edge><meta content=always name=
     *     referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link
     *     =#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div id=
     *     u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id
     *     =ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=
     *     cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
     *     
     *     }
     * </pre>
     * 
     * </blockquote>
     *
     * @param uri
     *            请求的uri地址
     * @param requestParamMap
     *            the request param map
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.12.5
     */
    public static String put(String uri,Map<String, String> requestParamMap){
        Validate.notBlank(uri, "uri can't be blank!");
        return getResponseBodyAsString(new HttpRequest(uri, requestParamMap, HttpMethodType.PUT));
    }

    //---------------------------------------------------------------

    /**
     * 发送post请求,获得请求的响应内容.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发信息给百度,并得到相应字符串
     * </p>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     LOGGER.debug(HttpClientUtil.post(urlString));
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    {@code
    <!DOCTYPE html>
    <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=
    IE=Edge><meta content=always name=
    referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link
    =#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div id=
    u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id
    =ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=
    cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
    
    }
     * </pre>
     * 
     * </blockquote>
     * 
     * @param uri
     *            请求的uri地址
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.10.7
     */
    public static String post(String uri){
        return post(uri, (Map<String, String>) null);
    }

    /**
     * 发送 post 请求,获得请求的响应内容.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发信息给百度,并得到相应字符串
     * </p>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     LOGGER.debug(HttpClientUtil.post(urlString, null));
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
     *     {@code
     *     <!DOCTYPE html>
     *     <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=
    X-UA-Compatible content=
     *     IE=Edge><meta content=always name=
     *     referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link
     *     =#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div id=
     *     u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id
     *     =ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=
     *     cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
     *     
     *     }
     * </pre>
     * 
     * </blockquote>
     *
     * @param uri
     *            请求的uri地址
     * @param requestParamMap
     *            the request param map
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.10.7
     */
    public static String post(String uri,Map<String, String> requestParamMap){
        Validate.notBlank(uri, "uri can't be blank!");
        return getResponseBodyAsString(new HttpRequest(uri, requestParamMap, HttpMethodType.POST));
    }

    /**
     * 发送 Post 请求,并且设置 RequestBody ,获得请求的响应内容.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 比如微信请求数据
     * </p>
     * 
     * <pre class="code">
     * 
     * private static String getResponse(String url,Map{@code <String, String>} map){
     *     String xmlInfo = XStreamUtil.toXML(map, "xml", false);
     * 
     *     return HttpClientUtil.post(url, xmlInfo);
     * }
     * 
     * </pre>
     * 
     * </blockquote>
     *
     * @param uri
     *            the uri
     * @param requestBody
     *            the request body
     * @return the string
     * 
     * @since 1.10.7
     */
    public static String post(String uri,String requestBody){
        Validate.notBlank(uri, "uri can't be blank!");

        HttpRequest httpRequest = new HttpRequest(uri, null, HttpMethodType.POST);
        httpRequest.setRequestBody(requestBody);
        return getResponseBodyAsString(httpRequest);
    }

    //---------------------------------------------------------------

    /**
     * 发送请求,获得请求的响应内容.
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发信息给百度,并得到相应字符串
     * </p>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     LOGGER.debug(HttpClientUtil.getResponseBodyAsString(urlString));
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    {@code
    <!DOCTYPE html>
    <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=
    IE=Edge><meta content=always name=
    referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link
    =#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div id=
    u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id
    =ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=
    cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
    
    }
     * </pre>
     * 
     * </blockquote>
     * 
     * @param httpRequest
     *            the http request
     * @return the response body as string
     */
    public static String getResponseBodyAsString(HttpRequest httpRequest){
        Validate.notNull(httpRequest, "httpRequest can't be null!");
        return getResponseBodyAsString(httpRequest, null);
    }

    /**
     * 发送请求,获得请求的响应内容.
     *
     * @param uri
     *            the uri
     * @param requestParamMap
     *            the request param map
     * @param httpMethod
     *            the method
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.11.0
     */
    public static String getResponseBodyAsString(String uri,Map<String, String> requestParamMap,String httpMethod){
        Validate.notBlank(uri, "uri can't be blank!");
        return getResponseBodyAsString(new HttpRequest(uri, requestParamMap, httpMethod), null);
    }

    /**
     * 发送请求,获得请求的响应内容.
     *
     * @param uri
     *            the uri
     * @param requestParamMap
     *            the request param map
     * @param httpMethodType
     *            the http method type
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.11.0
     */
    public static String getResponseBodyAsString(String uri,Map<String, String> requestParamMap,HttpMethodType httpMethodType){
        Validate.notBlank(uri, "uri can't be blank!");
        return getResponseBodyAsString(new HttpRequest(uri, requestParamMap, httpMethodType), null);
    }

    //---------------------------------------------------------------

    /**
     * 发送请求,获得请求的响应内容.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * <b>场景:</b> 发信息给百度,并得到相应字符串
     * </p>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     LOGGER.debug(HttpClientUtil.getResponseBodyAsString(urlString));
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre>
    {@code
    <!DOCTYPE html>
    <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=
    IE=Edge><meta content=always name=
    referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link
    =#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div id=
    u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id
    =ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=
    cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
    
    }
     * </pre>
     * 
     * </blockquote>
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return 如果 <code>httpRequest</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>connectionConfig</code> 是null,使用 {@link ConnectionConfig#INSTANCE}<br>
     */
    public static String getResponseBodyAsString(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        Validate.notNull(httpRequest, "httpRequest can't be null!");

        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        HttpResponse httpResponse = HttpRequestExecuter.execute(httpRequest, connectionConfig);
        String resultString = HttpResponseUtil.getResultString(httpResponse);

        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(
                            "request:[{}],useConnectionConfig:[{}],resultString:[{}]",
                            JsonUtil.format(httpRequest),
                            JsonUtil.format(useConnectionConfig),
                            StringOverLengthJsonValueProcessor.format(resultString, 1000));
        }
        return resultString;
    }

}
