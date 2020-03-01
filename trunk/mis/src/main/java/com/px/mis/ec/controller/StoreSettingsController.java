package com.px.mis.ec.controller;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.StoreSettingsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//店铺设置
@RequestMapping(value="ec/storeSettings",method = RequestMethod.POST)
@Controller
public class StoreSettingsController {
	
	private Logger logger = LoggerFactory.getLogger(StoreSettingsController.class);
	
	@Autowired
	private StoreSettingsService storeSettingService;
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/storeSettings/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			StoreSettings storeSetting = BaseJson.getPOJO(jsonBody, StoreSettings.class);
			resp=storeSettingService.edit(storeSetting);
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
		logger.info("url:ec/storeSettings/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=storeSettingService.queryList(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	

}
