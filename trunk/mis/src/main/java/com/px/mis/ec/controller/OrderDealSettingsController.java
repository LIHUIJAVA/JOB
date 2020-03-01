package com.px.mis.ec.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.entity.OrderDealSettings;
import com.px.mis.ec.service.OrderDealSettingsService;
import com.px.mis.util.BaseJson;

//订单处理设置
@RequestMapping(value="ec/orderDealSettings",method = RequestMethod.POST)
@Controller
public class OrderDealSettingsController {
	
	private Logger logger = LoggerFactory.getLogger(OrderDealSettingsController.class);
	
	@Autowired
	private OrderDealSettingsService orderDealSettingsService;
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/storeSetting/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			OrderDealSettings orderDealSettings = BaseJson.getPOJO(jsonBody, OrderDealSettings.class);
			resp=orderDealSettingsService.edit(orderDealSettings);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/orderDealSettings/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp = orderDealSettingsService.query(BaseJson.getReqBody(jsonBody).get("settingId").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	

}
