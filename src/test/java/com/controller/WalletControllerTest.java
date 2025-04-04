package com.controller;

import com.controller.model.TransactionModel;
import com.controller.model.TransferModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void retrieveWallets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/wallet/"))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isOk());
    }

    @Test
    public void retrieveTransactions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/wallet/transaction/"))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isOk());
    }

    @Test
    public void retrieveBalanceWithoutUserIdError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/wallet/balance/"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void retrieveBalanceWithUserIdSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/wallet/balance/")
                .param("user_id", "1")
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void retrieveBalanceWithUserIdAndDatesSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/wallet/balance/")
                    .param("user_id", "1")
                    .param("init_date", "01/04/2025")
                    .param("end_date", "04/04/2025")
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void createTransaction() throws Exception {
        TransactionModel transaction = new TransactionModel(1L, new BigDecimal(150));
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/wallet/transaction/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transaction))
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void transferFunds() throws Exception {
        TransferModel transfer = new TransferModel(1L, 2L, new BigDecimal(10));
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/wallet/transfer/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transfer))
            )
            .andExpect(status().is2xxSuccessful());
    }
}
