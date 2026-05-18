package org.marsclub.test;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.util.*;

public class Temp {

    private static void a() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("E:\\south-africa-test\\src\\main\\resources\\temp\\A.txt"));
        StringBuilder sbf = new StringBuilder();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            sbf.append("'").append(line).append("',");
        }
        System.out.println(sbf);
    }

    public static void main(String[] args) throws Exception {
        a();
//        findVin();
        maintainModels();
        findNewDealership();
    }

    private static void maintainModels() {
        List<Entity> chery = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 0),
                oj = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 1),
                lepas = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 2),
                icaur = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 3);
        List<Entity> unconfirmed = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 4);
        List<Entity> allInDB = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 5);
        HashMap<String, List<Entity>> brandToEntities = new HashMap<>();
        allInDB.forEach(entity -> {
            List<Entity> list;
            if (brandToEntities.containsKey(entity.getString6().toLowerCase())) {
                list = brandToEntities.get(entity.getString6().toLowerCase());
            } else {
                brandToEntities.put(entity.getString6().toLowerCase(), list = new ArrayList<>());
            }
            list.add(entity);
        });
        HashMap<String, List<Entity>> unConfirmedMap = new HashMap<>();
        unconfirmed.forEach(entity -> {
            List<Entity> list;
            if (unConfirmedMap.containsKey(entity.getString1().toLowerCase())) {
                list = unConfirmedMap.get(entity.getString1().toLowerCase());
            } else {
                unConfirmedMap.put(entity.getString1().toLowerCase(), list = new ArrayList<>());
            }
            list.add(entity);
        });

        for(Map.Entry<String, List<Entity>> entry : brandToEntities.entrySet()) {
            String brand = entry.getKey();
            if (brand != null && brand.equalsIgnoreCase("chery")) {
                process(chery, unConfirmedMap.get("chery"), entry.getValue());
            }
            else if (brand != null && brand.equalsIgnoreCase("oj")) {
                process(oj, unConfirmedMap.get("oj"), entry.getValue());
            }
            else if (brand != null && brand.equalsIgnoreCase("lepas")) {
                process(lepas, unConfirmedMap.get("lepas"), entry.getValue());
            }
            else if (brand != null && brand.equalsIgnoreCase("icaur")) {
                process(icaur, unConfirmedMap.get("icaur"), entry.getValue());
            }
            else {
                for(Entity entity : entry.getValue()) {
                    System.out.println("无品牌：" + entity.getString1());
                }
            }
        }
    }

    private static void process(List<Entity> list, List<Entity> unconfirmed, List<Entity> inDBs) {
        for (Entity inDB : inDBs) {
            boolean found = false;
            for (Entity entity : list) {
                if (Objects.equals(entity.getString1().trim(), inDB.getString1().trim())) {
                    // 同一个物料
                    found = true;
                    if (!Objects.equals(entity.getString2(), inDB.getString2())
                            || !Objects.equals(entity.getString3(), inDB.getString3())) {
                        System.out.println("车型：" + inDB.getString1() + "有变化");
                    }
                    break;
                }
            }
            // 再尝试从未确认列表中找
            if (!found) {
                for (Entity entity : unconfirmed) {
                    if (Objects.equals(entity.getString2().trim(), inDB.getString1().trim())) {
                        found =  true;
                        break;
                    }
                }
            }
            if (!found) {
                System.out.println("新车型：" + inDB.getString1());
            }
        }
    }

    private static void findNewDealership() {
        List<Entity> current = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 6),
                inDBList = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 7);
        outer:
        for (Entity inDB : inDBList) {
            for (Entity entity : current) {
                if (Objects.equals(entity.getString1().trim(), inDB.getString1().trim())) {
                    continue outer;
                }
            }
            System.out.println("新经销商：" +  inDB.getString1());
        }
    }

    private static void findVin() throws Exception {
        File directory = new File("C:\\Users\\00537025\\Downloads\\EnatisVehicleInformation_20260318");
        ArrayList<JSONObject> list = new ArrayList();
        for (File file : directory.listFiles()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
            list.add(JSONObject.parseObject(sb.toString()));
        }
        String[] vinNumbers = new String[]{"LNNBBDEG9SC133637", "LVVDB21B6SD431645", "LVVDB21B7SD593333",
                "LVUGTBHD0TD060716", "LVVDB11B8SC194401", "LVVDB11B1SC118888"};
        list.forEach(obj -> {
            JSONArray vehicles = obj.getJSONArray("vehicles");
            for (int i = 0; i < vehicles.size(); i++) {
                JSONObject vehicle = vehicles.getJSONObject(i);
                for (String vin : vinNumbers) {
                    if (Objects.equals(vehicle.getString("vin_chassis_number"), vin)) {
                        System.out.println(vehicle);
                    }
                }
            }
        });
    }

    private static void b() throws Exception {
        File file = new File(ExcelReadUtil.PREFIX + "remark.sql");
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        Map<String, String> vinSaleOrgMap = vinSaleOrgCodeMap();
        for (Map.Entry<String, String> entry : vinSaleOrgMap.entrySet()) {
            String vin = entry.getKey(), saleOrgCode = entry.getValue();
            String sql = "\n" +
                    "update vehicle_base_info_ext set sale_org_code='" + saleOrgCode + "' where vin='" + vin + "';";
            writer.write(sql);
        }
        writer.flush();
        writer.close();
    }

    private static List<String> readExists() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("D:\\Workspace\\test\\src\\main\\resources\\temp\\A.txt"));
        LinkedList<String> exists = new LinkedList<>();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            exists.add(line);
        }
        return exists;
    }

    private static Map<String, String> vinSaleOrgCodeMap() throws Exception {
        List<Entity> list = ExcelReadUtil.read(ExcelReadUtil.PREFIX + "晶晶1_20260213.xls", 0);
        HashMap<String, String> map = new HashMap<>();
        list.forEach(e -> map.put(e.getString1(), e.getString2()));
        return map;
    }
}

