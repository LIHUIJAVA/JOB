package com.px.mis.system.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.px.mis.system.entity.MisUser;
import com.px.mis.system.entity.Role;
import com.px.mis.system.entity.RoleMenu;
import com.px.mis.system.service.RoleService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping(value="system/role",method = RequestMethod.POST)
@Controller
public class RoleController {
	
	private Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:system/role/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Role role = BaseJson.getPOJO(jsonBody, Role.class);
			resp=roleService.add(role);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:system/role/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=roleService.edit(jsonBody);
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:system/role/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=roleService.delete(BaseJson.getReqBody(jsonBody).get("list").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:system/role/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=roleService.query(BaseJson.getReqBody(jsonBody).get("id").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:system/role/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=roleService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryAll",method = RequestMethod.POST)
	@ResponseBody
	private String queryAll(@RequestBody String jsonBody) {
		logger.info("url:system/role/queryAll");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=roleService.queryAll();
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="permAss",method = RequestMethod.POST)
	@ResponseBody
	private String permAss(@RequestBody String jsonBody) {
		logger.info("url:system/role/permAss");
		logger.info("请求参数："+jsonBody);
		String resp=roleService.permAss(jsonBody);
		logger.info("返回参数："+resp);
		return resp;
	}
}
