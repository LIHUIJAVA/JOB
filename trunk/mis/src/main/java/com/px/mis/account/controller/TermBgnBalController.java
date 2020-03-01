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

/*核算的期初余额功能描述*/
@RequestMapping(value = "account/TermBgnBal")
@Controller
public class TermBgnBalController {

	private Logger logger = LoggerFactory.getLogger(TermBgnBalController.class);

	@Autowired
	private TermBgnBalService tbbs;

	// 新增期初余额信息
	@RequestMapping(value = "addTermBgnBal", method = RequestMethod.POST)
	@ResponseBody
	private String addTermBgnBal(@RequestBody String jsonBody) {
		logger.info("url:account/TermBgnBal/addTermBgnBal");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			TermBgnBal termBgnBal = BaseJson.getPOJO(jsonBody, TermBgnBal.class);
			resp = tbbs.insertTermBgnBal(termBgnBal);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析数据错误，新增失败");
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 修改期初余额信息
	@RequestMapping(value = "editTermBgnBal", method = RequestMethod.POST)
	@ResponseBody
	private String editTermBgnBal(@RequestBody String jsonData) {
		logger.info("url:account/TermBgnBal/editTermBgnBal");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		List<TermBgnBal> termBgnBalList = new ArrayList<TermBgnBal>();
		try {

			termBgnBalList = BaseJson.getPOJOList(jsonData, "termBgnBalList", TermBgnBal.class);
			resp = tbbs.updateTermBgnBalByOrdrNum(termBgnBalList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 批量删除期初余额信息
	@RequestMapping(value = "deleteTermBgnBalList", method = RequestMethod.POST)
	@ResponseBody
	private String deleteTermBgnBalList(@RequestBody String jsonBody) {
		logger.info("url:account/TermBgnBal/deleteTermBgnBalList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = tbbs.deleteTermBgnBalByOrdrNum(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	/*
	 * //查询销售单信息
	 * 
	 * @RequestMapping(value="querySellSngl",method = RequestMethod.POST)
	 * 
	 * @ResponseBody private String queryPursOrdr(@RequestBody String jsonBody) {
	 * logger.info("url:purc/SellSngl/querySellSngl");
	 * logger.info("请求参数："+jsonBody); String resp=""; try {
	 * resp=sss.querySellSngl(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText
	 * ()); } catch (IOException e) { e.printStackTrace(); }
	 * logger.info("返回参数："+resp); return resp; }
	 */
//期初余额
	@RequestMapping(value = "queryTermBgnBalList", method = RequestMethod.POST)
	@ResponseBody
	private String queryTermBgnBalList(@RequestBody String jsonBody) {
		logger.info("url:account/TermBgnBal/queryTermBgnBalList");
		logger.info("请求参数：" + jsonBody);
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
		logger.info("返回参数：" + resp);
		return resp;
	}

//	期初余额-导出
	@RequestMapping(value = "queryTermBgnBalListPrint", method = RequestMethod.POST)
	@ResponseBody
	private String queryTermBgnBalListPrint(@RequestBody String jsonBody) {
		logger.info("url:account/TermBgnBal/queryTermBgnBalListPrint");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp = tbbs.queryTermBgnBalListPrint(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 导入期初余额信息
	@RequestMapping("uploadTermBgnBalFile")
	@ResponseBody
	public Object uploadTermBgnBalFile(HttpServletRequest request) throws IOException {
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
					return ("请选择文件!");
				}
				return tbbs.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("account/TermBgnBal/uploadTermBgnBalFile", false, "请选择文件！", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/TermBgnBal/uploadTermBgnBalFile", false, e.getMessage(), null);
		}
	}
}
