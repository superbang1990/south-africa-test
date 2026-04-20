package org.marsclub.test;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.Test;
import org.marsclub.test.http.HttpTest;

public class InvoiceTest {

    @Test
    public void testDownloadInvoice() throws Exception {
        HttpTest.post();
    }

    // Authorization:{{token}}
    // tenant-code:{{tenant-code}}
    // tenant-id:{{tenant-id}}
    //accept-language:en-GB
    //reqFrom:DDMS
    //version:qgt
    //time-zone:Asia/Shanghai
    //from:Y
    @Test
    public void testExportActualSo() throws Exception {
        // 1. 创建HttpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2. 创建HttpPost实例
        HttpPost httpPost = new HttpPost("https://iodin.chery.co.za/api/sales/oem/actualSo/export2");
        httpPost.setHeader("From", "Y");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setHeader("Authorization", "Bearer 8yupmi3KfqXBXXtsRXs_usnmmyo_WpD9muSzacgC7FsJC8VeVH-ItdfX9-LBJtntPYYVUTWvCJrcYyM_OVh_CLCh8P6I67zd92dyZZj3b1Zv2GJkP7Ubik0wuHn7yqR9");
        httpPost.setHeader("Tenant-Code", "570272200");
        httpPost.setHeader("Tenant-Id", "570272200");
        httpPost.setHeader("Cookie", "HWWAFSESID=9578273f0e4798b890; HWWAFSESTIME=1771857488002");
        httpPost.setEntity(new StringEntity("{\n" + "}"));
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
