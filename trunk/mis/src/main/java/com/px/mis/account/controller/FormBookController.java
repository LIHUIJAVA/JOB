package com.px.mis.account.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.AcctItmDoc;
import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.service.FormBookService;
import com.px.mis.account.service.InvtyAccountService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

/**
 * ���ݼ��˿��Ʋ�
 *
 */
@RequestMapping(value="account/form",method=RequestMethod.POST)
@Controller
@CrossOrigin
public class FormBookController {
	
	private Logger logger = LoggerFactory.getLogger(FormBookController.class); 
	
	@Autowired
	private FormBookService formBookService; //���ݼ���
	
	/**- 
	 * ��ѯ�������ݼ����б�
	 */
	@RequestMapping(value = "/normal/list")
	private @ResponseBody String selectNormalInvtyAccount(@RequestBody String jsonBody) {
		
		logger.info("url:account/form/normal/list");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String formSDt = (String)map.get("formSDt");
			String formEDt = (String)map.get("formEDt");
	
			if(StringUtils.isNotEmpty(formSDt) && StringUtils.isNotEmpty(formEDt)) {
				if (formSDt.length() < 12 ) {
					formSDt = formSDt + " 00:00:00";
				}
				if (formEDt.length() < 12) {
					formEDt = formEDt + " 23:59:59";
				}
				map.put("formSDt", formSDt);
				map.put("formEDt", formEDt);
			}
			
			resp = formBookService.selectNormalInvtyAccount(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:account/form/normal/list �쳣˵����",e);
		}

		return resp;
		
	}
	