// {"Feed":{"FeedDate":"2026-02-10","FeedName":"APPROVAL","FeedSource":"SUPPLIER","FileName":"OEMNAMEReleaseTest.xml","FullStockList":"NO","NumberOfParts":"1","PartNumber":"1","ID":"OEMNAMEReleaseTest","ToOrganisation":{"OrganisationType":"DEALING_COMPANY","OrganisationReference":"WESBANK"},"FromOrganisation":{"OrganisationType":"SUPPLIER","OrganisationReference":"WCHERY"},"Action":{"Date":"2026-02-10","Process":"APPROVAL","SequenceNumber":"1","OrganisationId":{"OrganisationType":"DEALER","OrganisationReference":"WOMOJAEGEO0001"},"ItemId":{"Identifier":"VIN","Identification":"LVVDB21B4SD501644"},"Loan":{"Plan":"OMF15","Currency":"ZAR","FinanceTerms":{"Items":{"Item":{"TypeOfItem":"ASSET","Supplier":"WCHERY","Identifier":"VIN","Identification":"LVVDB21B4SD501644","Make":"OMODA","Model":"OMODA C5 1.5T ELEGANCE X","Derivative":"","Cost":{"Gross":"435701.39"},"DescriptiveData":{"NameValue":[{"name":"VIN","value":"LVVDB21B4SD501644"},{"name":"ENGINE_NUMBER","value":"SQRE4T15CDQSH01479"},{"name":"COLOUR","value":"WHITE & BLACK"},{"name":"VRM","value":""},{"name":"MODEL_YEAR","value":"2025"},{"name":"VOUCHER_NUMBER","value":"VO1025553126020000100001"}]}}}}}}}}
// {"Feed":{"FeedDate":"2026-02-10","FeedName":"APPROVAL","FeedSource":"SUPPLIER","FileName":"OEMNAMEReleaseTest.xml","FullStockList":"NO","NumberOfParts":"1","PartNumber":"1","ID":"OEMNAMEReleaseTest","ToOrganisation":{"OrganisationType":"DEALING_COMPANY","OrganisationReference":"WESBANK"},"FromOrganisation":{"OrganisationType":"SUPPLIER","OrganisationReference":"WCHERY"},"Action":{"Date":"2026-02-10","Process":"APPROVAL","SequenceNumber":"1","OrganisationId":{"OrganisationType":"DEALER","OrganisationReference":"WOMOJAEGEO0001"},"ItemId":{"Identifier":"VIN","Identification":"LVUGTBCDXSDH67041"},"Loan":{"Plan":"OMF15","Currency":"ZAR","FinanceTerms":{"Items":{"Item":{"TypeOfItem":"ASSET","Supplier":"WCHERY","Identifier":"VIN","Identification":"LVUGTBCDXSDH67041","Make":"OMODA","Model":"OMODA C7 PHEV","Derivative":"","Cost":{"Gross":"651378.86"},"DescriptiveData":{"NameValue":[{"name":"VIN","value":"LVUGTBCDXSDH67041"},{"name":"ENGINE_NUMBER","value":"SQRH4J15DHSJ61553"},{"name":"COLOUR","value":"GREY"},{"name":"VRM","value":""},{"name":"MODEL_YEAR","value":"2025"},{"name":"VOUCHER_NUMBER","value":"VO1025553126020000100003"}]}}}}}}}}

