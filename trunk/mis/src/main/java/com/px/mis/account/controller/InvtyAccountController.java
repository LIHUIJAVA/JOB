package com.px.mis.account.controller;

import java.io.IOException;
import java.util.Map;

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
import com.px.mis.account.service.InvtyAccountService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

/**
 * �ʱ����Ʋ�
 *
 */
@RequestMapping(value = "account/invty", method = RequestMethod.POST)
@Controller
@CrossOrigin
public class InvtyAccountController {

	private Logger logger = LoggerFactory.getLogger(InvtyAccountController.class);

	@Autowired
	private InvtyAccountService invtyAccountService; // �ʱ�

	/**
	 * ��ϸ��
	 */
	@RequestMapping(value = "/detail/list")
	private @ResponseBody String detailList(@RequestBody String jsonBody) {

		logger.info("url:mis/account/invty/detail/list");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			String invtyEncd = (String) map.get("invtyEncd");
			String whsEncd = (String) map.get("whsEncd");
			String batNum = (String) map.get("batNum");
			String bookOkSDt = (String) map.get("bookOkSDt");
			String bookOkEDt = (String) map.get("bookOkEDt");
			if (StringUtils.isNotEmpty(invtyEncd) && StringUtils.isNotEmpty(whsEncd) && StringUtils.isNotEmpty(batNum)
					&& StringUtils.isNotEmpty(bookOkSDt) && StringUtils.isNotEmpty(bookOkEDt)) {
				resp = invtyAccountService.selectDetailList(map, loginTime);
			} else {
				resp = BaseJson.returnRespList("/account/invty/detail/list", false, "�����ӿ��쳣", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:mis/account/invty/detail/list �쳣˵����", e);
		}

		return resp;
	}
	
	/**
	 * ��ϸ��-�շ�����ܱ���ת
	 */
	@RequestMapping(value = "/detailed/list")
	private @ResponseBody String detailedList(@RequestBody String jsonBody) {
		String url = "account/invty/detailed/list";
		logger.info(url);
		logger.info("���������" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			resp = invtyAccountService.selectDetailedList(map, loginTime, url);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList(url, false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:" + url + "�쳣˵����", e);

		}

		return resp;

	}
	
	/**
	 * ��ϸ��-����
	 */
	@RequestMapping(value = "/detailed/listExport")
	private @ResponseBody String detailedListExport(@RequestBody String jsonBody) {
		String url = "account/invty/detailed/listExport";
		logger.info(url);
		logger.info("���������" + jsonBody);
		
		String resp = "";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			resp = invtyAccountService.selectDetailedListExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList(url, false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:" + url + "�쳣˵����", e);

		}

		return resp;

	}

	/**
	 * ������Ʒ��ϸ��
	 */
	@RequestMapping(value = "/sendProduct/list")
	private @ResponseBody String sendProductList(@RequestBody String jsonBody) {

		logger.info("url:mis/account/invty/sendProduct/list");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {

			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��

			String invtyEncd = (String) map.get("invtyEncd");
			String whsEncd = (String) map.get("whsEncd");
			String batNum = (String) map.get("batNum");
			String bookOkSDt = (String) map.get("bookOkSDt");
			String bookOkEDt = (String) map.get("bookOkEDt");

			if (StringUtils.isNotEmpty(invtyEncd) && StringUtils.isNotEmpty(whsEncd) && StringUtils.isNotEmpty(batNum)
					&& StringUtils.isNotEmpty(bookOkSDt) && StringUtils.isNotEmpty(bookOkEDt)) {
				if (bookOkSDt.length() < 12) {
					bookOkSDt = bookOkSDt + " 00:00:00";
				}
				if (bookOkEDt.length() < 12) {
					bookOkEDt = bookOkEDt + " 23:59:59";
				}
				map.put("bookOkSDt", bookOkSDt);
				map.put("bookOkEDt", bookOkEDt);
				resp = invtyAccountService.selectsendProductList(map, loginTime);
			} else {
				
				resp = BaseJson.returnRespList("account/invty/sendProduct/list", false, "�����ӿ��쳣", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:mis/account/invty/sendProduct/list�쳣˵����", e);
		}

		return resp;
	}

	/**
	 * ������Ʒ��ϸ��-����
	 */
	@RequestMapping(value = "/sendProduct/listExport")
	private @ResponseBody String sendProductListExport(@RequestBody String jsonBody) {
		logger.info("url:mis/account/invty/sendProduct/listExport");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {

			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��

			String invtyEncd = (String) map.get("invtyEncd");
			String whsEncd = (String) map.get("whsEncd");
			String batNum = (String) map.get("batNum");
			String bookOkSDt = (String) map.get("bookOkSDt");
			String bookOkEDt = (String) map.get("bookOkEDt");

			if (StringUtils.isNotEmpty(invtyEncd) && StringUtils.isNotEmpty(whsEncd) && StringUtils.isNotEmpty(batNum)
					&& StringUtils.isNotEmpty(bookOkSDt) && StringUtils.isNotEmpty(bookOkEDt)) {
				if (bookOkSDt.length() < 12) {
					bookOkSDt = bookOkSDt + " 00:00:00";
				}
				if (bookOkEDt.length() < 12) {
					bookOkEDt = bookOkEDt + " 23:59:59";
				}
				map.put("bookOkSDt", bookOkSDt);
				map.put("bookOkEDt", bookOkEDt);
				resp = invtyAccountService.selectsendProductListExport(map, loginTime);
			} else {
				resp = BaseJson.returnRespList("account/invty/sendProduct/listExport", false, "�����ӿ��쳣", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:mis/account/invty/sendProduct/list�쳣˵����", e);
		}

		return resp;
	}

	/**
	 * ��ˮ��
	 */
	@RequestMapping(value = "/stream/list")
	private @ResponseBody String streamList(@RequestBody String jsonBody) {

		logger.info("url:mis/account/invty/stream/list");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			resp = invtyAccountService.selectStreamList(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/stream/list", false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:mis/account/invty/stream/list�쳣˵����", e);

		}

		return resp;
	}
	
	/**
	 * ��ˮ��-����
	 */
	@RequestMapping(value = "/stream/listExport")
	private @ResponseBody String streamListExport(@RequestBody String jsonBody) {

		logger.info("url:mis/account/invty/stream/listExport");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
//			int pageNo = (int) map.get("pageNo");����ҳ ���ǿ��ܻ���������
//			int pageSize = (int) map.get("pageSize");
//			map.put("index", (pageNo - 1) * pageSize);
//			map.put("num", pageSize);

			resp = invtyAccountService.selectStreamListExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/stream/listExport", false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:mis/account/invty/stream/listExport�쳣˵����", e);

		}

		return resp;
	}

	/**
	 * �շ�����ܱ�-��ѯ
	 */
	@RequestMapping(value = "/sendAndReceive/pool")
	private @ResponseBody String sendAndReceivePool(@RequestBody String jsonBody) {
		logger.info("url:account/invty/sendAndReceive/pool");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			resp = invtyAccountService.sendAndReceivePool(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendAndReceive/pool", false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendAndReceive/pool�쳣˵����", e);

		}

		return resp;

	}

	/**
	 * �շ������-���ܱ�
	 */
	@RequestMapping(value = "/sendAndReceiveInvtyCls/pool")
	private @ResponseBody String sendAndReceiveInvtyCls(@RequestBody String jsonBody) {
		logger.info("url:account/invty/sendAndReceiveInvtyCls/pool");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			
			resp = invtyAccountService.sendAndReceiveInvtyClsPool(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendAndReceiveInvtyCls/pool", false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendAndReceiveInvtyCls/pool�쳣˵����", e);

		}

		return resp;

	}

	/**
	 * �շ�����ܱ�-����-����
	 */
	@RequestMapping(value = "/sendAndReceiveInvtyCls/poolExport")
	private @ResponseBody String sendAndReceiveInvtyClsExport(@RequestBody String jsonBody) {
		logger.info("url:account/invty/sendAndReceiveInvtyCls/poolExport");
		logger.info("���������" + jsonBody);

		String resp = "";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			resp = invtyAccountService.sendAndReceiveInvtyClsPoolExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendAndReceiveInvtyCls/poolExport", false, "�����ӿ��쳣",
						null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendAndReceiveInvtyCls/poolExport �쳣˵����", e);
		}

		return resp;
	}

	

