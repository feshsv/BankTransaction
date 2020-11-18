package com.proj.banktransaction;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CheckResponse {
    private Integer sender;
    private Integer host;
    private BigDecimal rightSumm;
    private String message;
    private boolean isValid;


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
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
