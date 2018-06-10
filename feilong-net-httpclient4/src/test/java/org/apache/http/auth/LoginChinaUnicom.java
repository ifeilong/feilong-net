package org.apache.http.auth;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author amosli
 *         登录并抓取中国联通数据
 */

public class LoginChinaUnicom{

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        String name = "中国联通手机号码";
        String pwd = "手机服务密码";

        String url = "https://uac.10010.com/portal/Service/MallLogin?callback=jQuery17202691898950318097_1403425938090&redirectURL=http%3A%2F%2Fwww.10010.com&userName="
                        + name + "&password=" + pwd + "&pwdType=01&productType=01&redirectType=01&rememberMe=1";

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse loginResponse = httpClient.execute(httpGet);

        if (loginResponse.getStatusLine().getStatusCode() == 200){
            for (Header head : loginResponse.getAllHeaders()){
                System.out.println(head);
            }
            HttpEntity loginEntity = loginResponse.getEntity();
            String loginEntityContent = EntityUtils.toString(loginEntity);
            System.out.println("登录状态:" + loginEntityContent);
            //如果登录成功
            if (loginEntityContent.contains("resultCode:\"0000\"")){

                //月份
                String months[] = new String[] { "201401", "201402", "201403", "201404", "201405" };

                for (String month : months){
                    String billurl = "http://iservice.10010.com/ehallService/static/historyBiil/execute/YH102010002/QUERY_YH102010002.processData/QueryYH102010002_Data/"
                                    + month + "/undefined";

                    HttpPost httpPost = new HttpPost(billurl);
                    HttpResponse billresponse = httpClient.execute(httpPost);

                    if (billresponse.getStatusLine().getStatusCode() == 200){

                        ////
                        //  saveToLocal(billresponse.getEntity(), "chinaunicom.bill." + month + ".2.html");
                    }
                }
            }
        }

    }
}