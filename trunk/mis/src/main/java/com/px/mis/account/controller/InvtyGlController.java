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
import com.px.mis.account.entity.InvtyGl;
import com.px.mis.account.service.InvtyGlService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//存货总账接口
@RequestMapping(value="account/invtyGl",method=RequestMethod.POST)
@Controller
public class InvtyGlController {
	private Logger logger = LoggerFactory.getLogger(InvtyGlController.class);
	@Autowired
	private InvtyGlService invtyGlService;
	/*添加*/
	@RequestMapping("insertInvtyGl")
	public @ResponseBody Object insertInvtyGl(@RequestBody String jsonData) {
		logger.info("url:/account/invtyGl/insertInvtyGl");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyGl invtyGl=null;
		try {
			invtyGl = BaseJson.getPOJO(jsonData,InvtyGl.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/insertInvtyGl 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyGl/insertInvtyGl", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/insertInvtyGl 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyGlService.insertInvtyGl(invtyGl);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyGl/insertInvtyGl", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyGl/insertInvtyGl", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/insertInvtyGl 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteInvtyGl")
	public @ResponseBody Object deleteInvtyGl(@RequestBody String jsonData) {
		logger.info("url:/account/invtyGl/deleteInvtyGl");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/deleteInvtyGl 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyGl/deleteInvtyGl", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/deleteInvtyGl 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyGlService.deleteInvtyGlByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyGl/deleteInvtyGl", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyGl/deleteInvtyGl", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/deleteInvtyGl 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateInvtyGl")
	public @ResponseBody Object updateInvtyGl(@RequestBody String jsonData) {
		logger.info("url:/account/invtyGl/updateInvtyGl");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyGl invtyGl=null;
		try {
			invtyGl = BaseJson.getPOJO(jsonData,InvtyGl.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/updateInvtyGl 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyGl/updateInvtyGl", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/updateInvtyGl 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyGlService.updateInvtyGlByOrdrNum(invtyGl);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyGl/updateInvtyGl", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyGl/updateInvtyGl", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/updateInvtyGl 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectInvtyGl")
	public @ResponseBody Object selectInvtyGl(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyGl/selectInvtyGl");
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
				resp=BaseJson.returnRespList("/account/invtyGl/selectInvtyGl", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/selectInvtyGl 异常说明：",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyGl/selectInvtyGl", false, "请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/selectInvtyGl 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyGlService.selectInvtyGlList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/selectInvtyGl 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectInvtyGlById")
	public @ResponseBody Object selectInvtyGlById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyGl/selectInvtyGlById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/selectInvtyGlById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyGl/selectInvtyGlById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/selectInvtyGlById 异常说明：",e);
			}
			return resp;
		}
		try {
			InvtyGl invtyGl = invtyGlService.selectInvtyGlByOrdrNum(ordrNum);
			if(invtyGl!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyGl/selectInvtyGlById",true,"处理成功！", invtyGl);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyGl/selectInvtyGlById",false,"没有查到数据！", invtyGl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/selectInvtyGlById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
