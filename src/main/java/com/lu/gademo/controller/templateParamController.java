package com.lu.gademo.controller;

import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.service.impl.ExcelParamServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/")
public class templateParamController {
    @Resource
    private ExcelParamServiceImpl excelParamService;


    @ResponseBody
    @RequestMapping("/appstore" + "param/list")
    public List<ExcelParam> appstoreParamList(){

        return excelParamService.getParams("appstore" + "_param");
    }

    @ResponseBody
    @RequestMapping("/onlinetaxi" + "param/list")
    public List<ExcelParam> onlinetaxiParamList(){

        return excelParamService.getParams("onlinetaxi" + "_param");
    }

    @ResponseBody
    @RequestMapping("/onlinetaxi2" + "param/list")
    public List<ExcelParam> onlinetaxi2ParamList(){

        return excelParamService.getParams("onlinetaxi2" + "_param");
    }

    @ResponseBody
    @RequestMapping("/map" + "param/list")
    public List<ExcelParam> mapParamList(){

        return excelParamService.getParams("map" + "_param");
    }

    @ResponseBody
    @RequestMapping("/communication" + "param/list")
    public List<ExcelParam> communicationParamList(){

        return excelParamService.getParams("communication" + "_param");
    }

    @ResponseBody
    @RequestMapping("/community" + "param/list")
    public List<ExcelParam> communityParamList(){

        return excelParamService.getParams("community" + "_param");
    }

    @ResponseBody
    @RequestMapping("/onlinepayment" + "param/list")
    public List<ExcelParam> onlinepayParamList(){

        return excelParamService.getParams("onlinepayment" + "_param");
    }

    @ResponseBody
    @RequestMapping("/onlineshopping" + "param/list")
    public List<ExcelParam> onlineshoppingParamList(){

        return excelParamService.getParams("onlineshopping" + "_param");
    }

    @ResponseBody
    @RequestMapping("/takeaway" + "param/list")
    public List<ExcelParam> takeawayParamList(){

        return excelParamService.getParams("takeaway" + "_param");
    }

    @ResponseBody
    @RequestMapping("/express" + "param/list")
    public List<ExcelParam> expressParamList(){

        return excelParamService.getParams("express" + "_param");
    }

    @ResponseBody
    @RequestMapping("/transportationticket" + "param/list")
    public List<ExcelParam> transportationticketParamList(){

        return excelParamService.getParams("transportationticket" + "_param");
    }

    @ResponseBody
    @RequestMapping("/marry" + "param/list")
    public List<ExcelParam> marryParamList(){

        return excelParamService.getParams("marry" + "_param");
    }

    @ResponseBody
    @RequestMapping("/employment" + "param/list")
    public List<ExcelParam> employmentParamList(){

        return excelParamService.getParams("employment" + "_param");
    }

    @ResponseBody
    @RequestMapping("/onlinelending" + "param/list")
    public List<ExcelParam> onlinelendingParamList(){

        return excelParamService.getParams("onlinelending" + "_param");
    }

    @ResponseBody
    @RequestMapping("/house" + "param/list")
    public List<ExcelParam> houseParamList(){

        return excelParamService.getParams("house" + "_param");
    }

    @ResponseBody
    @RequestMapping("/usedcar" + "param/list")
    public List<ExcelParam> usedcarParamList(){

        return excelParamService.getParams("usedcar" + "_param");
    }

    @ResponseBody
    @RequestMapping("/consultation" + "param/list")
    public List<ExcelParam> consultationParamList(){

        return excelParamService.getParams("consultation" + "_param");
    }

    @ResponseBody
    @RequestMapping("/travel" + "param/list")
    public List<ExcelParam> travelParamList(){

        return excelParamService.getParams("travel" + "_param");
    }

    /*@ResponseBody
    @RequestMapping("/hotel" + "param/list")
    public List<ExcelParam> hotelParamList(){

        return excelParamService.getParams("hotel" + "_param");
    }*/

    @ResponseBody
    @RequestMapping("/game" + "param/list")
    public List<ExcelParam> gameParamList(){

        return excelParamService.getParams("game" + "_param");
    }

    @ResponseBody
    @RequestMapping("/education" + "param/list")
    public List<ExcelParam> educationParamList(){

        return excelParamService.getParams("education" + "_param");
    }

    @ResponseBody
    @RequestMapping("/locallife" + "param/list")
    public List<ExcelParam>locallifeParamList(){

        return excelParamService.getParams("locallife" + "_param");
    }


    @ResponseBody
    @RequestMapping("/woman" + "param/list")
    public List<ExcelParam> womanParamList(){

        return excelParamService.getParams("woman" + "_param");
    }


    @ResponseBody
    @RequestMapping("/usecar" + "param/list")
    public List<ExcelParam> usecarParamList(){

        return excelParamService.getParams("usecar" + "_param");
    }

    @ResponseBody
    @RequestMapping("/investment" + "param/list")
    public List<ExcelParam> investmentParamList(){

        return excelParamService.getParams("investment" + "_param");
    }

