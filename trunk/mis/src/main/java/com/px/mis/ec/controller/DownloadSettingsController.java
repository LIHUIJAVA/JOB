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

import com.px.mis.ec.entity.DownloadSettings;
import com.px.mis.ec.service.DownloadSettingsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//电商订单下载设置
@RequestMapping(value = "ec/downloadSet", method = RequestMethod.POST)
@Controller
public class DownloadSettingsController {

	private Logger logger = LoggerFactory.getLogger(DownloadSettingsController.class);

	@Autowired
	private DownloadSettingsService downloadSettingsService;

	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/downloadSet/add");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			DownloadSettings downloadSettings = BaseJson.getPOJO(jsonBody, DownloadSettings.class);
			resp = downloadSettingsService.add(downloadSettings);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/downloadSet/edit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			DownloadSettings downloadSettings = BaseJson.getPOJO(jsonBody, DownloadSettings.class);
			resp = downloadSettingsService.edit(downloadSettings);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}



	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/downloadSet/queryList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = downloadSettingsService.selectList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

}
