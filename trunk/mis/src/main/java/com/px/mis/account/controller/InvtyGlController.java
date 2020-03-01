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
import com.px.mis.account.entity.InvtyGl;
import com.px.mis.account.service.InvtyGlService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//������˽ӿ�
@RequestMapping(value="account/invtyGl",method=RequestMethod.POST)
@Controller
public class InvtyGlController {
	private Logger logger = LoggerFactory.getLogger(InvtyGlController.class);
	@Autowired
	private InvtyGlService invtyGlService;
	/*���*/
	@RequestMapping("insertInvtyGl")
	public @ResponseBody Object insertInvtyGl(@RequestBody String jsonData) {
		logger.info("url:/account/invtyGl/insertInvtyGl");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyGl invtyGl=null;
		try {
			invtyGl = BaseJson.getPOJO(jsonData,InvtyGl.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/insertInvtyGl �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyGl/insertInvtyGl", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/insertInvtyGl �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyGlService.insertInvtyGl(invtyGl);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/invtyGl/insertInvtyGl", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyGl/insertInvtyGl", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/insertInvtyGl �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteInvtyGl")
	public @ResponseBody Object deleteInvtyGl(@RequestBody String jsonData) {
		logger.info("url:/account/invtyGl/deleteInvtyGl");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/deleteInvtyGl �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyGl/deleteInvtyGl", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/deleteInvtyGl �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyGlService.deleteInvtyGlByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyGl/deleteInvtyGl", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyGl/deleteInvtyGl", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/deleteInvtyGl �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateInvtyGl")
	public @ResponseBody Object updateInvtyGl(@RequestBody String jsonData) {
		logger.info("url:/account/invtyGl/updateInvtyGl");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		InvtyGl invtyGl=null;
		try {
			invtyGl = BaseJson.getPOJO(jsonData,InvtyGl.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/updateInvtyGl �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/invtyGl/updateInvtyGl", false, "���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/updateInvtyGl �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = invtyGlService.updateInvtyGlByOrdrNum(invtyGl);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/invtyGl/updateInvtyGl", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/invtyGl/updateInvtyGl", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/updateInvtyGl �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyGl")
	public @ResponseBody Object selectInvtyGl(@RequestBody String jsonBody) {
		logger.info("url:/account/invtyGl/selectInvtyGl");
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
				resp=BaseJson.returnRespList("/account/invtyGl/selectInvtyGl", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/selectInvtyGl �쳣˵����",e1);
			try {
				resp=TransformJson.returnRespList("/account/invtyGl/selectInvtyGl", false, "���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/selectInvtyGl �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=invtyGlService.selectInvtyGlList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/selectInvtyGl �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectInvtyGlById")
	public @ResponseBody Object selectInvtyGlById(@RequestBody String jsonData) {
		logger.info("url:/account/invtyGl/selectInvtyGlById");
		logger.info("���������"+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/invtyGl/selectInvtyGlById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/invtyGl/selectInvtyGlById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/invtyGl/selectInvtyGlById �쳣˵����",e);
			}
			return resp;
		}
		try {
			InvtyGl invtyGl = invtyGlService.selectInvtyGlByOrdrNum(ordrNum);
			if(invtyGl!=null) {
				 resp=BaseJson.returnRespObj("/account/invtyGl/selectInvtyGlById",true,"����ɹ���", invtyGl);
			}else {
				 resp=BaseJson.returnRespObj("/account/invtyGl/selectInvtyGlById",false,"û�в鵽���ݣ�", invtyGl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invtyGl/selectInvtyGlById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
