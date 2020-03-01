package com.px.mis.account.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.px.mis.account.entity.VouchCateDoc;
import com.px.mis.account.service.VouchCateDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//ƾ֤���ӿ�
@RequestMapping(value = "account/vouchCateDoc", method = RequestMethod.POST)
@Controller
public class VouchCateDocController {
	private Logger logger = LoggerFactory.getLogger(VouchCateDocController.class);
	@Autowired
	private VouchCateDocService vcds;

	/* ���ƾ֤��� */
	@RequestMapping("insertVouchCateDoc")
	public @ResponseBody Object insertVouchCateDoc(@RequestBody String jsonData) {
		logger.info("url:/account/vouchCateDoc/insertVouchCateDoc");
		logger.info("���������" + jsonData);
		ObjectNode on = null;
		String resp = "";
		VouchCateDoc vouchCateDoc = null;
		try {
			vouchCateDoc = BaseJson.getPOJO(jsonData, VouchCateDoc.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/insertVouchCateDoc �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/insertVouchCateDoc", false,
						"���������������������������Ƿ���д��ȷ��insert error!", vouchCateDoc);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/insertVouchCateDoc �쳣˵����", e);
			}
			return resp;
		}
		try {
			on = vcds.insertVouchCateDoc(vouchCateDoc);
			if (on != null) {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/insertVouchCateDoc",
						on.get("isSuccess").asBoolean(), on.get("message").asText(), vouchCateDoc);
			} else {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/insertVouchCateDoc", false, "insert error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/insertVouchCateDoc �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ɾ��ƾ֤��� */
	@RequestMapping("deleteVouchCateDoc")
	public @ResponseBody Object deleteVouchCateDoc(@RequestBody String jsonData) {
		logger.info("url:/account/vouchCateDoc/deleteVouchCateDoc");
		logger.info("���������" + jsonData);
		ObjectNode on = null;
		String resp = "";
		String vouchCateWor = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			vouchCateWor = reqBody.get("vouchCateWor").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/deleteVouchCateDoc �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/deleteVouchCateDoc", false,
						"���������������������������Ƿ���д��ȷ��delete error!", vouchCateWor);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/deleteVouchCateDoc �쳣˵����", e);
			}
			return null;
		}
		try {

			resp = vcds.deleteVouchCateDocByVouchCateWor(vouchCateWor);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/deleteVouchCateDoc �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ����ƾ֤��� */
	@RequestMapping("updateVouchCateDoc")
	public @ResponseBody Object updateVouchCateDoc(@RequestBody String jsonData) {
		logger.info("url:/account/vouchCateDoc/updateVouchCateDoc");
		logger.info("���������" + jsonData);
		ObjectNode on = null;
		String resp = "";
		String list = null;
		List<VouchCateDoc> vouchCateDocList = new ArrayList<VouchCateDoc>();
		try {

//            JSONObject jsonObject = JSON.parseObject(jsonData);
//            JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//            JSONArray jsonArray = jsonObjectList.getJSONArray("list");
//            vouchCateDocList = jsonArray.toJavaList(VouchCateDoc.class);
			vouchCateDocList = BaseJson.getPOJOList(jsonData, "list", VouchCateDoc.class);

		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/updateVouchCateDoc �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/updateVouchCateDoc", false,
						"���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/updateVouchCateDoc �쳣˵����", e);
			}
			return resp;
		}
		try {
			on = vcds.updateVouchCateDocById(vouchCateDocList);
			if (on != null) {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/updateVouchCateDoc",
						on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			} else {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/updateVouchCateDoc", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/updateVouchCateDoc �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ��ѯ����ƾ֤��� */
	@RequestMapping(value = "selectVouchCateDoc")
	public @ResponseBody Object selectVouchCateDoc(@RequestBody String jsonBody) {
		logger.info("url:/account/vouchCateDoc/selectVouchCateDoc");
		logger.info("���������" + jsonBody);

		String resp = "";
		Map map = null;
		Integer pageNo = null;
		Integer pageSize = null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int) map.get("pageNo");
			pageSize = (int) map.get("pageSize");
			if (pageNo == 0 || pageSize == 0) {
				resp = BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDoc", false,
						"�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDoc �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDoc", false,
						"���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/selectVouchCateDoc �쳣˵����", e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = vcds.selectVouchCateDocList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDoc �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ��ѯ����ƾ֤��� */
	@RequestMapping(value = "selectVouchCateDocById")
	public @ResponseBody Object selectVouchCateDocById(@RequestBody String jsonData) {
		logger.info("url:/account/vouchCateDoc/selectVouchCateDocById");
		logger.info("���������" + jsonData);

		String resp = "";
		String vouchCateId = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			vouchCateId = reqBody.get("vouchCateWor").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDocById �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/selectVouchCateDocById", false,
						"���������������������������Ƿ���д��ȷ��û���������ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/selectVouchCateDocById �쳣˵����", e);
			}
			return resp;
		}
		try {
			VouchCateDoc vouchCateDoc = vcds.selectVouchCateDocById(vouchCateId);
			if (vouchCateDoc != null) {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/selectVouchCateDocById", true, "����ɹ���",
						vouchCateDoc);
			} else {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/selectVouchCateDocById", false, "û���������ݣ�",
						vouchCateDoc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDocById �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ��ѯ����ƾ֤����ӡ */
	@RequestMapping(value = "selectVouchCateDocPrint")
	public @ResponseBody Object selectVouchCateDocPrint(@RequestBody String jsonBody) {
		logger.info("url:/account/vouchCateDoc/selectVouchCateDocPrint");
		logger.info("���������" + jsonBody);

		String resp = "";
		Map map = null;

		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDocPrint �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDocPrint", false,
						"���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/selectVouchCateDocPrint �쳣˵����", e);
			}
			return resp;
		}
		try {

			resp = vcds.selectVouchCateDocPrint(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDocPrint �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ���뵥��
	@RequestMapping("uploadFileAddDb")
	@ResponseBody
	public String uploadPursOrderFile(HttpServletRequest request) throws IOException {
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
					return BaseJson.returnRespObj("account/vouchCateDoc/uploadFileAddDb", false, "��ѡ���ļ���", null);
				}
				return vcds.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("account/vouchCateDoc/uploadFileAddDb", false, "��ѡ���ļ���", null);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/vouchCateDoc/uploadFileAddDb", false, e.getMessage(), null);

		}

	}
}
