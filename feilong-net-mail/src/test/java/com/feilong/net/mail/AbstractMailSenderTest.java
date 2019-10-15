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

import java.util.ResourceBundle;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.junit.After;
import org.junit.Before;

import com.feilong.core.bean.BeanUtil;
import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.io.FileUtil;
import com.feilong.net.mail.entity.MailSenderConfig;

public abstract class AbstractMailSenderTest{

    /** The Constant folder. */
    private static final String folder  = "/Users/feilong/Development/DataCommon/Files/Java/config/";

    //---------------------------------------------------------------

    /** The mail sender config. */
    protected MailSenderConfig  mailSenderConfig;

    static final String         toEmail = "xin.jin@baozun.com";

    //---------------------------------------------------------------

    /**
     * Before.
     */
    @Before
    public void before(){
        loadMailSenderConfig();
    }

    //---------------------------------------------------------------

    /**
     * Load mail sender config.
     *
     * @since 1.10.0
     */
    private void loadMailSenderConfig(){
        ResourceBundle resourceBundle = ResourceBundleUtil.getResourceBundle(FileUtil.getFileInputStream(folder + getConfigFile()));

        ArrayConverter arrayConverter = new ArrayConverter(String[].class, new StringConverter(), 2);
        char[] allowedChars = { '@' };
        arrayConverter.setAllowedChars(allowedChars);
        ConvertUtils.register(arrayConverter, String[].class);

        //---------------------------------------------------------------

        mailSenderConfig = new MailSenderConfig();
        mailSenderConfig = BeanUtil.populate(mailSenderConfig, ResourceBundleUtil.toMap(resourceBundle));

        //LOGGER.debug(JsonUtil.format(mailSenderConfig));
    }

    //---------------------------------------------------------------

    /**
     * Gets the config file.
     *
     * @return the config file
     */
    protected String getConfigFile(){
        return "mail-feilongtestemail.properties";
    }

    //---------------------------------------------------------------

    /**
     * After.
     */
    @After
    public void after(){
        MailSender mailSender = new DefaultMailSender();
        mailSender.sendMail(mailSenderConfig);
    }

}
