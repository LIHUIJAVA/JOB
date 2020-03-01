package com.px.mis.ec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.service.OrderDealSettingsService;

@RequestMapping(value = "ec/platWhsSpecial", method = RequestMethod.POST)
@Controller
public class PlatWhsSpecialController {
	
	@Autowired
	private OrderDealSettingsService orderDealSettingsService;
	
	private Logger logger = LoggerFactory.getLogger(PlatWhsSpecialController.class);
	
	
	@RequestMapping(value="selectList",method = RequestMethod.POST)
	@ResponseBody
	private String selectList(@RequestBody String jsonBody) {
		logger.info("url:ec/platWhsSpecial/selectList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = orderDealSettingsService.selectPlatWhsSpecialList(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/platWhsSpecial/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = orderDealSettingsService.addPlatWhsSpecial(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/platWhsSpecial/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = orderDealSettingsService.deletePlatWhs(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	private String update(@RequestBody String jsonBody) {
		logger.info("url:ec/platWhsSpecial/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = orderDealSettingsService.updatePlatWhs(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
}
