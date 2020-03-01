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
import com.px.mis.account.entity.InvtyCapOcupLaytTab;
import com.px.mis.account.service.InvtyCapOcupLaytTabService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//����ʽ�ռ�ù滮��ӿ�
@RequestMapping(value="account/invtyCapOcupLaytTab",method=RequestMethod.POST)
@Controller
public class InvtyCapOcupLaytTabController {
	private Logger logger = LoggerFactory.getLogger(InvtyCapOcupLaytTabController.class);
	@Autowired
	private InvtyCapOcupLaytTabService invtyCapOcupLaytTabService;
	/*���*/
	@RequestMapping("insertInvtyCapOcupLaytTab")
	public @ResponseBody Object insertInvtyCapOcupLaytTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyCapOcupLaytTab invtyCapOcupLaytTab=null;
		try {
			invtyCapOcupLaytTab = BaseJson.getPOJO(jsonData,InvtyCapOcupLaytTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyCapOcupLaytTabService.insertInvtyCapOcupLaytTab(invtyCapOcupLaytTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/insertInvtyCapOcupLaytTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteInvtyCapOcupLaytTab")
	public @ResponseBody Object deleteInvtyCapOcupLaytTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyCapOcupLaytTabService.deleteInvtyCapOcupLaytTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/deleteInvtyCapOcupLaytTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateInvtyCapOcupLaytTab")
	public @ResponseBody Object updateInvtyCapOcupLaytTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyCapOcupLaytTab invtyCapOcupLaytTab=null;
		try {
			invtyCapOcupLaytTab = BaseJson.getPOJO(jsonData,InvtyCapOcupLaytTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab", false, "���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyCapOcupLaytTabService.updateInvtyCapOcupLaytTabByOrdrNum(invtyCapOcupLaytTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/updateInvtyCapOcupLaytTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyCapOcupLaytTab")
	public @ResponseBody Object selectInvtyCapOcupLaytTab(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab");
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
				resp=BaseJson.returnRespList("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab �쳣˵����",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab", false, "���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyCapOcupLaytTabService.selectInvtyCapOcupLaytTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyCapOcupLaytTabById")
	public @ResponseBody Object selectInvtyCapOcupLaytTabById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById");
		logger.info("���������"+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById �쳣˵����",e);
			}
			return resp;
		}
		try {
			InvtyCapOcupLaytTab invtyCapOcupLaytTab = invtyCapOcupLaytTabService.selectInvtyCapOcupLaytTabByOrdrNum(ordrNum);
			if(invtyCapOcupLaytTab!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById",true,"����ɹ���", invtyCapOcupLaytTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById",false,"û�в鵽���ݣ�", invtyCapOcupLaytTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTabById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
