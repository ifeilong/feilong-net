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
package com.feilong.net.mail;

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import org.junit.Test;

import com.feilong.velocity.VelocityUtil;

/**
 * The Class MailSenderTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.9
 */
public class HelloWorldMailSenderTest extends AbstractMailSenderTest{

    @Test
    public void helloWorld(){
        Map<String, Object> map = newHashMap();
        String parseTemplateWithClasspathResourceLoader = VelocityUtil.INSTANCE
                        .parseTemplateWithClasspathResourceLoader("hello world.vm", map);

        mailSenderConfig.setSubject("hello world");
        mailSenderConfig.setContent(parseTemplateWithClasspathResourceLoader);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.mail.BaseMailSenderTest#getConfigFile()
     */
    @Override
    protected String getConfigFile(){
        String folder = "/Users/feilong/Development/DataCommon/Files/Java/config";

        String file = "";
        // file ="/mail-adidas-test.properties";
        //file = "/mail-bztest-test.properties";
        file = "/mail-feilongtestemail.properties";

        return folder + file;
    }
}
