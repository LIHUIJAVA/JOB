package com.px.mis.sell.controller;

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
import com.px.mis.sell.entity.Printers;
import com.px.mis.sell.service.PrintersService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping(value = "sell/Management", method = RequestMethod.POST)
@Controller
public class PrinterManagement {
	private Logger logger = LoggerFactory.getLogger(PrinterManagement.class);
	@Autowired
	private PrintersService printersService;

	
	@RequestMapping(value = "selectList", method = RequestMethod.POST)
	@ResponseBody
	private String selectList(@RequestBody String jsonBody) {
		logger.info("url:sell/Management/selectList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = printersService.selectListPrinters(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	/*����*/
	@RequestMapping(value = "updatePrinters", method = RequestMethod.POST)
	@ResponseBody
	public Object updateCurmthIntoWhsCostTab(@RequestBody String jsonData) {
		logger.info("url:sell/Management/updatePrinters");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Printers printers=null;
		try {
			printers = BaseJson.getPOJO(jsonData,Printers.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:sell/Management/selectList �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("url:sell/Management/selectList", false, "���������������������������Ƿ���ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:sell/Management/selectList �쳣˵����",e);
			}
			return resp;
		}
		try {
			 on = printersService.updatePrinters(printers);
			
			if(on !=null) {
				resp =BaseJson.returnRespObj("url:sell/Management/selectList", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("url:sell/Management/selectList", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:sell/Management/selectList �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*���*/
	@RequestMapping(value = "insertPrinters", method = RequestMethod.POST)
	public @ResponseBody Object insertCurmthIntoWhsCostTab(@RequestBody String jsonData) {
		logger.info("url:/sell/Management/insertPrinters");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Printers printers=null;
		try {
			printers = BaseJson.getPOJO(jsonData,Printers.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/sell/Management/insertPrinters �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("url:/sell/Management/insertPrinters", false, "���������������������������Ƿ���ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/sell/Management/insertPrinters �쳣˵����",e);
			}
			return resp;
		}
		try {
			 on = printersService.insertPrinters(printers);
			if(on==null) {
				resp =BaseJson.returnRespObj("url:/sell/Management/insertPrinters", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("url:/sell/Management/insertPrinters", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/sell/Management/insertPrinters �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	/*ɾ��*/
	@RequestMapping(value="deletePrinters",method = RequestMethod.POST)
	public @ResponseBody Object deletePrinters(@RequestBody String jsonData) {
		logger.info("url:/sell/Management/deletePrinters");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer idx=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			idx = reqBody.get("idx").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/sell/Management/deletePrinters �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("url:/sell/Management/deletePrinters", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/sell/Management/deletePrinters �쳣˵����",e);
			}
			return resp;
		}
		try {
			 on = printersService.deletePrinters(idx);
			if(on !=null) {
				resp =BaseJson.returnRespObj("url:/sell/Management/deletePrinters", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("url:/sell/Management/deletePrinters", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/sell/Management/deletePrinters �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
}
