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
import com.px.mis.account.entity.InvtyDtlAcct;
import com.px.mis.account.service.InvtyDtlAcctService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//存货明细账接口
@RequestMapping(value="account/invtyDtlAcct",method=RequestMethod.POST)
@Controller
public class InvtyDtlAcctController {
	private Logger logger = LoggerFactory.getLogger(InvtyDtlAcctController.class);
	@Autowired
	private InvtyDtlAcctService invtyDtlAcctService;
	/*添加*/
	@RequestMapping("insertInvtyDtlAcct")
	public @ResponseBody Object insertInvtyDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/invtyDtlAcct/insertInvtyDtlAcct");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyDtlAcct invtyDtlAcct=null;
		try {
			invtyDtlAcct = BaseJson.getPOJO(jsonData,InvtyDtlAcct.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/insertInvtyDtlAcct 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/insertInvtyDtlAcct", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/insertInvtyDtlAcct 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyDtlAcctService.insertInvtyDtlAcct(invtyDtlAcct);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/insertInvtyDtlAcct", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/insertInvtyDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/insertInvtyDtlAcct 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteInvtyDtlAcct")
	public @ResponseBody Object deleteInvtyDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/invtyDtlAcct/deleteInvtyDtlAcct");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/deleteInvtyDtlAcct 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/deleteInvtyDtlAcct", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/deleteInvtyDtlAcct 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyDtlAcctService.deleteInvtyDtlAcctByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/deleteInvtyDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/deleteInvtyDtlAcct", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/deleteInvtyDtlAcct 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateInvtyDtlAcct")
	public @ResponseBody Object updateInvtyDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/invtyDtlAcct/updateInvtyDtlAcct");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyDtlAcct invtyDtlAcct=null;
		try {
			invtyDtlAcct = BaseJson.getPOJO(jsonData,InvtyDtlAcct.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/updateInvtyDtlAcct 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/updateInvtyDtlAcct", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/updateInvtyDtlAcct 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyDtlAcctService.updateInvtyDtlAcctByOrdrNum(invtyDtlAcct);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/updateInvtyDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/updateInvtyDtlAcct", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/updateInvtyDtlAcct 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectInvtyDtlAcct")
	public @ResponseBody Object selectInvtyDtlAcct(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyDtlAcct/selectInvtyDtlAcct");
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
				resp=BaseJson.returnRespList("/account/invtyDtlAcct/selectInvtyDtlAcct", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcct 异常说明：",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyDtlAcct/selectInvtyDtlAcct", false, "请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcct 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyDtlAcctService.selectInvtyDtlAcctList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcct 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectInvtyDtlAcctById")
	public @ResponseBody Object selectInvtyDtlAcctById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyDtlAcct/selectInvtyDtlAcctById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcctById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcctById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcctById 异常说明：",e);
			}
			return resp;
		}
		try {
			InvtyDtlAcct invtyDtlAcct = invtyDtlAcctService.selectInvtyDtlAcctByOrdrNum(ordrNum);
			if(invtyDtlAcct!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcctById",true,"处理成功！", invtyDtlAcct);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcctById",false,"没有查到数据！", invtyDtlAcct);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcctById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
