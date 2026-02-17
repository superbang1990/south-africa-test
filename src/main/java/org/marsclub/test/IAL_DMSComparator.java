package org.marsclub.test;

import org.apache.poi.util.StringUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * IAL数据和DMS数据进行比对，IAL指https://my-ichery.feishu.cn/wiki/COj0wxofLiqty0k4ubMcjqHknmw?sheet=9I8Pw0
 * 在线文档的数据
 * DMS 是指从线上库中导出的数据，比对之后，确定哪些vin号，IAL里有，DMS里没有
 */
public class IAL_DMSComparator {

    private static final HashSet<String> A = new HashSet<>();

    static {
        A.add("LVVDB11BXMD600093");
        A.add("LVVDB21B8MD600132");
        A.add("LVVDB11BXMD160103");
        A.add("LVVDB21B4MD188274");
        A.add("LVTDB21B1MD233809");
        A.add("LVTDB21BXMD233808");
        A.add("LVVDB21B8MD600129");
        A.add("LVVDB21B5MD179874");
        A.add("LVVDB21B9MD173365");
        A.add("LVVDB21B2RD900952");
        A.add("LVTDB21BXRD617210");
        A.add("LVUGFB2275D831492");
    }

    public static void diffSetBetweenIAL() throws Exception {
        Set<String> ialVinSet = readIALVin();
        Set<String> idmsVinSet = readIDMSVin();
        Map<String, DMSVehicleInfo> dmsVinSet = readDMSVin();
        Set<String> actualSold = readActualSold();
        printExistInIAL_NinIDMS(ialVinSet, idmsVinSet);
        printExistInIDMS_NinIAL(idmsVinSet, ialVinSet);
        printExistInIAL_NinDMS(ialVinSet, dmsVinSet);
        printExistInIDMS_NinDMS(idmsVinSet, dmsVinSet);
        printIDMSNotSold(idmsVinSet, actualSold);
    }

    private static void printExistInIAL_NinIDMS(Set<String> ialVinSet, Set<String> idmsVinSet) throws Exception {
        HashSet<String> r = new HashSet<>();
        for (String vin : ialVinSet) {
            if (!idmsVinSet.contains(vin)) {
                r.add(vin);
            }
        }
        write("车辆在IAL有，IDMS没有.txt", r);
    }

    private static void printExistInIDMS_NinIAL(Set<String> idmsVinSet, Set<String> ialVinSet) throws Exception {
        HashSet<String> r = new HashSet<>();
        for (String vin : idmsVinSet) {
            if (!ialVinSet.contains(vin)) {
                r.add(vin);
            }
        }
        write("车辆在IDMS有，IAL没有.txt", r);
    }

    /**
     * IAL提供的数据与DMS系统比对
     *  (IAL有车辆，DMS没有车辆）
     */
    private static void printExistInIAL_NinDMS(Set<String> ialVinSet, Map<String, DMSVehicleInfo> dmsMap) throws Exception {
        HashSet<String> r = new HashSet<>();
        for (String vin : ialVinSet) {
            if (!dmsMap.containsKey(vin) && !A.contains(vin)) {
                r.add(vin);
            }
        }
        write("IAL有车辆，DMS没有车辆.txt", r);
    }

    /**
     * DMS与IDMS比对结果
     * （IDMS存在，DMS不存在的车辆）
     */
    private static void printExistInIDMS_NinDMS(Set<String> idmsVinSet, Map<String, DMSVehicleInfo> dmsMap) throws Exception {
        HashSet<String> r = new HashSet<>();
        for (String vin : idmsVinSet) {
            if (!dmsMap.containsKey(vin) && !A.contains(vin)) {
                r.add(vin);
            }
        }
        write("IDMS存在，DMS不存在的车辆.txt", r);
    }

    private static void printIDMSNotSold(Set<String> idmsVinSet, Set<String> actualSoldVinSet) throws Exception {
        HashSet<String> r = new HashSet<>();
        for (String vin : idmsVinSet) {
            if (!actualSoldVinSet.contains(vin)) {
                r.add(vin);
            }
        }
        write("IDMS存在，DMS还未卖出.txt", r);
    }

