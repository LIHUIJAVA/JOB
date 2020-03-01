package com.px.mis.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.system.entity.MisLog;
import com.px.mis.system.service.MisLogService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping("system/misLog")
@Controller
public class MisLogController {

	private static final Logger logger = LoggerFactory.getLogger(MisLogController.class);

	@Autowired
	private MisLogService misLogService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody String jsonBody) {
		logger.info("url:system/misLog/add");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			MisLog Mislog = BaseJson.getPOJO(jsonBody, MisLog.class);
			resp = misLogService.add(Mislog);
//			resp = BaseJson.returnRespObj("system/misLog/add", true, "新增成功", Mislog);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("system/misLog/add", false, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		logger.info("返回参数" + resp);
		return resp;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestBody String jsonBody) {
		logger.info("url:system/misLog/delete");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			String list = BaseJson.getReqBody(jsonBody).get("list").asText();
			resp = misLogService.delete(list);
//			resp = BaseJson.returnRespObj("system/misLog/delete", true, "删除成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("system/misLog/delete", false, e.getMessage(), null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		logger.info("返回参数" + resp);
		return resp;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public String edit(@RequestBody String jsonBody) {
		logger.info("url:system/misLog/edit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			MisLog Mislog = BaseJson.getPOJO(jsonBody, MisLog.class);
			resp = misLogService.edit(Mislog);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("system/misLog/edit", false, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		logger.info("返回参数" + resp);
		return resp;
	}

	@RequestMapping(value = "editList", method = RequestMethod.POST)
	@ResponseBody
	public String editList(@RequestBody String jsonBody) {
		logger.info("url:system/misLog/editList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			List<Map> cTabMap = BaseJson.getList(jsonBody);
			List<MisLog> list = new ArrayList<>();
			for (Map map : cTabMap) {
				MisLog misLog = new MisLog();
				BeanUtils.populate(misLog, map);
				list.add(misLog);
			}
			resp = misLogService.editList(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("system/misLog/editList", false, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		logger.info("返回参数" + resp);
		return resp;
	}

	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	public String queryList(@RequestBody String jsonBody) {
		logger.info("url:system/misLog/queryList");
		logger.info("请求参数：" + jsonBody);
		Map map = null;
		String resp = "";
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			if (map.get("operatingTimeEnd") !=null && !map.get("operatingTimeEnd").equals("")) {

				map.put("operatingTimeEnd", map.get("operatingTimeEnd").toString() + " 23:59:59");
			}
			if (map.get("accountLandingTimeEnd")!=null  && !map.get("accountLandingTimeEnd").equals("")) {

				map.put("accountLandingTimeEnd", map.get("accountLandingTimeEnd").toString() + " 23:59:59");
			}
			
			resp = misLogService.queryList(map);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespList("system/misLog/queryList", true, e.getMessage(), null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		logger.info("返回参数" + resp);
		return resp;
	}

	@RequestMapping(value = "queryPrint", method = RequestMethod.POST)
	@ResponseBody
	public String queryPrint(@RequestBody String jsonBody) {
		logger.info("url:system/misLog/queryPrint");
		logger.info("请求参数：" + jsonBody);
		Map map = null;
		String resp = "";
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp = misLogService.queryPrint(map);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespList("system/misLog/queryPrint", true, e.getMessage(), null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		logger.info("返回参数" + resp);
		return resp;
	}

}
