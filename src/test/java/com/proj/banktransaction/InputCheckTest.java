package com.proj.banktransaction;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    void checkValidSucces() {
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
}