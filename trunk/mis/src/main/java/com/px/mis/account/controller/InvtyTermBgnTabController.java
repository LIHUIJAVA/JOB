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
import com.px.mis.account.entity.InvtyTermBgnTab;
import com.px.mis.account.service.InvtyTermBgnTabService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//库存期初接口
@RequestMapping(value="account/invtyTermBgnTab",method=RequestMethod.POST)
@Controller
public class InvtyTermBgnTabController {
	private Logger logger = LoggerFactory.getLogger(InvtyTermBgnTabController.class);
	@Autowired
	private InvtyTermBgnTabService invtyTermBgnTabService;
	/*添加*/
	@RequestMapping("insertInvtyTermBgnTab")
	public @ResponseBody Object insertInvtyTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyTermBgnTab/insertInvtyTermBgnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyTermBgnTab invtyTermBgnTab=null;
		try {
			invtyTermBgnTab = BaseJson.getPOJO(jsonData,InvtyTermBgnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/insertInvtyTermBgnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/insertInvtyTermBgnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/insertInvtyTermBgnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyTermBgnTabService.insertInvtyTermBgnTab(invtyTermBgnTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/insertInvtyTermBgnTab", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/insertInvtyTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/insertInvtyTermBgnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteInvtyTermBgnTab")
	public @ResponseBody Object deleteInvtyTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyTermBgnTab/deleteInvtyTermBgnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/deleteInvtyTermBgnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/deleteInvtyTermBgnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/deleteInvtyTermBgnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyTermBgnTabService.deleteInvtyTermBgnTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/deleteInvtyTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/deleteInvtyTermBgnTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/deleteInvtyTermBgnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateInvtyTermBgnTab")
	public @ResponseBody Object updateInvtyTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyTermBgnTab/updateInvtyTermBgnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyTermBgnTab invtyTermBgnTab=null;
		try {
			invtyTermBgnTab = BaseJson.getPOJO(jsonData,InvtyTermBgnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/updateInvtyTermBgnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/updateInvtyTermBgnTab",false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/updateInvtyTermBgnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyTermBgnTabService.updateInvtyTermBgnTabByOrdrNum(invtyTermBgnTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/updateInvtyTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/updateInvtyTermBgnTab",false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/updateInvtyTermBgnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectInvtyTermBgnTab")
	public @ResponseBody Object selectInvtyTermBgnTab(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyTermBgnTab/selectInvtyTermBgnTab");
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
				resp=BaseJson.returnRespList("/account/invtyTermBgnTab/selectInvtyTermBgnTab", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTab 异常说明：",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyTermBgnTab/selectInvtyTermBgnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyTermBgnTabService.selectInvtyTermBgnTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTab 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectInvtyTermBgnTabById")
	public @ResponseBody Object selectInvtyTermBgnTabById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyTermBgnTab/selectInvtyTermBgnTabById");
		logger.info("请求参数："+jsonData);

		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTabById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyTermBgnTab/selectInvtyTermBgnTabById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTabById 异常说明：",e);
			}
			return resp;
		}
		try {
			InvtyTermBgnTab invtyTermBgnTab = invtyTermBgnTabService.selectInvtyTermBgnTabByOrdrNum(ordrNum);
			if(invtyTermBgnTab!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyTermBgnTab/selectInvtyTermBgnTabById",true,"处理成功！", invtyTermBgnTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyTermBgnTab/selectInvtyTermBgnTabById",false,"没有查到数据！", invtyTermBgnTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTabById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
