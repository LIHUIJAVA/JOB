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
/*�ͻ����๦������*/
@RequestMapping(value="purc/CustCls")
@Controller
public class CustClsController {
	
	private Logger logger = LoggerFactory.getLogger(SellSnglController.class);
	
	@Autowired
	private CustClsService ccs;
	
	/*�����ͻ�����*/
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
	
	/*�޸Ŀͻ�����*/
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
	
	/*ɾ���ͻ�����*/
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
	
	/*���տͻ������Ų�ѯ�ͻ�����*/
	@RequestMapping("selectCustClsByClsId")
	@ResponseBody
	public String selectInvtyClsByInvtyClsEncd(@RequestBody String jsonData){
		logger.info("url:purc/CustCls/selectCustClsByClsId");
		logger.info("���������"+jsonData);
		String resp="";
		try {
			resp=ccs.selectCustClsByClsId(BaseJson.getReqBody(jsonData).get("clsId").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*��ѯ���пͻ�����*/
	@RequestMapping("selectCustCls")
	@ResponseBody
	public Object selectCustCls(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		List<Map> custClsList = ccs.selectCustCls();
		if(custClsList!=null) {
			 resp=BaseJson.returnRespObjList("purc/CustCls/selectCustCls",true,"����ɹ���", null, custClsList);
		}else {
			 resp=BaseJson.returnRespObjList("purc/CustCls/selectCustCls",false,"����ʧ�ܣ�", null, custClsList);
		}
		return resp;
	}
	
	//����ͻ�����
	@RequestMapping("uploadCustClsFile")
	@ResponseBody
	public Object uploadCustClsFile(HttpServletRequest request) throws IOException{
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
			    //return pos.uploadFileAddDb(file);
			    return ccs.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/CustCls/uploadCustClsFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/CustCls/uploadCustClsFile", false, "�����ļ���ʽ�����޷����룡", null);

		}
	}


}
