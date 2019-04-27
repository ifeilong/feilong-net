package com.feilong.net.mail;

import java.io.IOException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.junit.After;
import org.junit.Test;

import com.feilong.core.CharsetType;
import com.feilong.core.date.DateUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.net.mail.builer.MessageBuilder;
import com.feilong.net.mail.entity.MailSenderConfig;
import com.feilong.net.mail.ics.ICalendarBuilder;
import com.feilong.net.mail.ics.IcsBuilder;
import com.feilong.net.mail.util.MessageSendUtil;
import com.feilong.net.mail.util.MimeType;

public class Email1 extends AbstractMailSenderTest{

    @Test
    public void send() throws Exception{
        mailSenderConfig.setiCalendar(ICalendarBuilder.build());

        Message message = MessageBuilder.build(mailSenderConfig);

        if (null != mailSenderConfig.getiCalendar()){
            message.setContent(buildContent(mailSenderConfig));
        }
        MessageSendUtil.send(message);
    }

    static Multipart buildContent(MailSenderConfig mailSenderConfig) throws MessagingException,IOException{
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(build(mailSenderConfig));
        return multipart;
    }

    static BodyPart build(MailSenderConfig mailSenderConfig) throws MessagingException,IOException{
        String ics = IcsBuilder.buildIcs(mailSenderConfig);

        IOWriteUtil.writeStringToFile("/Users/feilong/feilong/email/" + DateUtil.getTime(new Date()) + ".ics", ics, CharsetType.UTF8);

        //测试下来如果不这么转换的话，会以纯文本的形式发送过去，  
        //如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求  

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(ics, MimeType.TYPE_ICS)));

        return messageBodyPart;
    }

    //---------------------------------------------------------------

    @Override
    protected String getConfigFile(){
        return "mail-baozun.properties";
    }

    //---------------------------------------------------------------

    @Override
    @After
    public void after(){
    }
}