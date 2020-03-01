package com.px.mis.ec.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.account.utils.TransformJson;
import com.px.mis.ec.service.AssociatedSearchService;
import com.px.mis.util.BaseJson;

//������ѯ
@RequestMapping(value = "ec/associatedSearch", method = RequestMethod.POST)
@Controller
public class AssociatedSearchController {
	private Logger logger = LoggerFactory.getLogger(AssociatedSearchController.class);
	@Autowired
	private AssociatedSearchService associatedSearchService;
	//������ѯ
	@RequestMapping(value = "quickSearchByOrderId", method = RequestMethod.POST)
	@ResponseBody
	private String quickSearchByOrderId(@RequestBody String jsonBody) {
		logger.info("url:ec/associatedSearch/quickSearchByOrderId");
		logger.info("���������" + jsonBody);
		String resp = "";
		String orderId=null;
		try {
			orderId = BaseJson.getReqBody(jsonBody).get("orderId").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/associatedSearch/quickSearchByOrderId �쳣˵����",e1);
			try {
				resp = TransformJson.returnRespList("ec/associatedSearch/quickSearchByOrderId", false, "���������������������������Ƿ���ȷ������ʧ�ܡ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/associatedSearch/quickSearchByOrderId �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp = associatedSearchService.quickSearchByOrderId(orderId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/associatedSearch/quickSearchByOrderId �쳣˵����",e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}
	
	//һ�����������
	@RequestMapping(value = "reverseOperationByOrderId", method = RequestMethod.POST)
	@ResponseBody
	private String reverseOperationByOrderId(@RequestBody String jsonBody) {
		logger.info("url:ec/associatedSearch/reverseOperationByOrderId");
		logger.info("���������" + jsonBody);
		String resp = "";
		String orderId=null;
		try {
			orderId = BaseJson.getReqBody(jsonBody).get("orderId").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/associatedSearch/reverseOperationByOrderId �쳣˵����",e1);
			try {
				resp = BaseJson.returnRespObj("ec/associatedSearch/reverseOperationByOrderId", false, "���������������������������Ƿ���ȷ���������ʧ�ܡ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/associatedSearch/reverseOperationByOrderId �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp = associatedSearchService.reverseOperationByOrderId(orderId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/associatedSearch/reverseOperationByOrderId �쳣˵����",e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}
	
	//�������
	@RequestMapping(value = "orderAuditByOrderId", method = RequestMethod.POST)
	@ResponseBody
	private String orderAuditByOrderId(@RequestBody String jsonBody) {
		logger.info("url:ec/associatedSearch/orderAuditByOrderId");
		logger.info("���������" + jsonBody);
		String resp = "";
		String orderIds=null;
		String account="";
		String loginDate="";
		try {
			orderIds = BaseJson.getReqBody(jsonBody).get("orderId").asText();
			loginDate = BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			account = BaseJson.getReqHead(jsonBody).get("accNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/associatedSearch/orderAuditByOrderId �쳣˵����",e1);
			try {
				resp = BaseJson.returnRespObj("ec/associatedSearch/orderAuditByOrderId", false, "���������������������������Ƿ���ȷ���������ʧ�ܡ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/associatedSearch/orderAuditByOrderId �쳣˵����",e);
			}
			return resp;
		}
		try {
			if(StringUtils.isEmpty(loginDate)) {
				resp = BaseJson.returnRespObj("ec/associatedSearch/orderAuditByOrderId", true, "���ʧ�ܣ���¼����Ϊ�գ������µ�¼", null);
			}else {
				resp = associatedSearchService.orderAuditByOrderId(orderIds,account,loginDate);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/associatedSearch/orderAuditByOrderId �쳣˵����",e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}
	
	
	//��������
		@RequestMapping(value = "orderAbandonAuditByOrderId", method = RequestMethod.POST)
		@ResponseBody
		private String orderAbandonAuditByOrderId(@RequestBody String jsonBody) {
			logger.info("url:ec/associatedSearch/orderAbandonAuditByOrderId");
			logger.info("���������" + jsonBody);
			String resp = "";
			String orderId=null;
			String accNum="";
			try {
				orderId = BaseJson.getReqBody(jsonBody).get("orderId").asText();
				accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error("url:ec/associatedSearch/orderAbandonAuditByOrderId �쳣˵����",e1);
				try {
					resp = BaseJson.returnRespObj("ec/associatedSearch/orderAbandonAuditByOrderId", false, "���������������������������Ƿ���ȷ���������ʧ�ܡ�", null);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("url:ec/associatedSearch/orderAbandonAuditByOrderId �쳣˵����",e);
				}
				return resp;
			}
			try {
				resp = associatedSearchService.orderAbandonAuditByOrderId(orderId,accNum);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/associatedSearch/orderAbandonAuditByOrderId �쳣˵����",e);
			}
			logger.info("���ز�����" + resp);
			return resp;
		}
		
		//����˶����б�
		
}
