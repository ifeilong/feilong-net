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
package com.feilong.net.handler;

import java.net.HttpURLConnection;

import com.feilong.net.entity.ConnectionConfig;

/**
 * 超时时间设置
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class TimeoutPacker{

    /** Don't let anyone instantiate this class. */
    private TimeoutPacker(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 一定要为HttpUrlConnection设置connectTimeout属性以防止连接被阻塞.
     * 
     * @param httpURLConnection
     * @param useConnectionConfig
     * @since 1.10.4
     */
    public static void packer(HttpURLConnection httpURLConnection,ConnectionConfig useConnectionConfig){
        //一定要为HttpUrlConnection设置connectTimeout属性以防止连接被阻塞
        httpURLConnection.setConnectTimeout(useConnectionConfig.getConnectTimeout());
        httpURLConnection.setReadTimeout(useConnectionConfig.getReadTimeout());
    }
}
