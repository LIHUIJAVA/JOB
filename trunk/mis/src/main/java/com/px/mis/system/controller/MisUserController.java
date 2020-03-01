package com.px.mis.system.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.px.mis.system.entity.MisUser;
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping("system/misUser")
@Controller
public class MisUserController {

	private Logger logger = LoggerFactory.getLogger(MisUserController.class);

	@Autowired
	private MisUserService misUserService;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public String login(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/login");
		logger.info("���������" + jsonBody);
		ObjectNode objectNode = null;
		String resp = "";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
			String accNum = reqBody.get("accNum").asText();
			String password = reqBody.get("password").asText();
			String accSet = reqBody.get("accSet").asText();
			resp = misUserService.login(accNum, password, accSet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/add");
		logger.info("���������" + jsonBody);
		ObjectNode objectNode = null;
		String resp = "";
		try {
			MisUser misUser = BaseJson.getPOJO(jsonBody, MisUser.class);
			misUser.setPassword("123456");
			misUser.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			objectNode = misUserService.insert(misUser);
			resp = BaseJson.returnRespObj("system/misUser/add", objectNode.get("isSuccess").asBoolean(),
					objectNode.get("message").asText(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/delete");
		logger.info("���������" + jsonBody);
		ObjectNode objectNode = null;
		String resp = "";
		try {
			String accNum = BaseJson.getReqBody(jsonBody).get("accNum").asText();
			objectNode = misUserService.delete(accNum);
			resp = BaseJson.returnRespObj("system/misUser/delete", objectNode.get("isSuccess").asBoolean(),
					objectNode.get("message").asText(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public String edit(@RequestBody String jsonReq) {
		logger.info("url:system/misUser/edit");
		logger.info("���������" + jsonReq);
		String resp = misUserService.edit(jsonReq);
		logger.info("���ز���" + resp);
		return resp;
	}

	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	public String queryList(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/queryList");
		logger.info("���������" + jsonBody);
		Map map = null;
		String resp = "";
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = misUserService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}
	//����
	@RequestMapping(value = "queryListExport", method = RequestMethod.POST)
	@ResponseBody
	public String queryListExoprt(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/queryListExport");
		logger.info("���������" + jsonBody);
		Map map = null;
		String resp = "";
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp = misUserService.queryListExport(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}

	@RequestMapping(value = "userTree", method = RequestMethod.POST)
	@ResponseBody
	public String userTree(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/userTree");
		logger.info("���������" + jsonBody);
		String resp = "";
		resp = misUserService.userTree();
		logger.info("���ز���" + resp);
		return resp;
	}

	@RequestMapping(value = "permAss", method = RequestMethod.POST)
	@ResponseBody
	private String permAss(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/permAss");
		logger.info("���������" + jsonBody);
		String resp = misUserService.permAss(jsonBody);
		logger.info("���ز�����" + resp);
		return resp;
	}

	@RequestMapping(value = "delMisUser", method = RequestMethod.POST)
	@ResponseBody
	public String delMisUser(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/delMisUser");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			ObjectNode objectNode = misUserService.delMisUser(jsonBody);
			resp = BaseJson.returnRespObj("system/misUser/delMisUser", objectNode.get("isSuccess").asBoolean(),
					objectNode.get("message").asText(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}
	
	/*���ձ�Ų�ѯ�û���Ϣ*/
	@RequestMapping("query")
	@ResponseBody
	public Object selectCustDocByCustId(@RequestBody String jsonData) {
		logger.info("system/misUser/query");
		logger.info("���������"+jsonData);
		String resp=""; 
		try {
			;
			String accNum = BaseJson.getReqBody(jsonData).get("accNum").asText();
			resp=misUserService.query(accNum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	
	@RequestMapping(value = "updateBatch", method = RequestMethod.POST)
	@ResponseBody
	public String updateBatch(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/updateBatch");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			resp = misUserService.updateBatch(jsonBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}
	// ���뵥��
	@RequestMapping("uploadFileAddDb")
	@ResponseBody
	public Object uploadPursOrderFile(HttpServletRequest request) {
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
					return BaseJson.returnRespObj("system/misUser/uploadFileAddDb", false, "��ѡ���ļ���", null);
				}

				return misUserService.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("system/misUser/uploadFileAddDb", false, "��ѡ���ļ���", null);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				return BaseJson.returnRespObj("system/misUser/uploadFileAddDb", false, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return null;

	}
	@RequestMapping(value = "userWhsList", method = RequestMethod.POST)
	@ResponseBody
	public String userLogicList(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/userWhsList");
		logger.info("���������"+jsonBody);
		String resp = "";
		try {
			resp = misUserService.selectUserLogicWhs(jsonBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}
	/*
	 * �����û��ֿ�Ȩ��
	 */
	@RequestMapping(value = "userWhsAdd", method = RequestMethod.POST)
	@ResponseBody
	public String userLogicAdd(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/userWhsAdd");
		logger.info("���������"+jsonBody);
		String resp = "";
		try {
			resp = misUserService.userWhsAdd(jsonBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}
	/*
	 * ɾ���û��ֿ�Ȩ��
	 */
	@RequestMapping(value = "userWhsUpdate", method = RequestMethod.POST)
	@ResponseBody
	public String userLogicDel(@RequestBody String jsonBody) {
		logger.info("url:system/misUser/userWhsUpdate");
		logger.info("���������"+jsonBody);
		String resp = "";
		try {
			resp = misUserService.userWhsUpdate(jsonBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("���ز���" + resp);
		return resp;
	}
	
}
