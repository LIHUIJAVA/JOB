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

//������Ʒί�д�����ϸ�˽ӿ�
@RequestMapping(value="account/delvMerchdEntrsAgnDtlAcct",method=RequestMethod.POST)
@Controller
public class DelvMerchdEntrsAgnDtlAcctController {
	private Logger logger = LoggerFactory.getLogger(DelvMerchdEntrsAgnDtlAcctController.class);
	@Autowired
	private DelvMerchdEntrsAgnDtlAcctService delvMerchdEntrsAgnDtlAcctService;
	/*���*/
	@RequestMapping("insertDelvMerchdEntrsAgnDtlAcct")
	public @ResponseBody Object insertDelvMerchdEntrsAgnDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct=null;
		try {
			delvMerchdEntrsAgnDtlAcct = BaseJson.getPOJO(jsonData,DelvMerchdEntrsAgnDtlAcct.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = delvMerchdEntrsAgnDtlAcctService.insertDelvMerchdEntrsAgnDtlAcct(delvMerchdEntrsAgnDtlAcct);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/insertDelvMerchdEntrsAgnDtlAcct �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteDelvMerchdEntrsAgnDtlAcct")
	public @ResponseBody Object deleteDelvMerchdEntrsAgnDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct �쳣˵����",e);
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
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/deleteDelvMerchdEntrsAgnDtlAcct �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateDelvMerchdEntrsAgnDtlAcct")
	public @ResponseBody Object updateDelvMerchdEntrsAgnDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct;
		try {
			delvMerchdEntrsAgnDtlAcct = BaseJson.getPOJO(jsonData,DelvMerchdEntrsAgnDtlAcct.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct", false, "���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct �쳣˵����",e);
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
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/updateDelvMerchdEntrsAgnDtlAcct �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectDelvMerchdEntrsAgnDtlAcct")
	public @ResponseBody Object selectDelvMerchdEntrsAgnDtlAcct(@RequestBody String jsonBody) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct");
		logger.info("���������"+jsonBody);
		String resp="";
		Map map=null;
		Integer pageNo=null;
		Integer pageSize=null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int)map.get("pageNo");
			pageSize = (int)map.get("pageSize");
			if(pageNo==0 || pageSize==0) {
				resp=BaseJson.returnRespList("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct �쳣˵����",e1);
			try {
				resp=TransformJson.returnRespList("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct", false, "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=delvMerchdEntrsAgnDtlAcctService.selectDelvMerchdEntrsAgnDtlAcctList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectDelvMerchdEntrsAgnDtlAcctById")
	public @ResponseBody Object selectDelvMerchdEntrsAgnDtlAcctById(@RequestBody String jsonData) {
		logger.info("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById");
		logger.info("���������"+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById �쳣˵����",e);
			}
			return resp;
		}
		try {
			DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct = delvMerchdEntrsAgnDtlAcctService.selectDelvMerchdEntrsAgnDtlAcctByOrdrNum(ordrNum);
			if(delvMerchdEntrsAgnDtlAcct!=null) {
				 resp=BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById",true,"����ɹ���", delvMerchdEntrsAgnDtlAcct);
			}else {
				 resp=BaseJson.returnRespObj("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById",false,"û�в鵽���ݣ�",delvMerchdEntrsAgnDtlAcct);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcctById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
