package com.controller;

import com.controller.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getUsersWithSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/"))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isOk());
    }

    @Test
    public void createUserWithSuccess() throws Exception {
        UserModel userModel = new UserModel("Neto", "neto@gmail.com");
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/user/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userModel))
            )
            .andExpect(status().isCreated());
    }
}
