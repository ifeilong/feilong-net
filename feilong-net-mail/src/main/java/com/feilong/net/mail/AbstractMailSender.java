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

import java.util.Date;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.feilong.net.mail.entity.MailSenderConfig;
import com.feilong.net.mail.entity.Priority;

/**
 * 邮件发送器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.mail.Message
 * @see javax.mail.Session
 * @see javax.mail.Transport#send(javax.mail.Message)
 * @see "org.springframework.mail.MailSender"
 * @see "org.springframework.mail.javamail.JavaMailSenderImpl"
 * @since 1.1.1
 */
public abstract class AbstractMailSender implements MailSender{

    /**
     * 设置 default command map.
     * <p>
     * 解决 bug javax.activation.UnsupportedDataTypeException: no object DCH for MIME type multipart/related;
     * </p>
     */
    protected static void setDefaultCommandMap(){
        MailcapCommandMap mailcapCommandMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mailcapCommandMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mailcapCommandMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mailcapCommandMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mailcapCommandMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mailcapCommandMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mailcapCommandMap);
    }

    //---------------------------------------------------------------

    /**
     * 设置 header 信息.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>邮件的优先级</li>
     * <li>是否需要回执</li>
     * <li>邮件客户端</li>
     * <li>邮件消息发送的时间</li>
     * </ol>
     * </blockquote>
     *
     * @param message
     *            the message
     * @param mailSenderConfig
     *            the headers
     * @throws MessagingException
     *             the messaging exception
     * @see javax.mail.Part#addHeader(String, String)
     * @see javax.mail.Part#setHeader(String, String)
     */
    protected static void setHeaders(Message message,MailSenderConfig mailSenderConfig) throws MessagingException{
        // 邮件的优先级
        Priority priority = mailSenderConfig.getPriority();
        if (null != priority){
            message.addHeader(MailHeader.X_PRIORITY, priority.getLevelValue());
        }

        //---------------------------------------------------------------
        // 是否需要回执
        if (mailSenderConfig.getIsNeedReturnReceipt()){
            message.setHeader(MailHeader.DISPOSITION_NOTIFICATION_TO, "1");
        }
        // 邮件客户端
        message.setHeader(MailHeader.X_MAILER, MailHeader.X_MAILER_VALUE);

        // 设置邮件消息发送的时间
        message.setSentDate(new Date());
    }

}