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

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.feilong.net.mail.entity.SessionConfig;

/**
 * {@link javax.mail.Session}
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.9
 */
class SessionFactory{

    /** Don't let anyone instantiate this class. */
    private SessionFactory(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 根据邮件会话属性和密码验证器构造一个发送邮件的session.
     * 
     * @param sessionConfig
     *            mailSenderInfo
     * @return Session
     * @see javax.mail.Session#getDefaultInstance(Properties, Authenticator)
     */
    public static Session createSession(SessionConfig sessionConfig){
        Authenticator authenticator = buildAuthenticator(sessionConfig);
        Properties properties = buildProperties(sessionConfig);

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getDefaultInstance(properties, authenticator);
        session.setDebug(sessionConfig.getIsDebug());
        return session;
    }

    //---------------------------------------------------------------

    /**
     * 判断是否需要身份认证,如果需要身份认证,则创建一个密码验证器.
     *
     * @param sessionConfig
     *            the base config
     * @return the authenticator
     * @see javax.mail.PasswordAuthentication#PasswordAuthentication(String, String)
     * @since 1.5.3
     */
    private static Authenticator buildAuthenticator(final SessionConfig sessionConfig){
        // 判断是否需要身份认证
        if (sessionConfig.getIsValidate()){
            return new Authenticator(){// 如果需要身份认证,则创建一个密码验证器

                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(sessionConfig.getUserName(), sessionConfig.getPassword());
                }
            };
        }
        return null;
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
     * @since 1.5.3
     */
    private static Properties buildProperties(SessionConfig sessionConfig){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", sessionConfig.getMailServerHost());
        properties.put("mail.smtp.port", sessionConfig.getMailServerPort());
        properties.put("mail.smtp.auth", sessionConfig.getIsValidate() ? "true" : "false");

        //    Caused by: com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first
        //properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }
}
