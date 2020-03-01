package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.PursComnInv;
import com.px.mis.account.entity.PursComnInvSub;
import com.px.mis.account.service.PursComnInvService;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//�ɹ���ͨ��Ʊ�ӿ�
@RequestMapping(value = "account/PursComnInv", method = RequestMethod.POST)
@Controller
public class PursComnInvController {
	private Logger logger = LoggerFactory.getLogger(PursComnInvController.class);
	@Autowired
	private PursComnInvService pursComnInvService;

	/* ��� */
	@RequestMapping("addPursComnInv")
	public @ResponseBody Object addPursComnInv(@RequestBody String jsonData) {
		logger.info("url:/account/PursComnInv/addPursComnInv");
		logger.info("���������" + jsonData);
		String resp = "";
		PursComnInv pursComnInv = null;
		List<PursComnInvSub> pursComnInvSub = new ArrayList<>();
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonData);
			String userId = aString.get("accNum").asText();
			String userName = aString.get("userName").asText();
			String loginTime = aString.get("loginTime").asText();
			pursComnInv = BaseJson.getPOJO(jsonData, PursComnInv.class);
			pursComnInv.setSetupPers(userName);
			pursComnInvSub = BaseJson.getPOJOList(jsonData, "list", PursComnInvSub.class);

			resp = pursComnInvService.addPursComnInv(pursComnInv, pursComnInvSub, userId, loginTime);
		} catch (Exception e) {

			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ɾ�� */
	@RequestMapping("deletePursComnInv")
	public @ResponseBody Object deletePursComnInv(@RequestBody String jsonData) {
		logger.info("url:/account/PursComnInv/deletePursComnInv");
		logger.info("���������" + jsonData);
		String resp = "";
		String pursInvNum = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			pursInvNum = reqBody.get("pursInvNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursComnInv/deletePursComnInv �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/PursComnInv/deletePursComnInv", false,
						"���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursComnInv/deletePursComnInv �쳣˵����", e);
			}
			return resp;
		}
		try {
			resp = pursComnInvService.deletePursComnInv(pursInvNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursComnInv/deletePursComnInv �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ���� */
	@RequestMapping("updatePursComnInv")
	public @ResponseBody Object updatePursComnInv(@RequestBody String jsonData) {
		logger.info("url:/account/PursComnInv/updatePursComnInv");
		logger.info("���������" + jsonData);
		String resp = "";
		PursComnInv pursComnInv = null;
		List<PursComnInvSub> pursComnInvSub = new ArrayList<>();
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonData);
			String userName = aString.get("userName").asText();
			pursComnInv = BaseJson.getPOJO(jsonData, PursComnInv.class);
			pursComnInv.setMdfr(userName);

			pursComnInvSub = BaseJson.getPOJOList(jsonData, "list", PursComnInvSub.class);

		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursComnInv/updatePursComnInv �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/PursComnInv/updatePursComnInv", false,
						"���������������������������Ƿ���д��ȷ��update error!", pursComnInv);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursComnInv/updatePursComnInv �쳣˵����", e);
			}
			return resp;
		}
		try {
			resp = pursComnInvService.updatePursComnInv(pursComnInv, pursComnInvSub);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursComnInv/updatePursComnInv �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ������ѯ���� */
	@RequestMapping(value = "queryPursComnInvList")
	public @ResponseBody Object queryPursComnInvList(@RequestBody String jsonBody) {
		logger.info("url:/account/PursComnInv/queryPursComnInvList");
		logger.info("���������" + jsonBody);
		String resp = "";
		Map map = null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
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
			if (pageNo == 0 || pageSize == 0) {
				resp = BaseJson.returnRespList("/account/PursComnInv/queryPursComnInvList", false,
						"�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursComnInv/queryPursComnInvList �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespList("/account/PursComnInv/queryPursComnInvList", false,
						"���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursComnInv/queryPursComnInvList �쳣˵����", e);
			}
			return resp;
		}
		try {
			resp = pursComnInvService.queryPursComnInvList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursComnInv/queryPursComnInvList �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ��ѯ���� */
	@RequestMapping(value = "queryPursComnInvByPursInvNum")
	public @ResponseBody Object queryPursComnInvByPursInvNum(@RequestBody String jsonData) {
		logger.info("url:/account/PursComnInv/queryPursComnInvByPursInvNum");
		logger.info("���������" + jsonData);

		String resp = "";
		String pursInvNum = null;
		try {
			pursInvNum = BaseJson.getReqBody(jsonData).get("pursInvNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursComnInv/queryPursComnInvByPursInvNum �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/account/PursComnInv/queryPursComnInvByPursInvNum", false,
						"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursComnInv/queryPursComnInvByPursInvNum �쳣˵����", e);
			}
			return resp;
		}
		try {
			resp = pursComnInvService.queryPursComnInvByPursInvNum(pursInvNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursComnInv/queryPursComnInvByPursInvNum �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ����ɾ��
	@RequestMapping(value = "deletePursComnInvList", method = RequestMethod.POST)
	@ResponseBody
	public Object deletePursComnInvList(@RequestBody String jsonBody) throws IOException {

		logger.info("url:account/PursComnInv/deletePursComnInvList");
		logger.info("���������" + jsonBody);
		String resp = "";
		resp = pursComnInvService.deletePursComnInvList(BaseJson.getReqBody(jsonBody).get("pursInvNum").asText());
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �����������״̬
	@RequestMapping(value = "updatePursComnInvIsNtChk", method = RequestMethod.POST)
	@ResponseBody
	public String updatePursComnInvIsNtChk(@RequestBody String jsonBody) throws IOException {

		logger.info("url:account/PursComnInv/updatePursComnInvIsNtChk");
		logger.info("���������" + jsonBody);
		Map<String, Object> result = new HashMap<>();
		String resp = "";
		boolean isSuccess = false;
		List<PursComnInv> pursComnInvList = new ArrayList<>();
		try {
//			JSON jsonObject = JSON.parseObject(jsonBody);
//			JSON jsonObjectList = jsonObject.getJSONObject("reqBody");
//			JSON jsonArray = jsonObjectList.getJSONArray("pursComnInvList");
//			pursComnInvList = jsonArray.toJavaList(PursComnInv.class);
			pursComnInvList = BaseJson.getPOJOList(jsonBody, "pursComnInvList", PursComnInv.class);

			for (PursComnInv pursComnInv : pursComnInvList) {
				try {
					ObjectNode aString = BaseJson.getReqHead(jsonBody);
					String userName = aString.get("userName").asText();
					pursComnInv.setChkr(userName);
					result = pursComnInvService.updatePursComnInvIsNtChkList(pursComnInv);
					isSuccess = (boolean) result.get("isSuccess");
					resp += result.get("message");
				} catch (Exception e) {
					isSuccess = false;
					resp += e.getMessage();
				}
			}
			resp = BaseJson.returnRespObj("account/PursComnInv/updatePursComnInvIsNtChk", isSuccess, resp, null);
			logger.info("���ز�����" + resp);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("���ز�����" + ex.getMessage());
			resp = BaseJson.returnRespObj("account/PursComnInv/updatePursComnInvIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}

	// ���Ųɹ���ⵥ����һ�Ųɹ���Ʊ
	@RequestMapping(value = "selectPursComnInvBingList", method = RequestMethod.POST)
	@ResponseBody
	private String selectPursComnInvBingList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:account/PursComnInv/selectPursComnInvBingList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			List<IntoWhs> intoWhsList = new ArrayList<>();
//			JSON jsonObject = JSON.parseObject(jsonBody);
//			JSON jsonObjectList = jsonObject.getJSONObject("reqBody");
//			JSON jsonArray = jsonObjectList.getJSONArray("lists");
//			intoWhsList = jsonArray.toJavaList(IntoWhs.class);
			intoWhsList = BaseJson.getPOJOList(jsonBody, "lists", IntoWhs.class);

			resp = pursComnInvService.selectPursComnInvBingList(intoWhsList);
		} catch (Exception e) {
			e.printStackTrace();
			return BaseJson.returnRespObj("/account/PursComnInv/selectPursComnInvBingList", false, e.getMessage(),
					null);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// ����ɹ���Ʊ
	@RequestMapping("uploadPursComnInvFile")
	@ResponseBody
	public Object uploadPursComnInvFile(HttpServletRequest request) throws IOException {
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
				return pursComnInvService.uploadFileAddDb(file, 0);
			} else {
				return BaseJson.returnRespObj("account/PursComnInv/uploadPursComnInvFile", false, "��ѡ���ļ���", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/PursComnInv/uploadPursComnInvFile", false, e.getMessage(), null);
		}
	}

	// U8����ɹ���Ʊ
	@RequestMapping("uploadPursComnInvFileU8")
	@ResponseBody
	public Object uploadPursComnInvFileU8(HttpServletRequest request) throws IOException {
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
				return pursComnInvService.uploadFileAddDb(file, 1);
			} else {
				return BaseJson.returnRespObj("account/PursComnInv/uploadPursComnInvFileU8", false, "��ѡ���ļ���", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/PursComnInv/uploadPursComnInvFileU8", false, e.getMessage(), null);
		}
	}

	// ԭ���ĵ����ӿ�,����ɾ
	@RequestMapping("printingPursComnInvList")
	@ResponseBody
	public Object printingPursComnInvList(@RequestBody String jsonBody) {

		logger.info("url:account/PursComnInv/printingPursComnInvList");
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
			resp = pursComnInvService.upLoadPursComnInvList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	// �µĵ����ӿ�
	@RequestMapping("printPursComnInvList")
	@ResponseBody
	public Object printPursComnInvList(@RequestBody String jsonBody) {

		logger.info("url:account/PursComnInv/printPursComnInvList");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String startBllgDt = (String) map.get("startBllgDt");
			String endBllgDt = (String) map.get("endBllgDt");
			if (startBllgDt != null && startBllgDt != "") {
				startBllgDt = startBllgDt + " 00:00:00";
			}
			if (endBllgDt != null && endBllgDt != "") {
				endBllgDt = endBllgDt + " 23:59:59";
			}
			map.put("startBllgDt", startBllgDt);
			map.put("endBllgDt", endBllgDt);
			resp = pursComnInvService.printPursComnInvList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

	/* ���͵�U8 */
	@RequestMapping("pushToU8")
	public @ResponseBody Object PushToU8(@RequestBody String jsonData) {
		logger.info("url:/account/PursComnInv/pushToU8");
		logger.info("���������" + jsonData);
		String resp = "";
		String message="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String ids = reqBody.get("ids").asText();
			if (ids!=null && ids.trim().length()!=0) {
				resp = pursComnInvService.pushToU8(ids);
			}else {
				message +="��Ʊ���Ų���Ϊ��,�����¹�ѡ";
				throw new RuntimeException(message);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnResp("url:/account/PursComnInv/pushToU8", false, "���ݽ����쳣", null);
			} catch (IOException e1) {
				e1.printStackTrace();//����json���������ץһ��,������ȫ����ӡ
			}
		}
		
		return resp;
	}

}
