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
import com.px.mis.account.entity.MthTermBgnTab;
import com.px.mis.account.service.MthTermBgnTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//各月期初表接口
@RequestMapping(value="account/mthTermBgnTab",method=RequestMethod.POST)
@Controller
public class MthTermBgnTabController {
	private Logger logger = LoggerFactory.getLogger(MthTermBgnTabController.class);
	@Autowired
	private MthTermBgnTabService mthTermBgnTabService;
	/*添加*/
	@RequestMapping("insertMthTermBgnTab")
	public @ResponseBody Object insertMthTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/mthTermBgnTab/insertMthTermBgnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		MthTermBgnTab mthTermBgnTab=null;
		try {
			mthTermBgnTab = BaseJson.getPOJO(jsonData,MthTermBgnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/insertMthTermBgnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/insertMthTermBgnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/insertMthTermBgnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = mthTermBgnTabService.insertMthTermBgnTab(mthTermBgnTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/insertMthTermBgnTab", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/insertMthTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/insertMthTermBgnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteMthTermBgnTab")
	public @ResponseBody Object deleteMthTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/mthTermBgnTab/deleteMthTermBgnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/deleteMthTermBgnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/deleteMthTermBgnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/deleteMthTermBgnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = mthTermBgnTabService.deleteMthTermBgnTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/deleteMthTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/deleteMthTermBgnTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/deleteMthTermBgnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateMthTermBgnTab")
	public @ResponseBody Object updateMthTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/mthTermBgnTab/updateMthTermBgnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		MthTermBgnTab mthTermBgnTab=null;
		try {
			mthTermBgnTab = BaseJson.getPOJO(jsonData,MthTermBgnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/updateMthTermBgnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/updateMthTermBgnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/updateMthTermBgnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = mthTermBgnTabService.updateMthTermBgnTabByOrdrNum(mthTermBgnTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/updateMthTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/updateMthTermBgnTab", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/updateMthTermBgnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectMthTermBgnTab")
	public @ResponseBody Object selectMthTermBgnTab(@RequestBody String jsonBody) {
		logger.info("url:/account/mthTermBgnTab/selectMthTermBgnTab");
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
				resp=BaseJson.returnRespList("/account/mthTermBgnTab/selectMthTermBgnTab", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTab 异常说明：",e1);
			try {
				resp=BaseJson.returnRespList("/account/mthTermBgnTab/selectMthTermBgnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=mthTermBgnTabService.selectMthTermBgnTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTab 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectMthTermBgnTabById")
	public @ResponseBody Object selectMthTermBgnTabById(@RequestBody String jsonData) {
		logger.info("url:/account/mthTermBgnTab/selectMthTermBgnTabById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTabById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/mthTermBgnTab/selectMthTermBgnTabById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTabById 异常说明：",e);
			}
			return resp;
		}
		try {
			MthTermBgnTab mthTermBgnTab = mthTermBgnTabService.selectMthTermBgnTabByOrdrNum(ordrNum);
			if(mthTermBgnTab!=null) {
				 resp=BaseJson.returnRespObj("/account/mthTermBgnTab/selectMthTermBgnTabById",true,"处理成功！", mthTermBgnTab);
			}else {
				resp=BaseJson.returnRespObj("/account/mthTermBgnTab/selectMthTermBgnTabById",false,"没有查到数据！", mthTermBgnTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTabById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
