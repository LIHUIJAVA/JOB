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
import com.px.mis.account.entity.InvtyDtlAcct;
import com.px.mis.account.service.InvtyDtlAcctService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//�����ϸ�˽ӿ�
@RequestMapping(value="account/invtyDtlAcct",method=RequestMethod.POST)
@Controller
public class InvtyDtlAcctController {
	private Logger logger = LoggerFactory.getLogger(InvtyDtlAcctController.class);
	@Autowired
	private InvtyDtlAcctService invtyDtlAcctService;
	/*���*/
	@RequestMapping("insertInvtyDtlAcct")
	public @ResponseBody Object insertInvtyDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/invtyDtlAcct/insertInvtyDtlAcct");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyDtlAcct invtyDtlAcct=null;
		try {
			invtyDtlAcct = BaseJson.getPOJO(jsonData,InvtyDtlAcct.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/insertInvtyDtlAcct �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/insertInvtyDtlAcct", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/insertInvtyDtlAcct �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyDtlAcctService.insertInvtyDtlAcct(invtyDtlAcct);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/insertInvtyDtlAcct", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/insertInvtyDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/insertInvtyDtlAcct �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteInvtyDtlAcct")
	public @ResponseBody Object deleteInvtyDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/invtyDtlAcct/deleteInvtyDtlAcct");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/deleteInvtyDtlAcct �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/deleteInvtyDtlAcct", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/deleteInvtyDtlAcct �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyDtlAcctService.deleteInvtyDtlAcctByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/deleteInvtyDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/deleteInvtyDtlAcct", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/deleteInvtyDtlAcct �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateInvtyDtlAcct")
	public @ResponseBody Object updateInvtyDtlAcct(@RequestBody String jsonData) {
		logger.info("url:/account/invtyDtlAcct/updateInvtyDtlAcct");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyDtlAcct invtyDtlAcct=null;
		try {
			invtyDtlAcct = BaseJson.getPOJO(jsonData,InvtyDtlAcct.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/updateInvtyDtlAcct �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/updateInvtyDtlAcct", false, "���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/updateInvtyDtlAcct �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyDtlAcctService.updateInvtyDtlAcctByOrdrNum(invtyDtlAcct);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/updateInvtyDtlAcct", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyDtlAcct/updateInvtyDtlAcct", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/updateInvtyDtlAcct �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyDtlAcct")
	public @ResponseBody Object selectInvtyDtlAcct(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyDtlAcct/selectInvtyDtlAcct");
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
				resp=BaseJson.returnRespList("/account/invtyDtlAcct/selectInvtyDtlAcct", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcct �쳣˵����",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyDtlAcct/selectInvtyDtlAcct", false, "���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcct �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyDtlAcctService.selectInvtyDtlAcctList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcct �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyDtlAcctById")
	public @ResponseBody Object selectInvtyDtlAcctById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyDtlAcct/selectInvtyDtlAcctById");
		logger.info("���������"+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcctById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcctById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcctById �쳣˵����",e);
			}
			return resp;
		}
		try {
			InvtyDtlAcct invtyDtlAcct = invtyDtlAcctService.selectInvtyDtlAcctByOrdrNum(ordrNum);
			if(invtyDtlAcct!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcctById",true,"����ɹ���", invtyDtlAcct);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcctById",false,"û�в鵽���ݣ�", invtyDtlAcct);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyDtlAcct/selectInvtyDtlAcctById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
