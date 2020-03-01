package com.px.mis.ec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.service.PlatWhsMappService;

@RequestMapping(value = "ec/platWhsMapp", method = RequestMethod.POST)
@Controller
public class PlatWhsMappController {
	private Logger logger = LoggerFactory.getLogger(PlatWhsMappController.class);
	@Autowired
	private PlatWhsMappService platWhsMappService;
	
	@RequestMapping(value="insert",method = RequestMethod.POST)
	@ResponseBody
	private String audit(@RequestBody String jsonBody) {
		logger.info("url:ec/platWhsMapp/insert");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = platWhsMappService.insert(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/platWhsMapp/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = platWhsMappService.delete(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	private String update(@RequestBody String jsonBody) {
		logger.info("url:ec/platWhsMapp/update");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = platWhsMappService.update(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="selectList",method = RequestMethod.POST)
	@ResponseBody
	private String selectList(@RequestBody String jsonBody) {
		logger.info("url:ec/platWhsMapp/selectList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp = platWhsMappService.selectList(jsonBody); 
		logger.info("返回参数："+resp);
		return resp;
	}

}
