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
import com.px.mis.account.entity.MthTermBgnTab;
import com.px.mis.account.service.MthTermBgnTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//�����ڳ���ӿ�
@RequestMapping(value="account/mthTermBgnTab",method=RequestMethod.POST)
@Controller
public class MthTermBgnTabController {
	private Logger logger = LoggerFactory.getLogger(MthTermBgnTabController.class);
	@Autowired
	private MthTermBgnTabService mthTermBgnTabService;
	/*���*/
	@RequestMapping("insertMthTermBgnTab")
	public @ResponseBody Object insertMthTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/mthTermBgnTab/insertMthTermBgnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		MthTermBgnTab mthTermBgnTab=null;
		try {
			mthTermBgnTab = BaseJson.getPOJO(jsonData,MthTermBgnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/insertMthTermBgnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/insertMthTermBgnTab", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/insertMthTermBgnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = mthTermBgnTabService.insertMthTermBgnTab(mthTermBgnTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/insertMthTermBgnTab", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/insertMthTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/insertMthTermBgnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteMthTermBgnTab")
	public @ResponseBody Object deleteMthTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/mthTermBgnTab/deleteMthTermBgnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/deleteMthTermBgnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/deleteMthTermBgnTab", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/deleteMthTermBgnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = mthTermBgnTabService.deleteMthTermBgnTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/deleteMthTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/deleteMthTermBgnTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/deleteMthTermBgnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateMthTermBgnTab")
	public @ResponseBody Object updateMthTermBgnTab(@RequestBody String jsonData) {
		logger.info("url:/account/mthTermBgnTab/updateMthTermBgnTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		MthTermBgnTab mthTermBgnTab=null;
		try {
			mthTermBgnTab = BaseJson.getPOJO(jsonData,MthTermBgnTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/updateMthTermBgnTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/updateMthTermBgnTab", false, "���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/updateMthTermBgnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = mthTermBgnTabService.updateMthTermBgnTabByOrdrNum(mthTermBgnTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/updateMthTermBgnTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/mthTermBgnTab/updateMthTermBgnTab", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/updateMthTermBgnTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectMthTermBgnTab")
	public @ResponseBody Object selectMthTermBgnTab(@RequestBody String jsonBody) {
		logger.info("url:/account/mthTermBgnTab/selectMthTermBgnTab");
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
				resp=BaseJson.returnRespList("/account/mthTermBgnTab/selectMthTermBgnTab", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTab �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespList("/account/mthTermBgnTab/selectMthTermBgnTab", false, "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=mthTermBgnTabService.selectMthTermBgnTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTab �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectMthTermBgnTabById")
	public @ResponseBody Object selectMthTermBgnTabById(@RequestBody String jsonData) {
		logger.info("url:/account/mthTermBgnTab/selectMthTermBgnTabById");
		logger.info("���������"+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTabById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/mthTermBgnTab/selectMthTermBgnTabById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTabById �쳣˵����",e);
			}
			return resp;
		}
		try {
			MthTermBgnTab mthTermBgnTab = mthTermBgnTabService.selectMthTermBgnTabByOrdrNum(ordrNum);
			if(mthTermBgnTab!=null) {
				 resp=BaseJson.returnRespObj("/account/mthTermBgnTab/selectMthTermBgnTabById",true,"����ɹ���", mthTermBgnTab);
			}else {
				resp=BaseJson.returnRespObj("/account/mthTermBgnTab/selectMthTermBgnTabById",false,"û�в鵽���ݣ�", mthTermBgnTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/mthTermBgnTab/selectMthTermBgnTabById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
