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

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.UnsupportedEncodingException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.mail.entity.MailSenderConfig;
import com.feilong.net.mail.exception.MailSenderException;
import com.feilong.net.mail.util.InternetAddressUtil;

/**
 * 邮件发送器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.mail.Message
 * @see javax.mail.Session
 * @see javax.mail.Transport#send(Message)
 * @see "org.springframework.mail.MailSender"
 * @see "org.springframework.mail.javamail.JavaMailSenderImpl"
 * @since 1.0.0
 */
public final class DefaultMailSender extends AbstractMailSender{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMailSender.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.mail.MailSender#sendMail(com.feilong.tools.mail.entity.MailSenderConfig)
     */
    @Override
    public void sendMail(MailSenderConfig mailSenderConfig){
        Validate.notNull(mailSenderConfig, "mailSenderConfig can't be null!");

        Validate.notBlank(mailSenderConfig.getUserName(), "mailSenderConfig.getUserName() can't be null!");
        Validate.notBlank(mailSenderConfig.getPassword(), "mailSenderConfig.getPassword() can't be null!");
        Validate.notEmpty(mailSenderConfig.getTos(), "mailSenderConfig.getTos() can't be null!");
        Validate.notBlank(mailSenderConfig.getFromAddress(), "mailSenderConfig.getFromAddress() can't be null!");
        Validate.notBlank(mailSenderConfig.getSubject(), "mailSenderConfig.getSubject() can't be null!");

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("mailSenderConfig:{}", JsonUtil.format(mailSenderConfig, new String[] { "attachList" }));
        }

        //---------------------------------------------------------------

        //since 1.13.0
        if (null == mailSenderConfig.getContent()){
            mailSenderConfig.setContent(EMPTY);
        }

        //---------------------------------------------------------------
        Message message = buildMessage(mailSenderConfig);
        setBody(message, mailSenderConfig);

        super.setDefaultCommandMap();
        send(message);
    }

    //---------------------------------------------------------------

    /**
     * Send.
     *
     * @param message
     *            the message
     * @since 1.10.2
     */
    static void send(Message message){
        try{
            // 发送邮件
            Transport.send(message);
        }catch (MessagingException e){
            throw new MailSenderException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 构造Message.
     * 
     * <p>
     * 基于 mailSenderConfig 构建 通用的 MimeMessage,然后设置message公共属性(含发件人 收件人等信息)
     * </p>
     *
     * @param mailSenderConfig
     *            the mail sender config
     * @return the message
     * @since 1.10.2
     */
    static Message buildMessage(MailSenderConfig mailSenderConfig){
        // 根据session创建一个邮件消息

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = SessionFactory.createSession(mailSenderConfig);

        // 根据session创建一个邮件消息
        Message message = new MimeMessage(session);

        try{
            setMessageAttribute(message, mailSenderConfig);
        }catch (UnsupportedEncodingException | MessagingException e){
            LOGGER.error("", e);
            throw new MailSenderException(e);
        }

        return message;
    }

    //---------------------------------------------------------------

    /**
     * setBody.
     *
     * @param message
     *            the message
     * @param mailSenderConfig
     *            the body
     */
    private static void setBody(Message message,MailSenderConfig mailSenderConfig){
        try{
            // if txt
            // message.setText(mailContent);
            MimeMultipart mimeMultipart = buildContent(mailSenderConfig);

            // 将MiniMultipart对象设置为邮件内容
            message.setContent(mimeMultipart);
        }catch (MessagingException e){
            LOGGER.error("", e);
            throw new MailSenderException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 构造邮件内容.
     *
     * @param mailSenderConfig
     *            the mail sender config
     * @return the mime multipart
     * @throws MessagingException
     *             the messaging exception
     * @since 1.10.2
     */
    static MimeMultipart buildContent(MailSenderConfig mailSenderConfig) throws MessagingException{
        // 以HTML格式发送邮件 (不带附件的邮件)

        // MiniMultipart类是一个容器类,包含MimeBodyPart类型的对象
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(buildHtmlContentBody(mailSenderConfig));

        //------------设置附件---------------------------------------------------
        AttachmentSetter.setAttachment(mimeMultipart, mailSenderConfig.getAttachFilePaths());
        return mimeMultipart;
    }

    //---------------------------------------------------------------

    /**
     * Builds the html content body.
     *
     * @param mailSenderConfig
     *            the mail sender config
     * @return the body part
     * @throws MessagingException
     *             the messaging exception
     * @since 1.10.2
     */
    static BodyPart buildHtmlContentBody(MailSenderConfig mailSenderConfig) throws MessagingException{
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart bodyPart = new MimeBodyPart();
        // 设置HTML内容
        // 设置邮件消息的主要内容
        bodyPart.setContent(mailSenderConfig.getContent(), mailSenderConfig.getContentMimeType());

        return bodyPart;
    }

    //---------------------------------------------------------------

    /**
     * 设置message公共属性.
     * 
     * <h3>公共属性</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>设置发送人</li>
     * <li>设置邮件接受人群(to cc bcc)</li>
     * <li>设置邮件消息的主题</li>
     * <li>header信息</li>
     * </ol>
     * </blockquote>
     *
     * @param message
     *            the message
     * @param mailSenderConfig
     *            属性
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     * @throws MessagingException
     *             the messaging exception
     * @see #setRecipients(Message, MailSenderConfig)
     * @see #setHeaders(Message, MailSenderConfig)
     */
    private static void setMessageAttribute(Message message,MailSenderConfig mailSenderConfig)
                    throws UnsupportedEncodingException,MessagingException{
        message.setFrom(InternetAddressUtil.buildFromAddress(mailSenderConfig.getPersonal(), mailSenderConfig.getFromAddress()));

        // 设置邮件接受人群
        // 支持 to cc bcc
        RecipientsSetter.setRecipients(message, mailSenderConfig);

        //---------------------------------------------------------------
        // 设置邮件消息的主题
        message.setSubject(mailSenderConfig.getSubject());

        //header信息
        setHeaders(message, mailSenderConfig);
    }
}