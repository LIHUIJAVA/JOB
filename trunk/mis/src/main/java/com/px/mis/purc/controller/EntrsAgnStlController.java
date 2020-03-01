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

/*委托代销结算单功能描述*/
@RequestMapping(value = "purc/EntrsAgnStl")
@Controller
public class EntrsAgnStlController {

	private Logger logger = LoggerFactory.getLogger(EntrsAgnStlController.class);

	@Autowired
	private EntrsAgnStlService eass;
	// 记账
	@Autowired
	FormBookService formBookService;

	// 新增委托代销结算单信息
	@RequestMapping(value = "addEntrsAgnStl", method = RequestMethod.POST)
	@ResponseBody
	private String addEntrsAgnStl(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/addEntrsAgnStl");
		logger.info("请求参数：" + jsonBody);
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
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 修改委托代销结算单信息
	@RequestMapping(value = "editEntrsAgnStl", method = RequestMethod.POST)
	@ResponseBody
	private String editEntrsAgnStl(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/editEntrsAgnStl");
		logger.info("请求参数：" + jsonBody);
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
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 删除委托代销结算单信息
	@RequestMapping(value = "deleteEntrsAgnStl", method = RequestMethod.POST)
	@ResponseBody
	private String deleteEntrsAgnStl(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/deleteEntrsAgnStl");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = eass.deleteEntrsAgnStl(BaseJson.getReqBody(jsonBody).get("stlSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 查询委托代销结算单信息
	@RequestMapping(value = "queryEntrsAgnStl", method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnStl(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/queryEntrsAgnStl");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = eass.queryEntrsAgnStl(BaseJson.getReqBody(jsonBody).get("stlSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "queryEntrsAgnStlList", method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnStlList(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/queryEntrsAgnStlList");
		logger.info("请求参数：" + jsonBody);
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
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 批量到货单信息删除
	@RequestMapping(value = "deleteEntrsAgnStlList", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteEntrsAgnStlList(@RequestBody String jsonBody) throws IOException {

		logger.info("url:purc/EntrsAgnStl/deleteEntrsAgnStlList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = eass.deleteEntrsAgnStlList(BaseJson.getReqBody(jsonBody).get("stlSnglId").asText());
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 打印及输入输出查询全部客户档案
	@RequestMapping("printingEntrsAgnStlList")
	@ResponseBody
	public Object printingEntrsAgnStlList(@RequestBody String jsonBody) {

		logger.info("url:purc/EntrsAgnStl/printingEntrsAgnStlList");
		logger.info("请求参数：" + jsonBody);
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
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 修改委托代销结算单审核状态
	@RequestMapping(value="updateEntrsAgnStlIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updatePursOrdrIsNtChk(@RequestBody String jsonBody) throws Exception {	
		
		logger.info("url:purc/EntrsAgnStl/updateEntrsAgnStlIsNtChk");
		logger.info("请求参数："+jsonBody);
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
				resp = BaseJson.returnRespObj("purc/EntrsAgnStl/updateEntrsAgnStlIsNtChk", false, "当月已封账！", null);
				logger.info("返回参数：" + resp);
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
			logger.info("返回参数："+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/updateEntrsAgnStlIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}

	// 导入销售入库单
	@RequestMapping("uploadEntrsAgnStlFile")
	@ResponseBody
	public Object uploadEntrsAgnStlFile(HttpServletRequest request) throws IOException {
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
				return eass.uploadFileAddDb(file);
			} else {
				return BaseJson.returnRespObj("purc/EntrsAgnStl/uploadEntrsAgnStlFile", false, "请选择文件！", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/EntrsAgnStl/uploadEntrsAgnStlFile", false, e.getMessage(), null);
		}
	}

	// 分页+排序查询委托代销结算单
	@RequestMapping(value = "queryEntrsAgnStlListOrderBy", method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnStlListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnStl/queryEntrsAgnStlListOrderBy");
		logger.info("请求参数：" + jsonBody);
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
		logger.info("返回参数：" + resp);
		return resp;
	}
}
