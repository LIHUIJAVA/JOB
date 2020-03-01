package com.px.mis.purc.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.MeasrCorpDoc;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.MeasrCorpDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*������λ������������*/
@RequestMapping(value="purc/MeasrCorpDoc")

@Controller
public class MeasrCorpDocController {
	
	@Autowired
	private MeasrCorpDocService mcds;
	
	private Logger logger = LoggerFactory.getLogger(MeasrCorpDocController.class);
	
	/*����������λ����*/
	@RequestMapping("insertMeasrCorpDoc")
	public @ResponseBody Object insertMeasrCorpDoc(@RequestBody String jsonData) {
	
		ObjectNode on=null;
		String resp="";
		try {
			MeasrCorpDoc measrCorpDoc=BaseJson.getPOJO(jsonData,MeasrCorpDoc.class);
			on=mcds.insertMeasrCorpDoc(measrCorpDoc);
			resp =BaseJson.returnRespObj("purc/MeasrCorpDoc/insertMeasrCorpDoc", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*�޸ļ�����λ����*/
	@RequestMapping("updateMeasrCorpDocByMeasrCorpId")
	public @ResponseBody Object updateMeasrCorpDocByMeasrCorpId(@RequestBody String jsonBody) throws Exception{
	
		String resp="";
		List<Map> mList=BaseJson.getList(jsonBody);
		List<MeasrCorpDoc> cList=new ArrayList<>();
		cList=BaseJson.getPOJOList(jsonBody, MeasrCorpDoc.class);
		resp =mcds.updateMeasrCorpDocByMeasrCorpId(cList);
		return resp;
	}
	
	/*����ɾ��������λ����*/
	@RequestMapping("deleteMeasrCorpDocByMeasrCorpId")
	public @ResponseBody Object deleteMeasrCorpDocByMeasrCorpId(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String measrCorpId = reqBody.get("measrCorpId").asText();
			on=mcds.deleteMeasrCorpDocByMeasrCorpId(measrCorpId);
			resp =BaseJson.returnRespObj("purc/MeasrCorpDoc/deleteMeasrCorpDocByMeasrCorpId", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*ɾ��������λ����*/
	@RequestMapping("deleteMeasrCorpDocList")
	public @ResponseBody String deleteMeasrCorpDocList(@RequestBody String jsonBody) {
		String resp="";
		try {
			resp=mcds.deleteMeasrCorpDocList(BaseJson.getReqBody(jsonBody).get("measrCorpId").asText());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*���ձ����ѯ������λ����*/
	@RequestMapping("selectMeasrCorpDocByMeasrCorpId")
	@ResponseBody
	public Object selectMeasrCorpDocByMeasrCorpId(@RequestBody String jsonData) throws IOException {
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String measrCorpId = reqBody.get("measrCorpId").asText();
		MeasrCorpDoc measrCorpDoc=mcds.selectMeasrCorpDocByMeasrCorpId(measrCorpId);
		if(measrCorpDoc!=null) {
			 resp=BaseJson.returnRespObj("purc/MeasrCorpDoc/selectMeasrCorpDocByMeasrCorpId",true,"����ɹ���", measrCorpDoc);
		}else {
			 resp=BaseJson.returnRespObj("purc/MeasrCorpDoc/selectMeasrCorpDocByMeasrCorpId",false,"����ʧ�ܣ�", measrCorpDoc);
		}
		return resp;
	}
	
	//��ѯ���в�ѯ������λ����
	@RequestMapping("selectMeasrCorpDocList")
	@ResponseBody
	public Object selectMeasrCorpDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/MeasrCorpDoc/selectMeasrCorpDocList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=mcds.selectMeasrCorpDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
		
	}
	
	//�����շ����
	@RequestMapping("uploadMeasrCorpDocFile")
	@ResponseBody
	public Object uploadMeasrCorpDocFile(HttpServletRequest request) throws IOException{
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
			    return mcds.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/MeasrCorpDoc/uploadMeasrCorpDocFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/MeasrCorpDoc/uploadMeasrCorpDocFile", false, e.getMessage(), null);
		}
	}

	//��ѯ���в�ѯ������λ����
	@RequestMapping("printingMeasrCorpDocList")
	@ResponseBody
	public Object printingMeasrCorpDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/MeasrCorpDoc/printingMeasrCorpDocList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=mcds.printingMeasrCorpDocList();
		} catch (Exception e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
		
	}
}
