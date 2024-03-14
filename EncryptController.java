package com.lu.gademo.controller;

import com.lu.gademo.trace.client.user.Customer;
import com.lu.gademo.trace.client.user.Driver;
import com.lu.gademo.trace.server.gui.ServerMain;
import com.lu.gademo.timeSeries.MainTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Encrypt")
public class EncryptController {
    @Autowired
    private Customer customer;
    @Autowired
    private Driver driver0;

    @Autowired
    @Qualifier("driver1")
    private Driver driver1;
    @Autowired
    private ServerMain server = new ServerMain();

    @Autowired
    private SimpMessagingTemplate template;

    @ResponseBody
    @RequestMapping(value = "/desenGraph", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenGraph(@RequestParam String rawData) throws Exception {

        return MainTest.encryptGraph(rawData);
    }

    @ResponseBody
    @RequestMapping(value = "/customerSetStartCoordinate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse customerSetStartCoordinate(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        try{
            customer.setStartLatitude(Double.parseDouble(startLatitude));
            customer.setStartLongitude(Double.parseDouble(startLongitude));
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/customerSetEndCoordinate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse customerSetEndCoordinate(@RequestParam String endLatitude, @RequestParam String endLongitude) throws Exception {
        try{
            customer.setEndLatitude(Double.parseDouble(endLatitude));
            customer.setEndLongitude(Double.parseDouble(endLongitude));
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }

    }
    @ResponseBody
    @RequestMapping(value = "/traceCustomerLogin", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse traceCustomerLogin() throws Exception {

        try {
            customer.login();

            customer.sendEncryptedMapData(customer.getStartLatitude(), customer.getEndLongitude());
            customer.sendSquareAndCircleData();

//            customer.connThread.join();
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/traceDriver1Login", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse traceDriver1Login(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        driver0.setStartLatitude(Double.parseDouble(startLatitude));
        driver0.setStartLongitude(Double.parseDouble(startLongitude));
        try {
            driver0.login();
            driver0.sendEncryptedMapData(driver0.getStartLatitude(), driver0.getStartLongitude());
//            driver1.connThread.join();
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/traceDriver2Login", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse traceDriver2Login(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        driver1.setStartLatitude(Double.parseDouble(startLatitude));
        driver1.setStartLongitude(Double.parseDouble(startLongitude));
        try {
            driver1.login();
            driver1.sendEncryptedMapData(driver1.getStartLatitude(), driver1.getStartLongitude());
//            driver2.connThread.join();
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/serverStart", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse serverStart() {
        try {
            server.start();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("running"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/serverStop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse serverStop() {
        try {
            server.stop();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/customerStop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse customerStop() {
        try {
            customer.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/driver1Stop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse driver1Stop() {
        try {
            driver0.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/driver2Stop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse driver2Stop() {
        try {
            driver1.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/orderResult", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse orderResult() {
        if (customer.IS_ORDER_ACCEPTED) {
            String driverName = customer.driverList.get(0).getUserName();
            if (driverName.equals("driver0")) {
                return new ServerResponse("ok", "司机：" + "driver1" + "接单了");
            } else if (driverName.equals("driver1")) {
                return new ServerResponse("ok", "司机：" + "driver2" + "接单了");
            } else {
                return new ServerResponse("error");
            }
        }
        return new ServerResponse("error");
    }

}
