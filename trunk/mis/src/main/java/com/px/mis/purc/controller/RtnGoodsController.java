package com.px.mis.purc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
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

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.service.RtnGoodsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

/*�����˻�����������*/
@RequestMapping(value = "purc/RtnGoods")
@Controller
public class RtnGoodsController {

	private Logger logger = LoggerFactory.getLogger(RtnGoodsController.class);

	@Autowired
	private RtnGoodsService rgs;
	// ����
	@Autowired
	FormBookService formBookService;

	// �����˻�����Ϣ
	@RequestMapping(value = "addRtnGoods", method = RequestMethod.POST)
	@ResponseBody
	private String addRtnGoods(@RequestBody String jsonBody) {
		logger.info("url:purc/RtnGoods/addRtnGoods");
		logger.info("���������" + jsonBody);
		String resp = "";
		String userId = null;
		RtnGoods rtnGoods = null;
		String loginTime="";
		List<RtnGoodsSub> rtnGoodsSubList = null;
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonBody);
			userId = aString.get("accNum").asText();
			String userName = aString.get("userName").asText();
			loginTime=aString.get("loginTime").asText();

			rtnGoods = BaseJson.getPOJO(jsonBody, RtnGoods.class);
			rtnGoods.setSetupPers(userName);
			rtnGoodsSubList = new ArrayList<>();
			rtnGoodsSubList=BaseJson.getPOJOList(jsonBody, RtnGoodsSub.class);
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			try {
				return resp = BaseJson.returnRespObj("purc/RtnGoods/addRtnGoods", false, "���ݸ�ʽ����", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		try {
			resp = rgs.addRtnGoods(userId, rtnGoods, rtnGoodsSubList,loginTime);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("purc/RtnGoods/addRtnGoods", false, ex.getMessage(), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �޸��˻�����Ϣ
	@RequestMapping(value = "editRtnGoods", method = RequestMethod.POST)
	@ResponseBody
	private String editRtnGoods(@RequestBody String jsonBody) {
		logger.info("url:purc/RtnGoods/editRtnGoods");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonBody);
			String userName = aString.get("userName").asText();
			RtnGoods rtnGoods = BaseJson.getPOJO(jsonBody, RtnGoods.class);
			rtnGoods.setMdfr(userName);
			List<RtnGoodsSub> rtnGoodsSubList = new ArrayList<>();
			rtnGoodsSubList=BaseJson.getPOJOList(jsonBody, RtnGoodsSub.class);
			resp = rgs.editRtnGoods(rtnGoods, rtnGoodsSubList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ɾ���˻�����Ϣ
	@RequestMapping(value = "deleteRtnGoods", method = RequestMethod.POST)
	@ResponseBody
	private String deleteRtnGoods(@RequestBody String jsonBody) {
		logger.info("url:purc/RtnGoods/deleteRtnGoods");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			resp = rgs.deleteRtnGoods(BaseJson.getReqBody(jsonBody).get("rtnGoodsId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �����˻�����Ų�ѯ�˻�����Ϣ
	@RequestMapping(value = "queryRtnGoods", method = RequestMethod.POST)
	@ResponseBody
	private String queryRtnGoods(@RequestBody String jsonBody) {
		logger.info("url:purc/RtnGoods/queryRtnGoods");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			resp = rgs.queryRtnGoods(BaseJson.getReqBody(jsonBody).get("rtnGoodsId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ������ѯ�˻�����Ϣ
	@RequestMapping(value = "queryRtnGoodsList", method = RequestMethod.POST)
	@ResponseBody
	private String queryRtnGoodsList(@RequestBody String jsonBody) {
		logger.info("url:purc/RtnGoods/queryRtnGoodsList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String rtnGoodsDt1 = (String) map.get("rtnGoodsDt1");
			String rtnGoodsDt2 = (String) map.get("rtnGoodsDt2");
			if (rtnGoodsDt1 != null && rtnGoodsDt1 != "") {
				rtnGoodsDt1 = rtnGoodsDt1 + " 00:00:00";
			}
			if (rtnGoodsDt2 != null && rtnGoodsDt2 != "") {
				rtnGoodsDt2 = rtnGoodsDt2 + " 23:59:59";
			}
			map.put("rtnGoodsDt1", rtnGoodsDt1);
			map.put("rtnGoodsDt2", rtnGoodsDt2);
			resp = rgs.queryRtnGoodsList(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ���˻����б�������ɾ���˻�����Ϣ
	@RequestMapping(value = "deleteRtnGoodsList", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteRtnGoodsList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/RtnGoods/deleteRtnGoodsList");
		logger.info("���������" + jsonBody);
		String resp = "";
		resp = rgs.deleteRtnGoodsList(BaseJson.getReqBody(jsonBody).get("rtnGoodsId").asText());
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ��ӡ�����������ѯȫ���˻���
	@RequestMapping("printingRtnGoodsList")
	@ResponseBody
	public Object printingRtnGoodsList(@RequestBody String jsonBody) {

		logger.info("url:purc/RtnGoods/printingRtnGoodsList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String rtnGoodsDt1 = (String) map.get("rtnGoodsDt1");
			String rtnGoodsDt2 = (String) map.get("rtnGoodsDt2");
			if (rtnGoodsDt1 != null && rtnGoodsDt1 != "") {
				rtnGoodsDt1 = rtnGoodsDt1 + " 00:00:00";
			}
			if (rtnGoodsDt2 != null && rtnGoodsDt2 != "") {
				rtnGoodsDt2 = rtnGoodsDt2 + " 23:59:59";
			}
			map.put("rtnGoodsDt1", rtnGoodsDt1);
			map.put("rtnGoodsDt2", rtnGoodsDt2);
			resp = rgs.printingRtnGoodsList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ����˻�����Ϣ
	@RequestMapping(value = "updateRtnGoodsIsNtChkList", method = RequestMethod.POST)
	@ResponseBody
	private String updateRtnGoodsIsNtChkList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/RtnGoods/updateRtnGoodsIsNtChkList");
		logger.info("���������" + jsonBody);
		Map<String, Object> result = new HashMap<>();
		String resp = "";
		boolean isSuccess = false;
		List<RtnGoods> rtnGoodsList = new ArrayList();
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonBody);
			String userId = aString.get("accNum").asText();
			String userName = aString.get("userName").asText();
			String loginTime = aString.get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/RtnGoods/updateRtnGoodsIsNtChkList", false, "�����ѷ��ˣ�", null);
				logger.info("���ز�����" + resp);
				return resp;
			}
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//			JSONArray jsonArray = jsonObjectList.getJSONArray("list");
//			rtnGoodsList = jsonArray.toJavaList(RtnGoods.class);
			rtnGoodsList = BaseJson.getPOJOList(jsonBody, RtnGoods.class);
			for (RtnGoods rtnGoods : rtnGoodsList) {
				try {
					rtnGoods.setChkr(userName);
					result = rgs.updateRtnGoodsIsNtChksList(userId, rtnGoods, loginTime);
					isSuccess = (boolean) result.get("isSuccess");
					resp += result.get("message");
				} catch (Exception e) {
					isSuccess = false;
					resp += e.getMessage();
				}
			}
			resp = BaseJson.returnRespObj("purc/RtnGoods/updateRtnGoodsIsNtChkList", isSuccess, resp, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			resp = BaseJson.returnRespObj("purc/RtnGoods/updateRtnGoodsIsNtChkList", false, ex.getMessage(), null);

		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �����˻���
	@RequestMapping("uploadRtnGoodsFile")
	@ResponseBody
	public Object uploadRtnGoodsFile(HttpServletRequest request) throws IOException {
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
				return rgs.uploadFileAddDb(file, 0);
			} else {
				return BaseJson.returnRespObj("purc/RtnGoods/uploadRtnGoodsFile", false, "��ѡ���ļ���", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/RtnGoods/uploadRtnGoodsFile", false, e.getMessage(), null);
		}
	}

	// �����˻���
	@RequestMapping("uploadRtnGoodsFileU8")
	@ResponseBody
	public Object uploadRtnGoodsFileU8(HttpServletRequest request) throws IOException {
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
				return rgs.uploadFileAddDb(file, 1);
			} else {
				return BaseJson.returnRespObj("purc/RtnGoods/uploadRtnGoodsFileU8", false, "��ѡ���ļ���", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/RtnGoods/uploadRtnGoodsFileU8", false, e.getMessage(), null);
		}
	}

	// ����+��ҳ��ѯ�˻�����Ϣ
	@RequestMapping(value = "queryRtnGoodsListOrderBy", method = RequestMethod.POST)
	@ResponseBody
	private String queryRtnGoodsListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/RtnGoods/queryRtnGoodsListOrderBy");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String rtnGoodsDt1 = (String) map.get("rtnGoodsDt1");
			String rtnGoodsDt2 = (String) map.get("rtnGoodsDt2");
			if (rtnGoodsDt1 != null && rtnGoodsDt1 != "") {
				rtnGoodsDt1 = rtnGoodsDt1 + " 00:00:00";
			}
			if (rtnGoodsDt2 != null && rtnGoodsDt2 != "") {
				rtnGoodsDt2 = rtnGoodsDt2 + " 23:59:59";
			}
			map.put("rtnGoodsDt1", rtnGoodsDt1);
			map.put("rtnGoodsDt2", rtnGoodsDt2);
			resp = rgs.queryRtnGoodsListOrderBy(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

}
