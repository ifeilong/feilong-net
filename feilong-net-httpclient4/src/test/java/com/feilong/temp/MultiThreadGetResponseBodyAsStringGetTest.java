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
package com.feilong.temp;

import static com.feilong.core.bean.ConvertUtil.toMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.ThreadUtil;
import com.feilong.net.httpclient4.HttpClientUtil;

public class MultiThreadGetResponseBodyAsStringGetTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadGetResponseBodyAsStringGetTest.class);

    @Test
    public void testGetResponseBodyAsString11(){
        ThreadUtil.execute(new Runnable(){

            @Override
            public void run(){
                String uri = "http://127.0.0.1:8084?name=jinxin&age=18";
                LOGGER.debug(HttpClientUtil.get(uri, toMap("country", "china")));

            }
        }, 20);
    }

}
