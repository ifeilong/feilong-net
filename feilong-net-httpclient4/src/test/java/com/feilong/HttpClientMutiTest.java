package com.feilong;

import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.net.httpclient4.HttpClientUtil.getResponseStatusCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.lang.ThreadUtil;
import com.feilong.core.lang.thread.PartitionPerHandler;
import com.feilong.core.lang.thread.PartitionThreadEntity;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.test.AbstractTest;

/**
 * @Author: zgd
 * @Date: 2019/8/15 00:04
 * @Description: 测试httpclient
 */
public class HttpClientMutiTest extends AbstractTest{

    final int j = 400;

    @Test
    public void run(){
        //System.setProperty("javax.net.debug", "all");
        Date beginDate = new Date();

        final ConnectionConfig conconfig = new ConnectionConfig(2000, 4000);

        List<String> list = build();

        final String string = "=======================[{}],{}    code:{}===============================";
        ThreadUtil.execute(list, 1, new PartitionPerHandler<String>(){

            @Override
            public void handle(List<String> perBatchList,PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap){
                for (String url : perBatchList){
                    LOGGER.info(string, 0, url, getResponseStatusCode(url, conconfig));
                }
            }
        });
        LOGGER.info("use time: [{}]", formatDuration(beginDate));

    }

    private List<String> build(){
        List<String> list = newArrayList();
        for (int i = 0; i < j; i++){
            String url = "";
            if (i > 200){
                url = "https://www.baidu.com";
            }else{
                url = "https://www.hao123.com";
            }
            list.add(url);
        }
        return list;
    }

}