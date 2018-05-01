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
public class HttpClientFirstlogisticsTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientFirstlogisticsTest.class);

    @Test
    public void getFLLogisticsTrack(){
        String uri = "http://firstlogistics.co.id/ws/demo/post/";

        Map<String, String> params = newHashMap();

        params.put("APPID", "EBDEMO01");
        params.put("ACCOUNT", "1300000430");
        params.put("FUNCTION", "track");
        params.put("REF", "MPE100503");

        uri = "http://117.102.249.96/ws/mpe/";

        uri = "http://117.102.249.96/ws/ecom/";
        params = newHashMap();
        params.put("APPID", "MP4PP01");
        params.put("ACCOUNT", "1300000430");
        params.put("FUNCTION", "track");
        params.put("REF", "81131531");

        // method = POST
        // Account = 1300000430
        // AppID = MP4PP01
        // Function = track
        //
        // And you can use this AWB to test, it's a real AWB 81129754

        // MPE100503 - SHIPPED
        // MPE100501 - PICKED UP
        // MPE100500 - FAILED
        // MPE100498 - DELIVERED

        LOGGER.debug(HttpClientUtil.post(uri, params));
    }
}