// {"Feed":{"FeedDate":"2026-02-10","FeedName":"APPROVAL","FeedSource":"SUPPLIER","FileName":"OEMNAMEReleaseTest.xml","FullStockList":"NO","NumberOfParts":"1","PartNumber":"1","ID":"OEMNAMEReleaseTest","ToOrganisation":{"OrganisationType":"DEALING_COMPANY","OrganisationReference":"WESBANK"},"FromOrganisation":{"OrganisationType":"SUPPLIER","OrganisationReference":"WCHERY"},"Action":{"Date":"2026-02-10","Process":"APPROVAL","SequenceNumber":"1","OrganisationId":{"OrganisationType":"DEALER","OrganisationReference":"WOMOJAEEDEN001"},"ItemId":{"Identifier":"VIN","Identification":"LVVDB21B3SD501649"},"Loan":{"Plan":"OMF10","Currency":"ZAR","FinanceTerms":{"Items":{"Item":{"TypeOfItem":"ASSET","Supplier":"WCHERY","Identifier":"VIN","Identification":"LVVDB21B3SD501649","Make":"OMODA","Model":"OMODA C5 1.5T ELEGANCE X","Derivative":"","Cost":{"Gross":"435701.39"},"DescriptiveData":{"NameValue":[{"name":"VIN","value":"LVVDB21B3SD501649"},{"name":"ENGINE_NUMBER","value":"SQRE4T15CDQSH01899"},{"name":"COLOUR","value":"SILVER & BLACK"},{"name":"VRM","value":""},{"name":"MODEL_YEAR","value":"2025"},{"name":"VOUCHER_NUMBER","value":"VO1025549926020000200001"}]}}}}}}}}
// {"Feed":{"FeedDate":"2026-02-10","FeedName":"APPROVAL","FeedSource":"SUPPLIER","FileName":"OEMNAMEReleaseTest.xml","FullStockList":"NO","NumberOfParts":"1","PartNumber":"1","ID":"OEMNAMEReleaseTest","ToOrganisation":{"OrganisationType":"DEALING_COMPANY","OrganisationReference":"WESBANK"},"FromOrganisation":{"OrganisationType":"SUPPLIER","OrganisationReference":"WCHERY"},"Action":{"Date":"2026-02-10","Process":"APPROVAL","SequenceNumber":"1","OrganisationId":{"OrganisationType":"DEALER","OrganisationReference":"WOMOJAEEDEN001"},"ItemId":{"Identifier":"VIN","Identification":"LVUGTB226SDH65478"},"Loan":{"Plan":"OMF10","Currency":"ZAR","FinanceTerms":{"Items":{"Item":{"TypeOfItem":"ASSET","Supplier":"WCHERY","Identifier":"VIN","Identification":"LVUGTB226SDH65478","Make":"JAECOO","Model":"JAECOO J5 CORE","Derivative":"","Cost":{"Gross":"325439.28"},"DescriptiveData":{"NameValue":[{"name":"VIN","value":"LVUGTB226SDH65478"},{"name":"ENGINE_NUMBER","value":"SQRE4T15CDESJ60437"},{"name":"COLOUR","value":"GREY"},{"name":"VRM","value":""},{"name":"MODEL_YEAR","value":"2025"},{"name":"VOUCHER_NUMBER","value":"VO1025549926020000200004"}]}}}}}}}}