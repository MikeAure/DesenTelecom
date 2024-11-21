package com.lu.gademo.controller;

import com.lu.gademo.utils.AlgorithmsFactory;
import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Dp;

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
 * 差分隐私算法
 */
@Controller
@RequestMapping("/DP")
@Slf4j
@AllArgsConstructor
public class DPController {
    AlgorithmsFactory algorithmsFactory;

    /**
     * 用于算法设置部分的接口
     * @param rawData 用户输入的原始数据
     * @param samples
     * @param algName
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/desenValue", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String desenValue(@RequestParam String rawData,
                             @RequestParam String samples,
                             @RequestParam String algName,
                             @RequestParam String params) {
        String[] types = algName.split(",");
        algName = types[types.length - 1];
        log.info(algName);
        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());
        DSObject resultDS = null;
        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);
        resultDS = algorithmsFactory.getAlgorithmInfoFromName(algName).execute(rawObject,samples, params);
        StringBuilder resultString = new StringBuilder();
        if (resultDS.getList() != null && !resultDS.getList().isEmpty()) {
            for (Object s : resultDS.getList()) {
                resultString.append(s).append("\n");
            }
            return resultString.toString();
        } else if (resultDS.getStringVal() != null) {
            return resultDS.getStringVal();
        } else {
            return "Error";
        }

    }

    @ResponseBody
    @RequestMapping(value = "/desenValue2", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String desenValue2(@RequestParam String rawData,
                              @RequestParam String samples,
                              @RequestParam String algName,
                              @RequestParam String c,
                              @RequestParam String t,
                              @RequestParam String params) {

        String[] types = algName.split(",");
        algName = types[types.length - 1];
        log.info(algName);
        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());
        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);
        DSObject result = algorithmsFactory.getAlgorithmInfoFromName(algName).execute(rawObject, c, t, params);
        StringBuilder resultString = new StringBuilder();
        for (Object s : result.getList()) {
            resultString.append(s).append("\n");
        }
        return resultString.toString();

    }


}
