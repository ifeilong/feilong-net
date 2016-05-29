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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.net.URLUtil;
import com.feilong.io.IOWriteUtil;

/**
 * The Class URLUtil.
 *
 * @author feilong
 * @since 1.5.1
 */
public final class URLDownloadUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(URLUtil.class);

    /** Don't let anyone instantiate this class. */
    private URLDownloadUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 将网络文件下载到文件夹.
     * 
     * <p>
     * 取到网络文件的文件名 原样下载到目标文件夹.
     * </p>
     *
     * @param urlString
     *            网络任意文件<br>
     *            url 不能带参数
     * @param directoryName
     *            目标文件夹
     * @throws IOException
     *             the IO exception
     * @see IOWriteUtil#write(InputStream, String, String)
     * 
     * @see org.apache.commons.io.FileUtils#copyURLToFile(URL, File)
     * @see org.apache.commons.io.FileUtils#copyURLToFile(URL, File, int, int)
     * 
     */
    public static void download(String urlString,String directoryName) throws IOException{
        Validate.notEmpty(urlString, "urlString can't be null/empty!");
        Validate.notEmpty(directoryName, "directoryName can't be null/empty!");

        LOGGER.info("begin download,urlString:[{}],directoryName:[{}]", urlString, directoryName);

        URL url = URLUtil.newURL(urlString);
        InputStream inputStream = url.openStream();

        File file = new File(urlString);
        String fileName = file.getName();

        IOWriteUtil.write(inputStream, directoryName, fileName);

        LOGGER.info("end download,url:[{}],directoryName:[{}]", urlString, directoryName);
    }
}
