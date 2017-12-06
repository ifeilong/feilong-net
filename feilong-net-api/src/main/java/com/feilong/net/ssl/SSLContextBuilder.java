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
package com.feilong.net.ssl;

import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.feilong.net.UncheckedHttpException;

/**
 * The Class SSLContextBuilder.
 * 
 * <p>
 * SSL的英文全称是“Secure Sockets Layer”，中文名为“安全套接层协议层 ”，它是网景（Netscape）公司提出的基于 WEB 应用的安全协议。
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class SSLContextBuilder{

    /** Don't let anyone instantiate this class. */
    private SSLContextBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param protocol
     *            the protocol
     * @return the SSL context
     * @see SSLProtocol
     */
    public static SSLContext build(String protocol){
        try{
            SSLContext sslContext = SSLContext.getInstance(defaultIfNullOrEmpty(protocol, SSLProtocol.TLS));

            sslContext.init(null, new TrustManager[] { TrustAnyTrustManager.INSTANCE }, null);

            return sslContext;
        }catch (Exception e){
            throw new UncheckedHttpException(e);
        }
    }
}
