package com.px.mis.purc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.service.IntoWhsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*采购入库单功能描述*/
@RequestMapping(value="purc/IntoWhs")
@Controller
public class PurcIntoWhsController {
	
	private Logger logger = LoggerFactory.getLogger(PurcIntoWhsController.class);
	
	@Autowired
	private IntoWhsService iws;
    //记账
	@Autowired
	FormBookService formBookService;
	
	//新增采购入库单信息
	@RequestMapping(value="addIntoWhs",method = RequestMethod.POST)
	@ResponseBody
	private String addIntoWhs(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/IntoWhs/addIntoWhs");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try{
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			
			IntoWhs intoWhs = BaseJson.getPOJO(jsonBody, IntoWhs.class);
			intoWhs.setSetupPers(userName);
			List<IntoWhsSub> intoWhsSubList=new ArrayList<>();
			intoWhsSubList=BaseJson.getPOJOList(jsonBody, IntoWhsSub.class);
			resp=iws.addIntoWhs(userId,intoWhs, intoWhsSubList,loginTime);
		} catch (Exception e) {
			resp=BaseJson.returnRespObj("purc/IntoWhs/addIntoWhs", false, e.getMessage(), null);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//修改采购入库单信息
	@RequestMapping(value="editIntoWhs",method = RequestMethod.POST)
	@ResponseBody
	private String editIntoWhs(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/IntoWhs/editIntoWhs");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			IntoWhs intoWhs = BaseJson.getPOJO(jsonBody, IntoWhs.class);
			intoWhs.setMdfr(userName);
			List<IntoWhsSub> intoWhsSubList=new ArrayList<>();
			intoWhsSubList=BaseJson.getPOJOList(jsonBody, IntoWhsSub.class);
			resp=iws.editIntoWhs(intoWhs, intoWhsSubList);
		} catch (Exception e) {
			resp=BaseJson.returnRespObj("purc/IntoWhs/editIntoWhs", false, e.getMessage(), null);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//删除采购订单信息
	@RequestMapping(value="deleteIntoWhs",method = RequestMethod.POST)
	@ResponseBody
	private String deleteIntoWhs(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/deleteIntoWhs");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=iws.deleteIntoWhs(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//查询采购入库单详情
	@RequestMapping(value="queryIntoWhs",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhs(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhs");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp = iws.queryIntoWhs(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//分页查询全部采购入库单信息
	@RequestMapping(value="queryIntoWhsList",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhsList(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhsList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			
			//仓库权限控制
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/IntoWhs/queryIntoWhsList", false, "用户没有该仓库权限" , null);
			}else {
			    map.put("whsId", whsId);
			    resp = iws.queryIntoWhsList(map);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//批量采购入库单信息删除
	@RequestMapping(value="deleteIntoWhsList",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteIntoWhsList(@RequestBody String jsonBody) throws IOException {
			
		logger.info("url:purc/IntoWhs/deleteIntoWhsList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=iws.deleteIntoWhsList(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
	    logger.info("返回参数："+resp);
		return resp;
	}
	
	//批量更新采购入库单审核状态
	@RequestMapping(value="updateIntoWhsIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updateIntoWhsIsNtChk(@RequestBody String jsonBody) throws Exception {	
		
		logger.info("url:purc/IntoWhs/updateIntoWhsIsNtChk");
		logger.info("请求参数："+jsonBody);
		Map<String,Object> result = new HashMap<>();
		String resp = "";
		boolean isSuccess = false;
		List<IntoWhs> intoWhsList=new ArrayList<>();
		try
		{
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/IntoWhs/updateIntoWhsIsNtChk", false, "当月已封账！", null);
				logger.info("返回参数：" + resp);
				return resp;
			}
			intoWhsList = BaseJson.getPOJOList(jsonBody,IntoWhs.class);
		    for(IntoWhs intoWhs :intoWhsList) {
				try
				{
					intoWhs.setChkr(userName);
					result=iws.updateIntoWhsIsNtChk(intoWhs);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}
			resp=BaseJson.returnRespObj("purc/IntoWhs/updateIntoWhsIsNtChk", isSuccess, resp, null);
		    logger.info("返回参数："+resp);
		}catch (Exception ex) {
			logger.info("返回参数："+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/IntoWhs/updateIntoWhsIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}

	//打印及输入输出查询全部采购入库单
	@RequestMapping("printingIntoWhsList")
	@ResponseBody
	public Object printingIntoWhsList(@RequestBody String jsonBody){
		
		logger.info("url:purc/IntoWhs/printingIntoWhsList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			//仓库权限控制
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/IntoWhs/printingIntoWhsList", false, "用户没有该仓库权限" , null);
			}else {
			    map.put("whsId", whsId);
			    resp=iws.printingIntoWhsList(map);
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//入库明细表
	@RequestMapping(value="queryIntoWhsByInvtyEncd",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhsByInvtyEncd(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhsByInvtyEncd");
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
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			resp=iws.queryIntoWhsByInvtyEncd(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//入库明细表-导出
		@RequestMapping(value="queryIntoWhsByInvtyEncdPrint",method = RequestMethod.POST)
		@ResponseBody
		private String queryIntoWhsByInvtyEncdPrint(@RequestBody String jsonBody) {
			logger.info("url:purc/IntoWhs/queryIntoWhsByInvtyEncdPrint");
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
				String intoWhsDt1 = (String) map.get("intoWhsDt1");
				String intoWhsDt2 = (String) map.get("intoWhsDt2");
				if (intoWhsDt1!=null && intoWhsDt1!="") {
					intoWhsDt1 = intoWhsDt1 + " 00:00:00";
				}
				if (intoWhsDt2!=null && intoWhsDt2!="") {
					intoWhsDt2 = intoWhsDt2 + " 23:59:59";
				}
				map.put("intoWhsDt1", intoWhsDt1);
				map.put("intoWhsDt2", intoWhsDt2);
				resp=iws.queryIntoWhsByInvtyEncdPrint(map);
			} catch (IOException e) {
				//e.printStackTrace();
			} 
			logger.info("返回参数："+resp);
			return resp;
		}
	
	//采购订收货统计表
	@RequestMapping(value="selectIntoWhsAndPursOrdr",method = RequestMethod.POST)
	@ResponseBody
	private String selectIntoWhsAndPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/selectIntoWhsAndPursOrdr");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			resp=iws.selectIntoWhsAndPursOrdr(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//导入采购入库单
	@RequestMapping("uploadIntoWhsFile")
	@ResponseBody
	public Object uploadIntoWhsFile(HttpServletRequest request) throws IOException{
		try {
			//创建一个通用的多部分解析器
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //判断 request 是否有文件上传,即多部分请求
		    if(multipartResolver.isMultipart(request)) {
		        //转换成多部分request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return  ("请选择文件!");
			    }
			    return iws.uploadFileAddDb(file,0);
		    }else {
		    	return  BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFile", false, e.getMessage(), null);
		}
	}
	//导入采购入库单
		@RequestMapping("uploadIntoWhsFileU8")
		@ResponseBody
		public Object uploadIntoWhsFileU8(HttpServletRequest request) throws IOException{
			try {
				//创建一个通用的多部分解析器
			    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			    //判断 request 是否有文件上传,即多部分请求
			    if(multipartResolver.isMultipart(request)) {
			        //转换成多部分request
				    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
			      	MultipartFile file = mRequest.getFile("file");
				    if(file == null) {
					   return  ("请选择文件!");
				    }
				    return iws.uploadFileAddDb(file,1);
			    }else {
			    	return  BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFileU8", false, "请选择文件！", null);
			    }
			} catch (Exception e) {
				// TODO: handle exception
				//e.printStackTrace();
				return BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFileU8", false, e.getMessage(), null);
			}
		}
		
	//采购发票参照采购入库单时带回多个采购入库子表信息
	@RequestMapping("queryIntoWhsByIntoWhsIds")
	@ResponseBody
	public Object queryIntoWhsByIntoWhsIds(@RequestBody String jsonBody){
		logger.info("url:purc/IntoWhs/queryIntoWhsByIntoWhsIds");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=iws.queryIntoWhsByIntoWhsIds(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//采购退货参照采购入库单时带回多个采购入库子表信息
	@RequestMapping("selectIntoWhsSubByUnReturnQty")
	@ResponseBody
	public Object selectIntoWhsSubByUnReturnQty(@RequestBody String jsonBody){
		logger.info("url:purc/IntoWhs/selectIntoWhsSubByUnReturnQty");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=iws.selectIntoWhsSubByUnReturnQty(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//参照时查询全部采购入库单主表信息
	@RequestMapping(value="queryIntoWhsLists",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhsLists(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhsLists");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			resp = iws.queryIntoWhsLists(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	//分页+排序
	@RequestMapping(value="queryIntoWhsListOrderBy",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhsListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhsListOrderBy");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			//仓库权限控制
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/IntoWhs/queryIntoWhsListOrderBy", false, "用户没有该仓库权限" , null);
			}else {
			    map.put("whsId", whsId);
			    resp = iws.queryIntoWhsListOrderBy(map);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
}
