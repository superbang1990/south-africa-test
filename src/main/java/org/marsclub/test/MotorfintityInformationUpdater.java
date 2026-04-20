package org.marsclub.test;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class MotorfintityInformationUpdater {

    private static final String CHERY = "Chery";
    private static final String iCAUR = "iCAUR";
    private static final String OJ = "OJ";
    private static final String LEPAS = "Lepas";

    private static final int CHERY_INDEX = 0;
    private static final int iCAUR_INDEX = 3;
    private static final int OJ_INDEX = 1;
    private static final int LEPAS_INDEX = 2;

    public static void main(String[] args) {
        List<Entity> fromDB = ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", 5);
        Map<String, List<Entity>> mapFromDB = map(fromDB);
        String[] brandArray = new String[]{CHERY, iCAUR, OJ, LEPAS};
        int[] indexArray = new int[]{CHERY_INDEX, iCAUR_INDEX, OJ_INDEX, LEPAS_INDEX};
        for (int i = 0; i < brandArray.length; i++) {
            String brand = brandArray[i];
            List<Entity> db = mapFromDB.get(brand);
            List<Entity> olds =ExcelReadUtil.read("C:\\Users\\00537025\\Downloads\\motorfinity information.xlsx", indexArray[i]);
            handleOneBrand(db, olds);
        }
    }

    private static void handleOneBrand(List<Entity> db, List<Entity> olds) {
        for(Entity eInDB : db) {
            Entity oldTarget = null;
            for(Entity eOld : olds) {
                if (!Objects.equals(eInDB.getString1(), eOld.getString1())) {
                    continue;
                }
                oldTarget =  eOld;
                break;
            }
            if(oldTarget == null) {
                System.out.println("新增物料号：" + eInDB.getString1());
            }
            else {
                boolean sc = false;
                StringBuilder sbf = new StringBuilder("[" + eInDB.getString1() + "],");
                if (!Objects.equals(eInDB.getString2(), oldTarget.getString2())) {
                    sbf.append("model code[").append(eInDB.getString2()).append("]").append(",");
                    sc = true;
                }
                if (!Objects.equals(eInDB.getString3(), oldTarget.getString3())) {
                    sbf.append("model name[").append(eInDB.getString3()).append("]").append(",");
                    sc = true;
                }
                if (sc) {
                    System.out.println(sbf);
                }
            }
        }
    }


    private static Map<String, List<Entity>> map(List<Entity> fromDB) {
        HashMap<String, List<Entity>> mapFromDB = new HashMap<>();
        if (CollectionUtils.isEmpty(fromDB)) {
            return mapFromDB;
        }
        fromDB.forEach(entity -> {
            List<Entity> list;
            if (mapFromDB.containsKey(entity.getString6())) {
                list = mapFromDB.get(entity.getString6());
            } else {
                mapFromDB.put(entity.getString6(), list = new ArrayList<>());
            }
            list.add(entity);
        });
        return mapFromDB;
    }
}
