package com.px.mis.purc.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.CustDoc;
import com.px.mis.purc.service.CustDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*客户档案功能描述*/
@RequestMapping(value="purc/CustDoc")
@Controller
public class CustDocController {
	
	@Autowired
	private CustDocService cds;
	
	private Logger logger = LoggerFactory.getLogger(CustDocController.class);
	
	/*新增客户档案*/
	@RequestMapping("insertCustDoc")
	public @ResponseBody String insertCustDoc(@RequestBody String jsonData) {
		
		String resp="";
		try {
			String userName=BaseJson.getReqHead(jsonData).get("userName").asText();
			CustDoc custDoc=BaseJson.getPOJO(jsonData,CustDoc.class);
			custDoc.setSetupPers(userName);
			resp=cds.insertCustDoc(custDoc);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*修改客户档案*/
	@RequestMapping("updateCustDocByCustId")
	public @ResponseBody Object updateCustDocByCustId(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			String userName=BaseJson.getReqHead(jsonData).get("userName").asText();
			CustDoc custDoc=BaseJson.getPOJO(jsonData,CustDoc.class);
			custDoc.setMdfr(userName);
			on=cds.updateCustDocByCustId(custDoc);
			resp =BaseJson.returnRespObj("purc/CustDoc/updateCustDocByCustId", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*删除客户档案*/
	@RequestMapping("deleteCustDocList")
	public @ResponseBody String deleteCustDocByCustId(@RequestBody String jsonBody) {
		String resp="";
		try {
			resp=cds.deleteCustDocList(BaseJson.getReqBody(jsonBody).get("custId").asText());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*按照编号查询客户档案*/
	@RequestMapping("selectCustDocByCustId")
	@ResponseBody
	public Object selectCustDocByCustId(@RequestBody String jsonData) {
		logger.info("purc/CustDoc/selectCustDocByCustId");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		try {
			String custId = BaseJson.getReqBody(jsonData).get("custId").asText();
			resp=cds.selectCustDocByCustId(custId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}

	//查询所有客户档案信息
	@RequestMapping("selectCustDocList")
	@ResponseBody
	public Object selectCustDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/CustDoc/selectCustDocList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=cds.selectCustDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
		
	}
	
	//打印及输入输出查询全部客户档案
	@RequestMapping("printingCustDocList")
	@ResponseBody
	public Object printingCustDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/CustDoc/printingCustDocList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=cds.printingCustDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
		
	}
	
	//导入客户档案
	@RequestMapping("uploadCustDocFile")
	@ResponseBody
	public Object uploadCustDocFile(HttpServletRequest request) throws IOException{
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
			    return cds.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/CustDoc/uploadCustDocFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/CustDoc/uploadCustDocFile", false, e.getMessage(), null);
		}
	}
}
