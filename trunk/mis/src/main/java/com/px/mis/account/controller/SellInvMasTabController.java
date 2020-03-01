package com.px.mis.account.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellInvMasTab;
import com.px.mis.account.entity.SellInvSubTab;
import com.px.mis.account.service.SellInvMasTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//���۷�Ʊ�ӿ�
@RequestMapping(value = "account/sellInvMasTab", method = RequestMethod.POST)
@Controller
public class SellInvMasTabController {
	private Logger logger = LoggerFactory.getLogger(SellInvMasTabController.class);
	@Autowired
	private SellInvMasTabService sellInvMasTabService;

	/* ���� */
	@RequestMapping("insertSellInvMasTab")
	public @ResponseBody Object insertSellInvMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/sellInvMasTab/insertSellInvMasTab");
		logger.info("���������" + jsonData);
		ObjectNode on = null;
		String resp = "";
		SellInvMasTab sellInvMasTab = null;
		String userId = "";
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonData);
			userId = aString.get("accNum").asText();
			sellInvMasTab = BaseJson.getPOJO(jsonData, SellInvMasTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/sellInvMasTab/insertSellInvMasTab �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/insertSellInvMasTab", false,
						"���������������������������Ƿ���д��ȷ��insert error!", sellInvMasTab);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/sellInvMasTab/insertSellInvMasTab �쳣˵����", e);
			}
			return resp;
		}
		try {
			on = sellInvMasTabService.insertSellInvMasTab(sellInvMasTab, userId);
			if (on != null) {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/insertSellInvMasTab",
						on.get("isSuccess").asBoolean(), on.get("message").asText(), sellInvMasTab);

			} else {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/insertSellInvMasTab", false, "insert error!",
						null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/sellInvMasTab/insertSellInvMasTab �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ɾ�� */
	@RequestMapping("deleteSellInvMasTab")
	public @ResponseBody Object deleteSellInvMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/sellInvMasTab/deleteSellInvMasTab");
		logger.info("���������" + jsonData);
		ObjectNode on = null;
		String resp = "";
		String sellInvNum = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			sellInvNum = reqBody.get("sellInvNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/sellInvMasTab/deleteSellInvMasTab �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/deleteSellInvMasTab", false,
						"���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/sellInvMasTab/deleteSellInvMasTab �쳣˵����", e);
			}
			return resp;
		}
		try {
			on = sellInvMasTabService.deleteSellInvMasTabBySellInvNum(sellInvNum);
			if (on != null) {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/deleteSellInvMasTab",
						on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			} else {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/deleteSellInvMasTab", false, "delete error!",
						null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/sellInvMasTab/deleteSellInvMasTab �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ���� */
	@RequestMapping("updateSellInvMasTab")
	public @ResponseBody Object updateSellInvMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/sellInvMasTab/updateSellInvMasTab");
		logger.info("���������" + jsonData);
		ObjectNode on = null;
		String resp = "";
		SellInvMasTab sellInvMasTab = null;
		List<SellInvSubTab> sellInvSubTabList = new ArrayList<>();
		try {
			sellInvMasTab = BaseJson.getPOJO(jsonData, SellInvMasTab.class);
			List<Map> mList = BaseJson.getList(jsonData);

			for (Map map : mList) {
				SellInvSubTab sellInvSubTab = new SellInvSubTab();
				BeanUtils.populate(sellInvSubTab, map);
				sellInvSubTabList.add(sellInvSubTab);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/sellInvMasTab/updateSellInvMasTab �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/updateSellInvMasTab", false,
						"���������������������������Ƿ���д��ȷ��update error!", sellInvMasTab);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/sellInvMasTab/updateSellInvMasTab �쳣˵����", e);
			}
			return resp;
		}
		try {
			on = sellInvMasTabService.updateSellInvMasTab(sellInvMasTab, sellInvSubTabList);
			if (on != null) {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/updateSellInvMasTab",
						on.get("isSuccess").asBoolean(), on.get("message").asText(), sellInvMasTab);
			} else {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/updateSellInvMasTab", false, "update error!",
						null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/sellInvMasTab/updateSellInvMasTab �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ��ѯ���� */
	@RequestMapping(value = "selectSellInvMasTab")
	public @ResponseBody Object selectSellInvMasTab(@RequestBody String jsonBody) {
		logger.info("url:/account/sellInvMasTab/selectSellInvMasTab");
		logger.info("���������" + jsonBody);

		String resp = "";
		Map map = null;
		Integer pageNo = null;
		Integer pageSize = null;
		String startBllgDt = "";
		String endBllgDt = "";
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int) map.get("pageNo");
			pageSize = (int) map.get("pageSize");
			startBllgDt = (String) map.get("startBllgDt");
			endBllgDt = (String) map.get("endBllgDt");
			if (startBllgDt != null || startBllgDt != "") {
				startBllgDt = startBllgDt + " 00:00:00";
			}
			if (endBllgDt != null || endBllgDt != "") {
				endBllgDt = endBllgDt + " 23:59:59";
			}
			if (pageNo == 0 || pageSize == 0) {
				resp = BaseJson.returnRespList("/account/sellInvMasTab/selectSellInvMasTab", false,
						"�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/sellInvMasTab/selectSellInvMasTab �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespList("/account/sellInvMasTab/selectSellInvMasTab", false,
						"���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/sellInvMasTab/selectSellInvMasTab �쳣˵����", e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			map.put("startBllgDt", startBllgDt);
			map.put("endBllgDt", endBllgDt);
			resp = sellInvMasTabService.selectSellInvMasTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/sellInvMasTab/selectSellInvMasTab �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ��ѯ���� */
	@RequestMapping(value = "selectSellInvMasTabById")
	public @ResponseBody Object selectSellInvMasTabById(@RequestBody String jsonData) {
		logger.info("url:/account/sellInvMasTab/selectSellInvMasTabById");
		logger.info("���������" + jsonData);
		String resp = "";
		String sellInvNum = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			sellInvNum = reqBody.get("sellInvNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/sellInvMasTab/selectSellInvMasTabById �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/selectSellInvMasTabById", false,
						"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/sellInvMasTab/selectSellInvMasTabById �쳣˵����", e);
			}
			return resp;
		}
		try {
			SellInvMasTab sellInvMasTab = sellInvMasTabService.selectSellInvMasTabBySellInvNum(sellInvNum);
			if (sellInvMasTab != null) {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/selectSellInvMasTabById", true, "����ɹ���",
						sellInvMasTab);
			} else {
				resp = BaseJson.returnRespObj("/account/sellInvMasTab/selectSellInvMasTabById", false, "û�в鵽���ݣ�",
						sellInvMasTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/sellInvMasTab/selectSellInvMasTabById �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ����ɾ��
	@RequestMapping(value = "deleteSellInvMasTabList", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteSellInvMasTabList(@RequestBody String jsonBody) throws IOException {

		logger.info("url:account/sellInvMasTab/deleteSellInvMasTabList");
		logger.info("���������" + jsonBody);
		String resp = "";
		resp = sellInvMasTabService.deleteSellInvMasTabList(BaseJson.getReqBody(jsonBody).get("sellInvNum").asText());
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �����������״̬
	@RequestMapping(value = "updateSellInvMasTabIsNtChk", method = RequestMethod.POST)
	@ResponseBody
	public String updateSellInvMasTabIsNtChk(@RequestBody String jsonBody) {

		logger.info("url:account/sellInvMasTab/updateSellInvMasTabIsNtChk");
		logger.info("���������" + jsonBody);
		String resp = "";
		List<SellInvMasTab> sellInvMasTabList = new ArrayList<>();
//		JSONObject jsonObject = JSON.parseObject(jsonBody);
//		JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//		JSONArray jsonArray = jsonObjectList.getJSONArray("sellInvMasTabList");
//		sellInvMasTabList = jsonArray.toJavaList(SellInvMasTab.class);
		try {
			sellInvMasTabList = BaseJson.getPOJOList(jsonBody, "sellInvMasTabList", SellInvMasTab.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		resp = sellInvMasTabService.updateSellInvMasTabIsNtChkList(sellInvMasTabList);
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �������۵�����һ�����۷�Ʊ
	@RequestMapping(value = "selectSellInvMasTabBingList", method = RequestMethod.POST)
	@ResponseBody
	private String selectSellInvMasTabBingList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:account/sellInvMasTab/selectSellInvMasTabBingList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			List<SellComnInv> sellComnInvList = new ArrayList<>();
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//			JSONArray jsonArray = jsonObjectList.getJSONArray("lists");
//			sellComnInvList = jsonArray.toJavaList(SellComnInv.class);
			sellComnInvList = BaseJson.getPOJOList(jsonBody, "lists", SellComnInv.class);
			resp = sellInvMasTabService.selectSellInvMasTabBingList(sellComnInvList);
		} catch (Exception e) {
			e.printStackTrace();
			return BaseJson.returnRespObj("/account/sellInvMasTab/selectSellInvMasTabBingList", false, e.getMessage(),
					null);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �������۷�Ʊ
	@RequestMapping("uploadSellInvMasTabFile")
	@ResponseBody
	public Object uploadSellInvMasTabFile(HttpServletRequest request) throws IOException {
		try {
			// ����һ��ͨ�õĶಿ�ֽ�����
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// �ж� request �Ƿ����ļ��ϴ�,���ಿ������
			if (multipartResolver.isMultipart(request)) {
				// ת���ɶಿ��request
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// requestǿ��ת��ע��
				MultipartFile file = mRequest.getFile("file");
				if (file == null) {
					return ("��ѡ���ļ���");
				}
				return sellInvMasTabService.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("account/sellInvMasTab/uploadSellInvMasTabFile", false, "��ѡ���ļ���", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/sellInvMasTab/uploadSellInvMasTabFile", false, e.getMessage(), null);
		}
	}

	// ��ӡ�͵���ʱʹ�õĲ�ѯ�ӿ�
	@RequestMapping("printingSellInvMasTabList")
	@ResponseBody
	public Object printingSellInvMasTabList(@RequestBody String jsonBody) {

		logger.info("account/sellInvMasTab/printingSellInvMasTabList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String startBllgDt = (String) map.get("startBllgDt");
			String endBllgDt = (String) map.get("endBllgDt");
			if (startBllgDt != null || startBllgDt != "") {
				startBllgDt = startBllgDt + " 00:00:00";
			}
			if (endBllgDt != null || endBllgDt != "") {
				endBllgDt = endBllgDt + " 23:59:59";
			}
			map.put("startBllgDt", startBllgDt);
			map.put("endBllgDt", endBllgDt);
			resp = sellInvMasTabService.upLoadSellInvMasTabList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

}
