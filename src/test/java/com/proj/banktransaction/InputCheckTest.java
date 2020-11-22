package com.proj.banktransaction;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class InputCheckTest {

    @Autowired
    private InputCheck inputCheck;

    @MockBean
    private ClientsRep clientsRep;

    @Test
    void checkValidSuccess() {
        when(clientsRep.existsById(anyInt())).thenReturn(true);
        Integer id1 = 1;
        Integer id2 = 2;
        BigDecimal sum = new BigDecimal("1234.230");
        CheckResponse checkResponse = inputCheck.checkValid(id1.toString(), id2.toString(), sum.toString());

        Assert.assertTrue(checkResponse.isValid());
        Assert.assertEquals(checkResponse.getSender(), id1);
        Assert.assertEquals(checkResponse.getHost(), id2);
        Assert.assertEquals(checkResponse.getRightSumm().compareTo(sum), 0);

    }

    @Test
    void checkValidAllIdFailure() {
        Integer id1 = 1;
        Integer id2 = 2;
        BigDecimal sum = new BigDecimal("1234.230");
        when(clientsRep.existsById(id1)).thenReturn(false);
        when(clientsRep.existsById(id2)).thenReturn(false);
        CheckResponse checkResponse = inputCheck.checkValid(id1.toString(), id2.toString(), sum.toString());

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Не найден клиент с id 1\n" +
                "Не найден клиент с id 2");
    }

    @Test
    void checkValidFistIdFailure() {
        Integer id1 = 1;
        Integer id2 = 2;
        BigDecimal sum = new BigDecimal("1234.230");
        when(clientsRep.existsById(id1)).thenReturn(false);
        when(clientsRep.existsById(id2)).thenReturn(true);
        CheckResponse checkResponse = inputCheck.checkValid(id1.toString(), id2.toString(), sum.toString());

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Не найден клиент с id 1");
    }

    @Test
    void checkValidSecondIdFailure() {
        Integer id1 = 1;
        Integer id2 = 2;
        BigDecimal sum = new BigDecimal("1234.230");
        when(clientsRep.existsById(id1)).thenReturn(true);
        when(clientsRep.existsById(id2)).thenReturn(false);
        CheckResponse checkResponse = inputCheck.checkValid(id1.toString(), id2.toString(), sum.toString());

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Не найден клиент с id 2");
    }


}