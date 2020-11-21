package com.proj.banktransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HttpController {
    private final InputCheck inputCheck;
    private final TransactionService transactionService;

    @Autowired
    public HttpController(InputCheck inputCheck, TransactionService transactionService) {
        this.inputCheck = inputCheck;
        this.transactionService = transactionService;
    }

    @GetMapping(value = "send", produces = MediaType.APPLICATION_JSON_VALUE)
    public String restInOut(String sendFromId, String sendToId, String money) {
        CheckResponse response = inputCheck.checkValid(sendFromId, sendToId, money);
        if (response.isValid()) {
            return transactionService.operation(response.getSender(), response.getHost(), response.getRightSumm());
        } else {
            return response.getMessage();
        }
    }
}
