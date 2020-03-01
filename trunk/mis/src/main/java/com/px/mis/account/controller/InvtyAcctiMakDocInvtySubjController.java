package com.px.mis.account.controller;

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
import com.px.mis.account.entity.InvtyAcctiMakDocInvtySubj;
import com.px.mis.account.service.InvtyAcctiMakDocInvtySubjService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//存货核算制单存货科目接口
@RequestMapping(value="account/InvtyAcctiMakDocInvtySubj",method=RequestMethod.POST)
@Controller
public class InvtyAcctiMakDocInvtySubjController {
	private Logger logger = LoggerFactory.getLogger(InvtyAcctiMakDocInvtySubjController.class);
	@Autowired
	private InvtyAcctiMakDocInvtySubjService invtyAcctiMakDocInvtySubjService;
	/*添加*/
	@RequestMapping("insertInvtyAcctiMakDocInvtySubj")
	public @ResponseBody Object insertInvtyAcctiMakDocInvtySubj(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiMakDocInvtySubj/insertInvtyAcctiMakDocInvtySubj");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj=null;
		try {
			invtyAcctiMakDocInvtySubj = BaseJson.getPOJO(jsonData,InvtyAcctiMakDocInvtySubj.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/insertInvtyAcctiMakDocInvtySubj 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/insertInvtyAcctiMakDocInvtySubj", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiMakDocInvtySubj/insertInvtyAcctiMakDocInvtySubj 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiMakDocInvtySubjService.insertInvtyAcctiMakDocInvtySubj(invtyAcctiMakDocInvtySubj);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/insertInvtyAcctiMakDocInvtySubj", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/insertInvtyAcctiMakDocInvtySubj", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/insertInvtyAcctiMakDocInvtySubj 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteInvtyAcctiMakDocInvtySubj")
	public @ResponseBody Object deleteInvtyAcctiMakDocInvtySubj(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiMakDocInvtySubj/deleteInvtyAcctiMakDocInvtySubj");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/deleteInvtyAcctiMakDocInvtySubj 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/deleteInvtyAcctiMakDocInvtySubj", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiMakDocInvtySubj/deleteInvtyAcctiMakDocInvtySubj 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiMakDocInvtySubjService.deleteInvtyAcctiMakDocInvtySubjByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/deleteInvtyAcctiMakDocInvtySubj", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/deleteInvtyAcctiMakDocInvtySubj", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/deleteInvtyAcctiMakDocInvtySubj 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateInvtyAcctiMakDocInvtySubj")
	public @ResponseBody Object updateInvtyAcctiMakDocInvtySubj(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiMakDocInvtySubj/updateInvtyAcctiMakDocInvtySubj");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj=null;
		try {
			invtyAcctiMakDocInvtySubj = BaseJson.getPOJO(jsonData,InvtyAcctiMakDocInvtySubj.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/updateInvtyAcctiMakDocInvtySubj 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/updateInvtyAcctiMakDocInvtySubj", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiMakDocInvtySubj/updateInvtyAcctiMakDocInvtySubj 异常说明：",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiMakDocInvtySubjService.updateInvtyAcctiMakDocInvtySubjByOrdrNum(invtyAcctiMakDocInvtySubj);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/updateInvtyAcctiMakDocInvtySubj", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/updateInvtyAcctiMakDocInvtySubj", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/updateInvtyAcctiMakDocInvtySubj 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectInvtyAcctiMakDocInvtySubj")
	public @ResponseBody Object selectInvtyAcctiMakDocInvtySubj(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubj");
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
				resp=BaseJson.returnRespList("/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubj", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubj 异常说明：",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubj", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubj 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyAcctiMakDocInvtySubjService.selectInvtyAcctiMakDocInvtySubjList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubj 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectInvtyAcctiMakDocInvtySubjById")
	public @ResponseBody Object selectInvtyAcctiMakDocInvtySubjById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubjById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubjById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubjById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubjById 异常说明：",e);
			}
			return resp;
		}
		try {
			InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj = invtyAcctiMakDocInvtySubjService.selectInvtyAcctiMakDocInvtySubjByOrdrNum(ordrNum);
			if(invtyAcctiMakDocInvtySubj!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubjById",true,"处理成功！", invtyAcctiMakDocInvtySubj);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubjById",false,"没有查到数据！", invtyAcctiMakDocInvtySubj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubjById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
