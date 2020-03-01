package com.px.mis.ec.controller;

import java.util.List;
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
import com.px.mis.account.utils.TransformJson;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.service.LogisticsAdjustService;
import com.px.mis.util.BaseJson;
//物流表的手工拆分合并；
@RequestMapping(value="ec/logisticsAdjust",method = RequestMethod.POST)
@Controller
public class LogisticsAdjustController {
	
	private Logger logger = LoggerFactory.getLogger(LogisticsAdjustController.class);
	
	@Autowired
	private LogisticsAdjustService logisticsAdjustService;
	//手工拆分之前的展示
	@RequestMapping(value="showSplitLogistics",method = RequestMethod.POST)
	@ResponseBody
	private String showSplitLogistics(@RequestBody String jsonData) {
		logger.info("url:ec/logisticsAdjust/showSplitLogistics");
		logger.info("请求参数："+jsonData);
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/logisticsAdjust/showSplitLogistics 异常说明：",e1);
			 try {
				resp=BaseJson.returnRespObj("ec/logisticsAdjust/showSplitLogistics",false,"请求参数解析错误，请检查请求参数是否正确，请求失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/logisticsAdjust/showSplitLogistics 异常说明：",e);
			}
			return resp;
		}
		try {
			resp = logisticsAdjustService.showSplitLogistics(ordrNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/logisticsAdjust/showSplitLogistics 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	//拆分物流表；
	@RequestMapping(value="editSplitLogistics",method = RequestMethod.POST)
	@ResponseBody
	private String editSplitLogistics(@RequestBody String jsonData) {
		logger.info("url:ec/logisticsAdjust/editSplitLogistics");
		logger.info("请求参数："+jsonData);
		String resp="";
		LogisticsTab logisticsTab=null;
		try {
			logisticsTab = BaseJson.getPOJO(jsonData,LogisticsTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/logisticsAdjust/editSplitLogistics 异常说明：",e1);
			try {
				resp = BaseJson.returnRespObj("ec/logisticsAdjust/editSplitLogistics", false, "请求参数解析错误，请检查请求参数是否正确，拆分物流表失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/logisticsAdjust/editSplitLogistics 异常说明：",e);
			}
			return resp;
		}
		try {
			resp = logisticsAdjustService.editSplitLogistics(logisticsTab);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/logisticsAdjust/editSplitLogistics 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	//合并之前的展示；
	@RequestMapping(value="showMergeLogistics",method = RequestMethod.POST)
	@ResponseBody
	private String showMergeLogistics(@RequestBody String jsonData) {
		logger.info("url:ec/logisticsAdjust/showSplitLogistics");
		logger.info("请求参数："+jsonData);
		String resp="";
		String ordrNums=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNums = reqBody.get("ordrNums").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/logisticsAdjust/showSplitLogistics 异常说明：",e1);
			try {
				resp = TransformJson.returnRespList("ec/logisticsAdjust/showSplitLogistics",false,"请求参数解析错误，请检查请求参数是否正确，请求失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/logisticsAdjust/showSplitLogistics 异常说明：",e);
			}
			return resp;
		}
		try {
			resp = logisticsAdjustService.showMergeLogistics(ordrNums);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/logisticsAdjust/showSplitLogistics 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	//合并物流表
	@RequestMapping(value="editMergeLogistics",method = RequestMethod.POST)
	@ResponseBody
	private String editMergeLogistics(@RequestBody String jsonData) {
		logger.info("url:ec/logisticsAdjust/editMergeLogistics");
		logger.info("请求参数："+jsonData);
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/logisticsAdjust/editMergeLogistics 异常说明：",e1);
			try {
				resp = TransformJson.returnRespList("ec/logisticsAdjust/showSplitLogistics",false,"请求参数解析错误，请检查请求参数是否正确，合并物流表失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/logisticsAdjust/editMergeLogistics 异常说明：",e);
			}
			return resp;
		}
		try {
			System.out.println(ordrNum);
			List<Map> list = BaseJson.getList(jsonData);
			for(Map map:list) {
				System.out.println(map);
			}
			resp = logisticsAdjustService.editMergeLogistics(list,ordrNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/logisticsAdjust/editMergeLogistics 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
}
