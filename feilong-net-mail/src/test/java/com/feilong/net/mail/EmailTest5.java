package com.feilong.net.mail;

import java.util.UUID;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.junit.After;
import org.junit.Test;

import com.feilong.net.mail.SessionFactory;

public class EmailTest5 extends AbstractMailSenderTest{

    @Test
    public void send() throws Exception{

        mailSenderConfig.setContent("hello hahaha");

        String fromEmail = "feilongtestemail@163.com";
        String toEmail = "xin.jin@baozun.cn";
        Session session = SessionFactory.createSession(mailSenderConfig);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject("Outlook Meeting Request Using JavaMail");
        StringBuffer buffer = new StringBuffer();
        buffer.append(
                        "BEGIN:VCALENDAR\n" + "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" + "VERSION:2.0\n"
                                        + "METHOD:REQUEST\n" + "BEGIN:VEVENT\n" + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
                                        + toEmail + "\n" + "ORGANIZER:MAILTO:" + toEmail + "\n" + "DTSTART:20180302T060000Z\n"
                                        + "DTEND:20180302T070000Z\n" + "LOCATION:Conference room\n" + "UID:" + UUID.randomUUID().toString()
                                        + "\n"//如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。  
                                        + "CATEGORIES:SuccessCentral Reminder\n"
                                        + "DESCRIPTION:This the description of the meeting.<br>asd;flkjasdpfi\n\n"
                                        + "SUMMARY:Test meeting request\n" + "PRIORITY:5\n" + "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
                                        + "TRIGGER:-PT15M\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:Reminder\n" + "END:VALARM\n"
                                        + "END:VEVENT\n" + "END:VCALENDAR");
        BodyPart messageBodyPart = new MimeBodyPart();
        // 测试下来如果不这么转换的话，会以纯文本的形式发送过去，  
        //如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求  
        messageBodyPart.setDataHandler(
                        new DataHandler(new ByteArrayDataSource(buffer.toString(), "text/calendar;method=REQUEST;charset=\"UTF-8\"")));

        Multipart multipart = new MimeMultipart();

        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart bodyPart = new MimeBodyPart();
        // 设置HTML内容
        // 设置邮件消息的主要内容
        bodyPart.setContent(mailSenderConfig.getContent(), mailSenderConfig.getContentMimeType());
        multipart.addBodyPart(bodyPart);

        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    @Override
    @After
    public void after(){
    }
}