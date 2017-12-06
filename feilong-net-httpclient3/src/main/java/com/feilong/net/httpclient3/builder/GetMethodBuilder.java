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
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class GetMethodBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(GetMethodBuilder.class);

    /**
     * Builds the get method.
     *
     * @param uri
     *            the uri
     * @param nameValuePairs
     *            the name value pairs
     * @return the http method
     * @since 1.5.4
     */
    static HttpMethod buildGetMethod(String uri,NameValuePair[] nameValuePairs){
        //TODO 暂时还不支持 uri中含有参数且  nameValuePairs也有值的情况
        if (isNotNullOrEmpty(nameValuePairs) && StringUtils.contains(uri, "?")){
            throw new NotImplementedException("not implemented!");
        }

        GetMethod getMethod = new GetMethod(uri);
        if (isNotNullOrEmpty(nameValuePairs)){
            getMethod.setQueryString(nameValuePairs);
        }
        return getMethod;
    }
}
