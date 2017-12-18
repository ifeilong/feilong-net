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

import static com.feilong.io.entity.FileType.FILE;
import static org.apache.commons.lang3.SystemUtils.USER_HOME;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;
import com.feilong.io.entity.FileInfoEntity;
import com.feilong.tools.velocity.VelocityUtil;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.bean.ConvertUtil.toList;

/**
 * The Class MailSenderTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.9
 */
public class MailSenderTest extends BaseMailSenderTest{

    /**
     * Send mail1.
     *
     * @throws IOException
     *             the IO exception
     */
    @Test
    public void sendMail1() throws IOException{
        String path = USER_HOME + "/feilong/train/1201单元测试/generalRegulation/generalRegulation-20141125194610.html";
        String textContent = IOReaderUtil.readFileToString(path, UTF8);
        mailSenderConfig.setContent(textContent);
    }

    /**
     * Send mail.
     */
    @Test
    public void sendMail(){
        String textContent = "<html><body><hr/><div style='boder:1px #000 solid;color:red'>今天天气不错</div></body></html>";
        mailSenderConfig.setContent(textContent);
    }

    /**
     * Test send text mail.
     */
    @Test
    public void testSendTextMail(){
        String textContent = "测试回执";
        mailSenderConfig.setContent(textContent);
    }

    /**
     * Send mail with attach.
     */
    @Test
    public void sendMailWithAttach(){
        String templateInClassPath = "velocity/mailtest.vm";
        // ******************************************************************************************
        FileInfoEntity fileInfoEntity = new FileInfoEntity();

        fileInfoEntity.setFileType(FILE);
        fileInfoEntity.setLastModified(new Date().getTime());
        fileInfoEntity.setName("nikestore_china_cancel20130910.csv");
        fileInfoEntity.setSize(25655L);
        // ******************************************************************************************
        FileInfoEntity fileInfoEntity2 = new FileInfoEntity();

        fileInfoEntity2.setFileType(FILE);
        fileInfoEntity2.setLastModified(new Date().getTime());
        fileInfoEntity2.setName("nikestore_china_revenue20131022.csv");
        fileInfoEntity2.setSize(25655L);
        // ******************************************************************************************
        FileInfoEntity fileInfoEntity3 = new FileInfoEntity();
        fileInfoEntity3.setFileType(FILE);
        fileInfoEntity3.setLastModified(new Date().getTime());
        fileInfoEntity3.setName("nikestore_china_return20131022.csv");
        fileInfoEntity3.setSize(25655L);
        // ******************************************************************************************
        FileInfoEntity fileInfoEntity4 = new FileInfoEntity();
        fileInfoEntity4.setFileType(FILE);
        fileInfoEntity4.setLastModified(new Date().getTime());
        fileInfoEntity4.setName("nikestore_china_demand20130910.csv");
        fileInfoEntity4.setSize(25655L);
        // ******************************************************************************************
        List<FileInfoEntity> fileInfoEntityList = toList(fileInfoEntity, fileInfoEntity2, fileInfoEntity3, fileInfoEntity4);

        // ******************************************************************************************
        Map<String, Object> contextKeyValues = new HashMap<>();
        contextKeyValues.put("PREFIX_CONTENTID", DefaultMailSender.PREFIX_CONTENTID);
        contextKeyValues.put("fileInfoEntityList", fileInfoEntityList);
        // ******************************************************************************************

        String textContent = new VelocityUtil().parseTemplateWithClasspathResourceLoader(templateInClassPath, contextKeyValues);
        mailSenderConfig.setContent(textContent);

        String fileString = "E:\\DataFixed\\Material\\avatar\\飞龙.png";
        fileString = "E:\\Workspaces\\baozun\\BaozunSql\\train\\20150417Spring事务\\ppt-contents.png";
        mailSenderConfig.setAttachFilePaths(fileString);
    }

}
