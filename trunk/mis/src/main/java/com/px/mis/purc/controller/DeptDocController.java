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
/*���ŵ�����������*/
@RequestMapping(value="purc/DeptDoc")
@Controller
public class DeptDocController {
	
	@Autowired
	private DeptDocService dds;
	
	private Logger logger = LoggerFactory.getLogger(DeptDocController.class);
	
	/*�������ŵ���*/
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
	
	/*�޸Ĳ��ŵ���*/
	@RequestMapping("updateDeptDocByDeptEncd")
	public @ResponseBody Object updateDeptDocByDeptEncd(@RequestBody String jsonBody) throws Exception {
		
		String resp="";
		List<DeptDoc> cList=new ArrayList<>();
		cList=BaseJson.getPOJOList(jsonBody, DeptDoc.class);
		resp =dds.updateDeptDocByDeptEncd(cList);
		return resp;
	}
	
	/*ɾ�����ŵ���*/
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
	
	/*���ձ�Ų�ѯ���ŵ���*/
	@RequestMapping("selectDeptDocByDeptEncd")
	@ResponseBody
	public Object selectDeptDocByDeptEncd(@RequestBody String jsonData) throws IOException {
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String deptEncd = reqBody.get("deptId").asText();
		DeptDoc deptDoc=dds.selectDeptDocByDeptEncd(deptEncd);
		if(deptDoc!=null) {
			 resp=BaseJson.returnRespObj("purc/DeptDoc/selectDeptDocByDeptEncd",true,"��ѯ�ɹ���", deptDoc);
		}else {
			 resp=BaseJson.returnRespObj("purc/DeptDoc/selectDeptDocByDeptEncd",false,"��ѯʧ�ܣ�", deptDoc);
		}
		return resp;
	}
	
	//��ѯ���в�ѯ���ŵ���
	@RequestMapping("selectDeptDocList")
	@ResponseBody
	public Object selectDeptDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/DeptDoc/selectDeptDocList");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//��ӡ�����������ѯȫ���ͻ�����
	@RequestMapping("printingDeptDocList")
	@ResponseBody
	public Object printingDeptDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/DeptDoc/printingDeptDocList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=dds.printingDeptDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	

	//���벿�ŵ���
	@RequestMapping("uploadDeptDocFile")
	@ResponseBody
	public Object uploadDeptDocFile(HttpServletRequest request) throws IOException{
		try {
			//����һ��ͨ�õĶಿ�ֽ�����
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
		    if(multipartResolver.isMultipart(request)) {
		        //ת���ɶಿ��request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return ("��ѡ���ļ���");
			    }
			    return dds.uploadFileAddDb(file);
		    }
		    else {
		    	return BaseJson.returnRespObj("purc/DeptDoc/uploadDeptDocFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			return BaseJson.returnRespObj("purc/DeptDoc/uploadDeptDocFile", false,e.getMessage(), null);
		}
	}
}
