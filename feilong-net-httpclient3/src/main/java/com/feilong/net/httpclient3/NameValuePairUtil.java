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
package com.feilong.net.httpclient3;

import static com.feilong.core.Validator.isNullOrEmpty;
import static java.util.Collections.emptyMap;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.NameValuePair;

/**
 * The Class NameValuePairUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.8
 */
final class NameValuePairUtil{

    /**
     * An empty immutable {@code NameValuePair} array.
     */
    private static final NameValuePair[] EMPTY_NAME_VALUE_PAIR_ARRAY = new NameValuePair[0];

    /** Don't let anyone instantiate this class. */
    private NameValuePairUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 从一个map转换成NameValuePair数组.
     *
     * @param params
     *            the params
     * @return 如果 (isNotNullOrEmpty(params)), 返回 {@link #EMPTY_NAME_VALUE_PAIR_ARRAY}
     */
    public static NameValuePair[] fromMap(Map<String, String> params){
        if (isNullOrEmpty(params)){
            return EMPTY_NAME_VALUE_PAIR_ARRAY;
        }

        NameValuePair[] nameValuePairs = new NameValuePair[params.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()){
            nameValuePairs[i] = new NameValuePair(entry.getKey(), entry.getValue());
            i++;
        }
        return nameValuePairs;
    }

    /**
     * 将nameValuePairs转成Map.
     *
     * @param nameValuePairs
     *            the name value pairs
     * @return 如果 (isNotNullOrEmpty(nameValuePairs)), 返回 Collections.emptyMap()
     * @since 1.0.9
     */
    public static Map<String, String> toMap(NameValuePair[] nameValuePairs){
        if (isNullOrEmpty(nameValuePairs)){
            return emptyMap();
        }
        Map<String, String> map = new TreeMap<>();
        for (NameValuePair nameValuePair : nameValuePairs){
            map.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        return map;
    }
}
