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
package com.feilong.tools.mail.entity;

import com.feilong.tools.mail.ICalendar;

/**
 * 邮件发送配置.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.3
 */
public class MailSenderConfig extends BaseConfig{

    /** 是否需要回执, 默认不需要. */
    private boolean   isNeedReturnReceipt = false;

    // *******************************************************************
    /**
     * 邮件发送者的地址.<br>
     * example:huanyuansp@126.com
     */
    private String    fromAddress;

    // *******************************************************************

    /** 个人名义. */
    private String    personal            = "";

    // ***************************************************************

    /** 邮件多人接收地址. */
    private String[]  tos;

    /** 邮件多人接收地址(抄送). */
    private String[]  ccs;

    /** 邮件多人接收地址. */
    private String[]  bccs;

    // ***************************************************************
    /** 优先级. */
    private Priority  priority;

    // *******************************************************************

    /** 邮件主题. */
    private String    subject;

    /** 邮件的文本内容. */
    private String    content;

    /** MIME type of this object. */
    private String    contentMimeType     = "text/html; charset=gb2312";

    /**
     * 日历信息
     * 
     * @since 1.10.2
     */
    private ICalendar iCalendar;
    // ****************附件***************************************************

    /** 邮件附件的文件全路径, 比如 E:\Workspaces\baozun\BaozunSql\train\20150417Spring事务\ppt-contents.png. */
    private String[]  attachFilePaths;

    // ****************注意:这个参数 是 ...***************************************************
    /**
     * 设置 邮件附件的文件全路径, 比如 E:\Workspaces\baozun\BaozunSql\train\20150417Spring事务\ppt-contents.
     *
     * @param attachFilePaths
     *            the attachFilePaths to set
     */
    public void setAttachFilePaths(String...attachFilePaths){
        this.attachFilePaths = attachFilePaths;
    }

    //****************************************************************************

    /**
     * 获得 是否需要回执, 默认不需要.
     *
     * @return the isNeedReturnReceipt
     */
    public boolean getIsNeedReturnReceipt(){
        return isNeedReturnReceipt;
    }

    /**
     * 设置 是否需要回执, 默认不需要.
     *
     * @param isNeedReturnReceipt
     *            the isNeedReturnReceipt to set
     */
    public void setIsNeedReturnReceipt(boolean isNeedReturnReceipt){
        this.isNeedReturnReceipt = isNeedReturnReceipt;
    }

    /**
     * 获得 邮件发送者的地址.<br>
     * example:huanyuansp@126.com
     *
     * @return the fromAddress
     */
    public String getFromAddress(){
        return fromAddress;
    }

    /**
     * 设置 邮件发送者的地址.<br>
     * example:huanyuansp@126.com
     * 
     * @param fromAddress
     *            the fromAddress to set
     */
    public void setFromAddress(String fromAddress){
        this.fromAddress = fromAddress;
    }

    /**
     * 获得 个人名义.
     *
     * @return the personal
     */
    public String getPersonal(){
        return personal;
    }

    /**
     * 设置 个人名义.
     *
     * @param personal
     *            the personal to set
     */
    public void setPersonal(String personal){
        this.personal = personal;
    }

    /**
     * 获得 邮件多人接收地址.
     *
     * @return the tos
     */
    public String[] getTos(){
        return tos;
    }

    /**
     * 设置 邮件多人接收地址.
     *
     * @param tos
     *            the tos to set
     */
    public void setTos(String[] tos){
        this.tos = tos;
    }

    /**
     * 获得 邮件多人接收地址(抄送).
     *
     * @return the ccs
     */
    public String[] getCcs(){
        return ccs;
    }

    /**
     * 设置 邮件多人接收地址(抄送).
     *
     * @param ccs
     *            the ccs to set
     */
    public void setCcs(String[] ccs){
        this.ccs = ccs;
    }

    /**
     * 获得 邮件多人接收地址.
     *
     * @return the bccs
     */
    public String[] getBccs(){
        return bccs;
    }

    /**
     * 设置 邮件多人接收地址.
     *
     * @param bccs
     *            the bccs to set
     */
    public void setBccs(String[] bccs){
        this.bccs = bccs;
    }

    /**
     * 获得 优先级.
     *
     * @return the priority
     */
    public Priority getPriority(){
        return priority;
    }

    /**
     * 设置 优先级.
     *
     * @param priority
     *            the priority to set
     */
    public void setPriority(Priority priority){
        this.priority = priority;
    }

    /**
     * 获得 邮件主题.
     *
     * @return the subject
     */
    public String getSubject(){
        return subject;
    }

    /**
     * 设置 邮件主题.
     *
     * @param subject
     *            the subject to set
     */
    public void setSubject(String subject){
        this.subject = subject;
    }

    /**
     * 获得 邮件的文本内容.
     *
     * @return the content
     */
    public String getContent(){
        return content;
    }

    /**
     * 设置 邮件的文本内容.
     *
     * @param content
     *            the content to set
     */
    public void setContent(String content){
        this.content = content;
    }

    /**
     * 获得 mIME type of this object.
     *
     * @return the contentMimeType
     */
    public String getContentMimeType(){
        return contentMimeType;
    }

    /**
     * 设置 mIME type of this object.
     *
     * @param contentMimeType
     *            the contentMimeType to set
     */
    public void setContentMimeType(String contentMimeType){
        this.contentMimeType = contentMimeType;
    }

    /**
     * 获得 邮件附件的文件全路径, 比如 E:\Workspaces\baozun\BaozunSql\train\20150417Spring事务\ppt-contents.
     *
     * @return the attachFilePaths
     */
    public String[] getAttachFilePaths(){
        return attachFilePaths;
    }

    /**
     * @return the iCalendar
     */
    public ICalendar getiCalendar(){
        return iCalendar;
    }

    /**
     * @param iCalendar
     *            the iCalendar to set
     */
    public void setiCalendar(ICalendar iCalendar){
        this.iCalendar = iCalendar;
    }

}