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
import com.px.mis.account.entity.InvtyTermBgnTab;
import com.px.mis.account.service.InvtyTermBgnTabService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//����ڳ��ӿ�
@RequestMapping(value="account/invtyTermBgnTab",method=RequestMethod.POST)
@Controller
public class InvtyTermBgnTabController {
	private Logger logger = LoggerFactory.getLogger(InvtyTermBgnTabController.class);
	@Autowired
	private InvtyTermBgnTabService invtyTermBgnTabService;
	/*���*/
	@RequestMapping("insertInvtyTermBgnTab")
	public @ResponseBody Object insertInvtyTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyTermBgnTab/insertInvtyTermBgnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyTermBgnTab invtyTermBgnTab=null;
		try {
			invtyTermBgnTab = BaseJson.getPOJO(jsonData,InvtyTermBgnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/insertInvtyTermBgnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/insertInvtyTermBgnTab", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/insertInvtyTermBgnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyTermBgnTabService.insertInvtyTermBgnTab(invtyTermBgnTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/insertInvtyTermBgnTab", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/insertInvtyTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/insertInvtyTermBgnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteInvtyTermBgnTab")
	public @ResponseBody Object deleteInvtyTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyTermBgnTab/deleteInvtyTermBgnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/deleteInvtyTermBgnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/deleteInvtyTermBgnTab", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/deleteInvtyTermBgnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyTermBgnTabService.deleteInvtyTermBgnTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/deleteInvtyTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/deleteInvtyTermBgnTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/deleteInvtyTermBgnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateInvtyTermBgnTab")
	public @ResponseBody Object updateInvtyTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/invtyTermBgnTab/updateInvtyTermBgnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyTermBgnTab invtyTermBgnTab=null;
		try {
			invtyTermBgnTab = BaseJson.getPOJO(jsonData,InvtyTermBgnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/updateInvtyTermBgnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/updateInvtyTermBgnTab",false, "���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/updateInvtyTermBgnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyTermBgnTabService.updateInvtyTermBgnTabByOrdrNum(invtyTermBgnTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/updateInvtyTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyTermBgnTab/updateInvtyTermBgnTab",false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/updateInvtyTermBgnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyTermBgnTab")
	public @ResponseBody Object selectInvtyTermBgnTab(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyTermBgnTab/selectInvtyTermBgnTab");
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
				resp=BaseJson.returnRespList("/account/invtyTermBgnTab/selectInvtyTermBgnTab", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTab �쳣˵����",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyTermBgnTab/selectInvtyTermBgnTab", false, "���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyTermBgnTabService.selectInvtyTermBgnTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTab �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyTermBgnTabById")
	public @ResponseBody Object selectInvtyTermBgnTabById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyTermBgnTab/selectInvtyTermBgnTabById");
		logger.info("���������"+jsonData);

		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTabById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyTermBgnTab/selectInvtyTermBgnTabById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTabById �쳣˵����",e);
			}
			return resp;
		}
		try {
			InvtyTermBgnTab invtyTermBgnTab = invtyTermBgnTabService.selectInvtyTermBgnTabByOrdrNum(ordrNum);
			if(invtyTermBgnTab!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyTermBgnTab/selectInvtyTermBgnTabById",true,"����ɹ���", invtyTermBgnTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyTermBgnTab/selectInvtyTermBgnTabById",false,"û�в鵽���ݣ�", invtyTermBgnTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyTermBgnTab/selectInvtyTermBgnTabById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
