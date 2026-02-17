package org.marsclub.test.asa;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import org.marsclub.test.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class ASAMain {

    // 子公司在库
    private static final String PATH =
            "D:\\Workspace\\test\\src\\main\\resources\\生产环境迁移副本.xlsx";

    private static final HashMap<String, Integer> ONE = new HashMap<>();
    private static final HashMap<String, Integer> TWO = new HashMap<>();
    private static final HashMap<String, Integer> THREE = new HashMap<>();
    private static final HashMap<String, Integer> FOUR = new HashMap<>();
    private static final HashMap<String, Integer> FIVE = new HashMap<>();

    private static final CountDownLatch LATCH = new CountDownLatch(5);

    public static void analyze() throws Exception {
        start(() -> read(0, ONE, Entity::getString1),
                () -> read(1, TWO, Entity::getString1),
                () -> read(2, THREE, Entity::getString2),
                () -> read(3, FOUR, Entity::getString2),
                () -> read(4, FIVE, Entity::getString2)
        );
        LATCH.await();
        loopMap(ONE, "实销信息");
        loopMap(TWO, "实销信息IDMS");
        loopMap(THREE, "22483子公司车辆信息");
        loopMap(FOUR, "2200子公司车辆-已收集");
        loopMap(FIVE, "子公司车辆-IDMS-其他编码");

        for(String vin : ONE.keySet()) {
            if (TWO.containsKey(vin) || THREE.containsKey(vin) || FOUR.containsKey(vin) || FIVE.containsKey(vin)) {
                continue;
            }
            System.out.println(vin);
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

    private static void loopMap(HashMap<String, Integer> map, String message) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(message + "数据中，vin：" + entry.getKey() + " 超过1条数据");
            }
        }
    }

    private static void read(int index, HashMap<String, Integer> map, Function<Entity, String> func) {
        EasyExcel.read(PATH, Entity.class,
                new PageReadListener<Entity>(dataList -> {
                    for (Entity entity : dataList) {
                        String vin = func.apply(entity);
                        map.put(vin, map.getOrDefault(vin, 1));
                    }
        })).sheet(index).doRead();
    }
}
