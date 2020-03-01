package com.px.mis.ec.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.service.CompensateService;

//赔付管理
@RequestMapping(value="ec/compensate",method = RequestMethod.POST)
@Controller
public class CompensateController {
	
	private Logger logger = LoggerFactory.getLogger(CompensateController.class);
	
	@Autowired
	private CompensateService compensateService;
	
	@RequestMapping(value="audit",method = RequestMethod.POST)
	@ResponseBody
	private String audit(@RequestBody String jsonBody) {
		logger.info("url:ec/compensate/audit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		//resp = compensateService.audit(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/compensate/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = compensateService.queryList(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="download",method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/compensate/download");
		logger.info("请求参数："+jsonBody);
		String resp="";
		//resp = compensateService.download(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
}
