package com.px.mis.account.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import com.px.mis.account.entity.AcctItmDoc;
import com.px.mis.account.service.AcctItmDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//会计科目档案接口
@RequestMapping(value = "account/acctItmDoc", method = RequestMethod.POST)
@Controller
public class AcctItmDocController {
	private Logger logger = LoggerFactory.getLogger(AcctItmDocController.class);
	@Autowired
	private AcctItmDocService aids;

	/* 添加 */
	@RequestMapping("insertAcctItmDoc")
	public @ResponseBody Object insertAcctItmDoc(@RequestBody String jsonData) {
		logger.info("url:/account/acctItmDoc/insertAcctItmDoc");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		AcctItmDoc acctItmDoc = null;
		try {
			acctItmDoc = BaseJson.getPOJO(jsonData, AcctItmDoc.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp = aids.insertAcctItmDoc(acctItmDoc);

		return resp;
	}

	// 删除
	@RequestMapping("deleteAcctItmDoc")
	public @ResponseBody Object deleteAcctItmDoc(@RequestBody String jsonData) {
		logger.info("url:/account/acctItmDoc/deleteAcctItmDoc");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		try {
			resp = aids.deleteAcctItmDocById(BaseJson.getReqBody(jsonData).get("subjId").asText());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/acctItmDoc/deleteAcctItmDoc 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 更新
	@RequestMapping("updateAcctItmDoc")
	public @ResponseBody Object updateAcctItmDoc(@RequestBody String jsonData) {
		logger.info("url:/account/acctItmDoc/updateAcctItmDoc");
		logger.info("请求参数：" + jsonData);

		String resp = "";
		List<Map> mList;
		try {
			mList = BaseJson.getList(jsonData);
			List<AcctItmDoc> cList = new ArrayList<>();
			for (Map map : mList) {
				AcctItmDoc acctItmDoc = new AcctItmDoc();
				BeanUtils.populate(acctItmDoc, map);
				cList.add(acctItmDoc);
			}
			resp = aids.updateAcctItmDocById(cList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;

	}

	// 查询单个
	@RequestMapping(value = "selectAcctItmDocById")
	public @ResponseBody Object selectAcctItmDocById(@RequestBody String jsonData) {
		logger.info("url:/account/acctItmDoc/selectAcctItmDocById");
		logger.info("请求参数：" + jsonData);
		String resp = "";

		try {
			resp = aids.selectAcctItmDocById(BaseJson.getReqBody(jsonData).get("subjId").asText());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/acctItmDoc/selectAcctItmDocById 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 查询所有
	@RequestMapping(value = "queryAcctItmDocList")
	public @ResponseBody Object queryAcctItmDocList(@RequestBody String jsonBody) {
		logger.info("url:account/acctItmDoc/queryAcctItmDocList");
		logger.info("请求参数：" + jsonBody);

		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = aids.queryAcctItmDocList(map);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;

	}

	// 查询所有打印
	@RequestMapping(value = "queryAcctItmDocPrint")
	public @ResponseBody Object queryAcctItmDocPrint(@RequestBody String jsonBody) {
		logger.info("url:account/acctItmDoc/queryAcctItmDocPrint");
		logger.info("请求参数：" + jsonBody);

		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp = aids.queryAcctItmDocPrint(map);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					return BaseJson.returnRespObj("account/acctItmDoc/uploadFileAddDb", false, "请选择文件。", null);
				}
				return aids.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("account/acctItmDoc/uploadFileAddDb", false, "请选择文件。", null);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/acctItmDoc/uploadFileAddDb", false, e.getMessage(), null);

		}

	}

}
