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
package com.feilong.net.jsoup;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.PartitionRunnableBuilder;
import com.feilong.core.lang.PartitionThreadEntity;
import com.feilong.core.lang.ThreadUtil;
import com.feilong.core.util.SortUtil;
import com.feilong.json.jsonlib.JsonUtil;

/**
 * The Class JsoupUtilTest.
 */
public class CopyrightTest{

    /** The Constant log. */
    private static final Logger LOGGER      = LoggerFactory.getLogger(CopyrightTest.class);

    private static final String selectQuery = ":containsOwn(©),:containsOwn(版权),:containsOwn(版權)";

    //---------------------------------------------------------------
    @Test
    public void test(){
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");

        List<String> list = toList(//
                        "http://www.nike.com.hk/",
                        "http://m.nike.com.hk/",

                        "https://www.microsoftstore.com.hk/",

                        "http://www.calvinklein.cn/",
                        "http://m.calvinklein.cn/",

                        "http://www.esprit.cn/",
                        "http://m.esprit.cn/",

                        "http://www.columbiasportswear.hk/",
                        "http://m.columbiasportswear.hk/",

                        "http://www.converse.com.cn/",
                        "http://m.converse.com.cn/",

                        "http://www.godiva.cn/",

                        "http://www.4009870870.com/",
                        //"http://m.4009870871.com/",

                        "http://www.shop.philips.com.cn/",
                        "http://m.shop.philips.com.cn/",

                        "http://china.coach.com/index.htm",
                        "http://m.china.coach.com/index.htm",

                        "https://www.samsungeshop.com.cn/",
                        //"",

                        "http://www.nbastore.cn/",
                        "http://www.nbastore.hk/",

                        "http://www.levi.com.cn/",
                        // "",

                        "http://www.speedo.com.cn",
                        "http://m.speedo.com.cn",

                        "http://cn.puma.com/",
                        "http://m-cn.puma.com/",

                        "https://www.michaelkors.cn/",
                        "",

                        "https://m.gowild.hk/",
                        "",

                        "https://www.underarmour.cn/",
                        "https://www.underarmour.hk",
                        "https://www.underarmour.tw",

                        "http://mall.bydauto.com.cn/",

                        "https://www.reebok.com.cn/",

                        "https://ufsstore.jumbomart.cn/",
                        "",

                        //                        "https://share.jumbomart.cn/",
                        "",

                        "http://www.gucci.cn/zh/",

                        "https://www.tommy.com.cn",
                        "",

                        "https://www.adidas.com.cn",
                        "",

                        "https://cn.iteshop.com",

                        "https://www.lancome.com.cn/",

                        "https://www.yslbeautycn.com/",

                        "https://www.zwilling.com.cn/",

                        "https://www.ralphlauren.cn/",

                        "https://hk.puma.com/",
                        //"http://alipay.baozun.com/",
                        "");

        final List<String> noList = toList(//
                        "http://www.calvinklein.cn/",
                        "http://cn.puma.com/",
                        "https://ufsstore.jumbomart.cn/",
                        "http://www.converse.com.cn/",
                        "http://m-cn.puma.com/",
                        "https://www.tommy.com.cn",
                        "http://m.converse.com.cn/",
                        "http://m.nike.com.hk/",
                        "http://www.levi.com.cn/");

        //---------------------------------------------------------------

        LOGGER.debug("no ©:{}", JsonUtil.format(noList));

        //---------------------------------------------------------------
        // Map<String, String> map = handle(list, noList);

        final Map<String, String> map = newLinkedHashMap();

        Date beginDate = new Date();

        ThreadUtil.execute(list, 5, new PartitionRunnableBuilder<String>(){

            @Override
            public Runnable build(final List<String> perBatchList,PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap){

                return new Runnable(){

                    @Override
                    public void run(){
                        map.putAll(handle(perBatchList, noList));
                    }
                };
            }
        });

        LOGGER.debug("[{}],use time: [{}]", JsonUtil.format(SortUtil.sortMapByValueAsc(map)), formatDuration(beginDate));
    }

    private Map<String, String> handle(List<String> list,List<String> noList){
        Map<String, String> map = newLinkedHashMap();

        for (String urlString : list){
            if (isNullOrEmpty(urlString)){
                continue;
            }

            if (noList.contains(urlString)){
                continue;
            }

            //---------------------------------------------------------------
            String result = message(urlString, selectQuery);
            map.put(urlString, result);
            //LOGGER.debug("[{}] ---> {}", StringUtils.rightPad(urlString, 35), result);
        }

        return map;
    }

    private static String message(String urlString,String selectQuery){
        try{
            Document document = JsoupUtil.getDocument(urlString);
            Element element = document.selectFirst(selectQuery);
            return isNullOrEmpty(element) ? "" : element.text().trim();
        }catch (Exception e){
            return e.getMessage();
        }
    }

}