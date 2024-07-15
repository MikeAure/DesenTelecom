package com.lu.gademo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void locationControllerAccuracyTest() throws Exception {
        String url = "/Location/Accuracy_reduction";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("rawData", "116.359642,39.659324")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerKAnonyTest() throws Exception {
        String url = "/Location/K_anonymity_position";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("rawData", "116.359642,39.659324")
                        .param("k", "4")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerHilbertTest() throws Exception {
        String url = "/Location/Hilbert";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("rawData", "3.56230,4.36520")
                        .param("k", "4")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerCaDSATest() throws Exception {
        String url = "/Location/CaDSA";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("rawData", "116.359642,39.659324")
                        .param("op", "2")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerSpaceTwistTest() throws Exception {
        String url = "/Location/SpaceTwist";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("rawData", "116.359642,39.659324")
                        .param("k", "2")
                        .param("poi", "39.959201,116.319997")
                        .param("poi", "40.139234,115.892943")
                        .param("poi", "40.310622,117.003926")
                        .param("poi", "39.560823,116.790982;")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerMinZone1Test() throws Exception {
        String url = "/Location/mixzone_1";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("position", "116.435842,39.941626")
                        .param("id", "115")
                        .param("time", "8.5")
                        .param("points", "116.435842,39.941626;116.353714,39.939588;116.435806,39.908501;116.356866,39.907242")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerMinZone3Test() throws Exception {
        String url = "/Location/mixzone_3";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("position", "116.435842,39.941626")
                        .param("id", "115")
                        .param("time", "8.5")
                        .param("points", "116.435842,39.941626;116.353714,39.939588;116.435806,39.908501;116.356866,39.907242")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerCirDummyTest() throws Exception {
        String url = "/Location/CirDummy";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("position", "116.435842,39.941626")
                        .param("k", "3")
                        .param("s_cd", "10")
                        .param("rho", "0.5")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerGridDummyTest() throws Exception {
        String url = "/Location/GridDummy";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("position", "116.435842,39.941626")
                        .param("k", "6")
                        .param("s_cd", "16")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void locationControllerAdaptiveIntervalCloakingWrapperTest() throws Exception {
        String url = "/Location/adaptiveIntervalCloakingWrapper";
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .param("rawData", "39.952695,116.231002")
                        .param("k", "6")
                        .param("min", "39.921459,116.210864")
                        .param("max", "39.962555,116.291789")
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