    @ResponseBody
    @RequestMapping("/bank" + "param/list")
    public List<ExcelParam> bankParamList(){

        return excelParamService.getParams("bank" + "_param");
    }

    @ResponseBody
    @RequestMapping("/mailbox" + "param/list")
    public List<ExcelParam> mailboxParamList(){

        return excelParamService.getParams("mailbox" + "_param");
    }

    @ResponseBody
    @RequestMapping("/meeting" + "param/list")
    public List<ExcelParam> meetingParamList(){

        return excelParamService.getParams("meeting" + "_param");
    }


    @ResponseBody
    @RequestMapping("/webcast" + "param/list")
    public List<ExcelParam> webcastParamList(){

        return excelParamService.getParams("webcast" + "_param");
    }

    @ResponseBody
    @RequestMapping("/onlinemovie" + "param/list")
    public List<ExcelParam> onlinemovieParamList(){

        return excelParamService.getParams("onlinemovie" + "_param");
    }

    @ResponseBody
    @RequestMapping("/shortvideo" + "param/list")
    public List<ExcelParam> shortvideoParamList(){

        return excelParamService.getParams("shortvideo" + "_param");
    }

    @ResponseBody
    @RequestMapping("/news" + "param/list")
    public List<ExcelParam> newsParamList(){

        return excelParamService.getParams("news" + "_param");
    }

    @ResponseBody
    @RequestMapping("/sports" + "param/list")
    public List<ExcelParam> sportsParamList(){

        return excelParamService.getParams("sports" + "_param");
    }

    @ResponseBody
    @RequestMapping("/browser" + "param/list")
    public List<ExcelParam> browserParamList(){

        return excelParamService.getParams("browser" + "_param");
    }

    @ResponseBody
    @RequestMapping("/input" + "param/list")
    public List<ExcelParam> inputParamList(){

        return excelParamService.getParams("input" + "_param");
    }

    @ResponseBody
    @RequestMapping("/security" + "param/list")
    public List<ExcelParam> securityParamList(){

        return excelParamService.getParams("security" + "_param");
    }

    @ResponseBody
    @RequestMapping("/ebook" + "param/list")
    public List<ExcelParam> ebookParamList(){

        return excelParamService.getParams("ebook" + "_param");
    }

    @ResponseBody
    @RequestMapping("/capture" + "param/list")
    public List<ExcelParam> captureParamList(){

        return excelParamService.getParams("capture" + "_param");
    }

    @ResponseBody
    @RequestMapping("/tools" + "param/list")
    public List<ExcelParam> toolsParamList(){

        return excelParamService.getParams("tools" + "_param");
    }

    @ResponseBody
    @RequestMapping("/performanceticket" + "param/list")
    public List<ExcelParam> performanceticketParamList(){

        return excelParamService.getParams("performanceticket" + "_param");
    }

    @ResponseBody
    @RequestMapping("/networkaccess" + "param/list")
    public List<ExcelParam> networkaccessParamList(){

        return excelParamService.getParams("networkaccess" + "_param");
    }

    @ResponseBody
    @RequestMapping("/telecommunication" + "param/list")
    public List<ExcelParam> telecommunicationParamList(){

        return excelParamService.getParams("telecommunication" + "_param");
    }

    @ResponseBody
    @RequestMapping("/pay" + "param/list")
    public List<ExcelParam> payParamList(){

        return excelParamService.getParams("pay" + "_param");
    }

    @ResponseBody
    @RequestMapping("/monitor" + "param/list")
    public List<ExcelParam> monitorParamList(){

        return excelParamService.getParams("monitor" + "_param");
    }

    @ResponseBody
    @RequestMapping("/customerservice" + "param/list")
    public List<ExcelParam> customerserviceParamList(){

        return excelParamService.getParams("customerservice" + "_param");
    }

    @ResponseBody
    @RequestMapping("/schoolservice" + "param/list")
    public List<ExcelParam> schoolserviceParamList(){

        return excelParamService.getParams("schoolservice" + "_param");
    }

    @ResponseBody
    @RequestMapping("/smarthome" + "param/list")
    public List<ExcelParam> smarthomeParamList(){

        return excelParamService.getParams("smarthome" + "_param");
    }

    @ResponseBody
    @RequestMapping("/autonomousdriving" + "param/list")
    public List<ExcelParam> autonomousdrivingParamList(){

        return excelParamService.getParams("autonomousdriving" + "_param");
    }

    @ResponseBody
    @RequestMapping("/telemedicine" + "param/list")
    public List<ExcelParam> telemedicineParamList(){

        return excelParamService.getParams("telemedicine" + "_param");
    }

    @ResponseBody
    @RequestMapping("/vr" + "param/list")
    public List<ExcelParam> vrParamList(){

        return excelParamService.getParams("vr" + "_param");
    }

    @ResponseBody
    @RequestMapping("/onlinevoting" + "param/list")
    public List<ExcelParam> onlinevotingParamList(){

        return excelParamService.getParams("onlinevoting" + "_param");
    }

}
