package org.marsclub.test;

import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;

public class FloorplanLogExtractor {

    public void extract(String filename) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String dash = "结果：";
        for(String line; (line = br.readLine()) != null;) {
            int first = line.indexOf("{");
            String string = line.substring(first);
            int a = string.indexOf(dash);
            String requestStr = string.substring(0,a), responseStr = string.substring(a + dash.length());
            JSONObject request = JSONObject.parseObject(requestStr),
                    response = JSONObject.parseObject(responseStr);
            if (line.contains("WESBANK银行锁定余额调用ESB")) {
                wesbank(request, response);
            }
            else if (line.contains("ASBA银行锁定余额调用ESB")) {
                wesbank(request, response);
            }
            else if (line.contains("STD银行锁定余额调用ESB")) {
                wesbank(request, response);
            }
            else if (line.contains("MFC银行锁定余额调用ESB")) {
                wesbank(request, response);
            }
        }
    }

    private void wesbank(JSONObject request, JSONObject response) {
        JSONObject feed = request.getJSONObject("Feed");
        JSONObject action = feed.getJSONObject("Action");
        JSONObject organisationId = action.getJSONObject("OrganisationId"),
                itemId = action.getJSONObject("ItemId"),
                loan = action.getJSONObject("Loan");
        String account = organisationId.getString("OrganisationReference");
        String vin = itemId.getString("Identification");
        String plan = loan.getString("Plan");
        System.out.println("WESBANK," + account + "," + plan + "," + vin);
    }

    private void asba(JSONObject request, JSONObject response) {

    }

    private void std(JSONObject request, JSONObject response) {

    }

    private void mfc(JSONObject request, JSONObject response) {

    }
}
