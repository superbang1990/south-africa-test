package org.marsclub.test;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.Test;

import java.util.Map;

public class MIBTest {

    @Test
    public void printAvailable() throws Exception {
        String r = post("https://iodin-admin-dev.omodaglobal.com/api/integration/mib_test/available/print", "{}", null);
        System.out.println(r);
    }

    @Test
    public void resetAvailable() throws Exception {
        JSONArray array = new JSONArray();
        array.add("T6476DQJKHK0001");
        array.add("T6476DQVQHK0001");
        array.add("T7160NGGVFG0004");
        array.add("T7160NGWBFF0006");
        array.add("T7160NGWBFF0013");
        array.add("T7160GKNLJW0010");
        array.add("T7161NJZEFG0004");
        array.add("T7150EGSKFR0030");
        array.add("T7150VVSKGW0001");
        array.add("T7160WEZNMH0005");
        array.add("T7150U4CLNG0002");
        array.add("T7151Y1KHMH0002");
        array.add("T7150SSBWJV0016");
        array.add("T71503CLQFR0001");
        array.add("T71603USYJW0001");
        String r = post("https://iodin-admin-dev.omodaglobal.com/api/integration/mib_test/available/reset", array.toString(), null);
        System.out.println(r);
        printAvailable();
    }

    @Test
    public void printAvailableMib() throws Exception {
        String brand = "LEPAS";
        String r = post("https://iodin-admin-dev.omodaglobal.com/api/integration/mib_test/mib_entity/print/" + brand, "{}", null);
        System.out.println(r);
    }

    /**
     * chery 3 - 3f43dd46-91c5-4378-9d09-6721cc7c923d
     * oj 3 - ab15b7d5-b900-4159-9a63-63c577ee2c7a
     * @throws Exception
     */
    @Test
    public void introduce() throws Exception {
        // CHERY, iCAUR, LEPAS, OJ
        String brand = "CHERY";
        int size = 5;
        String url = "https://iodin-admin-dev.omodaglobal.com/api/integration/mib_test/introduce/" + brand + "/" + size;
        String r = post(url, "{}", null);
        System.out.println(r);
    }

    @Test
    public void introduceStatus() throws Exception {
        String brand = "CHERY", referenceId = "06194168-dd9e-4986-a7a1-3f72a6a8d871";
        String url = "https://iodin-admin-dev.omodaglobal.com/api/integration/mib_test/introduce/status/" + brand + "/" + referenceId;
        String r = post(url, "{}", null);
        System.out.println(r);
    }

    /**
     * 15981062 Chery test 001
     * 15981081 OMODA TEST 001
     * 15926701 Chery test 002
     * 15922063 OMODA TEST 002
     */
    @Test
    public void update() throws Exception {
        String brand = "CHERY", dealerCode="15981062", countryCode="001", returns="0";
        String url = "https://iodin-admin-dev.omodaglobal.com/api/integration/mib_test/update/"
                + brand + "/" + dealerCode + "/" + countryCode + "/" + returns;
        JSONObject json = new JSONObject();
        json.put("", "");
        String r = post(url, json.toString(), null);
        System.out.println(r);
    }

    @Test
    public void updateStatus() throws Exception {
        String brand = "CHERY", referenceId = "06194168-dd9e-4986-a7a1-3f72a6a8d871";
        String url = "https://iodin-admin-dev.omodaglobal.com/api/integration/mib_test/update/status/" + brand + "/" + referenceId;
        String r = post(url, "{}", null);
        System.out.println(r);
    }

    private static String post(String url, String body, Map<String, String> headers) throws Exception {
        // 1. 创建HttpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2. 创建HttpPost实例
        HttpPost httpPost = new HttpPost(url);
        if (MapUtils.isNotEmpty(headers)) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(new StringEntity(body));
        CloseableHttpResponse response = null;
        try {
            // 3. 调用HttpClient实例来执行HttpPost实例
            response = httpclient.execute(httpPost);
            // 4. 读 response
            int status = response.getCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
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
}
