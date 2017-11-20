package com;

import java.util.Map;

/**
 * Created by xianze.zxz on 2016/10/6.
 */
public interface HttpHelper{

    /**
     * 根据url查询
     * 
     * @param url
     * @param heads
     *            请求头
     * @return
     */
    String readToString(String url,Map<String, String> heads);
}
