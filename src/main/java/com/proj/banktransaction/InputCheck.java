package com.proj.banktransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Component
public class InputCheck {
    private final ClientsRep clientsRep;
    private final CheckResponse checkResponse;

    private Integer sender;
    private Integer host;
    private BigDecimal rightSumm;


    private static final String ERROR = "Для перевода необходимо ввести правильные значения:  (номер отправителя (целое число), " +
            "номер получателя (целое число), сумма перевода (пример: 2134.22))";
    private static final String ERROR_WITH_ZERO = "Заданное число меньше ноля. Укажите положительную сумму перевода.";
    private static final String ERROR_SAME_ID = "Перевод самому себе невозможен.";
    private static final String ERROR_CLIENT_NOT_FOUND = "Не найден клиент с id ";


    @Autowired
    public InputCheck(ClientsRep clientsRep, CheckResponse checkResponse) {
        this.clientsRep = clientsRep;
        this.checkResponse = checkResponse;
    }


    public CheckResponse checkValid(String sendFromId, String sendToId, String money) {
        String message = "ok";
        try {
            sender = Integer.valueOf(sendFromId);
            host  = Integer.valueOf(sendToId);
            rightSumm = new BigDecimal(money);
            rightSumm = rightSumm.setScale(2, RoundingMode.HALF_UP);
            if (BigDecimal.ZERO.compareTo(rightSumm) > 0) {
                message = ERROR_WITH_ZERO;
            }
            if (sender.equals(host)) {
                message = ERROR_SAME_ID;
            }
            if (!clientsRep.existsById(sender)) {
                message = ERROR_CLIENT_NOT_FOUND + sender.toString();
            }
            if (!clientsRep.existsById(host)) {
                if (message.contains("Не найден клиент с id")) {
                    message += "\n" + ERROR_CLIENT_NOT_FOUND + host.toString();
                } else {
                    message = ERROR_CLIENT_NOT_FOUND + host.toString();
                }
            }
        } catch (NumberFormatException | NullPointerException e) {
            message = ERROR;
        } catch (Exception e) {
            message = e.toString();
        }

        if (message.equals("ok")) {
            checkResponse.setSender(sender);
            checkResponse.setHost(host);
            checkResponse.setRightSumm(rightSumm);
            checkResponse.setMessage(message);
            checkResponse.setValid(true);
        } else {
            checkResponse.setMessage(message);
            checkResponse.setValid(false);
        }
        return checkResponse;
    }
}