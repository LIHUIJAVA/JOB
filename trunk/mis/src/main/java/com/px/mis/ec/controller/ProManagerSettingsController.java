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

import com.px.mis.ec.entity.ProManagerSettings;
import com.px.mis.ec.service.ProManagerSettingsService;
import com.px.mis.util.BaseJson;

@RequestMapping(value="ec/proManagerSettings",method = RequestMethod.POST)
@Controller
public class ProManagerSettingsController {
	
	private Logger logger = LoggerFactory.getLogger(ProManagerSettingsController.class);
	
	@Autowired
	private ProManagerSettingsService proManagerSettingsService;
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/proManagerSettings/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ProManagerSettings proManagerSettings = BaseJson.getPOJO(jsonBody, ProManagerSettings.class);
			resp=proManagerSettingsService.edit(proManagerSettings);
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
		logger.info("url:ec/proManagerSettings/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=proManagerSettingsService.query(BaseJson.getReqBody(jsonBody).get("settingId").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	

}
