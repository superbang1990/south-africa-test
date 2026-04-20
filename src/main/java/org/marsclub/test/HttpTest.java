package org.marsclub.test;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.HashMap;
import java.util.Map;

public class HttpTest {

    public static JSONObject post(String url, String body, Map<String, String> headers) throws Exception {
        // 1. 创建HttpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2. 创建HttpPost实例
        HttpPost httpPost = new HttpPost(url);
        if (MapUtils.isNotEmpty(headers)) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        httpPost.setEntity(new StringEntity(body));
        CloseableHttpResponse response = null;
        try {
            // 3. 调用HttpClient实例来执行HttpPost实例
            response = httpclient.execute(httpPost);
            // 4. 读 response
            int status = response.getCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return JSONObject.parse(EntityUtils.toString(entity));
            } else {
                HttpEntity entity = response.getEntity();
                throw new ClientProtocolException("Unexpected response status: " + status + ", message: " + EntityUtils.toString(entity));
            }
        } finally {
            // 5. 释放连接
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
    }

    private static void post(String url, Long start, Long end) throws Exception {
        // 1. 创建HttpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2. 创建HttpPost实例
        HttpPost httpPost = new HttpPost("https://iodin.chery.co.za/api/sales/common/cache/clear-all");
        httpPost.setHeader("From", "Y");
        httpPost.setHeader("tenant-id", "570272200");
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.addPart("", new StringBody(start + "", ContentType.create("text/plain", "utf-8")));
//        builder.addPart("", new StringBody(end + "", ContentType.create("text/plain", "utf-8")));
//        httpPost.setEntity(builder.build());
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
        post("https://iodin.chery.co.za/api/sales/importdata/vehicle-and-so/sync-from-record", 51L, 288792L);
    }
}
