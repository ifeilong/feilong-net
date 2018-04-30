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
package com.feilong.net.httpclient3.builder;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.net.entity.HttpRequest.DEFAULT_USER_AGENT;
import static org.apache.commons.httpclient.params.HttpMethodParams.RETRY_HANDLER;
import static org.apache.commons.httpclient.params.HttpMethodParams.USER_AGENT;

import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.httpclient3.HttpClientConfig;

/**
 * The Class HttpMethodUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public final class HttpMethodUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpMethodUtil.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private HttpMethodUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Package and.
     *
     * @param httpClientConfig
     *            the http client config
     * @return the http method
     * @since 1.5.4
     */
    public static HttpMethod buildHttpMethod(HttpClientConfig httpClientConfig){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("[httpClientConfig]:{}", JsonUtil.format(httpClientConfig));
        }

        //---------------------------------------------------------------
        HttpMethod httpMethod = HttpMethodBuilder
                        .build(httpClientConfig.getUri(), httpClientConfig.getParamMap(), httpClientConfig.getHttpMethodType());

        //---------------------------------------------------------------

        packHttpMethodParams(httpMethod);

        return executeMethod(httpMethod, httpClientConfig);
    }

    //---------------------------------------------------------------

    /**
     * @param httpMethod
     */
    private static void packHttpMethodParams(HttpMethod httpMethod){
        HttpMethodParams httpMethodParams = httpMethod.getParams();
        // TODO
        httpMethodParams.setParameter(USER_AGENT, DEFAULT_USER_AGENT);

        // 使用系统提供的默认的恢复策略
        httpMethodParams.setParameter(RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        //httpMethod.getParams().setContentCharset(charSet);
    }

    //---------------------------------------------------------------

    /**
     * (底层方法)use httpState to create httpmethod.
     *
     * @param httpMethod
     *            the http method
     * @param httpClientConfig
     *            the http client config
     * @return the http method
     */
    private static HttpMethod executeMethod(HttpMethod httpMethod,HttpClientConfig httpClientConfig){
        HttpClient httpClient = HttpClientBuilder.build();

        //---------------------------------------------------------------
        // 认证
        setAuthentication(httpMethod, httpClientConfig, httpClient);

        // 代理
        setProxy(httpClientConfig, httpClient);

        try{
            if (LOGGER.isDebugEnabled()){
                // String[] excludes = new String[] { "defaults" };
                // HttpClientParams httpClientParams = httpClient.getParams();
                //
                // LOGGER.debug("[httpClient.getParams()]:{}", JsonUtil.format(httpClientParams, excludes));
                //
                // HttpMethodParams httpMethodParams = httpMethod.getParams();
                // LOGGER.debug("[httpMethod.getParams()]:{}", JsonUtil.format(httpMethodParams, excludes));

                Map<String, Object> map = HttpMethodRequestLogMapBuilder.build(httpMethod);
                String[] excludes = new String[] { "values", "elements"
                        // "rawAuthority",
                        // "rawCurrentHierPath",
                        // "rawPath",
                        // "rawPathQuery",
                        // "rawQuery",
                        // "rawScheme",
                        // "rawURI",
                        // "rawURIReference",
                        // "rawUserinfo",
                        // "rawFragment",
                        // "rawHost",
                        // "rawName",
                        // "protocol",
                        // "defaults",
                        // "class"
                };
                LOGGER.debug(JsonUtil.format(map, excludes));
            }

            // 执行该方法后服务器返回的状态码
            // 该状态码能表示出该方法执行是否成功、需要认证或者页面发生了跳转 (默认状态下GetMethod的实例是自动处理跳转的)
            int statusCode = httpClient.executeMethod(httpMethod);
            if (statusCode != HttpStatus.SC_OK){
                LOGGER.warn("statusCode is:[{}]", statusCode);
            }

        }catch (Exception e){
            //SSL证书过期
            //PKIX path validation failed: java.security.cert.CertPathValidatorException: timestamp check failed
            Map<String, Object> map = HttpMethodResponseLogMapBuilder.build(httpMethod, httpClientConfig);
            LOGGER.error(e.getClass().getName() + " HttpMethodResponseAttributeMapForLog:" + JsonUtil.format(map), e);
            throw new UncheckedHttpException(e);
        }
        return httpMethod;
    }

    //---------------------------------------------------------------

    /**
     * 设置 proxy.
     * 
     * @param httpClientConfig
     *            the http client config
     * @param httpClient
     *            the http client
     */
    // TODO未测试
    private static void setProxy(HttpClientConfig httpClientConfig,HttpClient httpClient){
        // 设置代理
        String hostName = httpClientConfig.getProxyAddress();
        if (isNotNullOrEmpty(hostName)){
            HostConfiguration hostConfiguration = httpClient.getHostConfiguration();
            hostConfiguration.setProxy(hostName, httpClientConfig.getProxyPort());
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 authentication.
     * 
     * @param httpMethod
     *            the http method
     * @param httpClientConfig
     *            the http client config
     * @param httpClient
     *            the http client
     */
    private static void setAuthentication(HttpMethod httpMethod,HttpClientConfig httpClientConfig,HttpClient httpClient){
        UsernamePasswordCredentials usernamePasswordCredentials = httpClientConfig.getUsernamePasswordCredentials();
        // 设置认证
        if (isNotNullOrEmpty(usernamePasswordCredentials)){
            httpMethod.setDoAuthentication(true);

            AuthScope authScope = AuthScope.ANY;
            Credentials credentials = usernamePasswordCredentials;

            httpClient.getState().setCredentials(authScope, credentials);

            // 1.1抢先认证(Preemptive Authentication)
            // 在这种模式时,HttpClient会主动将basic认证应答信息传给服务器,即使在某种情况下服务器可能返回认证失败的应答,
            // 这样做主要是为了减少连接的建立.

            HttpClientParams httpClientParams = new HttpClientParams();
            httpClientParams.setAuthenticationPreemptive(true);
            httpClient.setParams(httpClientParams);
        }
    }

}
