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
import com.px.mis.account.entity.CurmthIntoWhsCostTab;
import com.px.mis.account.service.CurmthIntoWhsCostTabService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//本月入库成本表接口
@RequestMapping(value="account/curmthIntoWhsCostTab",method=RequestMethod.POST)
@Controller
public class CurmthIntoWhsCostTabController {
	private Logger logger = LoggerFactory.getLogger(CurmthIntoWhsCostTabController.class);
	@Autowired
	private CurmthIntoWhsCostTabService curmthIntoWhsCostTabService;
	/*添加*/
	@RequestMapping("insertCurmthIntoWhsCostTab")
	public @ResponseBody Object insertCurmthIntoWhsCostTab(@RequestBody String jsonData) {
		logger.info("url:/account/curmthIntoWhsCostTab/insertCurmthIntoWhsCostTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		CurmthIntoWhsCostTab curmthIntoWhsCostTab=null;
		try {
			curmthIntoWhsCostTab = BaseJson.getPOJO(jsonData,CurmthIntoWhsCostTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/insertCurmthIntoWhsCostTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/insertCurmthIntoWhsCostTab", false, "请求参数解析错误，请检查请求参数是否正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthIntoWhsCostTab/insertCurmthIntoWhsCostTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = curmthIntoWhsCostTabService.insertCurmthIntoWhsCostTab(curmthIntoWhsCostTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/insertCurmthIntoWhsCostTab", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/insertCurmthIntoWhsCostTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/insertCurmthIntoWhsCostTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteCurmthIntoWhsCostTab")
	public @ResponseBody Object deleteCurmthIntoWhsCostTab(@RequestBody String jsonData) {
		logger.info("url:/account/curmthIntoWhsCostTab/deleteCurmthIntoWhsCostTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/deleteCurmthIntoWhsCostTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/deleteCurmthIntoWhsCostTab", false, "请求参数解析错误，请检查请求参数是否正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthIntoWhsCostTab/deleteCurmthIntoWhsCostTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = curmthIntoWhsCostTabService.deleteCurmthIntoWhsCostTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/deleteCurmthIntoWhsCostTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/deleteCurmthIntoWhsCostTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/deleteCurmthIntoWhsCostTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateCurmthIntoWhsCostTab")
	public @ResponseBody Object updateCurmthIntoWhsCostTab(@RequestBody String jsonData) {
		logger.info("url:/account/curmthIntoWhsCostTab/updateCurmthIntoWhsCostTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		CurmthIntoWhsCostTab curmthIntoWhsCostTab=null;
		try {
			curmthIntoWhsCostTab = BaseJson.getPOJO(jsonData,CurmthIntoWhsCostTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/updateCurmthIntoWhsCostTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/updateCurmthIntoWhsCostTab", false, "请求参数解析错误，请检查请求参数是否正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthIntoWhsCostTab/updateCurmthIntoWhsCostTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = curmthIntoWhsCostTabService.updateCurmthIntoWhsCostTabByOrdrNum(curmthIntoWhsCostTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/updateCurmthIntoWhsCostTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/updateCurmthIntoWhsCostTab", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/updateCurmthIntoWhsCostTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectCurmthIntoWhsCostTab")
	public @ResponseBody Object selectCurmthIntoWhsCostTab(@RequestBody String jsonBody) {
		logger.info("url:/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTab");
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
				resp=BaseJson.returnRespList("/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTab", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTab 异常说明：",e1);
			try {
				resp=TransformJson.returnRespList("/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTab", false, "请求参数解析错误，请检查请求参数是否正确，分页查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTab 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=curmthIntoWhsCostTabService.selectCurmthIntoWhsCostTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTab 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectCurmthIntoWhsCostTabById")
	public @ResponseBody Object selectCurmthIntoWhsCostTabById(@RequestBody String jsonData) {
		logger.info("url:/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTabById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTabById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTabById",false,"请求参数解析错误，请检查请求参数是否正确，查询失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTabById 异常说明：",e);
			}
			return resp;
		}
		try {
			CurmthIntoWhsCostTab curmthIntoWhsCostTab = curmthIntoWhsCostTabService.selectCurmthIntoWhsCostTabByOrdrNum(ordrNum);
			if(curmthIntoWhsCostTab!=null) {
				 resp=BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTabById",true,"处理成功！", curmthIntoWhsCostTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTabById",false,"没有查到数据！", curmthIntoWhsCostTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTabById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
