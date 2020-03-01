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

import com.px.mis.ec.service.AftermarketService;
import com.px.mis.util.BaseJson;

//自主售后
@RequestMapping(value="ec/aftermarket",method = RequestMethod.POST)
@Controller
public class AftermarketController {
	
	private Logger logger = LoggerFactory.getLogger(AftermarketController.class);
	
	@Autowired
	private AftermarketService aftermarketService;
	
	@RequestMapping(value="audit",method = RequestMethod.POST)
	@ResponseBody
	private String audit(@RequestBody String jsonBody) {
		logger.info("url:ec/aftermarket/audit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			String serviceId = BaseJson.getReqBody(jsonBody).get("serviceId").asText();
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			//resp = aftermarketService.audit(serviceId,accNum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/aftermarket/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = aftermarketService.queryList(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="download",method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/aftermarket/download");
		logger.info("请求参数："+jsonBody);
		String resp="";
		//resp = aftermarketService.download(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	@RequestMapping(value="downloadByStoreId",method = RequestMethod.POST)
	@ResponseBody
	private String downloadByStoreId(@RequestBody String jsonBody) {
		logger.info("url:ec/aftermarket/downloadByStoreId");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = aftermarketService.downloadByStoreId(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
}
