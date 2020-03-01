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

//凭证类别接口
@RequestMapping(value = "account/vouchCateDoc", method = RequestMethod.POST)
@Controller
public class VouchCateDocController {
	private Logger logger = LoggerFactory.getLogger(VouchCateDocController.class);
	@Autowired
	private VouchCateDocService vcds;

	/* 添加凭证类别 */
	@RequestMapping("insertVouchCateDoc")
	public @ResponseBody Object insertVouchCateDoc(@RequestBody String jsonData) {
		logger.info("url:/account/vouchCateDoc/insertVouchCateDoc");
		logger.info("请求参数：" + jsonData);
		ObjectNode on = null;
		String resp = "";
		VouchCateDoc vouchCateDoc = null;
		try {
			vouchCateDoc = BaseJson.getPOJO(jsonData, VouchCateDoc.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/insertVouchCateDoc 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/insertVouchCateDoc", false,
						"请求参数解析错误，请检查请求参数是否书写正确，insert error!", vouchCateDoc);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/insertVouchCateDoc 异常说明：", e);
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
			logger.error("url:/account/vouchCateDoc/insertVouchCateDoc 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 删除凭证类别 */
	@RequestMapping("deleteVouchCateDoc")
	public @ResponseBody Object deleteVouchCateDoc(@RequestBody String jsonData) {
		logger.info("url:/account/vouchCateDoc/deleteVouchCateDoc");
		logger.info("请求参数：" + jsonData);
		ObjectNode on = null;
		String resp = "";
		String vouchCateWor = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			vouchCateWor = reqBody.get("vouchCateWor").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/deleteVouchCateDoc 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/deleteVouchCateDoc", false,
						"请求参数解析错误，请检查请求参数是否书写正确，delete error!", vouchCateWor);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/deleteVouchCateDoc 异常说明：", e);
			}
			return null;
		}
		try {

			resp = vcds.deleteVouchCateDocByVouchCateWor(vouchCateWor);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/deleteVouchCateDoc 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 更新凭证类别 */
	@RequestMapping("updateVouchCateDoc")
	public @ResponseBody Object updateVouchCateDoc(@RequestBody String jsonData) {
		logger.info("url:/account/vouchCateDoc/updateVouchCateDoc");
		logger.info("请求参数：" + jsonData);
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
			logger.error("url:/account/vouchCateDoc/updateVouchCateDoc 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/updateVouchCateDoc", false,
						"请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/updateVouchCateDoc 异常说明：", e);
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
			logger.error("url:/account/vouchCateDoc/updateVouchCateDoc 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 查询所有凭证类别 */
	@RequestMapping(value = "selectVouchCateDoc")
	public @ResponseBody Object selectVouchCateDoc(@RequestBody String jsonBody) {
		logger.info("url:/account/vouchCateDoc/selectVouchCateDoc");
		logger.info("请求参数：" + jsonBody);

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
						"请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDoc 异常说明：", e1);
			try {
				resp = BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDoc", false,
						"请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/selectVouchCateDoc 异常说明：", e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = vcds.selectVouchCateDocList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDoc 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 查询单个凭证类别 */
	@RequestMapping(value = "selectVouchCateDocById")
	public @ResponseBody Object selectVouchCateDocById(@RequestBody String jsonData) {
		logger.info("url:/account/vouchCateDoc/selectVouchCateDocById");
		logger.info("请求参数：" + jsonData);

		String resp = "";
		String vouchCateId = null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			vouchCateId = reqBody.get("vouchCateWor").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDocById 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/selectVouchCateDocById", false,
						"请求参数解析错误，请检查请求参数是否书写正确，没有这条数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/selectVouchCateDocById 异常说明：", e);
			}
			return resp;
		}
		try {
			VouchCateDoc vouchCateDoc = vcds.selectVouchCateDocById(vouchCateId);
			if (vouchCateDoc != null) {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/selectVouchCateDocById", true, "处理成功！",
						vouchCateDoc);
			} else {
				resp = BaseJson.returnRespObj("/account/vouchCateDoc/selectVouchCateDocById", false, "没有这条数据！",
						vouchCateDoc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDocById 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/* 查询所有凭证类别打印 */
	@RequestMapping(value = "selectVouchCateDocPrint")
	public @ResponseBody Object selectVouchCateDocPrint(@RequestBody String jsonBody) {
		logger.info("url:/account/vouchCateDoc/selectVouchCateDocPrint");
		logger.info("请求参数：" + jsonBody);

		String resp = "";
		Map map = null;

		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDocPrint 异常说明：", e1);
			try {
				resp = BaseJson.returnRespList("/account/vouchCateDoc/selectVouchCateDocPrint", false,
						"请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchCateDoc/selectVouchCateDocPrint 异常说明：", e);
			}
			return resp;
		}
		try {

			resp = vcds.selectVouchCateDocPrint(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchCateDoc/selectVouchCateDocPrint 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 导入单据
	@RequestMapping("uploadFileAddDb")
	@ResponseBody
	public String uploadPursOrderFile(HttpServletRequest request) throws IOException {
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
					return BaseJson.returnRespObj("account/vouchCateDoc/uploadFileAddDb", false, "请选择文件。", null);
				}
				return vcds.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("account/vouchCateDoc/uploadFileAddDb", false, "请选择文件。", null);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/vouchCateDoc/uploadFileAddDb", false, e.getMessage(), null);

		}

	}
}
