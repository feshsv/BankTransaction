package com.proj.banktransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(HttpController.class)
class HttpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private CheckResponse checkResponse;

    private List<Clients> clientsList;

//    @BeforeEach
//    void setUp() {
//        this.clientsList = new ArrayList<>();
//        this.clientsList.add(new Clients(1, "Sasha", new BigDecimal("1234.11")));
//        this.clientsList.add(new Clients(2, "Masha", new BigDecimal("234")));
//        this.clientsList.add(new Clients(3, "Dasha", new BigDecimal("134.10")));
//    }

    @Test
    public void restInOut() throws Exception {
        when(transactionService.operation(checkResponse.getSender(), checkResponse.getHost(), checkResponse.getRightSumm())).thenReturn("Hello, Mock");
//        this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
//                .andExpect(content().string(containsString("Hello, Mock")));
    }
}