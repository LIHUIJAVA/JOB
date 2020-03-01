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


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.PursInvMasTab;
import com.px.mis.account.entity.PursInvSubTab;
import com.px.mis.account.service.PursInvMasTabService;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
//采购发票接口
@RequestMapping(value="account/pursInvMasTab",method=RequestMethod.POST)
@Controller
public class PursInvMasTabController {
	private Logger logger = LoggerFactory.getLogger(PursInvMasTabController.class);
	@Autowired
	private PursInvMasTabService pursInvMasTabService;
	
	/*添加*/
	@RequestMapping("insertPursInvMasTab")
	public @ResponseBody Object insertPursInvMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/pursInvMasTab/insertPursInvMasTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		PursInvMasTab pursInvMasTab=null;
		try {
			pursInvMasTab = BaseJson.getPOJO(jsonData,PursInvMasTab.class); 
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/pursInvMasTab/insertPursInvMasTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/insertPursInvMasTab", false, "请求参数解析错误，请检查请求参数是否书写正确，insert error!", pursInvMasTab);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/pursInvMasTab/insertPursInvMasTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on=pursInvMasTabService.insertPursInvMasTab(pursInvMasTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/insertPursInvMasTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), pursInvMasTab);
			}else {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/insertPursInvMasTab", false, "insert error!", pursInvMasTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/pursInvMasTab/insertPursInvMasTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deletePursInvMasTab")
	public @ResponseBody Object deletePursInvMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/pursInvMasTab/deletePursInvMasTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		String pursInvNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			pursInvNum = reqBody.get("pursInvNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/pursInvMasTab/deletePursInvMasTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/deletePursInvMasTab", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/pursInvMasTab/deletePursInvMasTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on=pursInvMasTabService.deletePursInvMasTabByPursInvNum(pursInvNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/deletePursInvMasTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/deletePursInvMasTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/pursInvMasTab/deletePursInvMasTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updatePursInvMasTab")
	public @ResponseBody Object updatePursInvMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/pursInvMasTab/updatePursInvMasTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		PursInvMasTab pursInvMasTab=null;
		List<PursInvSubTab> pursInvSubTabList=new ArrayList<>();
		try {
			pursInvMasTab = BaseJson.getPOJO(jsonData,PursInvMasTab.class);
			List<Map> mList=BaseJson.getList(jsonData);
			for (Map map : mList) {
				PursInvSubTab pursInvSubTab = new PursInvSubTab();
				BeanUtils.populate(pursInvSubTab, map);
				pursInvSubTabList.add(pursInvSubTab);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/pursInvMasTab/updatePursInvMasTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/updatePursInvMasTab", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", pursInvMasTab);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/pursInvMasTab/updatePursInvMasTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on=pursInvMasTabService.updatePursInvMasTabById(pursInvMasTab,pursInvSubTabList);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/updatePursInvMasTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), pursInvMasTab);
			}else {
				resp =BaseJson.returnRespObj("/account/pursInvMasTab/updatePursInvMasTab", false, "update error!", pursInvMasTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/pursInvMasTab/updatePursInvMasTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*条件查询所有*/
	@RequestMapping(value="selectPursInvMasTab")
	public @ResponseBody Object selectPursInvMasTab(@RequestBody String jsonBody) {
		logger.info("url:/account/pursInvMasTab/selectPursInvMasTab");
		logger.info("请求参数："+jsonBody);

		String resp="";
		Map map=null;
		Integer pageNo=null;
		Integer pageSize=null;
		Object startBllgDt=null;
		Object endBllgDt=null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int)map.get("pageNo");
			pageSize = (int)map.get("pageSize");
			startBllgDt = map.get("startBllgDt");
			endBllgDt = map.get("endBllgDt");
			if (startBllgDt!=null || startBllgDt!="") {
				startBllgDt = startBllgDt + " 00:00:00";
			}
			if (endBllgDt!=null || endBllgDt!="") {
				endBllgDt = endBllgDt + " 23:59:59";
			}
			if(pageNo==0 || pageSize==0) {
				resp=BaseJson.returnRespList("/account/pursInvMasTab/selectPursInvMasTab", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/pursInvMasTab/selectPursInvMasTab 异常说明：",e1);
			try {
				resp=BaseJson.returnRespList("/account/pursInvMasTab/selectPursInvMasTab", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/pursInvMasTab/selectPursInvMasTab 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			map.put("startBllgDt", startBllgDt);
			map.put("endBllgDt", endBllgDt);
			resp=pursInvMasTabService.selectPursInvMasTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/pursInvMasTab/selectPursInvMasTab 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	/*查询单个*/
	@RequestMapping(value="selectPursInvMasTabById")
	public @ResponseBody Object selectPursInvMasTabById(@RequestBody String jsonData) {
		logger.info("url:/account/pursInvMasTab/selectPursInvMasTabById");
		logger.info("请求参数："+jsonData);

		String resp=""; 
		String pursInvNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			pursInvNum = reqBody.get("pursInvNum").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/pursInvMasTab/selectPursInvMasTabById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/pursInvMasTab/selectPursInvMasTabById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/pursInvMasTab/selectPursInvMasTabById 异常说明：",e);
			}
			return resp;
		}
		try {
			PursInvMasTab pursInvMasTab=pursInvMasTabService.selectPursInvMasTabByPursInvNum(pursInvNum);
			if(pursInvMasTab!=null) {
				 resp=BaseJson.returnRespObj("/account/pursInvMasTab/selectPursInvMasTabById",true,"处理成功！", pursInvMasTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/pursInvMasTab/selectPursInvMasTabById",false,"没有查到数据！", pursInvMasTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/pursInvMasTab/selectPursInvMasTabById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	
	//批量删除
	@RequestMapping(value="deletePursInvMasTabList",method = RequestMethod.POST)
	@ResponseBody
	public Object deletePursInvMasTabList(@RequestBody String jsonBody) throws IOException {

		logger.info("url:account/pursInvMasTab/deletePursInvMasTabList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=pursInvMasTabService.deletePursInvMasTabList(BaseJson.getReqBody(jsonBody).get("pursInvNum").asText());
	    logger.info("返回参数："+resp);
		return resp;
	}
	
	//批量更新审核状态
	@RequestMapping(value="updatePursInvMasTabIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public String updatePursInvMasTabIsNtChk(@RequestBody String jsonBody) throws IOException {	
		
		logger.info("url:account/pursInvMasTab/updatePursInvMasTabIsNtChk");
		logger.info("请求参数："+jsonBody);
		String resp="";
		List<PursInvMasTab> pursInvMasTabList=new ArrayList<>();
//		JSONObject jsonObject = JSON.parseObject(jsonBody);
//		JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//		JSONArray jsonArray = jsonObjectList.getJSONArray("pursInvMasTabList");
//		pursInvMasTabList= jsonArray.toJavaList(PursInvMasTab.class);
		try {
			pursInvMasTabList=BaseJson.getPOJOList(jsonBody, "pursInvMasTabListkey", PursInvMasTab.class);
		} catch (IOException e) {
			return BaseJson.returnRespObj("/account/pursInvMasTab/selectPursInvMasTabBingList", false, e.getMessage(), null);
		}
		resp=pursInvMasTabService.updatePursInvMasTabIsNtChkList(pursInvMasTabList);
	    logger.info("返回参数："+resp);
		return resp;
	}
	
	//多张采购入库单生成一张采购发票
	@RequestMapping(value="selectPursInvMasTabBingList",method = RequestMethod.POST)
	@ResponseBody
	private String selectPursInvMasTabBingList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:account/pursInvMasTab/selectPursInvMasTabBingList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			List<IntoWhs> intoWhsList=new ArrayList<>();
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//			JSONArray jsonArray = jsonObjectList.getJSONArray("lists");
//			intoWhsList= jsonArray.toJavaList(IntoWhs.class);
			intoWhsList = BaseJson.getPOJOList(jsonBody, "lists",IntoWhs.class);
			resp=pursInvMasTabService.selectPursInvMasTabBingList(intoWhsList);
		} catch (Exception e) {
			e.printStackTrace();
			return BaseJson.returnRespObj("/account/pursInvMasTab/selectPursInvMasTabBingList", false, e.getMessage(), null);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//导入采购发票
	@RequestMapping("uploadPursInvMasTabFile")
	@ResponseBody
	public Object uploadPursInvMasTabFile(HttpServletRequest request) throws IOException{
		try {
			//创建一个通用的多部分解析器
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //判断 request 是否有文件上传,即多部分请求
		    if(multipartResolver.isMultipart(request)) {
		        //转换成多部分request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return ("请选择文件。");
			    }
			    return pursInvMasTabService.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("account/pursInvMasTab/uploadPursInvMasTabFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BaseJson.returnRespObj("account/pursInvMasTab/uploadPursInvMasTabFile", false, e.getMessage(), null);
		}
	}
	
	//打印和导出时使用的查询接口
	@RequestMapping("printingPursInvMasTabList")
	@ResponseBody
	public Object printingPursInvMasTabList(@RequestBody String jsonBody){
		
		logger.info("url:account/pursInvMasTab/printingPursInvMasTabList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String startBllgDt = (String) map.get("startBllgDt");
			String endBllgDt = (String) map.get("endBllgDt");
			if (startBllgDt!=null || startBllgDt!="") {
				startBllgDt = startBllgDt + " 00:00:00";
			}
			if (endBllgDt!=null || endBllgDt!="") {
				endBllgDt = endBllgDt + " 23:59:59";
			}
			map.put("startBllgDt", startBllgDt);
			map.put("endBllgDt", endBllgDt);
			resp=pursInvMasTabService.upLoadPursInvMasTabList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
}
