package com.lu.gademo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class  GaController extends BaseController{


	@RequestMapping(value ={"/","/index"})
	public String index(){
		return "index";
	}
	@RequestMapping(value = {"/home"})
	public String home(){
		return "home";
	}

	@RequestMapping(value = {"/welcome"})
	public String welcome(){
		return "welcome";
	}
	
	@RequestMapping(value = {"/statistics"})
	public String resultShow(){
		return "statistics";
	}

	@RequestMapping(value = {"maptest"})
	public String maptest(){ return "maptest";}

	@RequestMapping(value = {"/file"})
	public String file(){
		return "file";
	}

	// 五十场景模板
	@RequestMapping(value = { "/fifty_scene/{name}" })
	public String getFiftyScenesView(@PathVariable String name){ return "fifty_scene/" + name;}

	// 脱敏算法验证  按照模态分类
	@RequestMapping(value = {"/verify/{name}"})
	public String getVerifyViews(@PathVariable String name){ return "verify/" + name;}

	// 脱敏工具设置
	@RequestMapping(value = {"/desentools/{toolName}"})
	public String getDesenToolsView(@PathVariable String toolName) {
		return "desentools/" + toolName;
	}

	// 脱敏工具设置
//	@RequestMapping(value = {"/desentools/distortion" + "text"})
//	public String distortiontext(){ return "/desentools/distortion"+"text";}
//
//	@RequestMapping(value = {"/desentools/nodistortion" + "text"})
//	public String nodistortiontext(){ return "/desentools/nodistortion"+"text";}
//
//	@RequestMapping(value = {"/desentools/distortion" + "image"})
//	public String distortionimage(){ return "/desentools/distortion"+"image";}
//
//	@RequestMapping(value = {"/desentools/nodistortion" + "image"})
//	public String nodistortionimage(){ return "/desentools/nodistortion"+"image";}
//
//	@RequestMapping(value = {"/desentools/distortion" + "video"})
//	public String distortionvideo(){ return "/desentools/distortion"+"video";}
//
//	@RequestMapping(value = {"/desentools/nodistortion" + "video"})
//	public String nodistortionvideo(){ return "/desentools/nodistortion"+"video";}
//
//	@RequestMapping(value = {"/desentools/distortion" + "audio"})
//	public String distortionaudio(){ return "/desentools/distortion"+"audio";}
//
//	@RequestMapping(value = {"/desentools/nodistortion" + "audio"})
//	public String nodistortionaudio(){ return "/desentools/nodistortion"+"audio";}
//
//	@RequestMapping(value = {"/desentools/distortion" + "graph"})
//	public String distortiongraph(){ return "/desentools/distortion"+"graph";}
//
//	@RequestMapping(value = {"/desentools/nodistortion" + "graph"})
//	public String nodistortiongraph(){ return "/desentools/nodistortion"+"graph";}

//	// 50种场景模板
//	@RequestMapping(value = { "/fifty_scene" + "/map"})
//	public String map(){ return "fifty_scene/" + "map";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/onlinetaxi"})
//	public String onlineTaxi(){ return "fifty_scene/" + "onlinetaxi";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/communication"})
//	public String communication(){ return "fifty_scene/" + "communication";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/community"})
//	public String community(){ return "fifty_scene/" + "community";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/onlinepayment"})
//	public String onlinePayment(){ return "fifty_scene/" + "onlinepayment";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/onlineshopping"})
//	public String onlineShopping(){ return "fifty_scene/" + "onlineshopping";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/takeaway"})
//	public String takeaway(){ return "fifty_scene/" + "takeaway";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/express"})
//	public String express(){ return "fifty_scene/" + "express";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/transportationticket"})
//	public String transportationTicket(){ return "fifty_scene/" + "transportationticket";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/marry"})
//	public String marry(){ return "fifty_scene/" + "marry";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/employment"})
//	public String employment(){ return "fifty_scene/" + "employment";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/onlinelending"})
//	public String onlineLending(){ return "fifty_scene/" + "onlinelending";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/house"})
//	public String house(){ return "fifty_scene/" + "house";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/usedcar"})
//	public String usedCar(){ return "fifty_scene/" + "usedcar";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/consultation"})
//	public String consultation(){ return "fifty_scene/" + "consultation";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/travel"})
//	public String travel(){ return "fifty_scene/" + "travel";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/hotel"})
//	public String hotel(){ return "fifty_scene/" + "hotel";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/game"})
//	public String game(){ return "fifty_scene/" + "game";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/education"})
//	public String education(){ return "fifty_scene/" + "education";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/locallife"})
//	public String localLife(){ return "fifty_scene/" + "locallife";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/woman"})
//	public String woman(){ return "fifty_scene/" + "woman";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/usecar"})
//	public String useCar(){ return "fifty_scene/" + "usecar";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/investment"})
//	public String investment(){ return "fifty_scene/" + "investment";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/bank"})
//	public String bank(){ return "fifty_scene/" + "bank";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/mailbox"})
//	public String mailbox(){ return "fifty_scene/" + "mailbox";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/meeting"})
//	public String meeting(){ return "fifty_scene/" + "meeting";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/webcast"})
//	public String webcast(){ return "fifty_scene/" + "webcast";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/onlinemovie"})
//	public String onlineMovie(){ return "fifty_scene/" + "onlinemovie";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/shortvideo"})
//	public String shortVideo(){ return "fifty_scene/" + "shortvideo";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/news"})
//	public String news(){ return "fifty_scene/" + "news";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/sports"})
//	public String sports(){ return "fifty_scene/" + "sports";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/browser"})
//	public String browser(){ return "fifty_scene/" + "browser";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/input"})
//	public String input(){ return "fifty_scene/" + "input";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/security"})
//	public String security(){ return "fifty_scene/" + "security";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/ebook"})
//	public String ebook(){ return "fifty_scene/" + "ebook";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/capture"})
//	public String capture(){ return "fifty_scene/" + "capture";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/appstore"})
//	public String appStore(){ return "fifty_scene/" + "appstore";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/tools"})
//	public String tools(){ return "fifty_scene/" + "tools";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/performanceticket"})
//	public String performanceTicket(){ return "fifty_scene/" + "performanceticket";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/networkaccess"})
//	public String networkaccess(){ return "fifty_scene/" + "networkaccess";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/telecommunication"})
//	public String telecommunication(){ return "fifty_scene/" + "telecommunication";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/monitor"})
//	public String monitor(){ return "fifty_scene/" + "monitor";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/pay"})
//	public String pay(){ return "fifty_scene/" + "pay";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/customerservice"})
//	public String customerservice(){ return "fifty_scene/" + "customerservice";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/schoolservice"})
//	public String schoolservice(){ return "fifty_scene/" + "schoolservice";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/smarthome"})
//	public String smarthome(){ return "fifty_scene/" + "smarthome";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/autonomousdriving"})
//	public String autonomousdriving(){ return "fifty_scene/" + "autonomousdriving";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/telemedicine"})
//	public String telemedicine(){ return "fifty_scene/" + "telemedicine";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/vr"})
//	public String vr(){ return "fifty_scene/" + "vr";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/onlinevoting"})
//	public String onlinevoting(){ return "fifty_scene/" + "onlinevoting";}
//
//	@RequestMapping(value = {"/fifty_scene" + "/onlinetaxi2"})
//	public String onlinetaxi2(){ return "fifty_scene/" + "onlinetaxi2";}




//	@RequestMapping(value = {"/verify/text"})
//	public String text(){ return "verify/"+"text";}
//
//	@RequestMapping(value = {"/verify/excel"})
//	public String excel(){ return "verify/"+"excel";}
//
//	@RequestMapping(value = {"/verify/image"})
//	public String image(){ return "verify/"+"image";}
//
//	@RequestMapping(value = {"/verify/video"})
//	public String video(){ return "verify/"+"video";}
//
//	@RequestMapping(value = {"/verify/audio"})
//	public String audio(){ return "verify/"+"audio";}
//
//	@RequestMapping(value = {"/verify/graph"})
//	public String graph(){ return "verify/"+"graph";}


//	@RequestMapping(value = {"/desentools/differential_privacy_laplace"})
//	public String dp_laplace_alg(){ return "desentools/"+"differential_privacy_laplace";}
//
//	@RequestMapping(value = {"/desentools/differential_privacy_exp"})
//	public String dp_exp_alg(){ return "desentools/"+"differential_privacy_exp";}
//
//	@RequestMapping(value = {"/desentools/differential_privacy_svt"})
//	public String dp_svt_alg(){ return "desentools/"+"differential_privacy_svt";}
//
//	@RequestMapping(value = {"/desentools/differential_privacy_ldp"})
//	public String dp_ldp_alg(){ return "desentools/"+"differential_privacy_ldp";}
//
//	@RequestMapping(value = {"/desentools/differential_privacy_random_noise"})
//	public String dp_random_noise_alg(){ return "desentools/"+"differential_privacy_random_noise";}
//
//	@RequestMapping(value = {"/desentools/generalization_num_string"})
//	public String generalization_num_string(){ return "desentools/"+"generalization_num_string";}
//
//	@RequestMapping(value = {"/desentools/generalization_location"})
//	public String generalization_location(){ return "desentools/"+"generalization_location";}
//
//	@RequestMapping(value = {"/desentools/generalization_image"})
//	public String generalization_image(){ return "desentools/"+"generalization_image";}
//
//	@RequestMapping(value = {"/desentools/generalization_video"})
//	public String generalization_video(){ return "desentools/"+"generalization_video";}
//
//	@RequestMapping(value = {"/desentools/generalization_audio"})
//	public String generalization_audio(){ return "desentools/"+"generalization_audio";}
//
//	@RequestMapping(value = {"/desentools/suppression_alg"})
//	public String suppression_alg(){ return "desentools/"+"suppression_alg";}
//
//	@RequestMapping(value = {"/desentools/k_anonymity_alg"})
//	public String k_anonymity_alg(){ return "desentools/"+"k_anonymity_alg";}
//
//	@RequestMapping(value = {"/desentools/l_diversity_alg"})
//	public String l_diversity_alg(){ return "desentools/"+"l_diversity_alg";}
//
//	@RequestMapping(value = {"/desentools/t_closeness_alg"})
//	public String t_closeness_alg(){ return "desentools/"+"t_closeness_alg";}
//
//	@RequestMapping(value = {"/desentools/replacement_num_string"})
//	public String replacement_num_string(){ return "desentools/"+"replacement_num_string";}
//
//	@RequestMapping(value = {"/desentools/replacement_image"})
//	public String replacement_image(){ return "desentools/"+"replacement_image";}
//
//	@RequestMapping(value = {"/desentools/replacement_video"})
//	public String replacement_video(){ return "desentools/"+"replacement_video";}
//
//	@RequestMapping(value = {"/desentools/replacement_audio"})
//	public String replacement_audio(){ return "desentools/"+"replacement_audio";}
//
//	@RequestMapping(value = {"/desentools/synthesis_alg"})
//	public String synthesis_alg(){ return "desentools/"+"synthesis_alg";}
//
//	@RequestMapping(value = {"/desentools/replacement_alg"})
//	public String replacement_alg(){ return "desentools/"+"replacement_alg";}
//
//	@RequestMapping(value = {"/desentools/decoupling_alg"})
//	public String decoupling_alg(){ return "desentools/"+"decoupling_alg";}
}
