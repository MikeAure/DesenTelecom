package com.lu.gademo;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.ga.Meeting;
import com.lu.gademo.service.impl.MeetingAnalysisEventListener;
import com.lu.gademo.utils.AlgorithmsFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class DealExcelTest {
    @Autowired
    private AlgorithmsFactory algorithmsFactory;
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void event() throws Exception {
        String url = "/" + "meeting" + "param/list";
        // 模拟multipart/form-data请求
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ExcelParam> excelParamList = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<List<ExcelParam>>() {
        }) ;
        Map<String, ExcelParam> config = excelParamList.stream().collect(Collectors.toMap(ExcelParam::getFieldName, Function.identity()));
        String fileName = "D:\\meeting5000wv2\\meeting100w.xlsx";
        EasyExcel.read(fileName, Meeting.class, new MeetingAnalysisEventListener(algorithmsFactory, config, "./meeting3.xlsx")).sheet().doRead();
    }

    @Test
    void testReflection() {
        Map<Integer, List<Object>> map = new HashMap<>();
        Field[] fields = Meeting.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getName());
            map.put(i, new ArrayList<>());
        }
    }
}

