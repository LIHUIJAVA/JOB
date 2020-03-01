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
 * 帐薄控制层
 *
 */
@RequestMapping(value = "account/invty", method = RequestMethod.POST)
@Controller
@CrossOrigin
public class InvtyAccountController {

	private Logger logger = LoggerFactory.getLogger(InvtyAccountController.class);

	@Autowired
	private InvtyAccountService invtyAccountService; // 帐薄

	/**
	 * 明细表
	 */
	@RequestMapping(value = "/detail/list")
	private @ResponseBody String detailList(@RequestBody String jsonBody) {

		logger.info("url:mis/account/invty/detail/list");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
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
				resp = BaseJson.returnRespList("/account/invty/detail/list", false, "解析接口异常", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:mis/account/invty/detail/list 异常说明：", e);
		}

		return resp;
	}
	
	/**
	 * 明细帐-收发存汇总表跳转
	 */
	@RequestMapping(value = "/detailed/list")
	private @ResponseBody String detailedList(@RequestBody String jsonBody) {
		String url = "account/invty/detailed/list";
		logger.info(url);
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			resp = invtyAccountService.selectDetailedList(map, loginTime, url);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList(url, false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:" + url + "异常说明：", e);

		}

		return resp;

	}
	
	/**
	 * 明细帐-导出
	 */
	@RequestMapping(value = "/detailed/listExport")
	private @ResponseBody String detailedListExport(@RequestBody String jsonBody) {
		String url = "account/invty/detailed/listExport";
		logger.info(url);
		logger.info("请求参数：" + jsonBody);
		
		String resp = "";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			resp = invtyAccountService.selectDetailedListExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList(url, false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:" + url + "异常说明：", e);

		}

		return resp;

	}

	/**
	 * 发出商品明细表
	 */
	@RequestMapping(value = "/sendProduct/list")
	private @ResponseBody String sendProductList(@RequestBody String jsonBody) {

		logger.info("url:mis/account/invty/sendProduct/list");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {

			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间

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
				
				resp = BaseJson.returnRespList("account/invty/sendProduct/list", false, "解析接口异常", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:mis/account/invty/sendProduct/list异常说明：", e);
		}

		return resp;
	}

	/**
	 * 发出商品明细表-导出
	 */
	@RequestMapping(value = "/sendProduct/listExport")
	private @ResponseBody String sendProductListExport(@RequestBody String jsonBody) {
		logger.info("url:mis/account/invty/sendProduct/listExport");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {

			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间

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
				resp = BaseJson.returnRespList("account/invty/sendProduct/listExport", false, "解析接口异常", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:mis/account/invty/sendProduct/list异常说明：", e);
		}

		return resp;
	}

	/**
	 * 流水帐
	 */
	@RequestMapping(value = "/stream/list")
	private @ResponseBody String streamList(@RequestBody String jsonBody) {

		logger.info("url:mis/account/invty/stream/list");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			resp = invtyAccountService.selectStreamList(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/stream/list", false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:mis/account/invty/stream/list异常说明：", e);

		}

		return resp;
	}
	
	/**
	 * 流水帐-导出
	 */
	@RequestMapping(value = "/stream/listExport")
	private @ResponseBody String streamListExport(@RequestBody String jsonBody) {

		logger.info("url:mis/account/invty/stream/listExport");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
//			int pageNo = (int) map.get("pageNo");不分页 但是可能会限制条数
//			int pageSize = (int) map.get("pageSize");
//			map.put("index", (pageNo - 1) * pageSize);
//			map.put("num", pageSize);

			resp = invtyAccountService.selectStreamListExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/stream/listExport", false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:mis/account/invty/stream/listExport异常说明：", e);

		}

		return resp;
	}

	/**
	 * 收发存汇总表-查询
	 */
	@RequestMapping(value = "/sendAndReceive/pool")
	private @ResponseBody String sendAndReceivePool(@RequestBody String jsonBody) {
		logger.info("url:account/invty/sendAndReceive/pool");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			resp = invtyAccountService.sendAndReceivePool(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendAndReceive/pool", false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendAndReceive/pool异常说明：", e);

		}

		return resp;

	}

	/**
	 * 收发存分类-汇总表
	 */
	@RequestMapping(value = "/sendAndReceiveInvtyCls/pool")
	private @ResponseBody String sendAndReceiveInvtyCls(@RequestBody String jsonBody) {
		logger.info("url:account/invty/sendAndReceiveInvtyCls/pool");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			
			resp = invtyAccountService.sendAndReceiveInvtyClsPool(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendAndReceiveInvtyCls/pool", false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendAndReceiveInvtyCls/pool异常说明：", e);

		}

		return resp;

	}

	/**
	 * 收发存汇总表-分类-导出
	 */
	@RequestMapping(value = "/sendAndReceiveInvtyCls/poolExport")
	private @ResponseBody String sendAndReceiveInvtyClsExport(@RequestBody String jsonBody) {
		logger.info("url:account/invty/sendAndReceiveInvtyCls/poolExport");
		logger.info("请求参数：" + jsonBody);

		String resp = "";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			resp = invtyAccountService.sendAndReceiveInvtyClsPoolExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendAndReceiveInvtyCls/poolExport", false, "解析接口异常",
						null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendAndReceiveInvtyCls/poolExport 异常说明：", e);
		}

		return resp;
	}

	

	/*
	 * 进销存统计表
	 */
	@RequestMapping(value = "/invoiving/pool")
	private @ResponseBody String invoicingPool(@RequestBody String jsonBody) {
		logger.info("url:account/invty/invoiving/pool");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
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
				resp = BaseJson.returnRespList("account/invty/invoiving/pool", false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/invoiving/pool异常说明：", e);

		}
		return resp;

	}

	/*
	 * 进销存统计表-导出
	 */
	@RequestMapping(value = "/invoiving/poolExport")
	private @ResponseBody String invoicingPoolExport(@RequestBody String jsonBody) {
		logger.info("url:account/invty/invoiving/poolExport");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
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
				resp = BaseJson.returnRespListAnno("account/invty/invoiving/poolExport", false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/invoiving/poolExport异常说明：", e);

		}
		return resp;

	}

	/**
	 * 发出商品汇总表
	 */
	@RequestMapping(value = "/sendProducts/pool")
	private @ResponseBody String sendProductsPool(@RequestBody String jsonBody) {

		logger.info("url:account/invty/sendProducts/pool");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			resp = invtyAccountService.sendProductsPool(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendProducts/pool", false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendProducts/pool异常说明：", e);

		}

		return resp;

	}

	/**
	 * 发出商品汇总表-导出
	 */
	@RequestMapping(value = "/sendProducts/poolExport")
	private @ResponseBody String sendProductsPoolExport(@RequestBody String jsonBody) {

		logger.info("url:account/invty/sendProducts/poolExport");
		logger.info("请求参数：" + jsonBody);

		String resp = "";

		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); // 登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			resp = invtyAccountService.sendProductsPoolExport(map, loginTime);

		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespList("account/invty/sendProducts/poolExport", false, "解析接口异常", null);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/invty/sendProducts/poolExport异常说明：", e);

		}

		return resp;

	}

}
