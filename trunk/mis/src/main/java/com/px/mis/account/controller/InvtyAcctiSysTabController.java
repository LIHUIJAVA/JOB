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
import com.px.mis.account.entity.InvtyAcctiSysTab;
import com.px.mis.account.service.InvtyAcctiSysTabService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//存货核算系统表接口
@RequestMapping(value="account/invtyAcctiSysTab",method=RequestMethod.POST)
@Controller
public class InvtyAcctiSysTabController {
	private Logger logger = LoggerFactory.getLogger(InvtyAcctiSysTabController.class);
	@Autowired
	private InvtyAcctiSysTabService invtyAcctiSysTabService;
	/*添加*/
	@RequestMapping("insertInvtyAcctiSysTab")
	public @ResponseBody Object insertInvtyAcctiSysTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiSysTab/insertInvtyAcctiSysTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyAcctiSysTab invtyAcctiSysTab=null;
		try {
			invtyAcctiSysTab = BaseJson.getPOJO(jsonData,InvtyAcctiSysTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/insertInvtyAcctiSysTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/insertInvtyAcctiSysTab", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/insertInvtyAcctiSysTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiSysTabService.insertInvtyAcctiSysTab(invtyAcctiSysTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/insertInvtyAcctiSysTab", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/insertInvtyAcctiSysTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/insertInvtyAcctiSysTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteInvtyAcctiSysTab")
	public @ResponseBody Object deleteInvtyAcctiSysTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiSysTabService.deleteInvtyAcctiSysTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateInvtyAcctiSysTab")
	public @ResponseBody Object updateInvtyAcctiSysTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiSysTab/updateInvtyAcctiSysTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyAcctiSysTab invtyAcctiSysTab=null;
		try {
			invtyAcctiSysTab = BaseJson.getPOJO(jsonData,InvtyAcctiSysTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/updateInvtyAcctiSysTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/updateInvtyAcctiSysTab", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/updateInvtyAcctiSysTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiSysTabService.updateInvtyAcctiSysTabByOrdrNum(invtyAcctiSysTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/updateInvtyAcctiSysTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/updateInvtyAcctiSysTab", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/updateInvtyAcctiSysTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectInvtyAcctiSysTab")
	public @ResponseBody Object selectInvtyAcctiSysTab(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTab");
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
				resp=BaseJson.returnRespList("/account/invtyAcctiSysTab/selectInvtyAcctiSysTab", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTab 异常说明：",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyAcctiSysTab/selectInvtyAcctiSysTab", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTab 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyAcctiSysTabService.selectInvtyAcctiSysTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTab 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectInvtyAcctiSysTabById")
	public @ResponseBody Object selectInvtyAcctiSysTabById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById 异常说明：",e);
			}
			return resp;
		}
		try {
			InvtyAcctiSysTab invtyAcctiSysTab = invtyAcctiSysTabService.selectInvtyAcctiSysTabByOrdrNum(ordrNum);
			if(invtyAcctiSysTab!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById",true,"处理成功！", invtyAcctiSysTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById",false,"没有查到数据！", invtyAcctiSysTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
