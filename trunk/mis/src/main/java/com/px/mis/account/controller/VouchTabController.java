package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.VouchTab;
import com.px.mis.account.entity.VouchTabSub;
import com.px.mis.account.service.VouchTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//ƾ֤��ӿ�
@RequestMapping(value="account/vouchTab",method=RequestMethod.POST)
@Controller
public class VouchTabController {
	private Logger logger = LoggerFactory.getLogger(VouchTabController.class);
	@Autowired
	private VouchTabService vouchTabService;
	/*���*/
	@RequestMapping("insertVouchTab")
	public @ResponseBody Object insertVouchTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchTab/insertVouchTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		VouchTab vouchTab=null;
		try {
			vouchTab = BaseJson.getPOJO(jsonData,VouchTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchTab/insertVouchTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchTab/insertVouchTab", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchTab/insertVouchTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = vouchTabService.insertVouchTab(vouchTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/vouchTab/insertVouchTab", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchTab/insertVouchTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/insertVouchTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteVouchTab")
	public @ResponseBody Object deleteVouchTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchTab/deleteVouchTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchTab/deleteVouchTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchTab/deleteVouchTab", false, "���������������������������Ƿ���д��ȷ��ɾ��������", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchTab/deleteVouchTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = vouchTabService.deleteVouchTabByOrdrNum(ordrNum);
			if(on != null) {
				resp =BaseJson.returnRespObj("/account/vouchTab/deleteVouchTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchTab/deleteVouchTab", false, "ɾ��������", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/deleteVouchTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateVouchTab")
	public @ResponseBody Object updateVouchTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchTab/updateVouchTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		VouchTab vouchTab=null;
		try {
			vouchTab = BaseJson.getPOJO(jsonData,VouchTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchTab/updateVouchTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchTab/updateVouchTab", false, "���������������������������Ƿ���д��ȷ������ʧ��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchTab/updateVouchTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = vouchTabService.updateVouchTabByOrdrNum(vouchTab);
			if(on != null) {
				resp =BaseJson.returnRespObj("/account/vouchTab/updateVouchTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchTab/updateVouchTab", false, "����ʧ��", null);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/updateVouchTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectVouchTab")
	public @ResponseBody Object selectVouchTab(@RequestBody String jsonBody) {
		logger.info("url:/account/vouchTab/selectVouchTab");
		logger.info("���������"+jsonBody);

		String resp="";
		try {
			Map map= JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			String start = (String)map.get("start");
			String end = (String)map.get("end");
			
			if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
				if (start.length() < 12 ) {
					start = start + " 00:00:00";
				}
				if (end.length() < 12) {
					end = end + " 23:59:59";
				}
				map.put("start", start);
				map.put("end", end);
			}
			resp=vouchTabService.selectVouchTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/selectVouchTab �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectVouchTabById")
	public @ResponseBody Object selectVouchTabById(@RequestBody String jsonData) {
		logger.info("url:/account/vouchTab/selectVouchTabById");
		logger.info("���������"+jsonData);

		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchTab/selectVouchTabById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/vouchTab/selectVouchTabById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchTab/selectVouchTabById �쳣˵����",e);
			}
			return resp;
		}
		try {
			VouchTab vouchTab = vouchTabService.selectVouchTabByOrdrNum(ordrNum);
			if(vouchTab!=null) {
				 resp=BaseJson.returnRespObj("/account/vouchTab/selectVouchTabById",true,"����ɹ���", vouchTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/vouchTab/selectVouchTabById",false,"û�в鵽���ݣ�", vouchTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/selectVouchTabById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/**
	 * ����ƾ֤
	 */
	@RequestMapping(value = "/voucher/generate")
	private @ResponseBody String VoucherGenerate(@RequestBody String jsonBody) {

		logger.info("url:account/vouchTab/voucher/generate");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //��ǰ�û�
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��ǰ�û�
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
			ArrayNode platOrdersArray = (ArrayNode) reqBody.get("list");
			List<VouchTabSub> paltOrdersList = new ArrayList<>();
			for(Iterator<JsonNode> platOrdersArrayIterator = platOrdersArray.iterator(); platOrdersArrayIterator.hasNext();) {
				JsonNode platOrdersNode = platOrdersArrayIterator.next();
				VouchTabSub platOrders=JacksonUtil.getPOJO((ObjectNode)platOrdersNode, VouchTabSub.class);
				paltOrdersList.add(platOrders);
			}
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			String vouchCateWor = (String)map.get("vouchCateWor"); //ƾ֤�����
			String comnVouchComnt = (String)map.get("comnVouchComnt"); //ժҪ
			if(StringUtils.isNotEmpty(vouchCateWor) || paltOrdersList.size() > 0) {
				map.put("vouchCateWor", vouchCateWor);
				map.put("comnVouchComnt", comnVouchComnt);
				map.put("paltOrdersList", paltOrdersList);
				map.put("userName", userName);
				map.put("loginTime", loginTime);
				map.put("accNum", accNum);
				resp = vouchTabService.voucherGenerate(map);
			} else {
				resp = BaseJson.returnRespObj("account/vouchTab/voucher/generate", false, "�ӿڽ����쳣", null);
			}
			
			
		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespObj("account/vouchTab/voucher/generate", false, "�ӿڽ����쳣", null);
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/vouchTab/voucher/generate �쳣˵����",e);
		}

		return resp;
	}
	
	/**
	 * ��ѯƾ֤-����
	 */
	@RequestMapping(value = "/voucher/formlist")
	private @ResponseBody String VoucherList(@RequestBody String jsonBody) {

		logger.info("url:account/vouchTab/voucher/formlist");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			
			resp = vouchTabService.selectvoucherList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/voucher/formlist �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * ����ƾ֤
	 */
	@RequestMapping(value = "/voucher/export")
	private @ResponseBody String VoucherExport(@RequestBody String jsonBody) {

		logger.info("url:account/vouchTab/voucher/export");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼��
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //��ǰ�û�
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			String start = (String)map.get("start");
			String end = (String)map.get("end");
			
			if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
				if (start.length() < 12 ) {
					start = start + " 00:00:00";
				}
				if (end.length() < 12) {
					end = end + " 23:59:59";
				}
				map.put("start", start);
				map.put("end", end);
			}
			
			map.put("user", userName);
			map.put("loginTime", loginTime);
			resp = vouchTabService.exportvoucherList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/voucher/export �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * ����ƾ֤
	 */
	@RequestMapping(value = "/voucher/import")
	private @ResponseBody String VoucherImport(HttpServletRequest request,String jsonBody) {

		logger.info("url:account/vouchTab/voucher/import");
		String resp="";
		
		try {
				
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼��
			//����һ��ͨ�õĶಿ�ֽ�����
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
		    if(multipartResolver.isMultipart(request)) {
		        //ת���ɶಿ��request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return  ("��ѡ���ļ�!");
			    }
			    return vouchTabService.importVoucherList(file,accNum);
		    }else {
		    	return  BaseJson.returnRespObj("url:/account/vouchTab/voucher/import", false, "��ѡ���ļ���", null);
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/voucher/import �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * ɾ����ѯ
	 */
	@RequestMapping(value = "/voucher/del")
	private @ResponseBody String VoucherDel(@RequestBody String jsonBody) {

		logger.info("url:account/vouchTab/voucher/del");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //��ǰ�û�
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��ǰ�û�
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			map.put("userName", userName);
			map.put("loginTime", loginTime);
			map.put("accNum", accNum);
			resp = vouchTabService.voucherDel(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invty/voucher/list �쳣˵����",e);
		}

		return resp;
	}
}