	/**
	 * ��ѯ���ݸ��ݵ��ݺŵ�������
	 */
	@RequestMapping(value = "/doument")
	private @ResponseBody String selectDocument(@RequestBody String jsonBody) {
		
		logger.info("url:account/invty/doument");
		logger.info("���������"+jsonBody);
		
		Object data = new Object();
		String resp="";
		
		try {
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			data = formBookService.selectDoument(map);
			
			resp = BaseJson.returnRespObj("account/form/doument", true, "��ѯ�ɹ���", data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:account/form/doument �쳣˵����",e);
		}

		return resp;
		
	}
	
	/**
	 * ����
	 */
	@RequestMapping(value = "/normal/book")
	private @ResponseBody String formBook(@RequestBody String jsonBody) {
		
		logger.info("url:account/form/normal/book");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��ǰ�û�
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //��ǰ�û�
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			
			List<Map> mList = BaseJson.getList(jsonBody);
			List<FormBookEntry> list = new ArrayList<>();
			
			for (Map map : mList) {
				FormBookEntry formBook = new FormBookEntry();
				BeanUtils.populate(formBook, map);
				list.add(formBook);
			}
			if(list.size() != 0) {
				
				Map<String,FormBookEntry> parmMap = new HashMap<>();
				List<FormBookEntry> lists = new ArrayList<>();
				for(FormBookEntry form : list) {
					
					if(!parmMap.containsKey(form.getOutIntoWhsTypId()+"-"+form.getFormNum())) {
						parmMap.put(form.getOutIntoWhsTypId()+"-"+form.getFormNum(), form);
						lists.add(form);
					}
				}
				//����ߵ�list��lists��һ����,paramMap��֪��ȥ����
				resp = formBookService.formBook(lists,userName,loginTime);
			} else {
				resp = BaseJson.returnRespObj("account/form/normal/book", false, "�������ݲ���Ϊ��", null);
				
			}
				
		} catch (Exception e) {
			
			try {
				resp = BaseJson.returnRespObj("account/form/normal/book", false, "�ӿ��쳣���¼��ʱ", null);
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/form/normal/book �쳣˵����",e);
		}

		return resp;
		
	}
 
	/**
	 * ������ - �����ָ����˲���
	 * @param jsonBody
	 * @return
	 */
	@RequestMapping(value = "/back/book")
	private @ResponseBody String backFormBook(@RequestBody String jsonBody) {
		
		logger.info("url:mis/account/form/back/book");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��ǰ�û�
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //��ǰ�û�
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			String formNum = (String)map.get("formNum");
			
			if(StringUtils.isEmpty(formNum)) {
				resp = BaseJson.returnRespObj("account/form/back/book", false, "�ָ��������ݲ���Ϊ��", null);
			} else {
				resp = formBookService.backFormBook(formNum,userName,loginTime);
			}
				
		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespObj("account/form/back/book", false, "�ӿ��쳣���¼��ʱ", null);
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/form/back/book �쳣˵����",e);
		}

		return resp;
		
	}
	/**
	 * �ָ����˵����б�
	 */
	@RequestMapping(value = "/backForm/list")
	private @ResponseBody String bookFormList(@RequestBody String jsonBody) {

		logger.info("url:account/form/backForm/list");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			
			String formSDt = (String)map.get("formSDt");
			String formEDt = (String)map.get("formEDt");
			String bookOkSDt = (String)map.get("bookOkSDt");
			String bookOkEDt = (String)map.get("bookOkEDt");
			
			if(StringUtils.isNotEmpty(formSDt) && StringUtils.isNotEmpty(formEDt)) {
				if (formSDt.length() < 12 ) {
					formSDt = formSDt + " 00:00:00";
				}
				if (formEDt.length() < 12) {
					formEDt = formEDt + " 23:59:59";
				}
				map.put("formSDt", formSDt);
				map.put("formEDt", formEDt);
			}
			
			if(StringUtils.isNotEmpty(bookOkSDt) && StringUtils.isNotEmpty(bookOkEDt)) {
				if (bookOkSDt.length() < 12 ) {
					bookOkSDt = bookOkSDt + " 00:00:00";
				}
				if (bookOkEDt.length() < 12) {
					bookOkEDt = bookOkEDt + " 23:59:59";
				}
				map.put("bookOkSDt", bookOkSDt);
				map.put("bookOkEDt", bookOkEDt);
			}
			resp = formBookService.selectBackFormList(map,loginTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/form/backForm/list �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * δ����ƾ֤�����б�
	 */
	@RequestMapping(value = "/novoucher/list")
	private @ResponseBody String isNoVoucherList(@RequestBody String jsonBody) {

		logger.info("url:account/form/novoucher/list");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			
			String formSDt = (String)map.get("formSDt");
			String formEDt = (String)map.get("formEDt");
			String bookOkSDt = (String)map.get("bookOkSDt");
			String bookOkEDt = (String)map.get("bookOkEDt");
			
			if(StringUtils.isNotEmpty(formSDt) && StringUtils.isNotEmpty(formEDt)) {
				if (formSDt.length() < 12 ) {
					formSDt = formSDt + " 00:00:00";
				}
				if (formEDt.length() < 12) {
					formEDt = formEDt + " 23:59:59";
				}
				map.put("formSDt", formSDt);
				map.put("formEDt", formEDt);
			}
			
			if(StringUtils.isNotEmpty(bookOkSDt) && StringUtils.isNotEmpty(bookOkEDt)) {
				if (bookOkSDt.length() < 12 ) {
					bookOkSDt = bookOkSDt + " 00:00:00";
				}
				if (bookOkEDt.length() < 12) {
					bookOkEDt = bookOkEDt + " 23:59:59";
				}
				map.put("bookOkSDt", bookOkSDt);
				map.put("bookOkEDt", bookOkEDt);
			}
			
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			
			
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = formBookService.selectNovoucherList(map,"0");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/form/novoucher/list �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * ��ѯƾ֤-���ݲ�ѯ
	 */
	@RequestMapping(value = "/voucher/formList")
	private @ResponseBody String voucherFormList(@RequestBody String jsonBody) {

		logger.info("url:account/form/voucher/formList");
		logger.info("���������"+jsonBody);
		String resp="";
		
		try {
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
		
			String formSDt = (String)map.get("formSDt");
			String formEDt = (String)map.get("formEDt");
			String bookOkSDt = (String)map.get("bookOkSDt");
			String bookOkEDt = (String)map.get("bookOkEDt");
			
			if(StringUtils.isNotEmpty(formSDt) && StringUtils.isNotEmpty(formEDt)) {
				if (formSDt.length() < 12 ) {
					formSDt = formSDt + " 00:00:00";
				}
				if (formEDt.length() < 12) {
					formEDt = formEDt + " 23:59:59";
				}
				map.put("formSDt", formSDt);
				map.put("formEDt", formEDt);
			}
			
			if(StringUtils.isNotEmpty(bookOkSDt) && StringUtils.isNotEmpty(bookOkEDt)) {
				if (bookOkSDt.length() < 12 ) {
					bookOkSDt = bookOkSDt + " 00:00:00";
				}
				if (bookOkEDt.length() < 12) {
					bookOkEDt = bookOkEDt + " 23:59:59";
				}
				map.put("bookOkSDt", bookOkSDt);
				map.put("bookOkEDt", bookOkEDt);
			}
			
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			
			
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			if(StringUtils.isNotEmpty((String)map.get("isFindForm"))) {
				
				resp = formBookService.selectNovoucherList(map,"111");
			} else {
				resp = formBookService.selectNovoucherList(map,"0");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/form/novoucher/list �쳣˵����",e);
		}

		return resp;
	}
	
	/**
	 * ��ĩ����
	 */
	@RequestMapping(value = "/final/deal")
	private @ResponseBody String finalDeal(@RequestBody String jsonBody) {

		logger.info("url:account/form/final/deal");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNm = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼��
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //��ǰ�û�
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			resp = formBookService.finalDealForm(map,loginTime,accNm,userName);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/form/final/deal �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * �ָ���ĩ����
	 */
	@RequestMapping(value = "/final/backDeal")
	private @ResponseBody String finalBackDeal(@RequestBody String jsonBody) {

		logger.info("url:account/form/final/backDeal");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNm = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			resp = formBookService.finalBackDealForm(map,loginTime,accNm);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/form/final/backDeal �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * δ��ĩ������
	 */
	@RequestMapping(value = "/final/noDeal")
	private @ResponseBody String finalNoDeal(@RequestBody String jsonBody) {

		logger.info("url:account/form/final/noDeal");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNm = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			resp = formBookService.selectNoFinalDealList(map,loginTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:account/form/final/noDeal �쳣˵����",e);
		}

		return resp;
	}
	
	/**
	 * ��ĩ����
	 */
	@RequestMapping(value = "/final/dealMonth")
	private @ResponseBody String finalMonthDeal(@RequestBody String jsonBody) {

		logger.info("url:account/form/final/dealMonth");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String orderNum = (String)map.get("orderNum");
			if(StringUtils.isNotEmpty(orderNum)) {
				resp = formBookService.finalMonthDeal(map,accNum,loginTime);
			} 
		
		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespObj("account/form/final/dealMonth", false, "�����·ݲ���Ϊ��", null);
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/form/final/dealMonth �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * ��ѯ��ĩ�����¶�
	 */
	@RequestMapping(value = "/final/dealMonthList")
	private @ResponseBody String isMonthDeal(@RequestBody String jsonBody) {

		logger.info("url:account/form/final/dealMonthList");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNm = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			resp = formBookService.selectFinalMonthDealList(map,loginTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:account/form/final/dealMonthList �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * ������ĩ�����¶�
	 */
	@RequestMapping(value = "/final/settingDealMonth")
	private @ResponseBody String settingMonthDeal(@RequestBody String jsonBody) {

		logger.info("url:account/form/final/settingDealMonth");
		logger.info("���������"+jsonBody);
		
		String resp="";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			resp = formBookService.settingDealMonth(map,accNum,loginTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:account/form/final/dealMonthList �쳣˵����",e);
		}

		return resp;
	}
	/**
	 * �ָ���ĩ����
	 */
	@RequestMapping(value = "/final/backDealMonth")
	private @ResponseBody String finalBackMonthDeal(@RequestBody String jsonBody) {

		logger.info("url:account/form/final/backDealMonth");
		logger.info("���������"+jsonBody);
		
		String resp="";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			
			
			resp = formBookService.finalMonthDealBack(map,accNum,loginTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:account/form/final/backDealMonth �쳣˵����",e);
		}

		return resp;
	}
	
	/**
	 * ����-�ڳ����
	 */
	@RequestMapping(value = "/final/termBgnBook")
	private @ResponseBody String termBgnBook(@RequestBody String jsonBody) {
		logger.info("url:account/form/final/termBgnBook");
		logger.info("���������"+jsonBody);
		
		String resp="";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			resp = formBookService.finalTermBgnBook(map,accNum,loginTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:account/form/final/termBgnBook �쳣˵����",e);
		}
		return resp;
	}
	/**
	 * �ָ�����-�ڳ����
	 */
	@RequestMapping(value = "/final/termBgnBackBook")
	private @ResponseBody String termBgnBackBook(@RequestBody String jsonBody) {
		logger.info("url:account/form/final/backTermBgnBook");
		logger.info("���������"+jsonBody);
		
		String resp="";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			resp = formBookService.finalTermBgnBackBook(map,accNum,loginTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:account/form/final/termBgnBackBook �쳣˵����",e);
		}
		return resp;
	}
	/**
	 * �س嵥�б�
	 * @return
	 */
	@RequestMapping(value = "/backFlush/list")
	private @ResponseBody String backFlushFormList(@RequestBody String jsonBody) {
		String url = "account/form/backFlush/list";
		logger.info(url);
		logger.info("���������"+jsonBody);	
		String resp="";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			
			String formSDt = (String)map.get("formSDt");
			String formEDt = (String)map.get("formEDt");
			
			String bookOkSDt = (String)map.get("bookOkSDt");
			String bookOkEDt = (String)map.get("bookOkEDt");
			
			if(StringUtils.isNotEmpty(formSDt) && StringUtils.isNotEmpty(formEDt)) {
				if (formSDt.length() < 12 ) {
					formSDt = formSDt + " 00:00:00";
				}
				if (formEDt.length() < 12) {
					formEDt = formEDt + " 23:59:59";
				}
				map.put("formSDt", formSDt);
				map.put("formEDt", formEDt);
			}
			
			if(StringUtils.isNotEmpty(bookOkSDt) && StringUtils.isNotEmpty(bookOkEDt)) {
				if (bookOkSDt.length() < 12 ) {
					bookOkSDt = bookOkSDt + " 00:00:00";
				}
				if (bookOkEDt.length() < 12) {
					bookOkEDt = bookOkEDt + " 23:59:59";
				}
				map.put("bookOkSDt", bookOkSDt);
				map.put("bookOkEDt", bookOkEDt);
			}
			
			
			resp = formBookService.backFlushFormList(map,url);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(url+ "�쳣˵����",e);
		}
		return resp;
	}
	/**
	 * ��ĩ����ʱ-����
	 */
	@RequestMapping(value = "/mth/sealBook")
	private @ResponseBody String mthSealBook(@RequestBody String jsonBody) {
		String url = "account/form/mth/sealBook";
		logger.info(url);
		logger.info("���������"+jsonBody);	
		String resp="";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			
			resp = formBookService.mthSealBook(map,url,loginTime,accNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(url+ "�쳣˵����",e);
		}
		return resp;
	}
	
	/**
	 * ��������������
	 * @return
	 */
	@RequestMapping(value = "/outIntoAdj/formBook")
	private String outIntoAdjFormBook(@RequestBody String jsonBody) {
		String url = "account/form/outIntoAdj/formBook";
		logger.info(url);
		logger.info("���������"+jsonBody);	
		String resp="";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼��
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //��¼��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			
			resp = formBookService.outIntoAdjFormBook(map,url,loginTime,userName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(url+ "�쳣˵����",e);
		}
		return resp;
		
	}

	/**
	 * �����������ָ�����
	 * @return
	 */
	@RequestMapping(value = "/outIntoAdj/backFormBook")
	private String outIntoAdjBackFormBook(@RequestBody String jsonBody) {
		String url = "account/form/outIntoAdj/backFormBook";
		logger.info(url);
		logger.info("���������"+jsonBody);	
		String resp="";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //��¼ʱ��
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //��¼��
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //��¼��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			
			resp = formBookService.outIntoAdjBackFormBook(map,url,loginTime,userName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(url+ "�쳣˵����",e);
		}
		return resp;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
