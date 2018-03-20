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
import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;

import com.feilong.core.TimeInterval;

/**
 * 链接的一些参数,比如超时时间,代理等等.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.3.0
 */
public final class ConnectionConfig{

    /** Static instance. */
    // the static instance works for all types
    public static final ConnectionConfig INSTANCE       = new ConnectionConfig();

    //---------------------------------------------------------------

    /**
     * HTTP标准规范中定义的认证模式(即Http Basic Authentication 基本认证)的用户名.
     * 
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * 
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @since 1.10.6
     */
    private String                       userName;

    /**
     * HTTP标准规范中定义的认证模式(即Http Basic Authentication 基本认证)的密码.
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @since 1.10.6
     */
    private String                       password;

    //---------------------------------------------------------------

    /** 代理地址. */
    private String                       proxyAddress;

    /**
     * 代理端口 <br>
     * A valid port value is between 0 ~ 65535. <br>
     * A port number of zero will let the system pick up an ephemeral port in a bind operation.
     */
    private Integer                      proxyPort;

    //---------------------------------------------------------------

    /**
     * 该值将在打开到此 URLConnection引用的资源的通信链接时超时值(以毫秒为单位).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 1分钟 {@link TimeInterval#MILLISECOND_PER_MINUTE}</li>
     * <li>超时时间为零表示无穷大超时.</li>
     * <li>如果在建立连接之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     * 
     * @see TimeInterval#MILLISECOND_PER_MINUTE
     */
    private int                          connectTimeout = 20 * MILLISECOND_PER_SECONDS;

    /**
     * 将读超时设置为指定的超时值,以毫秒为单位.用一个非零值指定在建立到资源的连接后从 Input流读入时的超时时间.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 1分钟 {@link TimeInterval#MILLISECOND_PER_MINUTE}</li>
     * <li>超时时间为零表示无穷大超时.</li>
     * <li>如果在数据可读取之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     * 
     * @see TimeInterval#MILLISECOND_PER_MINUTE
     */
    private int                          readTimeout    = 20 * MILLISECOND_PER_SECONDS;

    //---------------------------------------------------------------

    /** 内容的字符集. */
    private String                       contentCharset = UTF8;

    //---------------------------------------------------------------
    /**
     * Instantiates a new connection config.
     * 
     * @since 1.11.0
     */
    public ConnectionConfig(){
        super();
    }

    /**
     * Instantiates a new connection config.
     *
     * @param userName
     *            the user name
     * @param password
     *            the password
     * @since 1.11.0
     */
    public ConnectionConfig(String userName, String password){
        super();
        this.userName = userName;
        this.password = password;
    }

    //---------------------------------------------------------------

    /**
     * 该值将在打开到此 URLConnection引用的资源的通信链接时超时值(以毫秒为单位).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 1分钟 {@link TimeInterval#MILLISECOND_PER_MINUTE}</li>
     * <li>超时时间为零表示无穷大超时.</li>
     * <li>如果在建立连接之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @return the 该值将在打开到此 URLConnection引用的资源的通信链接时超时值(以毫秒为单位)
     * @see TimeInterval#MILLISECOND_PER_MINUTE
     */
    public int getConnectTimeout(){
        return connectTimeout;
    }

    /**
     * 该值将在打开到此 URLConnection引用的资源的通信链接时超时值(以毫秒为单位).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 1分钟 {@link TimeInterval#MILLISECOND_PER_MINUTE}</li>
     * <li>超时时间为零表示无穷大超时.</li>
     * <li>如果在建立连接之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @param connectTimeout
     *            the connectTimeout to set
     * @see TimeInterval#MILLISECOND_PER_MINUTE
     */
    public void setConnectTimeout(int connectTimeout){
        this.connectTimeout = connectTimeout;
    }

    /**
     * 将读超时设置为指定的超时值,以毫秒为单位.用一个非零值指定在建立到资源的连接后从 Input流读入时的超时时间.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 1分钟 {@link TimeInterval#MILLISECOND_PER_MINUTE}</li>
     * <li>超时时间为零表示无穷大超时.</li>
     * <li>如果在数据可读取之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @return the 将读超时设置为指定的超时值,以毫秒为单位
     * @see TimeInterval#MILLISECOND_PER_MINUTE
     */
    public int getReadTimeout(){
        return readTimeout;
    }

    /**
     * 将读超时设置为指定的超时值,以毫秒为单位.用一个非零值指定在建立到资源的连接后从 Input流读入时的超时时间.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 1分钟 {@link TimeInterval#MILLISECOND_PER_MINUTE}</li>
     * <li>超时时间为零表示无穷大超时.</li>
     * <li>如果在数据可读取之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @param readTimeout
     *            the readTimeout to set
     * @see TimeInterval#MILLISECOND_PER_MINUTE
     */
    public void setReadTimeout(int readTimeout){
        this.readTimeout = readTimeout;
    }

    /**
     * 获得 内容的字符集.
     *
     * @return the contentCharset
     */
    public String getContentCharset(){
        return contentCharset;
    }

    /**
     * 设置 内容的字符集.
     *
     * @param contentCharset
     *            the contentCharset to set
     */
    public void setContentCharset(String contentCharset){
        this.contentCharset = contentCharset;
    }

    /**
     * 获得 the proxy address.
     *
     * @return the proxyAddress
     */
    public String getProxyAddress(){
        return proxyAddress;
    }

    /**
     * 设置 the proxy address.
     *
     * @param proxyAddress
     *            the proxyAddress to set
     */
    public void setProxyAddress(String proxyAddress){
        this.proxyAddress = proxyAddress;
    }

    /**
     * 获得 代理端口 <br>
     * A valid port value is between 0 ~ 65535.
     *
     * @return the proxyPort
     */
    public Integer getProxyPort(){
        return proxyPort;
    }

    /**
     * 设置 代理端口 <br>
     * A valid port value is between 0 ~ 65535.
     *
     * @param proxyPort
     *            the proxyPort to set
     */
    public void setProxyPort(Integer proxyPort){
        this.proxyPort = proxyPort;
    }

    /**
     * HTTP标准规范中定义的认证模式(即Http Basic Authentication 基本认证)的用户名.
     * 
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @return the userName
     * @since 1.10.6
     */
    public String getUserName(){
        return userName;
    }

    /**
     * HTTP标准规范中定义的认证模式(即Http Basic Authentication 基本认证)的用户名.
     * 
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @param userName
     *            the userName to set
     * @since 1.10.6
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * HTTP标准规范中定义的认证模式(即Http Basic Authentication 基本认证)的密码.
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @return the password
     * @since 1.10.6
     */
    public String getPassword(){
        return password;
    }

    /**
     * HTTP标准规范中定义的认证模式(即Http Basic Authentication 基本认证)的密码.
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @param password
     *            the password to set
     * @since 1.10.6
     */
    public void setPassword(String password){
        this.password = password;
    }
}
