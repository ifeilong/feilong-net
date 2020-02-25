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
package com.feilong.net.httpclient4;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtilUriSpaceTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtilUriSpaceTest.class);

    //---------------------------------------------------------------

    @Test
    public void test(){
        String uri = "https://www.baidu.com/item/BA8 900";
        HttpClientUtil.get(uri);

        LOGGER.info("get over");

    }
}
