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
package com.feilong.net.jsoup.yitao;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.jsoup.JsoupUtil;
import com.feilong.net.jsoup.JsoupUtilException;

/**
 * The Class FeiLongYiTaoCrawler.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class YiTaoCrawler{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(YiTaoCrawler.class);

    /** The url. */
    String                      url    = "http://s.etao.com/search?q=439182-452&tbpm=t";

    /**
     * Test.
     *
     * @throws JsoupUtilException
     *             the jsoup util exception
     */
    @Test
    public void test() throws JsoupUtilException{
        // 数据
        Elements elements = JsoupUtil.getElementsBySelect(url, ".section");
        Element element = elements.get(0);
        // 列表数据
        Elements elements1 = elements.select(".listview");
        Element element1 = elements1.get(0);
        // 列表li
        Elements lis = element1.select(".listitem");
        for (Element li : lis){
            LOGGER.info("title:{}", li.select(".title a").get(0).text());
            LOGGER.info("price:{}", li.select(".price").get(0).text());
            LOGGER.info("freight:{}", li.select(".freight").get(0).text());
            LOGGER.info("url:{}", li.select(".title a").get(0).attr("href"));
            LOGGER.info("--------------------------------------------------");
        }
        // LOGGER.info(element.html());
        // LOGGER.info(element1.html());
    }
}
