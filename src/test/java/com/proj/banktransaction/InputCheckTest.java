package com.proj.banktransaction;

import com.proj.banktransaction.repository.ClientsRep;
import com.proj.banktransaction.validator.CheckResponse;
import com.proj.banktransaction.validator.InputCheck;
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
    private static final String ERROR = "EXCEPTION ERROR";
    private static final Integer ID1 = 1;
    private static final Integer ID2 = 2;
    private static final BigDecimal SUM = new BigDecimal("1234.230");

    @Autowired
    private InputCheck inputCheck;

    @MockBean
    private ClientsRep clientsRep;

    @Test
    void checkValidSuccess() {
        when(clientsRep.existsById(anyInt())).thenReturn(true);
        CheckResponse checkResponse = inputCheck.checkValid(ID1.toString(), ID2.toString(), SUM.toString());

        Assert.assertTrue(checkResponse.isValid());
        Assert.assertEquals(checkResponse.getSender(), ID1);
        Assert.assertEquals(checkResponse.getHost(), ID2);
        Assert.assertEquals(checkResponse.getRightSumm().compareTo(SUM), 0);
    }

    @Test
    void checkValidAllInputDataFailure() {
        CheckResponse checkResponse = inputCheck.checkValid(null, null, null);

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Для перевода необходимо ввести правильные значения:  (номер отправителя (целое число), " +
                "номер получателя (целое число), сумма перевода (пример: 2134.22))");
    }

    @Test
    void checkValidAllIdFailure() {
        when(clientsRep.existsById(anyInt())).thenReturn(false);
        CheckResponse checkResponse = inputCheck.checkValid(Integer.toString(ID1), Integer.toString(ID2), SUM.toString());

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Не найден клиент с id 1\n" +
                "Не найден клиент с id 2");
    }

    @Test
    void checkValidFistIdFailure() {
        when(clientsRep.existsById(ID1)).thenReturn(false);
        when(clientsRep.existsById(ID2)).thenReturn(true);
        CheckResponse checkResponse = inputCheck.checkValid(ID1.toString(), ID2.toString(), SUM.toString());

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Не найден клиент с id 1");
    }

    @Test
    void checkValidSecondIdFailure() {
        when(clientsRep.existsById(ID1)).thenReturn(true);
        when(clientsRep.existsById(ID2)).thenReturn(false);
        CheckResponse checkResponse = inputCheck.checkValid(ID1.toString(), ID2.toString(), SUM.toString());

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Не найден клиент с id 2");
    }

    @Test
    void checkValidGivesSameIdFailure() {
        when(clientsRep.existsById(anyInt())).thenReturn(true);
        CheckResponse checkResponse = inputCheck.checkValid(Integer.toString(ID1), Integer.toString(ID1), SUM.toString());

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Перевод самому себе невозможен. Укажите id получателя отличный от id отправителя");
    }

    @Test
    void checkValidMoneyIsMinusFailure() {
        BigDecimal minusSum = new BigDecimal("-1234.230");
        when(clientsRep.existsById(anyInt())).thenReturn(true);
        CheckResponse checkResponse = inputCheck.checkValid(Integer.toString(ID1), Integer.toString(ID2), minusSum.toString());

        Assert.assertFalse(checkResponse.isValid());
        Assert.assertNull(checkResponse.getSender());
        Assert.assertNull(checkResponse.getHost());
        Assert.assertNull(checkResponse.getRightSumm());
        Assert.assertEquals(checkResponse.getMessage(), "Заданное число меньше ноля. Укажите положительную сумму перевода.");
    }

    @Test
    void checkValidEX() {
        when(clientsRep.existsById(anyInt())).thenThrow(new RuntimeException(ERROR));

        Assert.assertEquals(inputCheck.checkValid(Integer.toString(ID1), Integer.toString(ID2), SUM.toString()).getMessage(), "EXCEPTION ERROR");
    }
}