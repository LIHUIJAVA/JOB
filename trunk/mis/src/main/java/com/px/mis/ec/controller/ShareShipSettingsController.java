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

import com.px.mis.ec.entity.ShareShipSettings;
import com.px.mis.ec.service.ShareShipSettingsService;
import com.px.mis.util.BaseJson;

//合并发货策略
@RequestMapping(value="ec/shareShipSettings",method = RequestMethod.POST)
@Controller
public class ShareShipSettingsController {
	
	private Logger logger = LoggerFactory.getLogger(ShareShipSettingsController.class);
	
	@Autowired
	private ShareShipSettingsService shareShipSettingsService;
	
	@RequestMapping("edit")
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/shareShipSettings/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ShareShipSettings shareShipSettings = BaseJson.getPOJO(jsonBody, ShareShipSettings.class);
			resp=shareShipSettingsService.edit(shareShipSettings);
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
		logger.info("url:ec/shareShipSettings/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=shareShipSettingsService.query(BaseJson.getReqBody(jsonBody).get("settingId").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	

}
