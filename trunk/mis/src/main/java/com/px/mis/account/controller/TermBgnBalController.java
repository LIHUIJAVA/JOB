package com.px.mis.account.controller;

import com.px.mis.account.entity.TermBgnBal;
import com.px.mis.account.service.TermBgnBalService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
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
import java.util.List;
import java.util.Map;

/*������ڳ���������*/
@RequestMapping(value = "account/TermBgnBal")
@Controller
public class TermBgnBalController {

	private Logger logger = LoggerFactory.getLogger(TermBgnBalController.class);

	@Autowired
	private TermBgnBalService tbbs;

	// �����ڳ������Ϣ
	@RequestMapping(value = "addTermBgnBal", method = RequestMethod.POST)
	@ResponseBody
	private String addTermBgnBal(@RequestBody String jsonBody) {
		logger.info("url:account/TermBgnBal/addTermBgnBal");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			TermBgnBal termBgnBal = BaseJson.getPOJO(jsonBody, TermBgnBal.class);
			resp = tbbs.insertTermBgnBal(termBgnBal);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�������ݴ�������ʧ��");
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �޸��ڳ������Ϣ
	@RequestMapping(value = "editTermBgnBal", method = RequestMethod.POST)
	@ResponseBody
	private String editTermBgnBal(@RequestBody String jsonData) {
		logger.info("url:account/TermBgnBal/editTermBgnBal");
		logger.info("���������" + jsonData);
		String resp = "";
		List<TermBgnBal> termBgnBalList = new ArrayList<TermBgnBal>();
		try {

			termBgnBalList = BaseJson.getPOJOList(jsonData, "termBgnBalList", TermBgnBal.class);
			resp = tbbs.updateTermBgnBalByOrdrNum(termBgnBalList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ����ɾ���ڳ������Ϣ
	@RequestMapping(value = "deleteTermBgnBalList", method = RequestMethod.POST)
	@ResponseBody
	private String deleteTermBgnBalList(@RequestBody String jsonBody) {
		logger.info("url:account/TermBgnBal/deleteTermBgnBalList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			resp = tbbs.deleteTermBgnBalByOrdrNum(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/*
	 * //��ѯ���۵���Ϣ
	 * 
	 * @RequestMapping(value="querySellSngl",method = RequestMethod.POST)
	 * 
	 * @ResponseBody private String queryPursOrdr(@RequestBody String jsonBody) {
	 * logger.info("url:purc/SellSngl/querySellSngl");
	 * logger.info("���������"+jsonBody); String resp=""; try {
	 * resp=sss.querySellSngl(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText
	 * ()); } catch (IOException e) { e.printStackTrace(); }
	 * logger.info("���ز�����"+resp); return resp; }
	 */
//�ڳ����
	@RequestMapping(value = "queryTermBgnBalList", method = RequestMethod.POST)
	@ResponseBody
	private String queryTermBgnBalList(@RequestBody String jsonBody) {
		logger.info("url:account/TermBgnBal/queryTermBgnBalList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = tbbs.queryTermBgnBalList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

//	�ڳ����-����
	@RequestMapping(value = "queryTermBgnBalListPrint", method = RequestMethod.POST)
	@ResponseBody
	private String queryTermBgnBalListPrint(@RequestBody String jsonBody) {
		logger.info("url:account/TermBgnBal/queryTermBgnBalListPrint");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp = tbbs.queryTermBgnBalListPrint(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �����ڳ������Ϣ
	@RequestMapping("uploadTermBgnBalFile")
	@ResponseBody
	public Object uploadTermBgnBalFile(HttpServletRequest request) throws IOException {
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
				return tbbs.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("account/TermBgnBal/uploadTermBgnBalFile", false, "��ѡ���ļ���", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/TermBgnBal/uploadTermBgnBalFile", false, e.getMessage(), null);
		}
	}
}
