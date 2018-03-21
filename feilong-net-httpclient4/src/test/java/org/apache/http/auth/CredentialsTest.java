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
package org.apache.http.auth;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.Test;

public class CredentialsTest{

    @Test
    public void testHttpClientUtilTest12(){
        //任何用户认证的过程，都需要一系列的凭证来确定用户的身份。最简单的用户凭证可以是用户名和密码这种形式。UsernamePasswordCredentials这个类可以用来表示这种情况，这种凭据包含明文的用户名和密码。
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user", "pwd");
        System.out.println(creds.getUserPrincipal().getName());
        System.out.println(creds.getPassword());

        //NTCredentials是微软的windows系统使用的一种凭据，包含username、password，还包括一系列其他的属性，比如用户所在的域名。在Microsoft Windows的网络环境中，同一个用户可以属于不同的域，所以他也就有不同的凭据。
        NTCredentials creds1 = new NTCredentials("user", "pwd", "workstation", "domain");
        System.out.println(creds1.getUserPrincipal().getName());
        System.out.println(creds1.getPassword());
    }

    @Test
    public void testCredentialsTest(){
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope("somehost", AuthScope.ANY_PORT), new UsernamePasswordCredentials("u1", "p1"));
        credentialsProvider.setCredentials(new AuthScope("somehost", 8080), new UsernamePasswordCredentials("u2", "p2"));
        credentialsProvider.setCredentials(
                        new AuthScope("otherhost", 8080, AuthScope.ANY_REALM, "ntlm"),
                        new UsernamePasswordCredentials("u3", "p3"));

        System.out.println(credentialsProvider.getCredentials(new AuthScope("somehost", 80, "realm", "basic")));
        System.out.println(credentialsProvider.getCredentials(new AuthScope("somehost", 8080, "realm", "basic")));
        System.out.println(credentialsProvider.getCredentials(new AuthScope("otherhost", 8080, "realm", "basic")));
        System.out.println(credentialsProvider.getCredentials(new AuthScope("otherhost", 8080, null, "ntlm")));
    }
}
