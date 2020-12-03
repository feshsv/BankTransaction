package com.proj.banktransaction.controllers;

import com.proj.banktransaction.validator.CheckResponse;
import com.proj.banktransaction.validator.InputCheck;
import com.proj.banktransaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс для обработки Http запроса с параметрами
 */

@RestController
public class HttpController {
    private final InputCheck inputCheck;
    private final TransactionService transactionService;

    @Autowired
    public HttpController(InputCheck inputCheck, TransactionService transactionService) {
        this.inputCheck = inputCheck;
        this.transactionService = transactionService;
    }

    /**
     * Принимает три параметра по HTTP и с помощью метода checkValid класса InputCheck
     * проверяет возможно ли продолжение транзакции
     * @param sendFromId    id отправителя
     * @param sendToId      id получателя
     * @param money         сумму перевода
     * @return              Сообщение о переводе или ошибке
     */
    @GetMapping(value = "send", produces = MediaType.APPLICATION_JSON_VALUE)
    public String restInOut(String sendFromId, String sendToId, String money) {
        CheckResponse checkResponse = inputCheck.checkValid(sendFromId, sendToId, money);
        if (checkResponse.isValid()) {
            return transactionService.operation(checkResponse.getSender(), checkResponse.getHost(), checkResponse.getRightSumm());
        } else {
            return checkResponse.getMessage();
        }
    }
}
