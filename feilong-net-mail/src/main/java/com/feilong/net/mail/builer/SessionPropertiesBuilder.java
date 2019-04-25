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
package com.feilong.net.mail.builer;

import static com.feilong.core.Validator.isNullOrEmpty;

import java.util.Properties;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.mail.entity.SessionConfig;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * The Class SessionPropertiesBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public class SessionPropertiesBuilder{

    /** Don't let anyone instantiate this class. */
    private SessionPropertiesBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Properties object. Used only if a new Session object is created.
     * 
     * <p>
     * It is expected that the client supplies values for the properties listed in Appendix A of the JavaMail spec <br>
     * (particularly
     * mail.store.protocol, mail.transport.protocol, mail.host, mail.user, and mail.from) as the defaults are unlikely to work in all cases.
     * </p>
     * 
     * @param sessionConfig
     *            the base config
     * @return the properties
     */
    public static Properties build(SessionConfig sessionConfig){
        String serverHost = sessionConfig.getServerHost();
        if (isNullOrEmpty(serverHost)){
            throw new IllegalArgumentException(
                            Slf4jUtil.format("serverHost can't be null/empty! sessionConfig:[{}]", JsonUtil.format(sessionConfig)));
        }

        //---------------------------------------------------------------
        String serverPort = sessionConfig.getServerPort();

        //---------------------------------------------------------------
        Properties properties = new Properties();

        properties.put("mail.smtp.host", serverHost);
        properties.put("mail.smtp.port", serverPort);
        properties.put("mail.smtp.auth", sessionConfig.getIsValidate() ? "true" : "false");

        //    Caused by: com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first
        //properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }
}
