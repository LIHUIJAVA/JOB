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

import com.px.mis.ec.entity.ECPlatform;
import com.px.mis.ec.service.ECPlatformService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//电商平台
@RequestMapping(value = "ec/ecPlatform", method = RequestMethod.POST)
@Controller
public class ECPlatformController {

	private Logger logger = LoggerFactory.getLogger(ECPlatformController.class);

	@Autowired
	private ECPlatformService ecPlatformService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/ecPlatform/add");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			ECPlatform ecPlatform = BaseJson.getPOJO(jsonBody, ECPlatform.class);
			resp = ecPlatformService.add(ecPlatform);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/ecPlatform/edit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			ECPlatform ecPlatform = BaseJson.getPOJO(jsonBody, ECPlatform.class);
			resp = ecPlatformService.edit(ecPlatform);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/ecPlatform/delete");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = ecPlatformService.delete(BaseJson.getReqBody(jsonBody).get("ecId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/ecPlatform/query");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = ecPlatformService.query(BaseJson.getReqBody(jsonBody).get("ecId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/ecPlatform/queryList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = ecPlatformService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "queryAll", method = RequestMethod.POST)
	@ResponseBody
	private String queryAll(@RequestBody String jsonBody) {
		logger.info("url:ec/ecPlatform/queryAll");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = ecPlatformService.queryAll(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}

}
