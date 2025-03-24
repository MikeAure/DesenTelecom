package com.lu.gademo.controller;

import com.lu.gademo.utils.AlgorithmsFactory;
import com.lu.gademo.utils.DSObject;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 随机加噪算法
 */
@Controller
@RequestMapping("/RandomNoise")
@AllArgsConstructor
@Slf4j
public class RandomNoiseController {
    AlgorithmsFactory algorithmsFactory;

    @ResponseBody
    @RequestMapping(value = "/desenValue", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenValue(@RequestParam String rawData,
                             @RequestParam String samples,
                             @RequestParam String algName,
                             @RequestParam String params) {
        log.info(algName);
        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());
        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);
        DSObject resultDS = algorithmsFactory.getAlgorithmInfoFromName(algName).execute(rawObject, samples, params);
        StringBuilder resultString = new StringBuilder();
        resultDS.getList().forEach(s -> resultString.append(s).append("\n"));
        return resultString.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/desenValue2", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String desenValue2(@RequestParam String rawData,
                              @RequestParam String samples,
                              @RequestParam String algName,
                              @RequestParam String c,
                              @RequestParam String t,
                              @RequestParam String params) {

        log.info(algName);
        List<Double> rawDataList = Arrays.stream(rawData.split(","))
                .filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());
        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);
        DSObject resultDS = algorithmsFactory.getAlgorithmInfoFromName(algName).execute(rawObject, c, t, params);
        StringBuilder resultString = new StringBuilder();
        resultDS.getList().forEach(s -> resultString.append(s).append("\n"));
        return resultString.toString();
    }
}


