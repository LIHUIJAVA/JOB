package com.px.mis.ec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.service.WhsPlatExpressMappService;

@RequestMapping(value="ec/whsPlatExpressMapp",method = RequestMethod.POST)
@Controller
public class WhsPlatExpressMappController {
private Logger logger = LoggerFactory.getLogger(WhsPlatExpressMappController.class);
	
	@Autowired
	private WhsPlatExpressMappService whsPlatExpressMappService;
	
	@RequestMapping(value="insert",method = RequestMethod.POST)
	@ResponseBody
	private String insert(@RequestBody String jsonBody) {
		logger.info("url:ec/whsPlatExpressMapp/insert");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = whsPlatExpressMappService.insert(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/whsPlatExpressMapp/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = whsPlatExpressMappService.delete(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}

	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	private String update(@RequestBody String jsonBody) {
		logger.info("url:ec/whsPlatExpressMapp/update");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = whsPlatExpressMappService.update(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}

	@RequestMapping(value="selectList",method = RequestMethod.POST)
	@ResponseBody
	private String selectList(@RequestBody String jsonBody) {
		logger.info("url:ec/whsPlatExpressMapp/selectList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = whsPlatExpressMappService.selectList(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}


	
}
