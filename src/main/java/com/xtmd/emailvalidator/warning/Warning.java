package com.xtmd.emailvalidator.warning;

public class Warning extends Exception {
    private int code;
    private String message;
    private int rfcNumber;

    public Warning() {
        this.code = 0;
        this.message = "";
        this.rfcNumber = 0;
    }

    public Warning(int code, String message, int rfcNumber) {
        super(message);
        this.code = code;
        this.message = message;
        this.rfcNumber = rfcNumber;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRfcNumber() {
        return rfcNumber;
    }

    public void setRfcNumber(int rfcNumber) {
        this.rfcNumber = rfcNumber;
    }
}
