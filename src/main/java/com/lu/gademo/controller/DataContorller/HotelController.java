package com.lu.gademo.controller.DataContorller;

import com.lu.gademo.controller.BaseController;
import com.lu.gademo.entity.Hotel;
import com.lu.gademo.entity.templateParam.HotelParam;
import com.lu.gademo.service.HotelParamService;
import com.lu.gademo.service.HotelService;
import com.lu.gademo.vo.NumsView;
import com.lu.gademo.vo.SingleView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hotel")
public class HotelController extends BaseController {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelParamService hotelParamService;

    @RequestMapping(value = { "/", "/index" })
    public String index() {
        return "/hotel/index";
    }

    @ResponseBody
    @RequestMapping(value = { "/list" })
    public Page<Hotel> list(
            @RequestParam(value="query1",required=false) String query1,
            @RequestParam(value="query2",required=false) String query2
    ) {

        System.out.println("查询值1:"+query1+"  "+"查询值2:"+query2);
        if (StringUtils.isNoneBlank(query1)){
            return hotelService.findByID(query1, getPageRequest());
        } else if (StringUtils.isNoneBlank(query2)) {
            return hotelService.findByName(query2, getPageRequest());
        }else {
            return hotelService.findAll(getPageRequest());
        }
    }

    //第二种写法，前台通过ajax传输数据
    @ResponseBody
    @RequestMapping(value = "/dp",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String dp(@RequestBody List<HotelParam> params) {
        System.out.println(params.size());
        try{
              hotelService.tmOperate(params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "successAll";
    }

    @ResponseBody
    @RequestMapping(value = { "/statistic/num" })
    public List<NumsView> stastic1(
            @RequestParam(value="query1",required=false) String query1,
            @RequestParam(value="query2",required=false) String query2
    ) {
        System.out.println(query1+"  "+query2);
        //划分字符串
        String[] seStr=query1.split("//+")[0].split(" ");
        for (String s:seStr
        ) {
            System.out.println(s);
        }
        List<NumsView> res = null;

        try {
            res=hotelService.statisticNums(seStr,query2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = { "/statistic/single" })
    public List<SingleView> stastic2(
            @RequestParam(value="query1",required=false) String query1,
            @RequestParam(value="query2",required=false) String query2
    ) {
        System.out.println(query1+"  "+query2);
        //划分字符串
        String[] seStr= query1.split("//+")[0].split(" ");
        for (String s:seStr
        ) {
            System.out.println(s);
        }

        List<SingleView> res = null;

        try {
            res=hotelService.statisticSingles(seStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
