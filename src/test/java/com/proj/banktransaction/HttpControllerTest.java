package com.proj.banktransaction;

import com.proj.banktransaction.controllers.HttpController;
import com.proj.banktransaction.service.TransactionService;
import com.proj.banktransaction.validator.CheckResponse;
import com.proj.banktransaction.validator.InputCheck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(HttpController.class)
class HttpControllerTest {

    private static final String OK = "Test ok";
    private static final String ERROR = "ERROR";

    @MockBean
    private InputCheck inputCheck;
    @MockBean
    private TransactionService transactionService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void restInOutSuccess() throws Exception {
        //Когда
        when(inputCheck.checkValid(any(), any(), any())).thenReturn(CheckResponse.of(10, 10, BigDecimal.TEN));
        when(transactionService.operation(eq(10), eq(10), eq(BigDecimal.TEN))).thenReturn(OK);
        //Событие
        this.mockMvc.perform(MockMvcRequestBuilders.get("/send"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString(OK)));
        //Верификация
        verify(inputCheck).checkValid(any(), any(), any());
        verify(transactionService).operation(anyInt(), anyInt(), any());
    }

    @Test
    public void restInOutFail() throws Exception {
        //Когда
        when(inputCheck.checkValid(any(), any(), any())).thenReturn(CheckResponse.of(ERROR));
        //Событие
        this.mockMvc.perform(MockMvcRequestBuilders.get("/send"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString(ERROR)));
        //Верификация
        verify(transactionService, never()).operation(any(), any(), any());
    }

}