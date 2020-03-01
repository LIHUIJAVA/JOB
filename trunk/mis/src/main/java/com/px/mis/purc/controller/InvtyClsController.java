package com.px.mis.purc.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.InvtyCls;
import com.px.mis.purc.service.InvtyClsService;
import com.px.mis.util.BaseJson;
/*������๦������*/
@RequestMapping(value="purc/InvtyCls")
@Controller
public class InvtyClsController {
	
	@Autowired
	private InvtyClsService ics;
	
	/*�����������*/
	@RequestMapping("insertInvtyCls")
	public @ResponseBody Object insertInvtyCls(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			InvtyCls invtyCls=BaseJson.getPOJO(jsonData,InvtyCls.class);
			on=ics.insertInvtyCls(invtyCls);
			resp =BaseJson.returnRespObj("purc/InvtyCls/insertInvtyCls", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*�޸Ĵ������*/
	@RequestMapping("updateInvtyClsByInvtyClsEncd")
	public @ResponseBody Object updateInvtyClsByInvtyClsEncd(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			InvtyCls invtyCls=BaseJson.getPOJO(jsonData,InvtyCls.class);
			on=ics.updateInvtyClsByInvtyClsEncd(invtyCls);
			resp =BaseJson.returnRespObj("purc/InvtyCls/updateInvtyClsByInvtyClsEncd", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*ɾ���������*/
	@RequestMapping("deleteInvtyClsByInvtyClsEncd")
	public @ResponseBody Object deleteInvtyClsByInvtyClsEncd(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String invtyClsEncd = reqBody.get("invtyClsEncd").asText();
			on=ics.deleteInvtyClsByInvtyClsEncd(invtyClsEncd);
			resp =BaseJson.returnRespObj("purc/InvtyCls/deleteInvtyClsByInvtyClsEncd", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
		
	}
	
	/*�򵥲�ѯ�������*/
	@RequestMapping("selectInvtyClsByInvtyClsEncd")
	@ResponseBody
	public Object selectInvtyClsByInvtyClsEncd(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String invtyClsEncd = reqBody.get("invtyClsEncd").asText();
		InvtyCls invtyCls=ics.selectInvtyClsByInvtyClsEncd(invtyClsEncd);
		if(invtyCls!=null) {
			 resp=BaseJson.returnRespObj("purc/InvtyCls/selectInvtyClsByInvtyClsEncd",true,"����ɹ���", invtyCls);
		}else {
			 resp=BaseJson.returnRespObj("purc/InvtyCls/selectInvtyClsByInvtyClsEncd",false,"����ʧ�ܣ�", invtyCls);
		}
		return resp;
	}
	
	/*��ѯ���д������*/
	@RequestMapping("selectInvtyCls")
	@ResponseBody
	public Object selectInvtyCls(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		List<Map> invtyClsList = ics.selectInvtyCls();
		if(invtyClsList!=null) {
			 resp=BaseJson.returnRespObjList("purc/InvtyCls/selectInvtyCls",true,"����ɹ���", null, invtyClsList);
		}else {
			 resp=BaseJson.returnRespObjList("purc/InvtyCls/selectInvtyCls",false,"����ʧ�ܣ�", null, invtyClsList);
		}
		return resp;
	}
	
	//����������
	@RequestMapping("uploadInvtyClsFile")
	@ResponseBody
	public Object uploadInvtyClsFile(HttpServletRequest request){
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
			    
			    return ics.uploadFileAddDb(file);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			try {
				return BaseJson.returnRespObj("purc/InvtyCls/uploadInvtyClsFile", false, "�����ļ���ʽ�����޷����룡", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
        return null;
	}

}
