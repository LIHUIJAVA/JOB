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

import com.px.mis.ec.entity.BusinessType;
import com.px.mis.ec.service.BusinessTypeService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//业务类型表
@RequestMapping(value="ec/businessType",method = RequestMethod.POST)
@Controller
public class BusinessTypeController {
	
	private Logger logger = LoggerFactory.getLogger(BusinessTypeController.class);
	
	@Autowired
	private BusinessTypeService businessTypeService;
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/businessType/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			BusinessType businessType = BaseJson.getPOJO(jsonBody, BusinessType.class);
			resp=businessTypeService.add(businessType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/businessType/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			BusinessType businessType = BaseJson.getPOJO(jsonBody, BusinessType.class);
			resp=businessTypeService.edit(businessType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/businessType/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=businessTypeService.delete(BaseJson.getReqBody(jsonBody).get("busTypeId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/businessType/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=businessTypeService.query(BaseJson.getReqBody(jsonBody).get("busTypeId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/businessType/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=businessTypeService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}

}