    private static void write(String fileName, Set<String> vinSet) throws Exception {
        File file = new File(ExcelReadUtil.PREFIX + fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String vin : vinSet) {
            if (StringUtil.isBlank(vin)) {
                continue;
            }
            writer.write(vin + "\r\n");
        }
        writer.flush();
        writer.close();
    }


    /**
     * 从上产环境迁移-数据核对 excel中读取IAL提供的所有的vin号
     * 是sheet7中，把所有列里的vin号累加起来
     */
    private static Set<String> readIALVin() {
        String path = ExcelReadUtil.PREFIX + "IAL实销数据.xlsx";
        List<Entity> list = ExcelReadUtil.read(path, 0);
        HashMap<String, Integer> map = new HashMap<>();
        list.forEach(entity -> {
            String vin = entity.getString1();
            map.put(vin, map.getOrDefault(vin, 1));
        });
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + "，存在" + entry.getValue() + "条重复");
            }
        }
        return map.keySet();
    }

    /**
     * 从上产环境迁移-数据核对 excel中读取IDMS提供的所有vin号
     * sheet1-实销信息-IDMS
     * sheet2-22483子公司车辆信息-IDMS
     * sheet3-2200子公司车辆-已收集-IDMS
     * 这三个sheet的综合
     */
    private static Set<String> readIDMSVin() {
        String path = ExcelReadUtil.PREFIX + "生产环境迁移-数据核对.xlsx";
        HashMap<String, Integer> map = new HashMap<>();
        ExcelReadUtil.read(path, 1).forEach(entity -> {
            String vin = entity.getString1();
            map.put(vin, map.getOrDefault(vin, 1));
        });
//        ExcelReadUtil.read(path, 2).forEach(entity -> {
//            String vin = entity.getString1();
//            map.put(vin, map.getOrDefault(vin, 1));
//        });
//        ExcelReadUtil.read(path, 3).forEach(entity -> {
//            String vin = entity.getString1();
//            map.put(vin, map.getOrDefault(vin, 1));
//        });
//        ExcelReadUtil.read(path, 4).forEach(entity -> {
//            String vin = entity.getString1();
//            map.put(vin, map.getOrDefault(vin, 1));
//        });
        return map.keySet();
    }

    private static Map<String, DMSVehicleInfo> readDMSVin() {
        HashMap<String, Entity> vehicleMap = new HashMap<>(), trackingMap = new HashMap<>();
        // 先读取车辆数据
        ExcelReadUtil.read(ExcelReadUtil.PREFIX + "02091817stock_subsidiary_vehicle.xls", 0)
                .forEach(entity -> vehicleMap.put(entity.getString2(), entity));
        ExcelReadUtil.read(ExcelReadUtil.PREFIX + "02091817stock_subsidiary_vehicle.xls", 1)
                .forEach(entity -> vehicleMap.put(entity.getString2(), entity));
        // 读取车辆跟踪状态信息
        ExcelReadUtil.read(ExcelReadUtil.PREFIX + "tracking_dealer_vehicle.csv", 0)
                .forEach(entity -> trackingMap.put(entity.getString2(), entity));

        Map<String, DMSVehicleInfo> r = new HashMap<>();
        for (Map.Entry<String, Entity> entry : vehicleMap.entrySet()) {
            r.put(entry.getKey(), new DMSVehicleInfo(entry.getValue(), trackingMap.get(entry.getKey())));
        }
        return r;
    }

    private static Set<String> readActualSold() {
        String path = ExcelReadUtil.PREFIX + "all_dealer_sold.csv";
        List<Entity> list = ExcelReadUtil.read(path, 0);
        HashMap<String, Integer> map = new HashMap<>();
        list.forEach(entity -> {
            String vin = entity.getString2();
            map.put(vin, map.getOrDefault(vin, 1));
        });
        return map.keySet();
    }
}
