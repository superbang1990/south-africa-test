package org.marsclub.test;

import com.alibaba.fastjson2.annotation.JSONField;

public class PullUpLoadStatusItem {

    @JSONField(name = "vin_chassis_number")
    private String vinChassisNumber;

    @JSONField(name = "register_number")
    private String registerNumber;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "error_type")
    private String errorType;

    @JSONField(name = "error_message")
    private String errorMessage;

    public String getVinChassisNumber() {
        return vinChassisNumber;
    }

    public void setVinChassisNumber(String vinChassisNumber) {
        this.vinChassisNumber = vinChassisNumber;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}