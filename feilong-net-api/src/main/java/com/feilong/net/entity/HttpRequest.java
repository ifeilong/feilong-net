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
package com.feilong.net.entity;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.net.HttpMethodType.GET;

import java.util.Map;

import com.feilong.core.net.ParamUtil;
import com.feilong.net.HttpMethodType;

/**
 * http 请求信息.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "org.apache.http.HttpRequest"
 * @since 1.2.0
 */
public class HttpRequest{

    /**
     * 伪造的 useragent.
     * 
     * @since 1.5.0
     */
    public static final String  DEFAULT_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

    //---------------------------------------------------------------
    /** 请求的uri地址. */
    private String              uri;

    /** 请求method 类型,默认 {@link HttpMethodType#GET}. */
    private HttpMethodType      httpMethodType     = GET;

    /** 请求参数. */
    private Map<String, String> paramMap;

    /** 请求头信息. */
    private Map<String, String> headerMap;

    /**
     * 请求正文,比如 webservice 可以传递 xml/json数据体.
     *
     * @see <a href="https://en.wikipedia.org/wiki/HTTP_message_body">HTTP_message_body</a>
     * @since 1.10.0
     */
    private String              requestBody;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     * 
     * @since 1.5.4
     */
    public HttpRequest(){
        super();
    }

    /**
     * Instantiates a new http request.
     *
     * @param uri
     *            the uri
     * @since 1.10.4
     */
    public HttpRequest(String uri){
        super();
        this.uri = uri;
    }

    /**
     * Instantiates a new http request.
     *
     * @param uri
     *            the uri
     * @param httpMethodType
     *            the http method type
     * @since 1.11.0
     */
    public HttpRequest(String uri, HttpMethodType httpMethodType){
        super();
        this.uri = uri;
        this.httpMethodType = httpMethodType;
    }

    /**
     * The Constructor.
     *
     * @param uri
     *            the uri
     * @param paramMap
     *            the param map
     * @param httpMethodType
     *            the http method type
     * @since 1.5.4
     */
    public HttpRequest(String uri, Map<String, String> paramMap, HttpMethodType httpMethodType){
        super();
        this.uri = uri;
        this.paramMap = paramMap;
        this.httpMethodType = httpMethodType;
    }

    /**
     * The Constructor.
     *
     * @param uri
     *            the uri
     * @param paramMap
     *            the param map
     * @param httpMethodType
     *            the http method type
     * @since 1.5.4
     */
    public HttpRequest(String uri, Map<String, String> paramMap, String httpMethodType){
        super();
        this.uri = uri;
        this.paramMap = paramMap;
        this.httpMethodType = HttpMethodType.getByMethodValueIgnoreCase(httpMethodType);
    }

    //---------------------------------------------------------------

    /**
     * 完整的请求路径.
     * 
     * @return the full encoded url
     */
    public String getFullEncodedUrl(){
        return ParamUtil.addParameterSingleValueMap(uri, paramMap, UTF8);
    }

    /**
     * 获得 请求的uri地址.
     *
     * @return the 请求的uri地址
     */
    public String getUri(){
        return uri;
    }

    /**
     * 设置 请求的uri地址.
     *
     * @param uri
     *            the new 请求的uri地址
     */
    public void setUri(String uri){
        this.uri = uri;
    }

    /**
     * 获得 请求method 类型,默认 {@link HttpMethodType#GET}.
     *
     * @return the httpMethodType
     */
    public HttpMethodType getHttpMethodType(){
        return httpMethodType;
    }

    /**
     * 设置 请求method 类型,默认 {@link HttpMethodType#GET}.
     *
     * @param httpMethodType
     *            the httpMethodType to set
     */
    public void setHttpMethodType(HttpMethodType httpMethodType){
        this.httpMethodType = httpMethodType;
    }

    /**
     * 获得 请求参数.
     *
     * @return the paramMap
     */
    public Map<String, String> getParamMap(){
        return paramMap;
    }

    /**
     * 设置 请求参数.
     *
     * @param paramMap
     *            the paramMap to set
     */
    public void setParamMap(Map<String, String> paramMap){
        this.paramMap = paramMap;
    }

    /**
     * 获得 请求头 信息.
     *
     * @return the headerMap
     */
    public Map<String, String> getHeaderMap(){
        return headerMap;
    }

    /**
     * 设置 请求头 信息.
     *
     * @param headerMap
     *            the headerMap to set
     */
    public void setHeaderMap(Map<String, String> headerMap){
        this.headerMap = headerMap;
    }

    /**
     * 获得 请求正文,比如 webservice 可以传递 xml/json数据体.
     *
     * @return the requestBody
     * @see <a href="https://en.wikipedia.org/wiki/HTTP_message_body">HTTP_message_body</a>
     * @since 1.10.0
     */
    public String getRequestBody(){
        return requestBody;
    }

    /**
     * 设置 请求正文,比如 webservice 可以传递 xml/json数据体.
     *
     * @param requestBody
     *            the requestBody to set
     * @see <a href="https://en.wikipedia.org/wiki/HTTP_message_body">HTTP_message_body</a>
     * @since 1.10.0
     */
    public void setRequestBody(String requestBody){
        this.requestBody = requestBody;
    }
}
