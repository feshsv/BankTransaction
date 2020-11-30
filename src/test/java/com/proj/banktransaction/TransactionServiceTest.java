package com.proj.banktransaction;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceTest {
    public static final Integer SENDER_ID = 1;
    public static final Integer HOST_ID = 2;
    public static final String SENDER_CLIENT_NAME = "Vova";
    public static final String HOST_CLIENT_NAME = "Rob";
    public static final BigDecimal SENDER_CLIENT_MONEY = new BigDecimal("1000");
    public static final BigDecimal HOST_CLIENT_MONEY = new BigDecimal("2000");
    public static final BigDecimal TRANSACTION_MONEY = new BigDecimal("900");
    public static final String MESSAGE_SUCCESS = "Начальный балланс:\nОтправитель " + SENDER_CLIENT_NAME + " балланс "
            + SENDER_CLIENT_MONEY + "\n" + "Получатель " + HOST_CLIENT_NAME + " балланс "
            + HOST_CLIENT_MONEY + "\nКонечный балланс:\nОтправитель " + SENDER_CLIENT_NAME
            + " балланс " + SENDER_CLIENT_MONEY.subtract(TRANSACTION_MONEY) + "\n" + "Получатель " + HOST_CLIENT_NAME
            + " балланс " + HOST_CLIENT_MONEY.add(TRANSACTION_MONEY);
    public static final String MESSAGE_FAILURE = "Суммы на счёте не дастаточно для совершения операции."
            + " Доступные средства " + SENDER_CLIENT_MONEY;

    @Autowired
    TransactionService transactionService;

    @MockBean
    ClientsRep clientsRep;

    @Test
    void operationSuccess() {
        when(clientsRep.findById(SENDER_ID)).thenReturn(Optional.of(new Clients(SENDER_ID, SENDER_CLIENT_NAME, SENDER_CLIENT_MONEY)));
        when(clientsRep.findById(HOST_ID)).thenReturn(Optional.of(new Clients(HOST_ID, HOST_CLIENT_NAME, HOST_CLIENT_MONEY)));

        Assert.assertEquals(transactionService.operation(SENDER_ID, HOST_ID, TRANSACTION_MONEY), MESSAGE_SUCCESS);
    }

    @Test
    void operationFailure() {
        when(clientsRep.findById(SENDER_ID)).thenReturn(Optional.of(new Clients(SENDER_ID, SENDER_CLIENT_NAME, SENDER_CLIENT_MONEY)));
        when(clientsRep.findById(HOST_ID)).thenReturn(Optional.of(new Clients(HOST_ID, HOST_CLIENT_NAME, HOST_CLIENT_MONEY)));

        Assert.assertEquals(transactionService.operation(SENDER_ID, HOST_ID, TRANSACTION_MONEY.add(SENDER_CLIENT_MONEY)), MESSAGE_FAILURE);
    }
}