package com.px.mis.ec.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.service.LogRecordService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//日志记录
@RequestMapping(value="ec/logRecord",method = RequestMethod.POST)
@Controller
public class LogRecordController {
	
	private Logger logger = LoggerFactory.getLogger(LogRecordController.class);
	
	@Autowired
	private LogRecordService logRecordService;
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/logRecord/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			LogRecord logRecord = BaseJson.getPOJO(jsonBody, LogRecord.class);
			resp=logRecordService.add(logRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/logRecord/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			LogRecord logRecord = BaseJson.getPOJO(jsonBody, LogRecord.class);
			resp=logRecordService.edit(logRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/logRecord/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=logRecordService.delete(BaseJson.getReqBody(jsonBody).get("logId").asInt());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/logRecord/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=logRecordService.query(BaseJson.getReqBody(jsonBody).get("logId").asInt());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/logRecord/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			
			String startDate = (String) map.get("startDate");
			String endDate = (String) map.get("endDate");
			if (StringUtils.isNotEmpty(startDate)) {
				startDate = startDate + " 00:00:00";
				map.put("startDate", startDate);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				endDate = endDate + " 23:59:59";
				map.put("endDate", endDate);
			}
			
			resp=logRecordService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="exportList",method = RequestMethod.POST)
	@ResponseBody
	private String exportList(@RequestBody String jsonBody) {
		logger.info("url:ec/logRecord/exportList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String startDate = (String) map.get("startDate");
			String endDate = (String) map.get("endDate");
			if (StringUtils.isNotEmpty(startDate)) {
				startDate = startDate + " 00:00:00";
				map.put("startDate", startDate);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				endDate = endDate + " 23:59:59";
				map.put("endDate", endDate);
			}
			resp=logRecordService.exportList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="logRecordList",method = RequestMethod.POST)
	@ResponseBody
	private String logRecordList(@RequestBody String jsonBody) {
		logger.info("url:ec/logRecord/logRecordList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			String ecOrderId = BaseJson.getReqBody(jsonBody).get("ecOrderId").asText();
			resp=logRecordService.logRecordList(ecOrderId);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="logTypeList",method = RequestMethod.POST)
	@ResponseBody
	private String logTypeList(@RequestBody String jsonBody) {
		logger.info("url:ec/logRecord/logTypeList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		//String ecOrderId = BaseJson.getReqBody(jsonBody).get("ecOrderId").asText();
		resp=logRecordService.logTypeList();
		logger.info("返回参数："+resp);
		return resp;
	}


}