	/*
	 * ������ͳ�Ʊ�
	 */
	@RequestMapping(value = "/invoiving/pool")
	private @ResponseBody String invoicingPool(@RequestBody String jsonBody) {
		logger.info("url:account/invty/invoiving/pool");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String formSDt = (String) map.get("formSDt");
			String formEDt = (String) map.get("formEDt");
			if (formSDt.length() < 12) {
				formSDt = formSDt + " 00:00:00";
			}
			if (formEDt.length() < 12) {
				formEDt = formEDt + " 23:59:59";
			}
			map.put("formEDt", formEDt);
			map.put("formSDt", formSDt);
			resp = invtyAccountService.invoicingPool(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/invoiving/pool", false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/invoiving/pool�쳣˵����", e);

		}
		return resp;

	}

	/*
	 * ������ͳ�Ʊ�-����
	 */
	@RequestMapping(value = "/invoiving/poolExport")
	private @ResponseBody String invoicingPoolExport(@RequestBody String jsonBody) {
		logger.info("url:account/invty/invoiving/poolExport");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String formSDt = (String) map.get("formSDt");
			String formEDt = (String) map.get("formEDt");
			if (formSDt.length() < 12) {
				formSDt = formSDt + " 00:00:00";
			}
			if (formEDt.length() < 12) {
				formEDt = formEDt + " 23:59:59";
			}
			map.put("formEDt", formEDt);
			map.put("formSDt", formSDt);
			resp = invtyAccountService.invoicingPoolExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespListAnno("account/invty/invoiving/poolExport", false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/invoiving/poolExport�쳣˵����", e);

		}
		return resp;

	}

	/**
	 * ������Ʒ���ܱ�
	 */
	@RequestMapping(value = "/sendProducts/pool")
	private @ResponseBody String sendProductsPool(@RequestBody String jsonBody) {

		logger.info("url:account/invty/sendProducts/pool");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			resp = invtyAccountService.sendProductsPool(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendProducts/pool", false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendProducts/pool�쳣˵����", e);

		}

		return resp;

	}

	/**
	 * ������Ʒ���ܱ�-����
	 */
	@RequestMapping(value = "/sendProducts/poolExport")
	private @ResponseBody String sendProductsPoolExport(@RequestBody String jsonBody) {

		logger.info("url:account/invty/sendProducts/poolExport");
		logger.info("���������" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // ��¼ʱ��
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			resp = invtyAccountService.sendProductsPoolExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendProducts/poolExport", false, "�����ӿ��쳣", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendProducts/poolExport�쳣˵����", e);

		}

		return resp;

	}

}
