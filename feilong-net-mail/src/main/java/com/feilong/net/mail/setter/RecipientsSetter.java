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
package com.feilong.net.mail.setter;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.net.mail.entity.MailSenderConfig;
import com.feilong.net.mail.util.InternetAddressUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

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
     */
    public static void setRecipients(Message message,MailSenderConfig mailSenderConfig){
        // 创建邮件的接收者地址,并设置到邮件消息中
        // Message.RecipientType.TO属性表示接收者的类型为TO
        set(message, Message.RecipientType.TO, mailSenderConfig.getTos());
        //cc 抄送
        set(message, Message.RecipientType.CC, mailSenderConfig.getCcs());
        //bcc 密送
        set(message, Message.RecipientType.BCC, mailSenderConfig.getBccs());
    }

    //---------------------------------------------------------------

    /**
     * 设置.
     *
     * @param message
     *            the message
     * @param recipientType
     *            the recipient type
     * @param addressArray
     *            the address array
     */
    private static void set(Message message,RecipientType recipientType,String[] addressArray){
        try{
            if (isNotNullOrEmpty(addressArray)){
                message.setRecipients(recipientType, InternetAddressUtil.toAddressArray(addressArray));
            }
        }catch (MessagingException e){
            //since 1.13.2
            throw new DefaultRuntimeException(Slf4jUtil.format("addressArray:[{}],recipientType:[{}]", addressArray, recipientType), e);
        }

    }
}
