package com.px.mis.ec.controller;

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
import com.px.mis.ec.entity.ProCondition;
import com.px.mis.ec.service.ProConditionService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//促销条件表接口
@Controller
@RequestMapping(value="ec/proCondition",method=RequestMethod.POST)
public class ProConditionController {
	private Logger logger = LoggerFactory.getLogger(ProConditionController.class);
	@Autowired
	private ProConditionService proConditionService;
	/*添加*/
	@RequestMapping("insertProCondition")
	public @ResponseBody Object insertProCondition(@RequestBody String jsonData) {
		logger.info("url:/ec/proCondition/insertProCondition");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		ProCondition proCondition=null;
		try {
			proCondition = BaseJson.getPOJO(jsonData,ProCondition.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/proCondition/insertProCondition 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/proCondition/insertProCondition", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/proCondition/insertProCondition 异常说明：",e);
			}
			return resp;
		}
		try {
			on = proConditionService.insertProCondition(proCondition);
			if(on==null) {
				resp =BaseJson.returnRespObj("/ec/proCondition/insertProCondition", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/ec/proCondition/insertProCondition", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/proCondition/insertProCondition 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteProCondition")
	public @ResponseBody Object deleteProCondition(@RequestBody String jsonData) {
		logger.info("url:/ec/proCondition/deleteProCondition");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer no=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			no = reqBody.get("no").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/proCondition/deleteProCondition 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/proCondition/deleteProCondition", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/proCondition/deleteProCondition 异常说明：",e);
			}
			return resp;
		}
		try {
			on = proConditionService.deleteProConditionById(no);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/ec/proCondition/deleteProCondition", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/ec/proCondition/deleteProCondition", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/proCondition/deleteProCondition 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateProCondition")
	public @ResponseBody Object updateProCondition(@RequestBody String jsonData) {
		logger.info("url:/ec/proCondition/updateProCondition");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		ProCondition proCondition=null;
		try {
			proCondition = BaseJson.getPOJO(jsonData,ProCondition.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/proCondition/updateProCondition 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/proCondition/updateProCondition", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/proCondition/updateProCondition 异常说明：",e);
			}
			return resp;
		}
		try {
			on = proConditionService.updateProConditionById(proCondition);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/ec/proCondition/updateProCondition", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/ec/proCondition/updateProCondition", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/proCondition/updateProCondition 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectProCondition")
	public @ResponseBody Object selectProCondition(@RequestBody String jsonBody) {
		logger.info("url:/ec/proCondition/selectProCondition");
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
				resp=BaseJson.returnRespList("/ec/proCondition/selectProCondition", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/proCondition/selectProCondition 异常说明：",e1);
			try {
				resp=BaseJson.returnRespList("/ec/proCondition/selectProCondition", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/proCondition/selectProCondition 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=proConditionService.selectProConditionList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/proCondition/selectProCondition 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectProConditionById")
	public @ResponseBody Object selectProConditionById(@RequestBody String jsonData) {
		logger.info("url:/ec/proCondition/selectProConditionById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer no=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			no = reqBody.get("no").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/proCondition/selectProConditionById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/ec/proCondition/selectProConditionById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/proCondition/selectProConditionById 异常说明：",e);
			}
			return resp;
		}
		try {
			ProCondition proCondition = proConditionService.selectProConditionById(no);
			if(proCondition!=null) {
				 resp=BaseJson.returnRespObj("/ec/proCondition/selectProConditionById",true,"处理成功！", proCondition);
			}else {
				 resp=BaseJson.returnRespObj("/ec/proCondition/selectProConditionById",false,"没有查到数据！", proCondition);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/proCondition/selectProConditionById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
