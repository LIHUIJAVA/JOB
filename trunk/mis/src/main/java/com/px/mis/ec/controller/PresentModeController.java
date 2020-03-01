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
import com.px.mis.ec.entity.PresentMode;
import com.px.mis.ec.service.PresentModeService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//赠品模式接口
@Controller
@RequestMapping(value="ec/presentMode",method=RequestMethod.POST)
public class PresentModeController {
	private Logger logger = LoggerFactory.getLogger(PresentModeController.class);
	@Autowired
	private PresentModeService presentModeService;
	/*添加*/
	@RequestMapping("insertPresentMode")
	public @ResponseBody Object insertPresentMode(@RequestBody String jsonData) {
		logger.info("url:/ec/presentMode/insertPresentMode");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		PresentMode presentMode=null;
		try {
			presentMode = BaseJson.getPOJO(jsonData,PresentMode.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/insertPresentMode 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/presentMode/insertPresentMode", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/insertPresentMode 异常说明：",e);
			}
			return resp;
		}
		try {
			on = presentModeService.insertPresentMode(presentMode);
			if(on==null) {
				resp =BaseJson.returnRespObj("/ec/presentMode/insertPresentMode", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/ec/presentMode/insertPresentMode", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/insertPresentMode 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deletePresentMode")
	public @ResponseBody Object deletePresentMode(@RequestBody String jsonData) {
		logger.info("url:/ec/presentMode/deletePresentMode");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer no=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			no = reqBody.get("no").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/deletePresentMode 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/presentMode/deletePresentMode", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/deletePresentMode 异常说明：",e);
			}
			return resp;
		}
		try {
			on = presentModeService.deletePresentModeById(no);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/ec/presentMode/deletePresentMode", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/ec/presentMode/deletePresentMode", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/deletePresentMode 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updatePresentMode")
	public @ResponseBody Object updatePresentMode(@RequestBody String jsonData) {
		logger.info("url:/ec/presentMode/updatePresentMode");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		PresentMode presentMode=null;
		try {
			presentMode = BaseJson.getPOJO(jsonData,PresentMode.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/updatePresentMode 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/presentMode/updatePresentMode", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/updatePresentMode 异常说明：",e);
			}
			return resp;
		}
		try {
			on = presentModeService.updatePresentModeById(presentMode);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/ec/presentMode/updatePresentMode", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/ec/presentMode/updatePresentMode", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/updatePresentMode 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectPresentMode")
	public @ResponseBody Object selectPresentMode(@RequestBody String jsonBody) {
		logger.info("url:/ec/presentMode/selectPresentMode");
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
				resp=BaseJson.returnRespList("/ec/presentMode/selectPresentMode", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/selectPresentMode 异常说明：",e1);
			try {
				resp=BaseJson.returnRespList("/ec/presentMode/selectPresentMode", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/selectPresentMode 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=presentModeService.selectPresentModeList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/selectPresentMode 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectPresentModeById")
	public @ResponseBody Object selectPresentModeById(@RequestBody String jsonData) {
		logger.info("url:/ec/presentMode/selectPresentModeById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer no=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			no = reqBody.get("no").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/selectPresentModeById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/ec/presentMode/selectPresentModeById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/selectPresentModeById 异常说明：",e);
			}
			return resp;
		}
		try {
			PresentMode presentMode = presentModeService.selectPresentModeById(no);
			if(presentMode!=null) {
				 resp=BaseJson.returnRespObj("/ec/presentMode/selectPresentModeById",true,"处理成功！",presentMode);
			}else {
				 resp=BaseJson.returnRespObj("/ec/presentMode/selectPresentModeById",false,"没有查到数据！",presentMode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/selectPresentModeById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
