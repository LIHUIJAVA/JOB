package com.px.mis.purc.controller;

import java.io.IOException;
import java.util.List;
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
import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.service.CustClsService;
import com.px.mis.util.BaseJson;
/*客户分类功能描述*/
@RequestMapping(value="purc/CustCls")
@Controller
public class CustClsController {
	
	private Logger logger = LoggerFactory.getLogger(SellSnglController.class);
	
	@Autowired
	private CustClsService ccs;
	
	/*新增客户分类*/
	@RequestMapping("insertCustCls")
	public @ResponseBody Object insertCustCls(@RequestBody String jsonData) {
		String resp="";
		try {
			CustCls custCls=BaseJson.getPOJO(jsonData,CustCls.class);
			resp=ccs.insertCustCls(custCls);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
		
	}
	
	/*修改客户分类*/
	@RequestMapping("updateCustClsByClsId")
	public @ResponseBody Object updateCustClsByClsId(@RequestBody String jsonData) {
		
		String resp="";
		try {
			CustCls custCls=BaseJson.getPOJO(jsonData,CustCls.class);
			resp=ccs.updateCustClsByClsId(custCls);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*删除客户分类*/
	@RequestMapping("deleteCustClsByClsId")
	public @ResponseBody Object deleteCustClsByClsId(@RequestBody String jsonData) {
		
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String clsId = reqBody.get("clsId").asText();
			resp=ccs.deleteCustClsByClsId(clsId);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*按照客户分类编号查询客户分类*/
	@RequestMapping("selectCustClsByClsId")
	@ResponseBody
	public String selectInvtyClsByInvtyClsEncd(@RequestBody String jsonData){
		logger.info("url:purc/CustCls/selectCustClsByClsId");
		logger.info("请求参数："+jsonData);
		String resp="";
		try {
			resp=ccs.selectCustClsByClsId(BaseJson.getReqBody(jsonData).get("clsId").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*查询所有客户分类*/
	@RequestMapping("selectCustCls")
	@ResponseBody
	public Object selectCustCls(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		List<Map> custClsList = ccs.selectCustCls();
		if(custClsList!=null) {
			 resp=BaseJson.returnRespObjList("purc/CustCls/selectCustCls",true,"处理成功！", null, custClsList);
		}else {
			 resp=BaseJson.returnRespObjList("purc/CustCls/selectCustCls",false,"处理失败！", null, custClsList);
		}
		return resp;
	}
	
	//导入客户分类
	@RequestMapping("uploadCustClsFile")
	@ResponseBody
	public Object uploadCustClsFile(HttpServletRequest request) throws IOException{
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
			    //return pos.uploadFileAddDb(file);
			    return ccs.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/CustCls/uploadCustClsFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/CustCls/uploadCustClsFile", false, "导入文件格式有误，无法导入！", null);

		}
	}


}
