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
import com.px.mis.account.entity.InvtyCapOcupLaytTab;
import com.px.mis.account.service.InvtyCapOcupLaytTabService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//存货资金占用规划表接口
@RequestMapping(value="account/invtyCapOcupLaytTab",method=RequestMethod.POST)
@Controller
public class InvtyCapOcupLaytTabController {
	private Logger logger = LoggerFactory.getLogger(InvtyCapOcupLaytTabController.class);
	@Autowired
	private InvtyCapOcupLaytTabService invtyCapOcupLaytTabService;
	/*添加*/
	@RequestMapping("insertInvtyCapOcupLaytTab")
	public @ResponseBody Object insertInvtyCapOcupLaytTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyCapOcupLaytTab invtyCapOcupLaytTab=null;
		try {
			invtyCapOcupLaytTab = BaseJson.getPOJO(jsonData,InvtyCapOcupLaytTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyCapOcupLaytTabService.insertInvtyCapOcupLaytTab(invtyCapOcupLaytTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteInvtyCapOcupLaytTab")
	public @ResponseBody Object deleteInvtyCapOcupLaytTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyCapOcupLaytTabService.deleteInvtyCapOcupLaytTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateInvtyCapOcupLaytTab")
	public @ResponseBody Object updateInvtyCapOcupLaytTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyCapOcupLaytTab invtyCapOcupLaytTab=null;
		try {
			invtyCapOcupLaytTab = BaseJson.getPOJO(jsonData,InvtyCapOcupLaytTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyCapOcupLaytTabService.updateInvtyCapOcupLaytTabByOrdrNum(invtyCapOcupLaytTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectInvtyCapOcupLaytTab")
	public @ResponseBody Object selectInvtyCapOcupLaytTab(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab");
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
				resp=BaseJson.returnRespList("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab 异常说明：",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab", false, "请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyCapOcupLaytTabService.selectInvtyCapOcupLaytTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectInvtyCapOcupLaytTabById")
	public @ResponseBody Object selectInvtyCapOcupLaytTabById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById 异常说明：",e);
			}
			return resp;
		}
		try {
			InvtyCapOcupLaytTab invtyCapOcupLaytTab = invtyCapOcupLaytTabService.selectInvtyCapOcupLaytTabByOrdrNum(ordrNum);
			if(invtyCapOcupLaytTab!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById",true,"处理成功！", invtyCapOcupLaytTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById",false,"没有查到数据！", invtyCapOcupLaytTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
