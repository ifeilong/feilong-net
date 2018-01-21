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
package com.feilong.net.filetransfer.ftp;

/**
 * The Class FTPFileTransferConfig.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.1
 */
public class FTPFileTransferConfig{

    /** The host name. */
    private String hostName;

    /** 用户名,默认 anonymous. */
    private String userName = "anonymous";

    /** 密码,默认 anonymous. */
    private String password = "anonymous";

    /**
     * 获得 the host name.
     *
     * @return the hostName
     */
    public String getHostName(){
        return hostName;
    }

    /**
     * 设置 the host name.
     *
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName){
        this.hostName = hostName;
    }

    /**
     * 获得 用户名,默认 anonymous.
     *
     * @return the userName
     */
    public String getUserName(){
        return userName;
    }

    /**
     * 设置 用户名,默认 anonymous.
     *
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * 获得 密码,默认 anonymous.
     *
     * @return the password
     */
    public String getPassword(){
        return password;
    }

    /**
     * 设置 密码,默认 anonymous.
     *
     * @param password
     *            the password to set
     */
    public void setPassword(String password){
        this.password = password;
    }

}
