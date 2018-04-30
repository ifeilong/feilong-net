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
package com.feilong.net.httpclient3.builder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;

/**
 * 构造 {@link HttpClient}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public class HttpClientBuilder{

    /** Don't let anyone instantiate this class. */
    private HttpClientBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     * 
     * <p>
     * 默认使用的是 {@link SimpleHttpConnectionManager}
     * </p>
     *
     * @return the http client
     * 
     * @see SimpleHttpConnectionManager
     * @see MultiThreadedHttpConnectionManager
     */
    static HttpClient build(){
        return buildDefault();
    }

    //---------------------------------------------------------------

    static HttpClient buildMulti(){
        //TODO 研究下  MultiThreadedHttpConnectionManager
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

        //        connectionManager.setDefaultMaxConnectionsPerHost(32); // 2
        //        connectionManager.setMaxTotalConnections(128); // 20

        return new HttpClient(connectionManager);
    }

    static HttpClient buildDefault(){
        return new HttpClient();
    }
}
