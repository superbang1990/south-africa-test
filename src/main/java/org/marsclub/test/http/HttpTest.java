package org.marsclub.test.http;

import com.alibaba.fastjson2.JSONObject;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class HttpTest {

    public static void post() throws Exception {
        // 1. 创建HttpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2. 创建HttpPost实例
        HttpPost httpPost = new HttpPost("https://iodin.chery.co.za/api/sales/integration/downloadOrderInvoiceFile");
        httpPost.setHeader("From", "Y");
        httpPost.setHeader("tenant-id", "570272200");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(new StringEntity("570272200"));
        // 3. 调用HttpClient实例来执行HttpPost实例
        CloseableHttpResponse response = httpclient.execute(httpPost);
        // 4. 读 response
        int status = response.getCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            System.out.println(html);
        } else {
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            System.out.println(html);
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        // 5. 释放连接
        response.close();
        httpclient.close();
    }

    public static void main(String[] args) throws Exception {
        // 1. 创建HttpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2. 创建HttpPost实例
        HttpPost httpPost = new HttpPost("https://dev-motorfinity.uk.auth0.com/oauth/token");
        JSONObject request = new JSONObject();
        request.put("client_id", "I2s5MY883up3obwKj8o1URu31oGKeFUr");
        request.put("client_secret", "fta4n4M8j8F02zhbq02baAAj2QNqkql4g0jxokqFVVc-R0gU9_4pn8F1swM8_eCU");
        request.put("audience", "https://zatest.vehiclemanagement.motorfinity.co.za");
        request.put("grant_type", "client_credentials");
        httpPost.setEntity(new StringEntity(request.toString()));
        // 3. 调用HttpClient实例来执行HttpPost实例
        CloseableHttpResponse response = httpclient.execute(httpPost);
        // 4. 读 response
        int status = response.getCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            System.out.println(html);
        } else {
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            throw new ClientProtocolException("Unexpected response status: " + status + ", response: " + html);
        }
        // 5. 释放连接
        response.close();
        httpclient.close();
    }

    public static void withdraw() throws Exception {
        // 1. 创建HttpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2. 创建HttpPost实例
        HttpPost httpPost = new HttpPost("https://iodin-sa-uat.chery.co.za/api/sales/oem/actualSoReturnNote/withdraw");
        httpPost.setHeader("From", "Y");
        httpPost.setHeader("tenant-id", "570272200");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(new StringEntity("570272200"));
        // 3. 调用HttpClient实例来执行HttpPost实例
        CloseableHttpResponse response = httpclient.execute(httpPost);
        // 4. 读 response
        int status = response.getCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            System.out.println(html);
        } else {
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            System.out.println(html);
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
        // 5. 释放连接
        response.close();
        httpclient.close();
    }
}
