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
package com.feilong.net;

import static com.feilong.core.CharsetType.GBK;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.net.HttpMethodType.POST;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;

/**
 * The Class URLConnectionUtilTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class BuyTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyTest.class);

    @Test
    public void getResponseBodyAsString2(){
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setContentCharset(GBK);

        //        "requestFullURL": "http://10.88.54.164:8081/shoppingcart/add",
        //        "request.getMethod": "POST",
        //        "parameterMap":         {
        //            "count": ["1"],
        //            "skuId": ["1198"]

        HttpRequest request = new HttpRequest();
        request.setUri("http://10.88.54.164:8081/shoppingcart/add");
        request.setHttpMethodType(POST);
        request.setParamMap(toMap("skuId", "1198", "count", "1"));

        String responseBodyAsString = URLConnectionUtil.getResponseBodyAsString(request, connectionConfig);
        LOGGER.debug(responseBodyAsString);
    }

}
