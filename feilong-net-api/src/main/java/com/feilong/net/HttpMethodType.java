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
package com.feilong.net;

import com.feilong.core.lang.EnumUtil;

/**
 * http请求方法,目前仅支持通用的get和post 其他不支持.
 * 
 * <h3>get && post区别:</h3>
 * 
 * <blockquote>
 * 
 * <h4>本质区别:</h4>
 * 
 * <p>
 * GET的语义是请求获取指定的资源。<br>
 * GET方法是安全、幂等、可缓存的（除非有 Cache-Control Header的约束）,GET方法的报文主体没有任何语义。
 * </p>
 * 
 * <p>
 * POST的语义是根据请求负荷（报文主体）对指定的资源做出处理，具体的处理方式视资源类型而不同。<br>
 * POST不安全，不幂等，（大部分实现）不可缓存。
 * </p>
 * 
 * <p>
 * 举一个通俗栗子吧，在微博这个场景里，GET的语义会被用在「看看我的Timeline上最新的20条微博」这样的场景，而POST的语义会被用在「发微博、评论、点赞」这样的场景中。
 * </p>
 * 
 * <h4>表现形式:</h4>
 * 
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">get</th>
 * <th align="left">post</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>后退按钮/刷新</td>
 * <td>无害</td>
 * <td>数据会被重新提交（浏览器应该告知用户数据会被重新提交)</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>行为</td>
 * <td>Get是用来从服务器上获得数据，</td>
 * <td>而Post是用来向服务器上传递数据</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>书签</td>
 * <td>可收藏为书签</td>
 * <td>不可收藏为书签</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>缓存</td>
 * <td>能被缓存</td>
 * <td>不能缓存</td>
 * </tr>
 * 
 * <tr valign="top" >
 * <td>编码类型</td>
 * <td>application/x-www-form-urlencoded</td>
 * <td>application/x-www-form-urlencoded 或multipart/form-data。为二进制数据使用多重编码</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>历史</td>
 * <td>参数保留在浏览器历史中</td>
 * <td>参数不会保存在浏览器历史中</td>
 * </tr>
 * 
 * <tr valign="top" >
 * <td>对数据长度的限制</td>
 * <td>当发送数据时，<s>GET URL 的长度是受限制的（URL 的最大长度是 2048个字符)</s><br>
 * <span style="color:red">HTTP协议并没有限制URI的长度，具体的长度是由浏览器和系统来约束的</span></td>
 * <td>无限制</td>
 * </tr>
 * <tr valign="top"style="background-color:#eeeeff">
 * <td>对数据类型的限制</td>
 * <td>只允许 ASCII 字符</td>
 * <td>没有限制。也允许二进制数据</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>可见性</td>
 * <td>GET的数据在 URL 中对所有人都是可见的</td>
 * <td>POST的数据不会显示在 URL 中</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>安全性</td>
 * <td>与 POST 相比，GET 的安全性较差，所发送的数据是 URL 的一部分。<br>
 * 在发送密码或其他敏感信息时绝不要使用 GET ！<br>
 * 
 * <p>
 * 因为在传输过程，如今现有的很多服务器、代理服务器或者用户代理都会将请求URL记录到日志文件中，然后放在某个地方，<br>
 * 这样就可能会有一些隐私的信息被第三方看到。
 * </p>
 * 
 * <p>
 * 另外，用户也可以在浏览器上直接看到提交的数据，一些系统内部消息将会一同显示在用户面前。
 * </p>
 * Post的所有操作对用户来说都是不可见的。</td>
 * <td>POST 比 GET更安全，因为参数不会被保存在浏览器历史或 web 服务器日志中</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "org.springframework.http.HttpMethod"
 * @see <a href="http://www.w3school.com.cn/tags/html_ref_httpmethods.asp">HTTP 方法：GET 对比 POST</a>
 * @since 1.7.3 move from feilong-core
 */
public enum HttpMethodType{

    /** get方式. */
    GET("get"),

    /** post方式. */
    POST("post");

    //---------------------------------------------------------------

    /** The method. */
    private String method;

    //---------------------------------------------------------------

    /**
     * Instantiates a new http method type.
     * 
     * @param method
     *            the method
     */
    private HttpMethodType(String method){
        this.method = method;
    }

    /**
     * Gets the method.
     * 
     * @return the method
     */
    public String getMethod(){
        return method;
    }

    //---------------------------------------------------------------

    /**
     * 传入一个 字符串的 HTTP method,比如 get,得到 {@link HttpMethodType#GET}.
     *
     * @param methodValue
     *            the method value
     * @return the by method value ignore case
     * @see EnumUtil#getEnumByPropertyValueIgnoreCase(Class, String, Object)
     * @since 1.0.8
     */
    public static HttpMethodType getByMethodValueIgnoreCase(String methodValue){
        String propertyName = "method";
        return EnumUtil.getEnumByPropertyValueIgnoreCase(HttpMethodType.class, propertyName, methodValue);
    }
}
