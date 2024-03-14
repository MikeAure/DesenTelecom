package com.lu.gademo.controller.DataContorller;

import com.lu.gademo.controller.BaseController;
import com.lu.gademo.entity.DpHotel;
import com.lu.gademo.service.DpHotelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/dphotel")
public class DpHotelController extends BaseController {
    @Autowired
    private DpHotelService dphotelService;

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "/hotel/index";
    }

    @ResponseBody
    @RequestMapping(value = {"/list"})
    public Page<DpHotel> list(
            @RequestParam(value = "query1", required = false) String query1,
            @RequestParam(value = "query2", required = false) String query2
    ) {
        if (StringUtils.isNoneBlank(query1)) {
            return dphotelService.findByID(query1, getPageRequest());
        } else if (StringUtils.isNoneBlank(query2)) {
            return dphotelService.findByName(query2, getPageRequest());
        } else {
            return dphotelService.findAll(getPageRequest());
        }
    }
}
