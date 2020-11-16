package com.proj.banktransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HttpController {
    private InputCheck inputCheck;

    @Autowired
    public HttpController(InputCheck inputCheck) {
        this.inputCheck = inputCheck;
    }

    @GetMapping(value = "send", produces = MediaType.APPLICATION_JSON_VALUE)
    public String restInOut(String sendFromId, String sendToId, String money) {
        return inputCheck.checkValid(sendFromId, sendToId, money);
    }
}
