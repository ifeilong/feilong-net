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

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class HttpClientUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class HttpClientTcashApiTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientTcashApiTest.class);
    //---------------------------------------------------------------

    @Test
    public void checkTcashTransaction(){
        //      String uri = "http://202.3.208.89:11080/tcash-api/api/check/customer/transaction?refNum=290914112053368&userKey=testing&passKey=1234&signKey=1234";
        String uri = "http://202.3.208.89:11080/tcash-api/api/check/customer/transaction";
        Map<String, String> params = newHashMap();

        params.put("terminalId", "8021");
        params.put("refNum", "290914112053368");
        params.put("userKey", "testing");
        params.put("passKey", "1234");
        params.put("signKey", "1234");

        LOGGER.debug(HttpClientUtil.get(uri, params));
    }
}
