package com.px.mis.purc.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.DeptDoc;
import com.px.mis.purc.service.DeptDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*部门档案功能描述*/
@RequestMapping(value="purc/DeptDoc")
@Controller
public class DeptDocController {
	
	@Autowired
	private DeptDocService dds;
	
	private Logger logger = LoggerFactory.getLogger(DeptDocController.class);
	
	/*新增部门档案*/
	@RequestMapping("insertDeptDoc")
	public @ResponseBody Object insertDeptDoc(@RequestBody String jsonData) {
		String resp="";
		try {
			DeptDoc deptDoc=BaseJson.getPOJO(jsonData,DeptDoc.class);
			resp=dds.insertDeptDoc(deptDoc);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*修改部门档案*/
	@RequestMapping("updateDeptDocByDeptEncd")
	public @ResponseBody Object updateDeptDocByDeptEncd(@RequestBody String jsonBody) throws Exception {
		
		String resp="";
		List<DeptDoc> cList=new ArrayList<>();
		cList=BaseJson.getPOJOList(jsonBody, DeptDoc.class);
		resp =dds.updateDeptDocByDeptEncd(cList);
		return resp;
	}
	
	/*删除部门档案*/
	@RequestMapping("deleteDeptDocList")
	public @ResponseBody String deleteDeptDocList(@RequestBody String jsonBody) {
		String resp="";
		try {
			resp=dds.deleteDeptDocList(BaseJson.getReqBody(jsonBody).get("deptId").asText());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*按照编号查询部门档案*/
	@RequestMapping("selectDeptDocByDeptEncd")
	@ResponseBody
	public Object selectDeptDocByDeptEncd(@RequestBody String jsonData) throws IOException {
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String deptEncd = reqBody.get("deptId").asText();
		DeptDoc deptDoc=dds.selectDeptDocByDeptEncd(deptEncd);
		if(deptDoc!=null) {
			 resp=BaseJson.returnRespObj("purc/DeptDoc/selectDeptDocByDeptEncd",true,"查询成功！", deptDoc);
		}else {
			 resp=BaseJson.returnRespObj("purc/DeptDoc/selectDeptDocByDeptEncd",false,"查询失败！", deptDoc);
		}
		return resp;
	}
	
	//查询所有查询部门档案
	@RequestMapping("selectDeptDocList")
	@ResponseBody
	public Object selectDeptDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/DeptDoc/selectDeptDocList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=dds.selectDeptDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//打印及输入输出查询全部客户档案
	@RequestMapping("printingDeptDocList")
	@ResponseBody
	public Object printingDeptDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/DeptDoc/printingDeptDocList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=dds.printingDeptDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	

	//导入部门档案
	@RequestMapping("uploadDeptDocFile")
	@ResponseBody
	public Object uploadDeptDocFile(HttpServletRequest request) throws IOException{
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
			    return dds.uploadFileAddDb(file);
		    }
		    else {
		    	return BaseJson.returnRespObj("purc/DeptDoc/uploadDeptDocFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			return BaseJson.returnRespObj("purc/DeptDoc/uploadDeptDocFile", false,e.getMessage(), null);
		}
	}
}
