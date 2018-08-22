package com.feilong.temp;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.UncheckedHttpException;
import com.feilong.net.ssl.SSLContextBuilder;
import com.feilong.net.ssl.SSLProtocol;

/**
 * Created by xianze.zxz on 2016/10/6.
 */
public class HttpHelperHttpClientImpl{

    /** The Constant log. */
    private static final Logger        LOGGER     = LoggerFactory.getLogger(HttpHelperHttpClientImpl.class);

    //创建http client
    private static CloseableHttpClient httpClient = createHttpsClient();

    public String readToString(String url,Map<String, String> heads){
        try{
            HttpEntity httpEntity = readToHttpEntity(url, heads);
            return EntityUtils.toString(httpEntity);
        }catch (ParseException | IOException e){
            throw new UncheckedHttpException(e);
        }
    }

    public HttpEntity readToHttpEntity(String url,Map<String, String> heads) throws ClientProtocolException,IOException{
        HttpGet httpGet = new HttpGet(url);
        if (isNotNullOrEmpty(heads)){
            Set<Map.Entry<String, String>> entries = heads.entrySet();
            for (Map.Entry<String, String> entry : entries){
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        return entity;
    }

    private static CloseableHttpClient createHttpsClient(){
        SSLConnectionSocketFactory sslsf = buildSSLConnectionSocketFactory();
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    private static SSLConnectionSocketFactory buildSSLConnectionSocketFactory(){
        SSLContext sslContext = SSLContextBuilder.build(SSLProtocol.TLS);
        return new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

}
