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

import com.px.mis.ec.service.CouponDetailService;
import com.px.mis.util.BaseJson;

@RequestMapping(value="ec/couponDetail",method = RequestMethod.POST)
@Controller
public class CouponDetailController {
	private Logger logger = LoggerFactory.getLogger(AuditStrategyController.class);
	@Autowired
	private CouponDetailService couponDetailService;

	@RequestMapping(value="couponDetailList",method = RequestMethod.POST)
	@ResponseBody
	private String couponDetailList(@RequestBody String jsonBody) {
		logger.info("url:ec/couponDetail/couponDetailList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			String ecOrderId = BaseJson.getReqBody(jsonBody).get("ecOrderId").asText();
			resp=couponDetailService.couponDetailList(ecOrderId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	
	
	
}
