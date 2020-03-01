package com.px.mis.account.controller;

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
import com.px.mis.account.entity.PursStlSnglMasTab;
import com.px.mis.account.service.PursStlSnglMasTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
//采购结算接口
@RequestMapping(value="account/PursStlSnglMasTab",method=RequestMethod.POST)
@Controller
public class PursStlSnglMasTabController {
	private Logger logger = LoggerFactory.getLogger(PursStlSnglMasTabController.class);
	@Autowired
	private PursStlSnglMasTabService pursStlSnglMasTabService;
	
	/*添加*/
	@RequestMapping("addPursStlSnglMasTab")
	private @ResponseBody String addPursStlSnglMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/PursStlSnglMasTab/addPursStlSnglMasTab");
		logger.info("请求参数："+jsonData);
		String resp="";
		PursStlSnglMasTab pursStlSnglMasTab=null;
		ObjectNode aString;
		String userId="";
		String loginTime = "";
		try {
			aString = BaseJson.getReqHead(jsonData);
			userId=aString.get("accNum").asText();
			loginTime = aString.get("loginTime").asText();
			pursStlSnglMasTab = BaseJson.getPOJO(jsonData,PursStlSnglMasTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/addPursStlSnglMasTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/PursStlSnglMasTab/addPursStlSnglMasTab", false, "请求参数解析错误，请检查请求参数是否书写正确！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/addPursStlSnglMasTab 异常说明：",e);
			}
			return resp;
		}
		try {
			resp=pursStlSnglMasTabService.addPursStlSnglMasTab(userId, pursStlSnglMasTab,loginTime);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/insertPursStlSnglMasTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deletePursStlSnglMasTabList")
	public @ResponseBody String deletePursStlSnglMasTabList(@RequestBody String jsonData) {
		logger.info("url:/account/PursStlSnglMasTab/deletePursStlSnglMasTabList");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		String stlSnglId=null;
		try {
			stlSnglId = BaseJson.getReqBody(jsonData).get("stlSnglId").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/deletePursStlSnglMasTabList 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/PursStlSnglMasTab/deletePursStlSnglMasTabList", false, "请求参数解析错误，请检查请求参数是否书写正确!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/deletePursStlSnglMasTabList 异常说明：",e);
			}
			return resp;
		}
		try {
			resp=pursStlSnglMasTabService.deletePursStlSnglMasTabList(stlSnglId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/deletePursStlSnglMasTabList 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updatePursStlSnglMasTab")
	public @ResponseBody String updatePursStlSnglMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/PursStlSnglMasTab/updatePursStlSnglMasTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		PursStlSnglMasTab pursStlSnglMasTab=null;
		try {
			pursStlSnglMasTab = BaseJson.getPOJO(jsonData,PursStlSnglMasTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/updatePursStlSnglMasTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/PursStlSnglMasTab/updatePursStlSnglMasTab", false, "请求参数解析错误，请检查请求参数是否书写正确!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/updatePursStlSnglMasTab 异常说明：",e);
			}
			return resp;
		}
		try {
			resp=pursStlSnglMasTabService.updatePursStlSnglMasTab(pursStlSnglMasTab);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/updatePursStlSnglMasTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectPursStlSnglMasTabList")
	public @ResponseBody String selectPursStlSnglMasTabList(@RequestBody String jsonBody) {
		logger.info("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		Map map=null;
		Integer pageNo=null;
		Integer pageSize=null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int)map.get("pageNo");
			pageSize = (int)map.get("pageSize");
			if(pageNo==0 || pageSize==0) {
				resp=BaseJson.returnRespList("/account/PursStlSnglMasTab/selectPursStlSnglMasTabList", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabList 异常说明：",e1);
			try {
				resp=BaseJson.returnRespList("/account/PursStlSnglMasTab/selectPursStlSnglMasTabList", false, "请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabList 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=pursStlSnglMasTabService.selectPursStlSnglMasTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabList 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	/*查询单个*/
	@RequestMapping(value="selectPursStlSnglMasTabByStlSnglId")
	public @ResponseBody Object selectPursStlSnglMasTabByStlSnglId(@RequestBody String jsonData) {
		logger.info("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		String stlSnglId=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			stlSnglId = reqBody.get("stlSnglId").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId 异常说明：",e);
			}
			return resp;
		}
		try {
			resp=pursStlSnglMasTabService.selectPursStlSnglMasTabByStlSnglId(stlSnglId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
