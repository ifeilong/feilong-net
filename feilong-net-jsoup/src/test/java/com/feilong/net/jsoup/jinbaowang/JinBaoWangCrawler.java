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
package com.feilong.net.jsoup.jinbaowang;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.DatePattern.TIMESTAMP;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.core.date.DateUtil;
import com.feilong.core.lang.StringUtil;
import com.feilong.io.IOReaderUtil;
import com.feilong.net.jsoup.JsoupUtil;
import com.feilong.net.jsoup.JsoupUtilException;

/**
 * 进包网.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @deprecated
 */
@Deprecated
public class JinBaoWangCrawler{

    /** The Constant LOGGER. */
    private static final Logger LOGGER     = LoggerFactory.getLogger(JinBaoWangCrawler.class);

    /** The search page. */
    private static String       searchPage = "http://www.jinbaowang.cn/gallery--n,%s-grid.html";

    /**
     * 将map 转成文件.
     * 
     * @param skuCodeAndImagesMap
     *            the sku code and images map
     * @param directoryName
     *            the directory name
     */
    public static void convertSkuCodeImagesToFile(Map<String, List<String>> skuCodeAndImagesMap,String directoryName){
        String columnTitles[] = { "code", "image" };
        List<Object[]> dataList = newArrayList();
        for (Map.Entry<String, List<String>> entry : skuCodeAndImagesMap.entrySet()){
            String code = entry.getKey();
            List<String> images = entry.getValue();
            if (isNotNullOrEmpty(images)){
                for (String image : images){
                    dataList.add(toArray(code, image));
                }
            }
        }
        Date now = new Date();
        String timestamp = DateUtil.toString(now, TIMESTAMP);
        //        try{
        //            CSVUtil.write(directoryName + "/" + timestamp + ".csv", columnTitles, dataList);
        //            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(directoryName + "/" + timestamp + ".obj"));
        //            out.writeObject(skuCodeAndImagesMap);
        //            out.close();
        //        }catch (FileNotFoundException e){
        //            throw new UncheckedIOException(e);
        //        }catch (IOException e){
        //            throw new UncheckedIOException(e);
        //        }
    }

    /**
     * 反序列化.
     * 
     * @param objPath
     *            the obj path
     * @return the map
     */
    public static Map<String, List<String>> convertObjectToMap(String objPath){
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(objPath));
            @SuppressWarnings("unchecked")
            Map<String, List<String>> skuCodeAndImagesMap = (Map<String, List<String>>) in.readObject();
            in.close();
            return skuCodeAndImagesMap;
        }catch (FileNotFoundException e){
            LOGGER.error(e.getClass().getName(), e);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }catch (ClassNotFoundException e){
            LOGGER.error(e.getClass().getName(), e);
        }
        return null;
    }

    /**
     * 解析商家编码文件.
     *
     * @param filePath
     *            文件路径
     * @return the code list
     * @throws IOException
     *             the IO exception
     */
    public static List<String> getCodeList(String filePath) throws IOException{
        String content = IOReaderUtil.readFileToString(filePath, UTF8);
        // 将内容以换行符转成数组
        String[] codeRows = StringUtil.split(content, "\r\n");
        if (isNotNullOrEmpty(codeRows)){
            List<String> codeList = newArrayList();
            for (String codeRow : codeRows){
                if (!codeRow.equals("商家编码") && isNotNullOrEmpty(codeRow)){
                    codeList.add(codeRow);
                }
            }
            return codeList;
        }
        return null;
    }

    /**
     * 根据codes 获得 skuCodeAndImages.
     * 
     * @param codes
     *            codes
     * @return skuCodeAndImages
     */
    public static Map<String, List<String>> getSkuCodeAndImagesMap(List<String> codes){
        Validate.notEmpty(codes, "codes can't be null/empty!");
        Map<String, List<String>> skuCodeAndImages = newHashMap();
        List<String> list = null;
        String skuDetailsRealUrl = null;
        for (String code : codes){
            skuDetailsRealUrl = getSkuDetailsRealUrl(code);
            if (isNotNullOrEmpty(skuDetailsRealUrl)){
                list = getSkuDetailsImages(skuDetailsRealUrl);
            }
            skuCodeAndImages.put(code, list);
        }
        return skuCodeAndImages;
    }

    /**
     * 通过code 商家编码 ,获得 在进包网上面的 真实的 商品详细页面url.
     * 
     * @param code
     *            商家编码
     * @return 在进包网上面的 真实的 商品详细页面url
     */
    public static String getSkuDetailsRealUrl(String code){
        String urlString = StringUtil.format(searchPage, code);
        try{
            Document document = JsoupUtil.getDocument(urlString);
            if (null != document){
                String query = ".items-gallery .goodinfo h6 a";
                Elements elements = document.select(query);
                for (Element element : elements){
                    String title = element.attr("title");
                    if (title.equals(code)){
                        return element.attr("href");
                    }
                }
            }
            LOGGER.error(code + " cannot be find!");
        }catch (JsoupUtilException e){
            LOGGER.error(e.getClass().getName(), e);
        }
        return null;
    }

    /**
     * 下载sku 的 内容介绍图片<br>
     * .
     * 
     * @param skuDetailsRealUrl
     *            sku 真实路径
     * @return the sku details images
     */
    public static List<String> getSkuDetailsImages(String skuDetailsRealUrl){
        try{
            Document document = JsoupUtil.getDocument(skuDetailsRealUrl);
            if (null != document){
                Elements elements = document.select("#goods-intro img");
                List<String> list = newArrayList();
                for (Element element : elements){
                    String url = element.attr("src");
                    list.add(url);
                }
                return list;
            }
            LOGGER.error(skuDetailsRealUrl + " cannot be find!");
        }catch (JsoupUtilException e){
            LOGGER.error(e.getClass().getName(), e);
        }
        return null;
    }

    /**
     * 自动抓取该详细页面所有的图片 下载到 directoryName/skuCode 目录下面.
     *
     * @param skuCodeAndImagesMap
     *            the sku code and images map
     * @param directoryName
     *            the directory name
     * @throws IOException
     *             the IO exception
     */
    public static void downSkuImages(Map<String, List<String>> skuCodeAndImagesMap,String directoryName) throws IOException{
        // 目标文件夹
        //        String destination = null;
        //        List<String> images = null;
        //        String skuCode = null;
        //        LOGGER.info("begin down...");
        //        for (Entry<String, List<String>> skuCodeAndImages : skuCodeAndImagesMap.entrySet()){
        //            skuCode = skuCodeAndImages.getKey();
        //            images = skuCodeAndImages.getValue();
        //            if (isNotNullOrEmpty(images)){
        //                destination = directoryName + "/" + skuCode;
        //                LOGGER.info("begin down sku:" + skuCode);
        //                for (String image : images){
        //                    URLDownloadUtil.download(image, destination);
        //                }
        //                LOGGER.info(skuCode + " down over");
        //            }
        //        }
        //        LOGGER.info("down over~~耶");
    }
}
