package com.px.mis.purc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
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
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.EntrsAgnStl;
import com.px.mis.purc.entity.EntrsAgnStlSub;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.EntrsAgnStlService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

/*ί�д������㵥��������*/
@RequestMapping(value = "purc/EntrsAgnStl")
@Controller
public class EntrsAgnStlController {

	private Logger logger = LoggerFactory.getLogger(EntrsAgnStlController.class);

	@Autowired
	private EntrsAgnStlService eass;
	// ����
	@Autowired
	FormBookService formBookService;

	// ����ί�д������㵥��Ϣ
	@RequestMapping(value = "addEntrsAgnStl", method = RequestMethod.POST)
	@ResponseBody
	private String addEntrsAgnStl(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/addEntrsAgnStl");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonBody);
			String userId = aString.get("accNum").asText();
			String userName = aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();

			EntrsAgnStl entrsAgnStl = BaseJson.getPOJO(jsonBody, EntrsAgnStl.class);
			entrsAgnStl.setSetupPers(userName);
			List<EntrsAgnStlSub> entrsAgnStlSubList = new ArrayList<>();
			entrsAgnStlSubList=BaseJson.getPOJOList(jsonBody, EntrsAgnStlSub.class);
			resp = eass.addEntrsAgnStl(userId, entrsAgnStl, entrsAgnStlSubList,loginTime);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �޸�ί�д������㵥��Ϣ
	@RequestMapping(value = "editEntrsAgnStl", method = RequestMethod.POST)
	@ResponseBody
	private String editEntrsAgnStl(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/editEntrsAgnStl");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonBody);
			String userName = aString.get("userName").asText();
			EntrsAgnStl entrsAgnStl = BaseJson.getPOJO(jsonBody, EntrsAgnStl.class);
			entrsAgnStl.setMdfr(userName);
			List<EntrsAgnStlSub> entrsAgnStlSubList = new ArrayList<>();
			entrsAgnStlSubList=BaseJson.getPOJOList(jsonBody, EntrsAgnStlSub.class);
			resp = eass.editEntrsAgnStl(entrsAgnStl, entrsAgnStlSubList);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ɾ��ί�д������㵥��Ϣ
	@RequestMapping(value = "deleteEntrsAgnStl", method = RequestMethod.POST)
	@ResponseBody
	private String deleteEntrsAgnStl(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/deleteEntrsAgnStl");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			resp = eass.deleteEntrsAgnStl(BaseJson.getReqBody(jsonBody).get("stlSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ��ѯί�д������㵥��Ϣ
	@RequestMapping(value = "queryEntrsAgnStl", method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnStl(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/queryEntrsAgnStl");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			resp = eass.queryEntrsAgnStl(BaseJson.getReqBody(jsonBody).get("stlSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	@RequestMapping(value = "queryEntrsAgnStlList", method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnStlList(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/queryEntrsAgnStlList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String stlSnglDt1 = (String) map.get("stlSnglDt1");
			String stlSnglDt2 = (String) map.get("stlSnglDt2");
			if (stlSnglDt1 != null && stlSnglDt1 != "") {
				stlSnglDt1 = stlSnglDt1 + " 00:00:00";
			}
			if (stlSnglDt2 != null && stlSnglDt2 != "") {
				stlSnglDt2 = stlSnglDt2 + " 23:59:59";
			}
			map.put("stlSnglDt1", stlSnglDt1);
			map.put("stlSnglDt2", stlSnglDt2);
			resp = eass.queryEntrsAgnStlList(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ������������Ϣɾ��
	@RequestMapping(value = "deleteEntrsAgnStlList", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteEntrsAgnStlList(@RequestBody String jsonBody) throws IOException {

		logger.info("url:purc/EntrsAgnStl/deleteEntrsAgnStlList");
		logger.info("���������" + jsonBody);
		String resp = "";
		resp = eass.deleteEntrsAgnStlList(BaseJson.getReqBody(jsonBody).get("stlSnglId").asText());
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ��ӡ�����������ѯȫ���ͻ�����
	@RequestMapping("printingEntrsAgnStlList")
	@ResponseBody
	public Object printingEntrsAgnStlList(@RequestBody String jsonBody) {

		logger.info("url:purc/EntrsAgnStl/printingEntrsAgnStlList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String stlSnglDt1 = (String) map.get("stlSnglDt1");
			String stlSnglDt2 = (String) map.get("stlSnglDt2");
			if (stlSnglDt1 != null && stlSnglDt1 != "") {
				stlSnglDt1 = stlSnglDt1 + " 00:00:00";
			}
			if (stlSnglDt2 != null && stlSnglDt2 != "") {
				stlSnglDt2 = stlSnglDt2 + " 23:59:59";
			}
			map.put("stlSnglDt1", stlSnglDt1);
			map.put("stlSnglDt2", stlSnglDt2);
			resp = eass.printingEntrsAgnStlList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �޸�ί�д������㵥���״̬
	@RequestMapping(value="updateEntrsAgnStlIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updatePursOrdrIsNtChk(@RequestBody String jsonBody) throws Exception {	
		
		logger.info("url:purc/EntrsAgnStl/updateEntrsAgnStlIsNtChk");
		logger.info("���������"+jsonBody);
		Map<String,Object> result = new HashMap<>();
		String resp = "";
		boolean isSuccess = false;
		List<EntrsAgnStl> entrsAgnStlList=new ArrayList();
		try
		{
			String userId=BaseJson.getReqHead(jsonBody).get("accNum").asText();
			String userName=BaseJson.getReqHead(jsonBody).get("userName").asText();
			String loginTime=BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/EntrsAgnStl/updateEntrsAgnStlIsNtChk", false, "�����ѷ��ˣ�", null);
				logger.info("���ز�����" + resp);
				return resp;
			}
			entrsAgnStlList = BaseJson.getPOJOList(jsonBody, EntrsAgnStl.class);
		    for(EntrsAgnStl entrsAgnStl :entrsAgnStlList) {
				try
				{
					entrsAgnStl.setChkr(userName);
					result=eass.updateEntrsAgnStlIsNtChk(userId, entrsAgnStl,loginTime);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}
		    resp = BaseJson.returnRespObj("purc/EntrsAgnStl/updateEntrsAgnStlIsNtChk", isSuccess, resp, null);
		}catch (Exception ex) {
			logger.info("���ز�����"+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/updateEntrsAgnStlIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}

	// ����������ⵥ
	@RequestMapping("uploadEntrsAgnStlFile")
	@ResponseBody
	public Object uploadEntrsAgnStlFile(HttpServletRequest request) throws IOException {
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
					return ("��ѡ���ļ�!");
				}
				return eass.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("purc/EntrsAgnStl/uploadEntrsAgnStlFile", false, "��ѡ���ļ���", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/EntrsAgnStl/uploadEntrsAgnStlFile", false, e.getMessage(), null);
		}
	}

	// ��ҳ+�����ѯί�д������㵥
	@RequestMapping(value = "queryEntrsAgnStlListOrderBy", method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnStlListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/queryEntrsAgnStlListOrderBy");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String stlSnglDt1 = (String) map.get("stlSnglDt1");
			String stlSnglDt2 = (String) map.get("stlSnglDt2");
			if (stlSnglDt1 != null && stlSnglDt1 != "") {
				stlSnglDt1 = stlSnglDt1 + " 00:00:00";
			}
			if (stlSnglDt2 != null && stlSnglDt2 != "") {
				stlSnglDt2 = stlSnglDt2 + " 23:59:59";
			}
			map.put("stlSnglDt1", stlSnglDt1);
			map.put("stlSnglDt2", stlSnglDt2);
			resp = eass.queryEntrsAgnStlListOrderBy(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}
}
