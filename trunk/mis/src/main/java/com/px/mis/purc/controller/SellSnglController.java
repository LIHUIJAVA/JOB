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
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.purc.service.SellSnglService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*销售单功能描述*/
@RequestMapping(value="purc/SellSngl")
@Controller
public class SellSnglController {
	
	private Logger logger = LoggerFactory.getLogger(SellSnglController.class);
	
	@Autowired
	private SellSnglService sss;
    //记账
	@Autowired
	FormBookService formBookService;
	
	//新增销售单信息
	@RequestMapping(value="addSellSngl",method = RequestMethod.POST)
	@ResponseBody
	private String addPursOrdr(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/SellSngl/addSellSngl");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			
			SellSngl sellSngl = BaseJson.getPOJO(jsonBody, SellSngl.class);
			sellSngl.setSetupPers(userName);
			List<Map> mList=BaseJson.getList(jsonBody);
			List<SellSnglSub> sellSnglSubList=new ArrayList<>();
			for (Map map : mList) {
				SellSnglSub sellSnglSub = new SellSnglSub();
				if(StringUtils.isNotEmpty((String)map.get("baoZhiQi"))){
					int baoZhiQi = Integer.parseInt(map.get("baoZhiQi").toString());
					sellSnglSub.setBaoZhiQi(baoZhiQi);
				}
				BeanUtils.populate(sellSnglSub, map);
				sellSnglSubList.add(sellSnglSub);
			}
			resp=sss.addSellSngl(userId,sellSngl, sellSnglSubList,loginTime); 
		} catch (Exception e) {
			//e.printStackTrace();
			resp=BaseJson.returnRespObj("purc/SellSngl/addSellSngl", false, e.getMessage(), null);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//修改销售单信息
	@RequestMapping(value="editSellSngl",method = RequestMethod.POST)
	@ResponseBody
	private String editPursOrdr(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/SellSngl/editSellSngl");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			SellSngl sellSngl = BaseJson.getPOJO(jsonBody, SellSngl.class);
			sellSngl.setMdfr(userName);
			List<Map> mList=BaseJson.getList(jsonBody);
			List<SellSnglSub> sellSnglSubList=new ArrayList<>();
			for (Map map : mList) {
				SellSnglSub sellSnglSub = new SellSnglSub();
				if(StringUtils.isNotEmpty((String)map.get("baoZhiQi"))){
					int baoZhiQi = Integer.parseInt(map.get("baoZhiQi").toString());
					sellSnglSub.setBaoZhiQi(baoZhiQi);
				}
				BeanUtils.populate(sellSnglSub, map);
				sellSnglSubList.add(sellSnglSub);
			}
			resp=sss.editSellSngl(sellSngl, sellSnglSubList);
		} catch (Exception e) {
			//e.printStackTrace();
			//e.printStackTrace();
			resp=BaseJson.returnRespObj("purc/SellSngl/editSellSngl", false, e.getMessage(), null);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//删除销售单信息
	@RequestMapping(value="deleteSellSngl",method = RequestMethod.POST)
	@ResponseBody
	private String deletePursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/deleteSellSngl");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=sss.deleteSellSngl(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//查询销售单信息
	@RequestMapping(value="querySellSngl",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSngl");
		logger.info("请求参数："+jsonBody);
		String resp="";
			try {
				resp=sss.querySellSngl(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="querySellSnglList",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrList(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSnglList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String sellSnglDt1 = (String) map.get("sellSnglDt1");
			String sellSnglDt2 = (String) map.get("sellSnglDt2");
			if (sellSnglDt1!=null && sellSnglDt1!="") {
				sellSnglDt1 = sellSnglDt1 + " 00:00:00";
			}
			if (sellSnglDt2!=null && sellSnglDt2!="") {
				sellSnglDt2 = sellSnglDt2 + " 23:59:59";
			}
			map.put("sellSnglDt1", sellSnglDt1);
			map.put("sellSnglDt2", sellSnglDt2);
			resp=sss.querySellSnglList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//批量销售单信息删除
	@RequestMapping(value="deleteSellSnglList",method = RequestMethod.POST)
	@ResponseBody
	public Object deletePursOrdrList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/SellSngl/deleteSellSnglList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=sss.deleteSellSnglList(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText());
	    logger.info("返回参数："+resp);
		return resp;
	}
	
	//批量更新销售单审核状态
	@RequestMapping(value="updateSellSnglIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updateSellSnglIsNtChk(@RequestBody String jsonBody) throws Exception {	
		
		logger.info("url:purc/SellSngl/updateSellSnglIsNtChk");
		logger.info("请求参数："+jsonBody);
		Map<String,Object> result = new HashMap<>();
		String resp = "";
		boolean isSuccess = false;
		List<SellSngl> SellSnglList=new ArrayList();
		try
		{
			String userId=BaseJson.getReqHead(jsonBody).get("accNum").asText();
			String loginTime=BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			String userName=BaseJson.getReqHead(jsonBody).get("userName").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/SellSngl/updateSellSnglIsNtChk", false, "当月已封账！", null);
				logger.info("返回参数：" + resp);
				return resp;
			}
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//		    JSONArray jsonArray = jsonObjectList.getJSONArray("list");
//		    SellSnglList= jsonArray.toJavaList(SellSngl.class);
			SellSnglList = BaseJson.getPOJOList(jsonBody, SellSngl.class);
			for(SellSngl sellSngl :SellSnglList) {
				try
				{
					sellSngl.setChkr(userName);
					result=sss.updateSellSnglIsNtChkList(userId,sellSngl,loginTime);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}
			resp = BaseJson.returnRespObj("purc/SellSngl/updateSellSnglIsNtChk", isSuccess, resp, null);
		    logger.info("返回参数："+resp);
		}catch (Exception ex) {
			logger.info("返回参数："+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/SellSngl/updateSellSnglIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}
	
	//销售明细表
	@RequestMapping(value="querySellSnglByInvtyEncd",method = RequestMethod.POST)
	@ResponseBody
	private String querySellSnglByInvtyEncd(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSnglByInvtyEncd");
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
			String sellSnglDt1 = (String) map.get("sellSnglDt1");
			String sellSnglDt2 = (String) map.get("sellSnglDt2");
			if (sellSnglDt1!=null && sellSnglDt1!="") {
				sellSnglDt1 = sellSnglDt1 + " 00:00:00";
			}
			if (sellSnglDt2!=null && sellSnglDt2!="") {
				sellSnglDt2 = sellSnglDt2 + " 23:59:59";
			}
			map.put("sellSnglDt1", sellSnglDt1);
			map.put("sellSnglDt2", sellSnglDt2);
			resp=sss.querySellSnglByInvtyEncd(map);
			
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	//导入销售入库单
	@RequestMapping("uploadSellSnglFile")
	@ResponseBody
	public Object uploadSellSnglFile(HttpServletRequest request) throws IOException{
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
			    return sss.uploadFileAddDb(file,0);
		    }else {
		    	return  BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/SellSngl/uploadIntoWhsFile", false, e.getMessage(), null);
		}
	}
	//导入销售入库单
	@RequestMapping("uploadSellSnglFileU8")
	@ResponseBody
	public Object uploadSellSnglFileU8(HttpServletRequest request) throws IOException{
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
			    return sss.uploadFileAddDb(file,1);
		    }else {
		    	return  BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFileU8", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFileU8", false, e.getMessage(), null);
		}
	}
	//打印及输入输出查询全部销售出库单
	@RequestMapping("printingSellSnglList")
	@ResponseBody
	public Object printingSellSnglList(@RequestBody String jsonBody){
		
		logger.info("url:purc/SellSngl/printingSellSnglList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String sellSnglDt1 = (String) map.get("sellSnglDt1");
			String sellSnglDt2 = (String) map.get("sellSnglDt2");
			if (sellSnglDt1!=null && !sellSnglDt1.equals("")) {
				sellSnglDt1 = sellSnglDt1 + " 00:00:00";
			}
			if (sellSnglDt2!=null && sellSnglDt2!="") {
				sellSnglDt2 = sellSnglDt2 + " 23:59:59";
			}
			map.put("sellSnglDt1", sellSnglDt1);
			map.put("sellSnglDt2", sellSnglDt2);
			resp=sss.printingSellSnglList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//根据销售出库单生成销售单的批号、生产日期等信息
	@RequestMapping(value="updateSellBySellOut",method = RequestMethod.POST)
	@ResponseBody
	private Object updateSellBySellOut(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/updateSellBySellOut");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp = sss.updateA(map);
		} catch (Exception e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//销售统计表
	@RequestMapping(value="selectXSTJList",method = RequestMethod.POST)
	@ResponseBody
	private String selectXSTJList(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/selectXSTJList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String bllgDt1 = (String) map.get("bllgDt1");
			String bllgDt2 = (String) map.get("bllgDt2");
			if (bllgDt1!=null && bllgDt1!="") {
				bllgDt1 = bllgDt1 + " 00:00:00";
			}
			if (bllgDt2!=null && bllgDt2!="") {
				bllgDt2 = bllgDt2 + " 23:59:59";
			}
			map.put("bllgDt1", bllgDt1);
			map.put("bllgDt2", bllgDt2);
			resp=sss.selectXSTJList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//采购退货参照采购入库单时带回多个采购入库子表信息
	@RequestMapping("selectSellSnglSubByRtnblQty")
	@ResponseBody
	public Object selectSellSnglSubByRtnblQty(@RequestBody String jsonBody){
		logger.info("url:purc/SellSngl/selectSellSnglSubByRtnblQty");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=sss.selectSellSnglSubByRtnblQty(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="querySellSnglLists",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrLists(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSnglLists");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String sellSnglDt1 = (String) map.get("sellSnglDt1");
			String sellSnglDt2 = (String) map.get("sellSnglDt2");
			if (sellSnglDt1!=null && sellSnglDt1!="") {
				sellSnglDt1 = sellSnglDt1 + " 00:00:00";
			}
			if (sellSnglDt2!=null && sellSnglDt2!="") {
				sellSnglDt2 = sellSnglDt2 + " 23:59:59";
			}
			map.put("sellSnglDt1", sellSnglDt1);
			map.put("sellSnglDt2", sellSnglDt2);
			resp=sss.querySellSnglLists(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	@RequestMapping(value="querySellSnglListOrderBy",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSnglListOrderBy");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String sellSnglDt1 = (String) map.get("sellSnglDt1");
			String sellSnglDt2 = (String) map.get("sellSnglDt2");
			if (sellSnglDt1!=null && sellSnglDt1!="") {
				sellSnglDt1 = sellSnglDt1 + " 00:00:00";
			}
			if (sellSnglDt2!=null && sellSnglDt2!="") {
				sellSnglDt2 = sellSnglDt2 + " 23:59:59";
			}
			map.put("sellSnglDt1", sellSnglDt1);
			map.put("sellSnglDt2", sellSnglDt2);
			resp=sss.querySellSnglListOrderBy(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	
}
