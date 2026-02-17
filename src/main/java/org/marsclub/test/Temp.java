package org.marsclub.test;

import java.io.*;
import java.util.*;

public class Temp {

    private static void a() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("D:\\Workspace\\test\\src\\main\\resources\\temp\\A.txt"));
        StringBuilder sbf = new StringBuilder();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            sbf.append("'" + line + "',");
        }
        System.out.println(sbf);
    }

    public static void main(String[] args) throws Exception {
//        List<String> exists = readExists();
//        List<Entity> list = ExcelReadUtil.read(ExcelReadUtil.PREFIX + "Subsidiary Vehicle Stock--gps.xlsx", 0);
//        File file = new File(ExcelReadUtil.PREFIX + "remark.sql");
//        file.createNewFile();
//        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//        long start = 0L;
//        for (Entity entity : list) {
//            String vin = entity.getString1().trim(),
//                    materialCode = entity.getString9().trim(),
//                    remark = entity.getString8().trim();
////            if (exists.contains(vin)) {
////                continue;
////            }
//            String sql = "\n" +
//                    "insert into vehicle_base_info_ext (`id`, `sales_org_code`, `vin`, `material_code`, `overseas_model`, `tenant_id`) " +
//                    "values (" +
//                    "'" + start ++ + "', " +
//                    "'', " +
//                    "'" + vin + "', " +
//                    "'" + materialCode + "', " +
//                    "\"" + remark + "\", " +
//                    "'570272200');";
//            writer.write(sql);
//        }
//        writer.flush();
//        writer.close();
        List<Entity> list = ExcelReadUtil.read(ExcelReadUtil.PREFIX + "MIB RETURN1.xlsx", 3);
        StringBuilder sbf = new StringBuilder();
        for (Entity entity : list) {
            sbf.append("'" + entity.getString1() + "',");
        }
        System.out.println(sbf);
//        a();
//        b();
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