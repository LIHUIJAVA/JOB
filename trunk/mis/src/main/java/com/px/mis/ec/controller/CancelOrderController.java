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

import com.px.mis.ec.service.CancelOrderService;
import com.px.mis.util.BaseJson;

//取消订单表
@RequestMapping(value="ec/cancelOrder",method = RequestMethod.POST)
@Controller
public class CancelOrderController {
	
	private Logger logger = LoggerFactory.getLogger(CancelOrderController.class);
	
	@Autowired
	private CancelOrderService cancelOrderService;
	
	@RequestMapping(value="audit",method = RequestMethod.POST)
	@ResponseBody
	private String audit(@RequestBody String jsonBody) {
		logger.info("url:ec/cancelOrder/audit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		//resp = cancelOrderService.audit(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/cancelOrder/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = cancelOrderService.queryList(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="download",method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/cancelOrder/download");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			String storeId = BaseJson.getReqBody(jsonBody).get("storeId").asText();
			String ecorderid = BaseJson.getReqBody(jsonBody).get("ecOrderId").asText();
			//resp = cancelOrderService.download(storeId,ecorderid); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("返回参数："+resp);
		return resp;
	}
	
}
