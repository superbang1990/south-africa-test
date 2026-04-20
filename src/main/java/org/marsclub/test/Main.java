package org.marsclub.test;

import java.util.*;

public class Main {

    private final static List<MIBEntity> CHERY = new LinkedList<>();
    private final static List<MIBEntity> iCAUR = new LinkedList<>();
    private final static List<MIBEntity> LEPAS = new LinkedList<>();
    private final static List<MIBEntity> OJ = new LinkedList<>();

    static {
        List<Entity> list = ExcelReadUtil.read("E:\\south-africa-test\\src\\main\\resources\\mib信息.xlsx", 0);
        list.forEach(entity -> {
            MIBEntity e = new MIBEntity(entity.getString1(), entity.getString2(), entity.getString3(), entity.getString4());
            if (Objects.equals(entity.getString2(), "iCAUR")) {
                iCAUR.add(e);
            }
            if (Objects.equals(entity.getString2(), "Lepas")) {
                LEPAS.add(e);
            }
            if (Objects.equals(entity.getString2(), "Chery")) {
                CHERY.add(e);
            }
            if (Objects.equals(entity.getString2(), "OJ")) {
                OJ.add(e);
            }
        });
    }

    private static final Random random = new Random(47);

    private final Motorfintity motorfintity = new Motorfintity();

    private final static char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static String randomUnitNumber() {
        // 0, 4 -> char
        return "3" + randomChar(1) + randomNumber(2) + randomChar(1) + randomNumber(5);
    }

    private static String randomVin() {
        // LVVD B 11 B 0 MD 148221
        String vin = "LVVD" + randomChar(1) + randomNumber(2)
                + randomChar(1) + Math.abs(random.nextInt(9))
                + randomChar(2)
                + randomNumber(6);
        return vin.toUpperCase();
    }

    private static String randomEngineSerialNo() {
        // SQRE 4 T 15 CALMD 03222
        String vin = "SQRE" + randomNumber(1) + randomChar(1) + randomNumber(2)
                + randomChar(5) + randomNumber(5);
        return vin.toUpperCase();
    }

    private static String randomChar(int size) {
        StringBuilder sbf = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sbf.append(chars[Math.abs(random.nextInt(25))]);
        }
        return sbf.toString();
    }

    private static String randomNumber(int size) {
        StringBuilder sbf = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sbf.append(Math.abs(random.nextInt(9)));
        }
        return sbf.toString();
    }

    public List<Motorfintity.VehicleInfo> createCheryVehicles(int size) {
        List<Motorfintity.VehicleInfo> list = new ArrayList<>(size);
        for (int i = 0; i < size; i ++) {
            MIBEntity entity = CHERY.get(Math.abs(CHERY.size() - 1));
            list.add(new Motorfintity.VehicleInfo(
                    randomVin(),
                    randomUnitNumber(),
                    randomEngineSerialNo(),
                    entity.getModelNumber(),
                    entity.getMaterialCode(),
                    null
            ));
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        String introduced = main.motorfintity.introduce(main.motorfintity.mibCodeOfChery(), main.createCheryVehicles(1));
        System.out.println(introduced);
    }
}
