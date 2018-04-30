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

import static com.feilong.core.Validator.isNotNullOrEmpty;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 构造 {@link PostMethod}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class PostMethodBuilder{

    /** Don't let anyone instantiate this class. */
    private PostMethodBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the post method.
     *
     * @param uri
     *            the uri
     * @param nameValuePairs
     *            the name value pairs
     * @return the http method
     * @since 1.5.4
     */
    static HttpMethod buildPostMethod(String uri,NameValuePair[] nameValuePairs){
        PostMethod postMethod = new PostMethod(uri);

        //---------------------------------------------------------------

        if (isNotNullOrEmpty(nameValuePairs)){
            postMethod.setRequestBody(nameValuePairs);
        }
        return postMethod;
    }
}
