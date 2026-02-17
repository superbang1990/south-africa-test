package org.marsclub.test.actualso;

import org.apache.commons.collections4.CollectionUtils;
import org.marsclub.test.Entity;
import org.marsclub.test.ExcelReadUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class ActualSOMain {

    private static final String PATH =
            "D:\\Workspace\\test\\src\\main\\resources\\生产环境迁移副本.xlsx";

    private static final CountDownLatch LATCH = new CountDownLatch(4);

    public static void analyze() throws Exception {
        Map<String, Integer> subsidiaryInStock = new HashMap<>(),
                dealerInStock = new HashMap<>(),
                dealerInTransit = new HashMap<>(),
                idms = new HashMap<>();
        start(() -> subsidiaryInStock.putAll(ExcelReadUtil.readVehicleSubsidiaryInStock()),
                () -> dealerInStock.putAll(ExcelReadUtil.readVehicleDealerInStock()),
                () -> dealerInTransit.putAll(ExcelReadUtil.readVehicleDealerInTransit()),
                () -> idms.putAll(ExcelReadUtil.readVehicleInIDMS()));
        LATCH.await();
        loopMap(subsidiaryInStock, "子公司在库预期数据");
        loopMap(dealerInStock, "经销商在库预期数据");
        loopMap(dealerInTransit, "子公司在途预期数据");
        loopMap(idms, "IDMS预期数据");
        // 读取除sheet0之外的所有数据
        Map<String, Integer> map = readAnother();
        for (String vin : subsidiaryInStock.keySet()) {
            if (!map.containsKey(vin)) {
                System.out.println(vin);
            }
        }
        for (String vin : dealerInStock.keySet()) {
            if (!map.containsKey(vin)) {
                System.out.println(vin);
            }
        }
        for (String vin : dealerInTransit.keySet()) {
            if (!map.containsKey(vin)) {
                System.out.println(vin);
            }
        }
        for (String vin : idms.keySet()) {
            if (!map.containsKey(vin)) {
                System.out.println(vin);
            }
        }
    }

    private static void start(Runnable ... runnables) {
        for (Runnable runnable : runnables) {
            new Thread(() -> {
                try {
                    runnable.run();
                } finally {
                    LATCH.countDown();
                }
            }).start();
        }
    }

    private static void loopMap(Map<String, Integer> map, String message) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(message + "数据中，vin：" + entry.getKey() + " 超过1条数据");
            }
        }
    }

    private static Map<String, Integer> readAnother() {
        HashMap<String, Integer> map = new HashMap<>();
        List<Entity> list = ExcelReadUtil.read(PATH, 2);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(entity ->
                    map.put(entity.getString2(), map.getOrDefault(entity.getString2(), 1)));
        }
        list = ExcelReadUtil.read(PATH, 3);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(entity ->
                    map.put(entity.getString2(), map.getOrDefault(entity.getString2(), 1)));
        }
        list.addAll(ExcelReadUtil.read(PATH, 4));
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(entity ->
                    map.put(entity.getString2(), map.getOrDefault(entity.getString2(), 1)));
        }
        return map;
    }
}
