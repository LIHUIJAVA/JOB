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
import com.px.mis.account.entity.InvtyAcctiSysTab;
import com.px.mis.account.service.InvtyAcctiSysTabService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//�������ϵͳ��ӿ�
@RequestMapping(value="account/invtyAcctiSysTab",method=RequestMethod.POST)
@Controller
public class InvtyAcctiSysTabController {
	private Logger logger = LoggerFactory.getLogger(InvtyAcctiSysTabController.class);
	@Autowired
	private InvtyAcctiSysTabService invtyAcctiSysTabService;
	/*���*/
	@RequestMapping("insertInvtyAcctiSysTab")
	public @ResponseBody Object insertInvtyAcctiSysTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiSysTab/insertInvtyAcctiSysTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyAcctiSysTab invtyAcctiSysTab=null;
		try {
			invtyAcctiSysTab = BaseJson.getPOJO(jsonData,InvtyAcctiSysTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/insertInvtyAcctiSysTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/insertInvtyAcctiSysTab", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/insertInvtyAcctiSysTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiSysTabService.insertInvtyAcctiSysTab(invtyAcctiSysTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/insertInvtyAcctiSysTab", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/insertInvtyAcctiSysTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/insertInvtyAcctiSysTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteInvtyAcctiSysTab")
	public @ResponseBody Object deleteInvtyAcctiSysTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiSysTabService.deleteInvtyAcctiSysTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/deleteInvtyAcctiSysTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateInvtyAcctiSysTab")
	public @ResponseBody Object updateInvtyAcctiSysTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiSysTab/updateInvtyAcctiSysTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyAcctiSysTab invtyAcctiSysTab=null;
		try {
			invtyAcctiSysTab = BaseJson.getPOJO(jsonData,InvtyAcctiSysTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/updateInvtyAcctiSysTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/updateInvtyAcctiSysTab", false, "���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/updateInvtyAcctiSysTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyAcctiSysTabService.updateInvtyAcctiSysTabByOrdrNum(invtyAcctiSysTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/updateInvtyAcctiSysTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyAcctiSysTab/updateInvtyAcctiSysTab", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/updateInvtyAcctiSysTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyAcctiSysTab")
	public @ResponseBody Object selectInvtyAcctiSysTab(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTab");
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
				resp=BaseJson.returnRespList("/account/invtyAcctiSysTab/selectInvtyAcctiSysTab", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTab �쳣˵����",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyAcctiSysTab/selectInvtyAcctiSysTab", false, "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyAcctiSysTabService.selectInvtyAcctiSysTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTab �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyAcctiSysTabById")
	public @ResponseBody Object selectInvtyAcctiSysTabById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById");
		logger.info("���������"+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById �쳣˵����",e);
			}
			return resp;
		}
		try {
			InvtyAcctiSysTab invtyAcctiSysTab = invtyAcctiSysTabService.selectInvtyAcctiSysTabByOrdrNum(ordrNum);
			if(invtyAcctiSysTab!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById",true,"����ɹ���", invtyAcctiSysTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById",false,"û�в鵽���ݣ�", invtyAcctiSysTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyAcctiSysTab/selectInvtyAcctiSysTabById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
