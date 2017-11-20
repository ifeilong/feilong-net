package com;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

/**
 * Created by xianze.zxz on 2016/10/6.
 */
public class HttpHelperHttpClientImpl implements HttpHelper{

    /** The Constant log. */
    private static final Logger        LOGGER     = LoggerFactory.getLogger(HttpHelperHttpClientImpl.class);

    //创建http client
    private static CloseableHttpClient httpClient = createHttpsClient();

    @Override
    public String readToString(String url,Map<String, String> heads){
        try{
            HttpEntity httpEntity = readToHttpEntity(url, heads);
            return EntityUtils.toString(httpEntity);
        }catch (ParseException | IOException e){
            LOGGER.error("", e);
            throw new RuntimeException(e);
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

    public static CloseableHttpClient createHttpsClient(){
        X509TrustManager x509mgr = new X509TrustManager(){

            @Override
            public void checkClientTrusted(X509Certificate[] xcs,String string){
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs,String string){
            }

            @Override
            public X509Certificate[] getAcceptedIssuers(){
                return null;
            }
        };
        SSLConnectionSocketFactory sslsf = null;
        try{
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { x509mgr }, null);
            sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }catch (KeyManagementException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
}
