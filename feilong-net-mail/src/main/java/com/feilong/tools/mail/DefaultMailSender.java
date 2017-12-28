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
package com.feilong.tools.mail;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.io.FileUtil;
import com.feilong.io.FilenameUtil;
import com.feilong.io.entity.MimeType;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.tools.mail.entity.MailSenderConfig;
import com.feilong.tools.mail.exception.MailSenderException;

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
    private static final Logger LOGGER           = LoggerFactory.getLogger(DefaultMailSender.class);

    /** contentId前缀. */
    public static final String  PREFIX_CONTENTID = "image";

    /** The Constant CHARSET_PERSONAL. */
    private static final String CHARSET_PERSONAL = UTF8;

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
        Validate.notBlank(mailSenderConfig.getContent(), "mailSenderConfig.getContent() can't be null!");

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("mailSenderConfig:{}", JsonUtil.format(mailSenderConfig, new String[] { "attachList" }));
        }

        //---------------------------------------------------------------

        Message message = buildMessage(mailSenderConfig);
        setBody(message, mailSenderConfig);

        super.setDefaultCommandMap();
        send(message);
    }

    /**
     * @param message
     * @throws MessagingException
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

    /**
     * 构造Message.
     * 
     * <p>
     * 基于 mailSenderConfig 构建 通用的 MimeMessage,然后设置message公共属性(含发件人 收件人等信息)
     * </p>
     * 
     * @param mailSenderConfig
     * @return
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

    /**
     * setBody.
     *
     * @param message
     *            the message
     * @param mailSenderConfig
     *            the body
     * @throws MessagingException
     *             the messaging exception
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

    /**
     * 构造邮件内容.
     * 
     * @param mailSenderConfig
     * @return
     * @throws MessagingException
     * @since 1.10.2
     */
    static MimeMultipart buildContent(MailSenderConfig mailSenderConfig) throws MessagingException{

        //---------------------------------------------------------------
        // 以HTML格式发送邮件 (不带附件的邮件)

        // MiniMultipart类是一个容器类,包含MimeBodyPart类型的对象
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(buildHtmlContentBody(mailSenderConfig));

        //*********设置附件***************************************************************
        setAttachment(mimeMultipart, mailSenderConfig);
        return mimeMultipart;
    }

    /**
     * @param mailSenderConfig
     * @param mimeMultipart
     * @throws MessagingException
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

    /**
     * 设置附件.
     *
     * @param mimeMultipart
     *            the mime multipart
     * @param mailSenderConfig
     *            the mail sender config
     * @throws MessagingException
     *             the messaging exception
     * @since 1.1.1
     */
    private static void setAttachment(MimeMultipart mimeMultipart,MailSenderConfig mailSenderConfig) throws MessagingException{
        String[] attachFilePaths = mailSenderConfig.getAttachFilePaths();

        // html
        if (isNullOrEmpty(attachFilePaths)){
            return;// nothing to do
        }
        // ***************以HTML格式发送邮件 带附件的邮件图片********************************************************

        List<byte[]> attachList = newArrayList();
        List<String> attachFileNames = newArrayList();

        for (String attachFilePath : attachFilePaths){
            attachFileNames.add(FilenameUtil.getFileName(attachFilePath));
            attachList.add(FileUtil.toByteArray(new File(attachFilePath)));
        }

        // MiniMultipart类是一个容器类,包含MimeBodyPart类型的对象
        //---------------------------------------------------------------
        System.setProperty("mail.mime.encodefilename", "true");

        // 用于组合文本和图片,"related"型的MimeMultipart对象  
        mimeMultipart.setSubType("related");

        for (int i = 0, j = attachList.size(); i < j; i++){
            String cid = PREFIX_CONTENTID + i;
            MimeBodyPart mimeBodyPart = buildMimeBodyPart(attachList.get(i), attachFileNames.get(i), cid);

            // 将含有附件的BodyPart加入到MimeMultipart对象中
            mimeMultipart.addBodyPart(mimeBodyPart);
        }
    }

    /**
     * Builds the mime body part.
     *
     * @param data
     *            the data
     * @param fileName
     *            the file name
     * @param cid
     *            the cid
     * @return the mime body part
     * @throws MessagingException
     *             the messaging exception
     * @since 1.8.2
     */
    private static MimeBodyPart buildMimeBodyPart(byte[] data,String fileName,String cid) throws MessagingException{
        String mimeType = MimeType.BIN.getMime();

        // 新建一个存放附件的BodyPart
        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        mimeBodyPart.setDataHandler(buildDataHandler(data, mimeType));
        // 加上这句将作为附件发送,否则将作为信件的文本内容
        mimeBodyPart.setFileName(fileName);
        mimeBodyPart.setContentID(cid);
        return mimeBodyPart;
    }

    /**
     * Builds the data handler.
     *
     * @param data
     *            the data
     * @param mimeType
     *            the mime type
     * @return the data handler
     * @since 1.8.2
     */
    private static DataHandler buildDataHandler(byte[] data,String mimeType){
        ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(data, mimeType);
        return new DataHandler(byteArrayDataSource);
    }

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
        message.setFrom(toFromAddress(mailSenderConfig));

        // 设置邮件接受人群
        // 支持 to cc bcc
        setRecipients(message, mailSenderConfig);

        //---------------------------------------------------------------
        // 设置邮件消息的主题
        message.setSubject(mailSenderConfig.getSubject());

        //header信息
        setHeaders(message, mailSenderConfig);
    }

    /**
     * To from address.
     *
     * @param mailSenderConfig
     *            the mail sender config
     * @return the address
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     * @throws AddressException
     *             the address exception
     * @since 1.7.1
     */
    private static Address toFromAddress(MailSenderConfig mailSenderConfig) throws UnsupportedEncodingException,AddressException{
        // 设置邮件消息的发送者
        String personal = mailSenderConfig.getPersonal();
        String fromAddress = mailSenderConfig.getFromAddress();
        if (isNotNullOrEmpty(personal)){
            //the encoding to be used. Currently supported values are "B" and "Q". 
            //If this parameter is null, then the "Q" encoding is used if most of characters to be encoded are in the ASCII charset, 
            //otherwise "B" encoding is used.
            //B为base64方式
            String encoding = "b";
            String encodeText = MimeUtility.encodeText(personal, CHARSET_PERSONAL, encoding);
            return new InternetAddress(fromAddress, encodeText);
        }
        return new InternetAddress(fromAddress);
    }
}