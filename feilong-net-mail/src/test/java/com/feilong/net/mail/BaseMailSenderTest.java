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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.BeanUtil;
import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.io.FileUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.mail.DefaultMailSender;
import com.feilong.net.mail.MailSender;
import com.feilong.net.mail.entity.MailSenderConfig;

/**
 * The Class MailSenderTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.9
 */
public abstract class BaseMailSenderTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMailSenderTest.class);

    /** The mail sender config. */
    protected MailSenderConfig  mailSenderConfig;

    /**
     * Before.
     */
    @Before
    public void before(){
        loadMailSenderConfig();
    }

    /**
     * 
     * @since 1.10.0
     */
    private void loadMailSenderConfig(){
        mailSenderConfig = new MailSenderConfig();
        ResourceBundle resourceBundle = ResourceBundleUtil.getResourceBundle(FileUtil.getFileInputStream(getConfigFile()));

        ArrayConverter arrayConverter = new ArrayConverter(String[].class, new StringConverter(), 2);
        char[] allowedChars = { '@' };
        arrayConverter.setAllowedChars(allowedChars);
        ConvertUtils.register(arrayConverter, String[].class);

        mailSenderConfig = BeanUtil.populate(mailSenderConfig, ResourceBundleUtil.toMap(resourceBundle));

        LOGGER.debug(JsonUtil.format(mailSenderConfig));
    }

    /**
     * After.
     */
    @After
    public void after(){
        MailSender mailSender = new DefaultMailSender();
        mailSender.sendMail(mailSenderConfig);
    }

    protected String getConfigFile(){
        return "/Users/feilong/Development/DataCommon/Files/Java/config/mail-feilongtestemail.properties";
    }
}
