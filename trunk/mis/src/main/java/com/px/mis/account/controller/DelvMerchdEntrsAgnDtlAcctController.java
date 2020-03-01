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
import com.px.mis.account.entity.DelvMerchdEntrsAgnDtlAcct;
import com.px.mis.account.service.DelvMerchdEntrsAgnDtlAcctService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//发出商品委托代销明细账接口
@RequestMapping(value="account/delvMerchdEntrsAgnDtlAcct",method=RequestMethod.POST)
@Controller
public class DelvMerchdEntrsAgnDtlAcctController {
	private Logger logger = LoggerFactory.getLogger(DelvMerchdEntrsAgnDtlAcctController.class);
	@Autowired
	private DelvMerchdEntrsAgnDtlAcctService delvMerchdEntrsAgnDtlAcctService;
	/*添加*/
	@RequestMapping("insertDelvMerchdEntrsAgnDtlAcct")
	public @ResponseBody Object insertDelvMerchdEntrsAgnDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct=null;
		try {
			delvMerchdEntrsAgnDtlAcct = BaseJson.getPOJO(jsonData,DelvMerchdEntrsAgnDtlAcct.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct 异常说明：",e);
			}
			return resp;
		}
		try {
			on = delvMerchdEntrsAgnDtlAcctService.insertDelvMerchdEntrsAgnDtlAcct(delvMerchdEntrsAgnDtlAcct);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteDelvMerchdEntrsAgnDtlAcct")
	public @ResponseBody Object deleteDelvMerchdEntrsAgnDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct 异常说明：",e);
			}
			return resp;
		}
		try {
			on = delvMerchdEntrsAgnDtlAcctService.deleteDelvMerchdEntrsAgnDtlAcctByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateDelvMerchdEntrsAgnDtlAcct")
	public @ResponseBody Object updateDelvMerchdEntrsAgnDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct;
		try {
			delvMerchdEntrsAgnDtlAcct = BaseJson.getPOJO(jsonData,DelvMerchdEntrsAgnDtlAcct.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct 异常说明：",e);
			}
			return resp;
		}
		try {
			on = delvMerchdEntrsAgnDtlAcctService.updateDelvMerchdEntrsAgnDtlAcctByOrdrNum(delvMerchdEntrsAgnDtlAcct);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectDelvMerchdEntrsAgnDtlAcct")
	public @ResponseBody Object selectDelvMerchdEntrsAgnDtlAcct(@RequestBody String jsonBody) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct");
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
				resp=BaseJson.returnRespList("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct 异常说明：",e1);
			try {
				resp=TransformJson.returnRespList("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=delvMerchdEntrsAgnDtlAcctService.selectDelvMerchdEntrsAgnDtlAcctList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectDelvMerchdEntrsAgnDtlAcctById")
	public @ResponseBody Object selectDelvMerchdEntrsAgnDtlAcctById(@RequestBody String jsonData) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById 异常说明：",e);
			}
			return resp;
		}
		try {
			DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct = delvMerchdEntrsAgnDtlAcctService.selectDelvMerchdEntrsAgnDtlAcctByOrdrNum(ordrNum);
			if(delvMerchdEntrsAgnDtlAcct!=null) {
				 resp=BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById",true,"处理成功！", delvMerchdEntrsAgnDtlAcct);
			}else {
				 resp=BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById",false,"没有查到数据！",delvMerchdEntrsAgnDtlAcct);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
