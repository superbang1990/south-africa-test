package org.marsclub.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ExcelReadUtil {

    public static final String PREFIX = "D:\\Workspace\\test\\src\\main\\resources\\";

    public static List<Entity> read(String path, int index) {
        final LinkedList<Entity> list = new LinkedList<>();
        EasyExcel.read(path, Entity.class,
                new PageReadListener<Entity>(list::addAll)).sheet(index).doRead();
        return list;
    }

    public static Map<String, Integer> readVehicleSubsidiaryInStock() {
        // 子公司在库
        List<Entity> list = read(
                PREFIX + "Vehicle on hand stock  January匹配库位 (2)-SAP提供-子公司库存车辆.xlsx",
                0);
        return buildMap(list, Entity::getString2);
    }

    public static Map<String, Integer> readVehicleDealerInStock() {
        // 经销商在库
        List<Entity> list = read(
                PREFIX + "Migration template Vehicle20260105-in stock-业务提供.xlsx",
                4);
        return buildMap(list, Entity::getString1);
    }

    public static Map<String, Integer> readVehicleDealerInTransit() {
        // 经销商在途
        List<Entity> list = read(
                PREFIX + "Migration template Vehicle20260105-dealer in transit111-业务提供.xlsx",
                2);
        return buildMap(list, Entity::getString1);
    }

    public static Map<String, Integer> readVehicleInIDMS() {
        // IDMS
        String[] paths = new String[]{
                PREFIX + "IDMS\\不在南非下的23台车.xlsx",
                PREFIX + "IDMS\\截止2200车辆档案.xlsx",
                PREFIX + "IDMS\\截止22483车辆档案.xlsx"
        };
        // 3, 2, 2
        Map<String, Integer> map = buildMap(read(paths[0], 0), Entity::getString3);
        map.putAll(buildMap(read(paths[1], 0), Entity::getString2));
        map.putAll(buildMap(read(paths[2], 0), Entity::getString2));
        return map;
    }

    private static Map<String, Integer> buildMap(List<Entity> list, Function<Entity, String> func) {
        HashMap<String, Integer> map = new HashMap<>();
        list.forEach(entity -> {
            String vin = func.apply(entity);
            map.put(vin, map.getOrDefault(vin, 1));
        });
        return map;
    }

    public static void loopMap(HashMap<String, Integer> map, String message) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(message + "数据中，vin：" + entry.getKey() + " 超过1条数据");
            }
        }
    }
}
