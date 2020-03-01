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
import com.px.mis.account.entity.VouchSupvnTab;
import com.px.mis.account.service.VouchSupvnTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//制凭证并发表接口
@RequestMapping(value="account/vouchSupvnTab",method=RequestMethod.POST)
@Controller
public class VouchSupvnTabController {
	private Logger logger = LoggerFactory.getLogger(VouchSupvnTabController.class);
	
	@Autowired
	private VouchSupvnTabService vouchSupvnTabService;
	
	/*添加*/
	@RequestMapping("insertVouchSupvnTab")
	public @ResponseBody Object insertVouchSupvnTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchSupvnTab/insertVouchSupvnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		VouchSupvnTab vouchSupvnTab=null;
		try {
			vouchSupvnTab = BaseJson.getPOJO(jsonData,VouchSupvnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/insertVouchSupvnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/insertVouchSupvnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/insertVouchSupvnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = vouchSupvnTabService.insertVouchSupvnTab(vouchSupvnTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/insertVouchSupvnTab", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/insertVouchSupvnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/insertVouchSupvnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteVouchSupvnTab")
	public @ResponseBody Object deleteVouchSupvnTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchSupvnTab/deleteVouchSupvnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/deleteVouchSupvnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/deleteVouchSupvnTab", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/deleteVouchSupvnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = vouchSupvnTabService.deleteVouchSupvnTabByOrdrNum(ordrNum);
			if(on != null) {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/deleteVouchSupvnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/deleteVouchSupvnTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/deleteVouchSupvnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateVouchSupvnTab")
	public @ResponseBody Object updateVouchSupvnTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchSupvnTab/updateVouchSupvnTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		VouchSupvnTab vouchSupvnTab=null;
		try {
			vouchSupvnTab = BaseJson.getPOJO(jsonData,VouchSupvnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/updateVouchSupvnTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/updateVouchSupvnTab", false,"请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/updateVouchSupvnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = vouchSupvnTabService.updateVouchSupvnTabByOrdrNum(vouchSupvnTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/updateVouchSupvnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/updateVouchSupvnTab", false,"update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/updateVouchSupvnTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectVouchSupvnTab")
	public @ResponseBody Object selectVouchSupvnTab(@RequestBody String jsonBody) {
		logger.info("url:/account/vouchSupvnTab/selectVouchSupvnTab");
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
				resp=BaseJson.returnRespList("/account/vouchSupvnTab/selectVouchSupvnTab", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTab 异常说明：",e1);
			try {
				resp=BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTab 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=vouchSupvnTabService.selectVouchSupvnTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTab 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectVouchSupvnTabById")
	public @ResponseBody Object selectVouchSupvnTabById(@RequestBody String jsonData) {
		logger.info("url:/account/vouchSupvnTab/selectVouchSupvnTabById");
		logger.info("请求参数："+jsonData);

		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTabById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/vouchSupvnTab/selectVouchSupvnTabById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTabById 异常说明：",e);
			}
			return resp;
		}
		try {
			VouchSupvnTab vouchSupvnTab = vouchSupvnTabService.selectVouchSupvnTabByOrdrNum(ordrNum);
			if(vouchSupvnTab!=null) {
				 resp=BaseJson.returnRespObj("/account/vouchSupvnTab/selectVouchSupvnTabById",true,"处理成功！", vouchSupvnTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/vouchSupvnTab/selectVouchSupvnTabById",false,"没有查到数据！", vouchSupvnTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTabById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
}
