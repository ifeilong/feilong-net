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
package com.feilong.net.httpclient4.builder;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

/**
 * The Class CredentialsProviderBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class CredentialsProviderBuilder{

    /** Don't let anyone instantiate this class. */
    private CredentialsProviderBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the credentials provider.
     *
     * @param targetHost
     *            the target host
     * @param userName
     *            the user name
     * @param password
     *            the password
     * @return the credentials provider
     */
    public static CredentialsProvider build(HttpHost targetHost,String userName,String password){
        AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
        return build(authScope, userName, password);
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param authScope
     *            the auth scope
     * @param userName
     *            the user name
     * @param password
     *            the password
     * @return the credentials provider
     */
    public static CredentialsProvider build(AuthScope authScope,String userName,String password){
        return build(authScope, new UsernamePasswordCredentials(userName, password));
    }

    /**
     * Builds the.
     *
     * @param authScope
     *            the auth scope
     * @param credentials
     *            the credentials
     * @return the credentials provider
     */
    public static CredentialsProvider build(AuthScope authScope,UsernamePasswordCredentials credentials){
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(authScope, credentials);
        return credentialsProvider;
    }

}
