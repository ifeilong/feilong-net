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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.coreextension.entity.BackWarnEntity;
import com.feilong.io.IOWriteUtil;
import com.feilong.json.jsonlib.JsonUtil;

/**
 * The Class HttpClientUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class HttpClientUtilTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtilTest.class);

    @Test
    public void getHttpMethodWithExecute1(){
        String uri = "http://127.0.0.1:6666/template.txt?sign=123456";
        uri = "http://sitemanager.underarmour.tw/brand-uastory.css";

        String responseBodyAsString = HttpClientUtil.get(uri);
        LOGGER.debug("responseBodyAsString:{}", responseBodyAsString);
    }

    @Test
    public void getHttpMethodWithExecute122(){
        String uri = "http://localhost:8222/json1";

        String responseBodyAsString = HttpClientUtil.get(uri);
        LOGGER.debug("responseBodyAsString:{}", responseBodyAsString);

        BackWarnEntity bean = JsonUtil.toBean(responseBodyAsString, BackWarnEntity.class);
        LOGGER.debug("BackWarnEntity:{}", bean);
    }

    @Test
    public void getHttpMethodWithExecute(){
        String uri = "http://www.google.com.hk/search?client=aff-cs-360se&forid=1&ie=utf-8&oe=UTF-8&q=enumeration";
        uri = "http://www.d9cn.org/d9cnbook/50/50537/10967924.html";
        uri = "http://www.kenwen.com/egbk/31/31186/4395342.txt";
        uri = "http://pandavip.www.net.cn/cgi-bin/Check.cgi?queryType=0&domain=feihe&big5=n&sign=2&url=www.net.cn&com=yes&cn=no&mobi=no&net=no&comcn=no&image.x=19&image.y=10";
        // HttpMethodParams params = new HttpMethodParams();
        // params.setParameter(HttpMethodParams.USER_AGENT, "");
        // httpMethod.setParams(params);

        String responseBodyAsString = HttpClientUtil.get(uri);

        LOGGER.debug("responseBodyAsString:{}", responseBodyAsString);
        LOGGER.debug("print httpMethod.getResponseHeaders()=======================");
    }

    @Test
    public void testFund(){
        String uri = "http://www.howbuy.com/fund/ajax/board/index.htm?glrm=&keyword=&radio=2&orderField=jjjz&orderType=asc&cat=All&level=";
        Map<String, String> params = newHashMap();

        String responseBodyAsString = HttpClientUtil.post(uri, params);
        String replace = responseBodyAsString.replace("<textarea style=\"display:none\">", "").replace("</textarea>", "")
                        .replace("</tbody>", "").replace("</table>", "").replace("onclick=\"move(this);\"", "")
                        .replace("href=\"/fund/", "href=\"http://www.howbuy.com/fund/")
                        .replaceAll("<input  type=\"checkbox\"  value=\"\\d{6}\"/>", "")//
                        .replaceAll("<td width=\"4%\"></td>", "")//
                        .replaceAll("<td width=\"5%\">\\d+</td>", "")//
        ;
        //^(\\d{3,4}-)?\\d{6,8}$
        StringBuilder sb = new StringBuilder();
        sb.append(replace).append("</tbody>").append("</table>");

        String filePath = "d:/1.html";
        IOWriteUtil.writeStringToFile(filePath, sb.toString(), UTF8);
        DesktopUtil.open(filePath);
    }

    /**
     * Testenclosing_type.
     */
    @Test
    public void testenclosing_type(){
        String aString = "22222<input  type=\"checkbox\"  value=\"161015\"/>3333";
        LOGGER.debug(aString.replaceAll("<input  type=\"checkbox\"  value=\"\\d{6}\"/>", ""));
        //assertEquals(expected, actual);
    }
}
