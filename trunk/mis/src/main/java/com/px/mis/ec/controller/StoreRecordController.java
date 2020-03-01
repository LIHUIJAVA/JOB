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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.service.StoreRecordService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//店铺档案
@RequestMapping(value="ec/storeRecord",method = RequestMethod.POST)
@Controller
public class StoreRecordController {
	
	private Logger logger = LoggerFactory.getLogger(StoreRecordController.class);
	
	@Autowired
	private StoreRecordService storeRecordService;
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/storeRecord/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			StoreRecord storeRecord = BaseJson.getPOJO(jsonBody, StoreRecord.class);
			resp=storeRecordService.add(storeRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/storeRecord/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			StoreRecord storeRecord = BaseJson.getPOJO(jsonBody, StoreRecord.class);
			resp=storeRecordService.edit(storeRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/storeRecord/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=storeRecordService.delete(BaseJson.getReqBody(jsonBody).get("storeId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/storeRecord/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=storeRecordService.query(BaseJson.getReqBody(jsonBody).get("storeId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/storeRecord/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=storeRecordService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryAll",method = RequestMethod.POST)
	@ResponseBody
	private String queryAll(@RequestBody String jsonBody) {
		logger.info("url:ec/storeRecord/queryAll");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=storeRecordService.queryAll(jsonBody);
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="download",method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/storeRecord/download");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ObjectNode objectNode=BaseJson.getReqBody(jsonBody);
			resp= storeRecordService.download(objectNode);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	

}
