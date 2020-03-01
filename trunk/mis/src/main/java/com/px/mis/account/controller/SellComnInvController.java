package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.service.SellComnInvService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//销售发票接口
@RequestMapping(value = "account/SellComnInv", method = RequestMethod.POST)
@Controller
public class SellComnInvController {
	private Logger logger = LoggerFactory.getLogger(SellComnInvController.class);
	@Autowired
	private SellComnInvService sellComnInvService;

	/* 新增 */
	@RequestMapping("addSellComnInv")
	public @ResponseBody Object addSellComnInv(@RequestBody String jsonData) {
		logger.info("url:/account/SellComnInv/addSellComnInv");
		logger.info("请求参数：" + jsonData);
		ObjectNode on = null;
		String resp = "";
		SellComnInv sellComnInv = null;
		String userId = "";
		String loginTime = "";
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonData);
			userId = aString.get("accNum").asText();
			String userName = aString.get("userName").asText();
			loginTime = aString.get("loginTime").asText();
			sellComnInv = BaseJson.getPOJO(jsonData, SellComnInv.class);
			sellComnInv.setSetupPers(userName);// 创建人
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/SellComnInv/addSellComnInv 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/account/SellComnInv/addSellComnInv", false,
						"请求参数解析错误，请检查请求参数是否书写正确，insert error!", sellComnInv);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/SellComnInv/addSellComnInv 异常说明：", e);
			}
			return resp;
		}
		try {
			on = sellComnInvService.addSellComnInv(sellComnInv, userId, loginTime);
			if (on != null) {
				resp = BaseJson.returnRespObj("/account/SellComnInv/addSellComnInv", on.get("isSuccess").asBoolean(),
						on.get("message").asText(), sellComnInv);

			} else {
				resp = BaseJson.returnRespObj("/account/SellComnInv/addSellComnInv", false, "insert error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/SellComnInv/addSellComnInv 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 删除 */
	@RequestMapping("deleteSellComnInv")
	public @ResponseBody Object deleteSellComnInv(@RequestBody String jsonData) {
		logger.info("url:/account/SellComnInv/deleteSellComnInv");
		logger.info("请求参数：" + jsonData);
		ObjectNode on = null;
		String resp = "";
		String sellInvNum = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			sellInvNum = reqBody.get("sellInvNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/SellComnInv/deleteSellComnInv 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/account/SellComnInv/deleteSellComnInv", false,
						"请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/SellComnInv/deleteSellComnInv 异常说明：", e);
			}
			return resp;
		}
		try {
			on = sellComnInvService.deleteSellComnInvBySellInvNum(sellInvNum);
			if (on != null) {
				resp = BaseJson.returnRespObj("/account/SellComnInv/deleteSellComnInv", on.get("isSuccess").asBoolean(),
						on.get("message").asText(), null);
			} else {
				resp = BaseJson.returnRespObj("/account/SellComnInv/deleteSellComnInv", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/SellComnInv/deleteSellComnInv 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 更新 */
	@RequestMapping("updateSellComnInv")
	public @ResponseBody Object updateSellComnInv(@RequestBody String jsonData) {
		logger.info("url:/account/SellComnInv/updateSellComnInv");
		logger.info("请求参数：" + jsonData);
		ObjectNode on = null;
		String resp = "";
		SellComnInv sellComnInv = null;
		List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonData);
			String userName = aString.get("userName").asText();
			sellComnInv = BaseJson.getPOJO(jsonData, SellComnInv.class);
			sellComnInv.setMdfr(userName);// 修改人
//			JSONObject jsonObject = JSON.parseObject(jsonData);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//			JSONArray jsonArray = jsonObjectList.getJSONArray("sellComnInvSubList");
//			sellComnInvSubList= jsonArray.toJavaList(SellComnInvSub.class);
			sellComnInvSubList = BaseJson.getPOJOList(jsonData, "sellComnInvSubList", SellComnInvSub.class);

		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/SellComnInv/updateSellComnInv 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/account/SellComnInv/updateSellComnInv", false,
						"请求参数解析错误，请检查请求参数是否书写正确，update error!", sellComnInv);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/SellComnInv/updateSellComnInv 异常说明：", e);
			}
			return resp;
		}
		try {
			on = sellComnInvService.editSellComnInv(sellComnInv, sellComnInvSubList);
			if (on != null) {
				resp = BaseJson.returnRespObj("/account/SellComnInv/updateSellComnInv", on.get("isSuccess").asBoolean(),
						on.get("message").asText(), sellComnInv);
			} else {
				resp = BaseJson.returnRespObj("/account/SellComnInv/updateSellComnInv", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/SellComnInv/updateSellComnInv 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 查询所有 */
	@RequestMapping(value = "selectSellComnInvList")
	public @ResponseBody Object selectSellComnInvList(@RequestBody String jsonBody) {
		logger.info("url:/account/SellComnInv/selectSellComnInvList");
		logger.info("请求参数：" + jsonBody);

		String resp = "";
		Map map = null;
		Integer pageNo = null;
		Integer pageSize = null;
		Object startBllgDt = null;
		Object endBllgDt = null;
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
				resp = BaseJson.returnRespList("/account/SellComnInv/selectSellComnInvList", false,
						"请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/SellComnInv/selectSellComnInvList 异常说明：", e1);
			try {
				resp = BaseJson.returnRespList("/account/SellComnInv/selectSellComnInvList", false,
						"请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/SellComnInv/selectSellComnInvList 异常说明：", e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			map.put("startBllgDt", startBllgDt);
			map.put("endBllgDt", endBllgDt);
			resp = sellComnInvService.selectSellComnInvList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/SellComnInv/selectSellComnInvList 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 查询单个 */
	@RequestMapping(value = "selectSellComnInvById")
	public @ResponseBody Object selectSellComnInvById(@RequestBody String jsonData) {
		logger.info("url:/account/SellComnInv/selectSellComnInvById");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		String sellInvNum = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			sellInvNum = reqBody.get("sellInvNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/SellComnInv/selectSellComnInvById 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/account/SellComnInv/selectSellComnInvById", false,
						"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/SellComnInv/selectSellComnInvById 异常说明：", e);
			}
			return resp;
		}
		try {
			resp = sellComnInvService.selectSellComnInvBySellInvNum(sellInvNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/SellComnInv/selectSellComnInvById 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 批量删除
	@RequestMapping(value = "deleteSellComnInvList", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteSellInvMasTabList(@RequestBody String jsonBody) throws IOException {

		logger.info("url:account/SellComnInv/deleteSellComnInvList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = sellComnInvService.deleteSellComnInvList(BaseJson.getReqBody(jsonBody).get("sellInvNum").asText());
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 批量更新审核状态
	@RequestMapping(value = "updateSellComnInvIsNtChk", method = RequestMethod.POST)
	@ResponseBody
	public String updateSellComnInvIsNtChk(@RequestBody String jsonBody) throws IOException {

		logger.info("url:account/SellComnInv/updateSellComnInvIsNtChk");
		logger.info("请求参数：" + jsonBody);
		Map<String, Object> result = new HashMap<>();
		String resp = "";
		boolean isSuccess = false;
		List<SellComnInv> sellComnInvList = new ArrayList<>();
		try {
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//			JSONArray jsonArray = jsonObjectList.getJSONArray("sellComnInvList");
//			sellComnInvList = jsonArray.toJavaList(SellComnInv.class);
			sellComnInvList = BaseJson.getPOJOList(jsonBody, "sellComnInvList", SellComnInv.class);
			for (SellComnInv sellComnInv : sellComnInvList) {
				try {
					ObjectNode aString = BaseJson.getReqHead(jsonBody);
					String userName = aString.get("userName").asText();
					sellComnInv.setChkr(userName);
					result = sellComnInvService.updateSellComnInvIsNtChkList(sellComnInv);
					isSuccess = (boolean) result.get("isSuccess");
					resp += result.get("message");
				} catch (Exception e) {
					isSuccess = false;
					resp += e.getMessage();
				}
			}
			resp = BaseJson.returnRespObj("account/PursComnInv/updatePursComnInvIsNtChk", isSuccess, resp, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("返回参数：" + ex.getMessage());
			resp = BaseJson.returnRespObj("account/SellComnInv/updateSellComnInvIsNtChk", false, ex.getMessage(), null);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 多张销售单生成一张销售发票
	@RequestMapping(value = "selectSellComnInvBingList", method = RequestMethod.POST)
	@ResponseBody
	private String selectSellComnInvBingList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:account/SellComnInv/selectSellComnInvBingList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			List<SellComnInv> sellComnInvList = new ArrayList<>();
			/*
			 * JSONObject jsonObject = JSON.parseObject(jsonBody); JSONObject jsonObjectList
			 * = jsonObject.getJSONObject("reqBody"); JSONArray jsonArray =
			 * jsonObjectList.getJSONArray("lists"); sellComnInvList =
			 * jsonArray.toJavaList(SellComnInv.class);
			 */
			sellComnInvList = BaseJson.getPOJOList(jsonBody, "lists", SellComnInv.class);
			resp = sellComnInvService.selectSellComnInvBingList(sellComnInvList);
		} catch (Exception e) {
			e.printStackTrace();
			resp = BaseJson.returnRespObj("/account/SellComnInv/selectSellComnInvBingList", false, e.getMessage(),
					null);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 导入销售发票
	@RequestMapping("uploadSellComnInvFile")
	@ResponseBody
	public Object uploadSellComnInvFile(HttpServletRequest request) throws IOException {
		try {
			// 创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// request强制转换注意
				MultipartFile file = mRequest.getFile("file");
				if (file == null) {
					return ("请选择文件。");
				}
				return sellComnInvService.uploadFileAddDb(file, 0);
			} else {
				return BaseJson.returnRespObj("/account/SellComnInv/uploadSellComnInvFile", false, "请选择文件！", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("/account/SellComnInv/uploadSellComnInvFile", false, e.getMessage(), null);
		}
	}

	// U8导入销售发票
	@RequestMapping("uploadSellComnInvFileU8")
	@ResponseBody
	public Object uploadSellComnInvFileU8(HttpServletRequest request) throws IOException {
		try {
			// 创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// request强制转换注意
				MultipartFile file = mRequest.getFile("file");
				if (file == null) {
					return ("请选择文件。");
				}
				return sellComnInvService.uploadFileAddDb(file, 1);
			} else {
				return BaseJson.returnRespObj("/account/SellComnInv/uploadSellComnInvFileU8", false, "请选择文件！", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("/account/SellComnInv/uploadSellComnInvFileU8", false, e.getMessage(), null);
		}
	}

	// 原来的导出接口
	@RequestMapping("printingSellComnInvList")
	@ResponseBody
	public Object printingSellComnInvList(@RequestBody String jsonBody) {

		logger.info("url:account/SellComnInv/printingSellComnInvList");
		logger.info("请求参数：" + jsonBody);
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
			resp = sellComnInvService.upLoadSellComnInvList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 销售发票参照查询主表功能
	@RequestMapping("selectSellReturnEntrs")
	@ResponseBody
	public Object selectSellReturnEntrs(@RequestBody String jsonBody) {

		logger.info("url:account/SellComnInv/selectSellReturnEntrs");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = sellComnInvService.selectSellReturnEntrs(map);
		} catch (Exception e) {
			try {
				resp = BaseJson.returnResp("account/SellComnInv/selectSellReturnEntrs", false, e.getMessage(), null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 销售发票参照查询子表功能
	@RequestMapping("selectSellComnInvBySellRtnEntList")
	@ResponseBody
	public Object selectSellComnInvBySellRtnEntList(@RequestBody String jsonBody) {

		logger.info("url:account/SellComnInv/selectSellComnInvBySellRtnEntList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			List<SellComnInv> sellComnInvList = new ArrayList<>();
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//			JSONArray jsonArray = jsonObjectList.getJSONArray("lists");
//			sellComnInvList = jsonArray.toJavaList(SellComnInv.class);
			sellComnInvList = BaseJson.getPOJOList(jsonBody, "lists", SellComnInv.class);
			resp = sellComnInvService.selectSellComnInvBySellRtnEntList(sellComnInvList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 新的导出接口
	@RequestMapping("printSellComnInvList")
	@ResponseBody
	public Object printSellComnInvList(@RequestBody String jsonBody) {

		logger.info("url:account/SellComnInv/printSellComnInvList");
		logger.info("请求参数：" + jsonBody);
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
			resp = sellComnInvService.printSellComnInvList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 推送到U8 */
	@RequestMapping("pushToU8")
	public @ResponseBody Object PushToU8(@RequestBody String jsonData) {
		logger.info("url:/account/SellComnInv/pushToU8");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String ids = reqBody.get("ids").asText();

			resp = sellComnInvService.pushToU8(ids);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnResp("/account/SellComnInv/pushToU8", false, "数据解析异常", null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		return resp;
	}

}
