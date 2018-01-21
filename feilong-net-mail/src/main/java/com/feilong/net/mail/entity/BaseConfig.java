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
package com.feilong.net.mail.entity;

/**
 * The Class BaseConfig.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.3
 */
public abstract class BaseConfig implements SessionConfig{

    /**
     * 发送邮件的服务器的IP. <br>
     * example:smtp.126.com
     */
    private String  mailServerHost;

    /** 邮件服务的端口 默认25. */
    private String  mailServerPort = "25";

    /**
     * 登录邮件发送服务器的用户名.<br>
     */
    private String  userName;

    /**
     * 登录邮件发送服务器的密码.<br>
     * example:******.
     */
    private String  password;

    /** 是否debug 输出. */
    private boolean isDebug        = false;

    /** 是否需要身份验证,默认 true. */
    private boolean isValidate     = true;

    /**
     * 获得 登录邮件发送服务器的用户名.
     *
     * @return the userName
     */
    @Override
    public String getUserName(){
        return userName;
    }

    /**
     * 设置 登录邮件发送服务器的用户名.
     *
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * 获得 登录邮件发送服务器的密码.
     *
     * @return the password
     */
    @Override
    public String getPassword(){
        return password;
    }

    /**
     * 设置 登录邮件发送服务器的密码.
     *
     * @param password
     *            the password to set
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * 获得 发送邮件的服务器的IP.
     *
     * @return the mailServerHost
     */
    @Override
    public String getMailServerHost(){
        return mailServerHost;
    }

    /**
     * 设置 发送邮件的服务器的IP.
     *
     * @param mailServerHost
     *            the mailServerHost to set
     */
    public void setMailServerHost(String mailServerHost){
        this.mailServerHost = mailServerHost;
    }

    /**
     * 获得 邮件服务的端口 默认25.
     *
     * @return the mailServerPort
     */
    @Override
    public String getMailServerPort(){
        return mailServerPort;
    }

    /**
     * 设置 邮件服务的端口 默认25.
     *
     * @param mailServerPort
     *            the mailServerPort to set
     */
    public void setMailServerPort(String mailServerPort){
        this.mailServerPort = mailServerPort;
    }

    /**
     * 获得 是否debug 输出.
     *
     * @return the isDebug
     */
    @Override
    public boolean getIsDebug(){
        return isDebug;
    }

    /**
     * 设置 是否debug 输出.
     *
     * @param isDebug
     *            the isDebug to set
     */
    public void setIsDebug(boolean isDebug){
        this.isDebug = isDebug;
    }

    /**
     * 获得 是否需要身份验证,默认 true.
     *
     * @return the isValidate
     */
    @Override
    public boolean getIsValidate(){
        return isValidate;
    }

    /**
     * 设置 是否需要身份验证,默认 true.
     *
     * @param isValidate
     *            the isValidate to set
     */
    public void setIsValidate(boolean isValidate){
        this.isValidate = isValidate;
    }
}
