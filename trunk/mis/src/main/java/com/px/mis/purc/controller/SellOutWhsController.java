package com.px.mis.purc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.SellOutWhsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*销售出库单功能描述*/
@RequestMapping(value="purc/SellOutWhs")
@Controller
public class SellOutWhsController {
	
	private Logger logger = LoggerFactory.getLogger(SellOutWhsController.class);
	
	@Autowired
	private SellOutWhsService sows;
    //记账
	@Autowired
	FormBookService formBookService;
	
	//新增销售出库单信息
	@RequestMapping(value="addSellOutWhs",method = RequestMethod.POST)
	@ResponseBody
	private String addPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/addSellOutWhs");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			
			SellOutWhs sellOutWhs = BaseJson.getPOJO(jsonBody, SellOutWhs.class);
			sellOutWhs.setSetupPers(userName);
			List<Map> mList=BaseJson.getList(jsonBody);
			List<SellOutWhsSub> sellOutWhsSubList=new ArrayList<>();
			for (Map map : mList) {
				SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();
				if(map.get("baoZhiQi")==null || map.get("baoZhiQi")=="") {
					sellOutWhsSub.setBaoZhiQi(0);
				}else {
					int baoZhiQi = Integer.parseInt(map.get("baoZhiQi").toString());
					sellOutWhsSub.setBaoZhiQi(baoZhiQi);
				}
				BeanUtils.populate(sellOutWhsSub, map);
				sellOutWhsSubList.add(sellOutWhsSub);
			}
			resp=sows.addSellOutWhs(userId, sellOutWhs, sellOutWhsSubList,loginTime);
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//修改销售出库单信息
	@RequestMapping(value="editSellOutWhs",method = RequestMethod.POST)
	@ResponseBody
	private String editPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/editSellOutWhs");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			SellOutWhs sellOutWhs = BaseJson.getPOJO(jsonBody, SellOutWhs.class);
			sellOutWhs.setMdfr(userName);
			List<Map> mList=BaseJson.getList(jsonBody);
			List<SellOutWhsSub> sellOutWhsSubList=new ArrayList<>();
			for (Map map : mList) {
				SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();
				int baoZhiQi;
				if(map.get("baoZhiQi").toString().equals("") || map.get("baoZhiQi").toString()==null ) {
					baoZhiQi = 0;
				}else {
					baoZhiQi = Integer.parseInt(map.get("baoZhiQi").toString());
				}
				sellOutWhsSub.setBaoZhiQi(baoZhiQi);
				BeanUtils.populate(sellOutWhsSub, map);
				sellOutWhsSubList.add(sellOutWhsSub);
			}
			resp=sows.editSellOutWhs(sellOutWhs, sellOutWhsSubList);
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//删除销售出库单信息
	@RequestMapping(value="deleteSellOutWhs",method = RequestMethod.POST)
	@ResponseBody
	private String deletePursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/deleteSellOutWhs");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=sows.deleteSellOutWhs(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//查询销售出库单信息
	@RequestMapping(value="querySellOutWhs",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhs");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=sows.querySellOutWhs(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="querySellOutWhsList",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrList(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhsList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			
			//仓库权限控制
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/SellOutWhs/querySellOutWhsList", false, "用户没有该仓库权限" , null);
			}else {
			    map.put("whsId", whsId);
			    resp=sows.querySellOutWhsList(map);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//批量销售出库单信息删除
	@RequestMapping(value="deleteSellOutWhsList",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteSellOutWhsList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/SellOutWhs/deleteSellOutWhsList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=sows.deleteSellOutWhsList(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
	    logger.info("返回参数："+resp);
		return resp;
	}
	
	//批量更新销售出库单审核状态
	@RequestMapping(value="updateSellOutWhsIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updateSellOutWhsIsNtChk(@RequestBody String jsonBody) throws Exception {	
		
//		logger.info("url:purc/SellOutWhs/updateSellOutWhsIsNtChk");
//		logger.info("请求参数："+jsonBody);
		Map<String,Object> result = new HashMap<>();
		String resp="";
		boolean isSuccess = false;
		List<SellOutWhs> SellOutWhsList=new ArrayList();
		try
		{
			String userId=BaseJson.getReqHead(jsonBody).get("accNum").asText();
			String userName=BaseJson.getReqHead(jsonBody).get("userName").asText();
			String loginTime=BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/SellOutWhs/updateSellOutWhsIsNtChk", false, "当月已封账！", null);
				logger.info("返回参数：" + resp);
				return resp;
			}
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//		    JSONArray jsonArray = jsonObjectList.getJSONArray("list");
//		    SellOutWhsList= jsonArray.toJavaList(SellOutWhs.class);	
			SellOutWhsList=BaseJson.getPOJOList(jsonBody, SellOutWhs.class);
		    for(SellOutWhs sellOutWhs :SellOutWhsList) {
				try
				{
					sellOutWhs.setChkr(userName);
					result=sows.updateSellOutWhsIsNtChk(sellOutWhs);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}
		    resp=BaseJson.returnRespObj("purc/SellOutWhs/updateSellOutWhsIsNtChk", isSuccess, resp, null);
//			logger.info("返回参数："+resp);
		}catch (Exception ex) {
//			logger.info("返回参数："+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/SellOutWhs/updateSellOutWhsIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}
	
	//打印及输入输出查询全部销售出库单
	@RequestMapping("printingSellOutWhsList")
	@ResponseBody
	public Object printingSellOutWhsList(@RequestBody String jsonBody){
		
		logger.info("url:purc/SellOutWhs/printingSellOutWhsList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			
			//仓库权限控制
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/SellOutWhs/printingSellOutWhsList", false, "用户没有该仓库权限" , null);
			}else {
			    map.put("whsId", whsId);
			    resp=sows.printingSellOutWhsList(map);
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//出库明细表
	@RequestMapping(value="querySellOutWhsByInvtyEncd",method = RequestMethod.POST)
	@ResponseBody
	private String querySellOutWhsByInvtyEncd(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhsByInvtyEncd");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
				int pageNo=(int)map.get("pageNo");
				int pageSize=(int)map.get("pageSize");
				map.put("index", (pageNo-1)*pageSize);
				map.put("num", pageSize);
			}
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			resp=sows.querySellOutWhsByInvtyEncd(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	//出库明细表--导出
	@RequestMapping(value="querySellOutWhsByInvtyEncdPrint",method = RequestMethod.POST)
	@ResponseBody
	private String querySellOutWhsByInvtyEncdPrint(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhsByInvtyEncdPrint");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			resp=sows.querySellOutWhsByInvtyEncdPrint(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//导入销售出库单
	@RequestMapping("uploadSellOutWhsFile")
	@ResponseBody
	public Object uploadSellOutWhsFile(HttpServletRequest request) throws IOException{
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
			    return sows.uploadFileAddDb(file,0);
		    }else {
		    	return BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFile", false, e.getMessage(), null);
		}
	}
	//导入销售出库单
	@RequestMapping("uploadSellOutWhsFileU8")
	@ResponseBody
	public Object uploadSellOutWhsFileU8(HttpServletRequest request) throws IOException{
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
			    return sows.uploadFileAddDb(file,1);
		    }else {
		    	return BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFileU8", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFileU8", false, e.getMessage(), null);
		}
	}
	//分页+排序销售出库单
	@RequestMapping(value="querySellOutWhsListOrderBy",method = RequestMethod.POST)
	@ResponseBody
	private String querySellOutWhsListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhsListOrderBy");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			
			//仓库权限控制
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/SellOutWhs/querySellOutWhsListOrderBy", false, "用户没有该仓库权限" , null);
			}else {
			    map.put("whsId", whsId);
			    resp=sows.querySellOutWhsListOrderBy(map);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
}
