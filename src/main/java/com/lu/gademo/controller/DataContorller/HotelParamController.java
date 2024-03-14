package com.lu.gademo.controller.DataContorller;

import com.lu.gademo.entity.templateParam.HotelParam;
import com.lu.gademo.service.HotelParamService;
import com.lu.gademo.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/hotelparam")
public class HotelParamController {

    @Autowired
    private HotelParamService hotelParamService;

    @Autowired
    private HotelService hotelService;

    //带有重定向的
    @RequestMapping(value = "/index")
    public String index(ModelMap model){
        List<HotelParam> params=hotelParamService.findAll();
        model.addAttribute("params",params);
        return "/params/hotel";
    }

    @ResponseBody
    @RequestMapping("/list")
    public List<HotelParam> list(){
        List<HotelParam> params=hotelParamService.findAll();
        return params;
    }

    @ResponseBody
    @RequestMapping("/statisticParam")
    public List<HotelParam> statisticParam(
            @RequestParam(value="query",required=false) Integer query
    ){
        System.out.println(query);
        List<HotelParam> params=hotelParamService.findBydatatype(query);
        return params;
    }


}
