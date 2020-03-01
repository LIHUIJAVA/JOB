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

import com.px.mis.ec.entity.SpecialSettings;
import com.px.mis.ec.service.SpecialSettingsService;
import com.px.mis.util.BaseJson;

//专用设置
@RequestMapping(value="ec/specialSettings",method = RequestMethod.POST)
@Controller
public class SpecialSettingsController {
	
	private Logger logger = LoggerFactory.getLogger(SpecialSettingsController.class);
	
	@Autowired
	private SpecialSettingsService specialSettingsService;
	
	@RequestMapping("edit")
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/specialSetting/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			SpecialSettings storeSetting = BaseJson.getPOJO(jsonBody, SpecialSettings.class);
			resp=specialSettingsService.edit(storeSetting);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping("query")
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/specialSetting/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=specialSettingsService.query(BaseJson.getReqBody(jsonBody).get("settingId").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	

}
