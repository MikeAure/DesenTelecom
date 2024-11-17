package com.lu.gademo.controller;

import com.lu.gademo.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Location")
public class LocationController {

    Util util;
    Generalization generalization;
    Anonymity anonymity;
    File directory;
    String currentPath;
    String program;
    String path;


    public LocationController(Util util, Generalization generalization, Anonymity anonymity) {
        this.util = util;
        this.generalization = generalization;
        this.anonymity = anonymity;
        this.directory = new File("");
        this.currentPath = directory.getAbsolutePath();
        this.program = util.isLinux() ? "LocationPrivacy" : "LocationPrivacy.exe";
        this.path = Paths.get(currentPath, program).toString();
    }

    @ResponseBody
    @RequestMapping(value = "/mixzone_1", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> mixzone_1(@RequestParam String position, @RequestParam String id, @RequestParam String time, @RequestParam String points) {

//        String[] s = position.split(",");
//        String param = "5 " + s[0] + " " + s[1] + " " + id + " " + time;
//        String[] strings = points.split(";");
//        for (String point : strings) {
//            String[] temp = point.split(",");
//            param += " " + temp[0] + " " + temp[1];
//        }
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        List<String> rawData = Arrays.asList(points.split(";"));
        DSObject dsObject = new DSObject(position, rawData);
        DSObject result = this.generalization.service(dsObject, 6, id,
                time);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());


    }

    @ResponseBody
    @RequestMapping(value = "/mixzone_3", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> mixzone_3(@RequestParam String position, @RequestParam String id, @RequestParam String time, @RequestParam String points){
//        String[] s = position.split(",");
//        String param = "6 " + s[0] + " " + s[1] + " " + id + " " + time;
//        String[] strings = points.split(";");
//        for (String point : strings) {
//            String[] temp = point.split(",");
//            param += " " + temp[0] + " " + temp[1];
//        }
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        List<String> rawData = Arrays.asList(points.split(";"));
        DSObject dsObject = new DSObject(position, rawData);
        DSObject result = this.generalization.service(dsObject, 7, id,
                time);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());

    }

    @ResponseBody
    @RequestMapping(value = "/Accuracy_reduction", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> Accuracy_reduction(@RequestParam String rawData){
//        String[] s = rawData.split(",");
//        String param = "10 " + s[0] + " " + s[1] + " 1000";
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = this.generalization.service(dsObject, 8);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/CirDummy", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> CirDummy(@RequestParam String position, @RequestParam String k,
                                 @RequestParam String s_cd, @RequestParam String rho)
            {
//        String[] s = position.split(",");
//        String param = "2 " + s[0] + " " + s[1] + " " + k + " " + s_cd + " " + rho;
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        DSObject dsObject = new DSObject(position);
        DSObject result = this.anonymity.service(dsObject, 2, k, s_cd, rho);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/GridDummy", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> GridDummy(@RequestParam String position, @RequestParam String k, @RequestParam String s_cd){
//        String[] s = position.split(",");
//        String param = "3 " + s[0] + " " + s[1] + " " + k + " " + s_cd;
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        DSObject dsObject = new DSObject(position);
        DSObject result = anonymity.service(dsObject, 3, k, s_cd);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/adaptiveIntervalCloakingWrapper", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> adaptiveIntervalCloakingWrapper(@RequestParam String rawData, @RequestParam String k, @RequestParam String min, @RequestParam String max){
//        String[] s = rawData.split(",");
//        String[] s1 = min.split(",");
//        String[] s2 = max.split(",");
//        String param = "4 " + s[0] + " " + s[1] + " " + k + " " + s1[0] + " " + s1[1] + " " + s2[0] + " " + s2[1];
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        List<String> rawList = Arrays.asList(min, max);
        DSObject dsObject = new DSObject(rawList);
        dsObject.setString(rawData);
        DSObject result = anonymity.service(dsObject, 4, k);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/CaDSA", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> CaDSA(@RequestParam String rawData, @RequestParam String op){
//        String[] s = rawData.split(",");
//        String param = "9 " + s[0] + " " + s[1] + " " + op;
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = anonymity.service(dsObject, 5, op);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/K_anonymity_position", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> K_anonymity_position(@RequestParam String rawData, @RequestParam String k){
//        String[] s = rawData.split(",");
//        String param = "1 " + s[0] + " " + s[1] + " " + k;
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = anonymity.service(dsObject, 6, k);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/Hilbert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> Hilbert(@RequestParam String rawData, @RequestParam String k){
//        String[] s = rawData.split(",");
//        String param = "8 " + s[0] + " " + s[1] + " " + k;
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = anonymity.service(dsObject, 11, k);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/SpaceTwist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<String> SpaceTwist(@RequestParam String rawData, @RequestParam String k, @RequestParam String poi){
//        String[] s = rawData.split(",");
//        String param = "7 " + s[0] + " " + s[1] + " " + k;
//        String[] strings = poi.split(";");
//        for (String point : strings) {
//            String[] temp = point.split(",");
//            param += " " + temp[0] + " " + temp[1];
//        }
//        String cmd = path + " " + param;
//        return CommandExecutor.openExe(cmd);
        List<String> poiList = Arrays.asList(poi.split(";"));
        DSObject dsObject = new DSObject(poiList);
        dsObject.setString(rawData);
        DSObject result = anonymity.service(dsObject, 12, k);
        return result.getList().stream().map(Object::toString).collect(Collectors.toList());
    }
}
