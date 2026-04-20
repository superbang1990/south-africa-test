package org.marsclub.test;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Motorfintity {

    private static final String CLIENT_ID = "I2s5MY883up3obwKj8o1URu31oGKeFUr";
    private static final String SECRET = "fta4n4M8j8F02zhbq02baAAj2QNqkql4g0jxokqFVVc-R0gU9_4pn8F1swM8_eCU";
    private static final String ADDITIONAL_INFO = "{\"audience\":\"https://zatest.vehiclemanagement.motorfinity.co.za\",\"mibcode\":{\"Chery\":\"1104EE7A-E15F-447E-B2F1-0F097E17458C\",\"Lepas\":\"C347FFFA-AF90-4F74-82E4-8688DECC87AB\",\"iCaur\":\"7E4FC60D-8CAA-487E-90BA-B50DA632595C\",\"JY12\":\"8B942BBD-9B10-46A6-B576-D48D71524DA1\",\"JY07\":\"A4327392-CC7F-4FEC-AF34-F1DB2FEBFA6A\"}}";
    private static final JSONObject INFO_JSON = JSONObject.parse(ADDITIONAL_INFO);

    private final HashMap<String, String> mibCodeMap = new HashMap<>();

    {
        JSONObject json = INFO_JSON.getJSONObject("mibcode");
        json.forEach((key, value) -> {
            mibCodeMap.put(key, value.toString());
        });
    }

    public String mibCodeOfChery() {
        return mibCodeMap.get("Chery");
    }

    public String mibCodeOfLepas() {
        return mibCodeMap.get("Lepas");
    }

    public String mibCodeOfICaur() {
        return mibCodeMap.get("iCaur");
    }

    public String mibCodeOfJY12() {
        return mibCodeMap.get("JY12");
    }

    public String mibCodeOfJY07() {
        return mibCodeMap.get("JY07");
    }

    private AccessToken accessToken;

    private Map<String, String> getHeadersMap() throws Exception {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Authorization", "Bearer " + getAccessToken());
        headersMap.put("Content-Type", "application/json");
        return headersMap;
    }

    public String getAccessToken() throws Exception {
        // 如果accessToken不为空且未过期，直接返回token
        if (accessToken != null && accessToken.expiresTime.isAfter(LocalDateTime.now())) {
            return accessToken.token;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("client_id", CLIENT_ID);
        jsonObject.put("client_secret", SECRET);
        jsonObject.put("audience", INFO_JSON.getString("audience"));
        jsonObject.put("grant_type", "client_credentials");
        System.out.println(jsonObject);
        JSONObject posted = HttpTest.post("https://dev-motorfinity.uk.auth0.com/oauth/token", jsonObject.toString(), null);
        String Access_token = posted.getString("access_token");
        Integer Expires_in = posted.getInteger("expires_in");
        // 计算过期时间并更新accessToken
        LocalDateTime expiresTime = LocalDateTime.now().plusSeconds(Expires_in / 2);
        this.accessToken = new AccessToken(Access_token, expiresTime);
        return Access_token;
    }

    public String introduce(String mibCode, List<VehicleInfo> list) throws Exception {
        JSONObject request = buildIntroduceRequest(mibCode, list);
        JSONObject posted = HttpTest.post("https://zatest.motorfinity.co.za:8447/api/introduction", request.toString(), getHeadersMap());
        if ("401".equals(posted.getString("status"))) {
            System.out.println("实销上报:上传车辆信息给监管平台:认证失败,清空本地Token");
            this.accessToken = null;
            getAccessToken();
            return introduce(mibCode, list);
        }
        return posted.getString("reference_id");
    }

    public PullUpLoadStatus introduceStatus(String mibCode, String ReleaseReferenceId) throws Exception {
        JSONObject request = new JSONObject();
        request.put("mib_code", mibCode);
        request.put("reference_id", ReleaseReferenceId);
        System.out.println("实销上报:查询车俩上传结果:请求参数:{" + request + "}");
        JSONObject posted = HttpTest.post("https://zatest.motorfinity.co.za:8447/api/introduction/status", request.toString(), getHeadersMap());
        System.out.println("实销上报:查询车俩上传结果:响应结果:{" + posted.toString() + "}");
        return JSONObject.parseObject(posted.toString(), PullUpLoadStatus.class);
    }

    public String update(List<VehicleInfo> reportRecordEntityList, String sapDealerCode, String mibCode,
                         String exportCountryCode, boolean isReturnToMIB) throws Exception {
        JSONObject request = buildUpdateRequest(reportRecordEntityList, sapDealerCode, mibCode,
                exportCountryCode, isReturnToMIB);
        JSONObject posted = HttpTest.post("https://zatest.motorfinity.co.za:8447/api/update", request.toString(), getHeadersMap());
        if ("401".equals(posted.getString("status"))) {
            System.out.println("实销上报:上传车辆信息给监管平台:认证失败,清空本地Token");
            this.accessToken = null;
            getAccessToken();
            return update(reportRecordEntityList, sapDealerCode, mibCode, exportCountryCode, isReturnToMIB);
        }
        return posted.getString("reference_id");
    }

    public PullUpLoadStatus updateStatus(String mibCode, String ReleaseReferenceId) throws Exception {
        JSONObject request = new JSONObject();
        request.put("mib_code", mibCode);
        request.put("reference_id", ReleaseReferenceId);
        System.out.println("实销上报:查询车俩上传结果:请求参数:{" + request + "}");
        JSONObject posted = HttpTest.post("https://zatest.motorfinity.co.za:8447/api/update/status", request.toString(), getHeadersMap());
        System.out.println("实销上报:查询车俩上传结果:响应结果:{" + posted.toString() + "}");
        return JSONObject.parseObject(posted.toString(), PullUpLoadStatus.class);
    }

    private JSONObject buildIntroduceRequest(String mibCode, List<VehicleInfo> list) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mib_code", mibCode);
        JSONArray array = new JSONArray();
        for (VehicleInfo record : list) {
            JSONObject vehicle = new JSONObject();
            vehicle.put("vin_chassis_number", record.vin);
            vehicle.put("unit_number", record.unitNumber);
            vehicle.put("engine_number", record.engineNumber); //发动机编号
            vehicle.put("model_number", record.modelNumber); //6 位数字
            vehicle.put("custom_model_name", record.materialCode); // motorfintity -> fill with material code
            vehicle.put("number_of_wheels", "4"); // 车轮数量
            vehicle.put("tare", "1250");//空重
            vehicle.put("class_type_code", ClassTypeCode.IMPORTER.code);
            vehicle.put("colour_code", ColourCode.OTHER.code);
            vehicle.put("transmission_code", TransmissionCode.AUTOMATIC.code);
            vehicle.put("import_country_code", "044");//中国
            vehicle.put("vehicle_state_code", VehicleStateCode.MIB_CONTROLLED.code);
            vehicle.put("ownership_type_code", OwnershipTypeCode.MIB_STOCK_OR_UNDER_CONSTRUCTION.code);
            vehicle.put("change_type_code", "005");
            vehicle.put("export_country_code", "001");
            vehicle.put("gearbox_number", "");//可不传
            vehicle.put("differential_number", "");//可不传
            vehicle.put("dealership_code", "");//可不传
            array.add(vehicle);
        }
        jsonObject.put("vehicles", array);
        return jsonObject;
    }

    private JSONObject buildUpdateRequest(List<VehicleInfo> reportRecordEntityList, String sapDealerCode, String mibCode,
                                          String exportCountryCode, boolean isReturnToMIB) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mib_code", mibCode);
        JSONArray vehicles = new JSONArray();
        for (VehicleInfo record : reportRecordEntityList) {
            JSONObject vehicle = new JSONObject();
            vehicle.put("vin_chassis_number", record.vin);
            vehicle.put("register_number", record.registerNumber);
            vehicle.put("engine_number", record.engineNumber);
            vehicle.put("model_number", record.modelNumber); //6 位数字
            vehicle.put("custom_model_name", record.materialCode); // motorfintity -> fill with material code
            vehicle.put("number_of_wheels", "4");
            vehicle.put("tare", "1250");
            vehicle.put("class_type_code", ClassTypeCode.IMPORTER.code);
            vehicle.put("colour_code", ColourCode.OTHER.code);
            vehicle.put("transmission_code", TransmissionCode.AUTOMATIC.code);
            vehicle.put("import_country_code", "044");
            vehicle.put("ownership_type_code", OwnershipTypeCode.MOTOR_DEALER_STOCK.code);
            vehicle.put("change_type_code", "005");
            vehicle.put("gearbox_number", "");
            vehicle.put("differential_number", "");
            if (isReturnToMIB) {
                vehicle.put("vehicle_state_code", VehicleStateCode.MIB_CONTROLLED.code);
                vehicle.put("export_country_code", "001");
                vehicle.put("dealership_code", "");
            } else {
                vehicle.put("vehicle_state_code", VehicleStateCode.MIB_RELEASED.code);
                vehicle.put("dealership_code", sapDealerCode);
                // 检查是否为跨境经销商
                vehicle.put("export_country_code", StringUtils.isBlank(exportCountryCode) ? "001" : exportCountryCode);
            }
            vehicles.add(vehicle);
        }
        jsonObject.put("vehicles", vehicles);
        return jsonObject;
    }

    private static class AccessToken {

        private final String token;
        private final LocalDateTime expiresTime;

        AccessToken(String token, LocalDateTime expiresTime) {
            this.token = token;
            this.expiresTime = expiresTime;
        }
    }

    public static class VehicleInfo {

        private String vin;

        private String unitNumber;

        private String engineNumber;

        private String modelNumber;

        private String materialCode;

        public void setRegisterNumber(String registerNumber) {
            this.registerNumber = registerNumber;
        }

        private String registerNumber;

        private String classTypeCode = ClassTypeCode.IMPORTER.code;

        private String colourCode = ColourCode.OTHER.code;

        private String transmissionCode = TransmissionCode.AUTOMATIC.code;

        private String vehicleStateCode = VehicleStateCode.MIB_CONTROLLED.code;

        private String ownershipTypeCode = OwnershipTypeCode.MIB_STOCK_OR_UNDER_CONSTRUCTION.code;

        public VehicleInfo(String vin, String unitNumber, String engineNumber, String modelNumber, String materialCode, String registerNumber) {
            this.vin = vin;
            this.unitNumber = unitNumber;
            this.engineNumber = engineNumber;
            this.modelNumber = modelNumber;
            this.materialCode = materialCode;
            this.registerNumber = registerNumber;
        }

        public String getVin() {
            return vin;
        }

        public String getUnitNumber() {
            return unitNumber;
        }

        public String getEngineNumber() {
            return engineNumber;
        }

        public String getModelNumber() {
            return modelNumber;
        }

        public String getMaterialCode() {
            return materialCode;
        }

        public String getRegisterNumber() {
            return registerNumber;
        }

        public String getClassTypeCode() {
            return classTypeCode;
        }

        public String getColourCode() {
            return colourCode;
        }

        public String getTransmissionCode() {
            return transmissionCode;
        }

        public String getVehicleStateCode() {
            return vehicleStateCode;
        }

        public String getOwnershipTypeCode() {
            return ownershipTypeCode;
        }
    }

    enum ClassTypeCode {
        BUILDER("001", "Builder"),
        IMPORTER("002", "Importer"),
        MANUFACTURER("003", "Manufacturer");

        private final String code;
        private final String desc;

        ClassTypeCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    enum ColourCode {
        BEIGE("001", "Beige"),
        BLACK("002", "Black"),
        BLUE("003", "Blue"),
        BRONZE("004", "Bronze"),
        BROWN("005", "Brown"),
        CREAM("006", "Cream"),
        GOLD("007", "Gold"),
        GREEN("008", "Green"),
        GREY("009", "Grey"),
        MAROON("010", "Maroon"),
        MUSTARD("011", "Mustard"),
        ORANGE("012", "Orange"),
        OTHER("013", "Other"),
        PINK("014", "Pink"),
        PURPLE("015", "Purple"),
        RED("016", "Red"),
        SILVER("017", "Silver"),
        TAN("018", "Tan"),
        UNKNOWN("019", "Unknown"),
        WHITE("020", "White"),
        YELLOW("021", "Yellow");

        private final String code;
        private final String desc;

        ColourCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    enum TransmissionCode {
        AUTOMATIC("001", "Automatic"),
        MANUAL("002", "Manual"),
        NONE("003", "None"),
        SEMI_AUTOMATIC("004", "Semi-automatic"),
        UNKNOWN("005", "Unknown");

        private final String code;
        private final String desc;

        TransmissionCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    enum VehicleStateCode {
        MIB_CONTROLLED("001", "MIB Controlled"),
        MIB_RELEASED("002", "MIB Released");

        private final String code;
        private final String desc;

        VehicleStateCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    enum OwnershipTypeCode {
        MIB_STOCK_OR_UNDER_CONSTRUCTION("001", "MIB Stock/Under construction"),
        MOTOR_DEALER_STOCK("002", "Motor Dealer Stock");

        private final String code;
        private final String desc;

        OwnershipTypeCode(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
