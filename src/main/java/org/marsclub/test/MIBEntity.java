package org.marsclub.test;

public class MIBEntity {

    private final String materialCode;

    private final String brandCode;

    private final String modelCode;

    private final String modelNumber;

    public MIBEntity(String materialCode, String brandCode, String modelCode, String modelNumber) {
        this.materialCode = materialCode;
        this.brandCode = brandCode;
        this.modelCode = modelCode;
        this.modelNumber = modelNumber;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public String getModelCode() {
        return modelCode;
    }

    public String getModelNumber() {
        return modelNumber;
    }
}
