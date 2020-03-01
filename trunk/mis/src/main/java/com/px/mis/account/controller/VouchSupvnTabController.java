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
import com.px.mis.account.entity.VouchSupvnTab;
import com.px.mis.account.service.VouchSupvnTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//��ƾ֤������ӿ�
@RequestMapping(value="account/vouchSupvnTab",method=RequestMethod.POST)
@Controller
public class VouchSupvnTabController {
	private Logger logger = LoggerFactory.getLogger(VouchSupvnTabController.class);
	
	@Autowired
	private VouchSupvnTabService vouchSupvnTabService;
	
	/*���*/
	@RequestMapping("insertVouchSupvnTab")
	public @ResponseBody Object insertVouchSupvnTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchSupvnTab/insertVouchSupvnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		VouchSupvnTab vouchSupvnTab=null;
		try {
			vouchSupvnTab = BaseJson.getPOJO(jsonData,VouchSupvnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/insertVouchSupvnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/insertVouchSupvnTab", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/insertVouchSupvnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = vouchSupvnTabService.insertVouchSupvnTab(vouchSupvnTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/insertVouchSupvnTab", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/insertVouchSupvnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/insertVouchSupvnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteVouchSupvnTab")
	public @ResponseBody Object deleteVouchSupvnTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchSupvnTab/deleteVouchSupvnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/deleteVouchSupvnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/deleteVouchSupvnTab", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/deleteVouchSupvnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = vouchSupvnTabService.deleteVouchSupvnTabByOrdrNum(ordrNum);
			if(on != null) {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/deleteVouchSupvnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/deleteVouchSupvnTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/deleteVouchSupvnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateVouchSupvnTab")
	public @ResponseBody Object updateVouchSupvnTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchSupvnTab/updateVouchSupvnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		VouchSupvnTab vouchSupvnTab=null;
		try {
			vouchSupvnTab = BaseJson.getPOJO(jsonData,VouchSupvnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/updateVouchSupvnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/updateVouchSupvnTab", false,"���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/updateVouchSupvnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = vouchSupvnTabService.updateVouchSupvnTabByOrdrNum(vouchSupvnTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/updateVouchSupvnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchSupvnTab/updateVouchSupvnTab", false,"update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/updateVouchSupvnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectVouchSupvnTab")
	public @ResponseBody Object selectVouchSupvnTab(@RequestBody String jsonBody) {
		logger.info("url:/account/vouchSupvnTab/selectVouchSupvnTab");
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
				resp=BaseJson.returnRespList("/account/vouchSupvnTab/selectVouchSupvnTab", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTab �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", false, "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=vouchSupvnTabService.selectVouchSupvnTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTab �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectVouchSupvnTabById")
	public @ResponseBody Object selectVouchSupvnTabById(@RequestBody String jsonData) {
		logger.info("url:/account/vouchSupvnTab/selectVouchSupvnTabById");
		logger.info("���������"+jsonData);

		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTabById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/vouchSupvnTab/selectVouchSupvnTabById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTabById �쳣˵����",e);
			}
			return resp;
		}
		try {
			VouchSupvnTab vouchSupvnTab = vouchSupvnTabService.selectVouchSupvnTabByOrdrNum(ordrNum);
			if(vouchSupvnTab!=null) {
				 resp=BaseJson.returnRespObj("/account/vouchSupvnTab/selectVouchSupvnTabById",true,"����ɹ���", vouchSupvnTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/vouchSupvnTab/selectVouchSupvnTabById",false,"û�в鵽���ݣ�", vouchSupvnTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchSupvnTab/selectVouchSupvnTabById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
}
