package com.lu.gademo.controllerTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NonDistortionGraphTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testTimeSeriesQuery() throws Exception {
        String request = "17,17,17,17,17,17,17,17,17,17,17,17";
        String url = "/Encrypt/desenGraph";
        for (int i = 0; i < 10; i++) {
            MvcResult result = mockMvc.perform(post(url)
                            .param("rawData", request)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED) // 设置内容类型为表单
                            .accept(MediaType.APPLICATION_JSON)) // 设置接受的返回值类型为JSON
                    .andExpect(status().isOk()) // 期待状态码200
                    .andExpect(content().contentType("application/json;charset=UTF-8")) // 期待返回的内容类型为JSON
                    .andReturn();

            String responseContent = result.getResponse().getContentAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseContent);
            String response = jsonResponse.get("message").get("result").asText();
            System.out.println(response);
        }

    }
}
