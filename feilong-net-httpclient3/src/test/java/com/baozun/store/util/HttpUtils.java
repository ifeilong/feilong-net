package com.baozun.store.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP工具类
 * 
 * @author lixiangyang
 * 
 */
public class HttpUtils{

    private static final Logger                       LOGGER                       = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 定义编码格式 UTF-8
     */
    public static final String                        URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";

    private static final String                       URL_PARAM_CONNECT_FLAG       = "&";

    private static final String                       EMPTY                        = "";

    private static MultiThreadedHttpConnectionManager connectionManager            = null;

    private static int                                connectionTimeOut            = 25000;

    private static int                                socketTimeOut                = 25000;

    private static int                                maxConnectionPerHost         = 20;

    private static int                                maxTotalConnections          = 20;

    private static final int                          BUFFER_SIZE                  = 4096;

    private static final String                       CONTENTTYPE_KEY              = "Content-Type";

    private static final String                       CONTENTTYPE_VALUE            = "application/x-www-form-urlencoded;charset=";

    private static final String                       CONTENTTYPE_JSON             = "application/json";

    private static HttpClient                         client;

    static{
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
    }

    public static String doJsonPost(String url,String json){
        String response = EMPTY;
        PostMethod postMethod = null;
        try{
            postMethod = new PostMethod(url);
            // 将表单的值放入postMethod中
            StringRequestEntity entity = new StringRequestEntity(json, CONTENTTYPE_JSON, URL_PARAM_DECODECHARSET_UTF8);
            postMethod.setRequestEntity(entity);
            // 执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK){
                response = postMethod.getResponseBodyAsString();
            }else{
                LOGGER.error("响应状态码 = " + postMethod.getStatusCode());
            }
        }catch (HttpException e){
            LOGGER.error("{}", e);
        }catch (IOException e){
            LOGGER.error("{}", e);
        }finally{
            if (postMethod != null){
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
        return response;
    }

    /**
     * POST方式提交数据
     * 
     * @param url
     *            待请求的URL
     * @param params
     *            要提交的数据
     * @param enc
     *            编码
     * @return 响应结果
     * @throws IOException
     *             IO异常
     */
    public static String URLPost(String url,Map<String, String> params,String enc){

        String response = EMPTY;
        PostMethod postMethod = null;
        try{
            postMethod = new PostMethod(url);
            postMethod.setRequestHeader(CONTENTTYPE_KEY, CONTENTTYPE_VALUE + HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
            // 将表单的值放入postMethod中
            Set<String> keySet = params.keySet();
            for (String key : keySet){
                String value = params.get(key);
                postMethod.addParameter(key, value);
            }
            // 执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK){
                response = postMethod.getResponseBodyAsString();
            }else{
                LOGGER.error("响应状态码 = " + postMethod.getStatusCode());
            }
        }catch (HttpException e){
            LOGGER.error("{}", e);
        }catch (IOException e){
            LOGGER.error("{}", e);
        }finally{
            if (postMethod != null){
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return response;
    }

    /**
     * GET方式提交数据
     * 
     * @param url
     *            待请求的URL
     * @param params
     *            要提交的数据
     * @param enc
     *            编码
     * @return 响应结果
     * @throws IOException
     *             IO异常
     */
    public static String URLGet(String url,Map<String, String> params,String enc){

        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(EMPTY);

        if (strtTotalURL.indexOf("?") == -1){
            strtTotalURL.append(url).append("?").append(getUrl(params, enc));
        }else{
            strtTotalURL.append(url).append("&").append(getUrl(params, enc));
        }
        LOGGER.debug("GET请求URL = \n" + strtTotalURL.toString());

        try{
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader(CONTENTTYPE_KEY, CONTENTTYPE_VALUE + enc);
            // 执行getMethod
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK){
                response = getMethod.getResponseBodyAsString();
            }else{
                LOGGER.debug("响应状态码 = " + getMethod.getStatusCode());
            }
        }catch (HttpException e){
            LOGGER.error("{}", e);
        }catch (IOException e){
            LOGGER.error("{}", e);
        }finally{
            if (getMethod != null){
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    /**
     * GET方式提交数据
     * 
     * @param url
     *            待请求的URL
     * @param params
     *            要提交的数据
     * @param enc
     *            编码
     * @return 响应结果
     * @throws IOException
     *             IO异常
     */
    public static String doGetReq(String url){

        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(url);

        LOGGER.debug("GET请求URL = \n" + strtTotalURL.toString());

        try{
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader(CONTENTTYPE_KEY, CONTENTTYPE_VALUE + URL_PARAM_DECODECHARSET_UTF8);
            // 执行getMethod
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK){
                InputStream in = getMethod.getResponseBodyAsStream();
                try{
                    response = InputStreamTOString(in);
                }catch (Exception e){
                    LOGGER.error(e.toString());
                    LOGGER.error("{}", e);
                }
            }else{
                LOGGER.debug("响应状态码 = " + getMethod.getStatusCode());
            }
        }catch (HttpException e){
            LOGGER.error("{}", e);
        }catch (IOException e){
            LOGGER.error("{}", e);
        }finally{
            if (getMethod != null){
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    public static String InputStreamTOString(InputStream in) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

    public static String doGetReq(String url,String enc){

        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(url);

        LOGGER.debug("GET请求URL = \n" + strtTotalURL.toString());

        try{
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader(CONTENTTYPE_KEY, CONTENTTYPE_VALUE + enc);
            // 执行getMethod
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK){
                response = getMethod.getResponseBodyAsString();
            }else{
                LOGGER.debug("响应状态码 = " + getMethod.getStatusCode());
            }
        }catch (HttpException e){
            LOGGER.error("{}", e);
        }catch (IOException e){
            LOGGER.error("{}", e);
        }finally{
            if (getMethod != null){
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    /**
     * 据Map生成URL字符串
     * 
     * @param map
     *            Map
     * @param valueEnc
     *            URL编码
     * @return URL
     */
    private static String getUrl(Map<String, String> map,String valueEnc){

        if (null == map || map.keySet().isEmpty()){
            return EMPTY;
        }
        StringBuffer url = new StringBuffer();
        Set<String> keys = map.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext();){
            String key = it.next();
            if (map.containsKey(key)){
                String val = map.get(key);
                String str = val != null ? val : EMPTY;
                try{
                    str = URLEncoder.encode(str, valueEnc);
                }catch (UnsupportedEncodingException e){
                    LOGGER.error("{}", e);
                }
                url.append(key).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
            }
        }
        String strURL = EMPTY;
        strURL = url.toString();
        if (URL_PARAM_CONNECT_FLAG.equals(EMPTY + strURL.charAt(strURL.length() - 1))){
            strURL = strURL.substring(0, strURL.length() - 1);
        }

        return strURL;
    }
}
