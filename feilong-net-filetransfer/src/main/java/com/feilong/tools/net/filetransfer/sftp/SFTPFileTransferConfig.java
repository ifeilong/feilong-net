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
package com.feilong.tools.net.filetransfer.sftp;

import java.util.Properties;

/**
 * The Class SFTPFileTransferConfig.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.1
 */
public class SFTPFileTransferConfig{

    /** 主机名. */
    private String     hostName;

    /** 用户名. */
    private String     userName;

    /** 密码. */
    private String     password;

    /** The port. */
    private Integer    port           = 22;

    /** The ssh config. */
    private Properties sshConfig;

    /** The connect timeout. */
    private int        sessionTimeout = 0;

    /**
     * 获得 主机名.
     *
     * @return the hostName
     */
    public String getHostName(){
        return hostName;
    }

    /**
     * 设置 主机名.
     *
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName){
        this.hostName = hostName;
    }

    /**
     * 获得 用户名.
     *
     * @return the userName
     */
    public String getUserName(){
        return userName;
    }

    /**
     * 设置 用户名.
     *
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * 获得 密码.
     *
     * @return the password
     */
    public String getPassword(){
        return password;
    }

    /**
     * 设置 密码.
     *
     * @param password
     *            the password to set
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * 获得 the port.
     *
     * @return the port
     */
    public Integer getPort(){
        return port;
    }

    /**
     * 设置 the port.
     *
     * @param port
     *            the port to set
     */
    public void setPort(Integer port){
        this.port = port;
    }

    /**
     * 获得 the ssh config.
     *
     * @return the sshConfig
     */
    public Properties getSshConfig(){
        return sshConfig;
    }

    /**
     * 设置 the ssh config.
     *
     * @param sshConfig
     *            the sshConfig to set
     */
    public void setSshConfig(Properties sshConfig){
        this.sshConfig = sshConfig;
    }

    /**
     * 获得 the connect timeout.
     *
     * @return the sessionTimeout
     */
    public int getSessionTimeout(){
        return sessionTimeout;
    }

    /**
     * 设置 the connect timeout.
     *
     * @param sessionTimeout
     *            the sessionTimeout to set
     */
    public void setSessionTimeout(int sessionTimeout){
        this.sessionTimeout = sessionTimeout;
    }

}
