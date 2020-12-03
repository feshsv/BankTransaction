package com.proj.banktransaction.validator;

import java.math.BigDecimal;

/**
 * Класс нужен для получения объекта валидации методом checkValid класса InputCheck.
 */

public class CheckResponse {
    private Integer sender;
    private Integer host;
    private BigDecimal rightSumm;
    private String message;
    private boolean valid;

    public static CheckResponse of(Integer sender, Integer host, BigDecimal rightSumm) {
        CheckResponse checkResponse = new CheckResponse();
        checkResponse.setHost(host);
        checkResponse.setSender(sender);
        checkResponse.setRightSumm(rightSumm);
        checkResponse.setValid(true);
        return checkResponse;
    }

    public static CheckResponse of(String message) {
        CheckResponse checkResponse = new CheckResponse();
        checkResponse.setValid(false);
        checkResponse.setMessage(message);
        return checkResponse;
    }


    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getHost() {
        return host;
    }

    public void setHost(Integer host) {
        this.host = host;
    }

    public BigDecimal getRightSumm() {
        return rightSumm;
    }

    public void setRightSumm(BigDecimal rightSumm) {
        this.rightSumm = rightSumm;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
