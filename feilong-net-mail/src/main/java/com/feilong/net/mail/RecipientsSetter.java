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

import static com.feilong.core.Validator.isNotNullOrEmpty;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.feilong.net.mail.entity.MailSenderConfig;
import com.feilong.net.mail.util.InternetAddressUtil;

/**
 * The Class RecipientsSetter.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public final class RecipientsSetter{

    /**
     * 设置邮件接受人群.
     * 
     * <p>
     * 支持 to cc bcc.
     * </p>
     *
     * @param message
     *            the message
     * @param mailSenderConfig
     *            the new recipients
     * @throws MessagingException
     *             the messaging exception
     */
    public static void setRecipients(Message message,MailSenderConfig mailSenderConfig) throws MessagingException{
        // 创建邮件的接收者地址,并设置到邮件消息中
        // Message.RecipientType.TO属性表示接收者的类型为TO
        String[] tos = mailSenderConfig.getTos();
        if (isNotNullOrEmpty(tos)){
            message.setRecipients(Message.RecipientType.TO, InternetAddressUtil.toAddressArray(tos));
        }

        //---------------------------------------------------------------

        //cc 抄送
        String[] ccs = mailSenderConfig.getCcs();
        if (isNotNullOrEmpty(ccs)){
            message.setRecipients(Message.RecipientType.CC, InternetAddressUtil.toAddressArray(ccs));
        }

        //---------------------------------------------------------------

        //bcc 密送
        String[] bccs = mailSenderConfig.getBccs();
        if (isNotNullOrEmpty(bccs)){
            message.setRecipients(Message.RecipientType.BCC, InternetAddressUtil.toAddressArray(bccs));
        }
    }
}
