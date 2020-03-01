package com.px.mis.account.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.CurmthAvgUprcTab;
import com.px.mis.account.service.CurmthAvgUprcTabService;
import com.px.mis.account.utils.ExcelUtils;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//����ƽ�����۱�ӿ�
@RequestMapping(value="account/curmthAvgUprcTab",method=RequestMethod.POST)
@Controller
public class CurmthAvgUprcTabController {
	private Logger logger = LoggerFactory.getLogger(CurmthAvgUprcTabController.class);
	@Autowired
	private CurmthAvgUprcTabService curmthAvgUprcTabService;
	/*���*/
	@RequestMapping("insertCurmthAvgUprcTab")
	public @ResponseBody Object insertCurmthAvgUprcTab(@RequestBody String jsonData) {
		logger.info("url:/account/curmthAvgUprcTab/insertCurmthAvgUprcTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		CurmthAvgUprcTab curmthAvgUprcTab=null;
		try {
			curmthAvgUprcTab = BaseJson.getPOJO(jsonData,CurmthAvgUprcTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/insertCurmthAvgUprcTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/insertCurmthAvgUprcTab", false, "���������������������������Ƿ���ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthAvgUprcTab/insertCurmthAvgUprcTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = curmthAvgUprcTabService.insertCurmthAvgUprcTab(curmthAvgUprcTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/insertCurmthAvgUprcTab", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/insertCurmthAvgUprcTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/insertCurmthAvgUprcTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deleteCurmthAvgUprcTab")
	public @ResponseBody Object deleteCurmthAvgUprcTab(@RequestBody String jsonData) {
		logger.info("url:/account/curmthAvgUprcTab/deleteCurmthAvgUprcTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/deleteCurmthAvgUprcTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/deleteCurmthAvgUprcTab", false, "���������������������������Ƿ���ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthAvgUprcTab/deleteCurmthAvgUprcTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = curmthAvgUprcTabService.deleteCurmthAvgUprcTabByOrdrNum(ordrNum);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/deleteCurmthAvgUprcTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/deleteCurmthAvgUprcTab", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/deleteCurmthAvgUprcTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updateCurmthAvgUprcTab")
	public @ResponseBody Object updateCurmthAvgUprcTab(@RequestBody String jsonData) {
		logger.info("url:/account/curmthAvgUprcTab/updateCurmthAvgUprcTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		CurmthAvgUprcTab curmthAvgUprcTab=null;
		try {
			curmthAvgUprcTab = BaseJson.getPOJO(jsonData,CurmthAvgUprcTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/updateCurmthAvgUprcTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/updateCurmthAvgUprcTab", false, "���������������������������Ƿ���ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthAvgUprcTab/updateCurmthAvgUprcTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = curmthAvgUprcTabService.updateCurmthAvgUprcTabByOrdrNum(curmthAvgUprcTab);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/updateCurmthAvgUprcTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/curmthAvgUprcTab/updateCurmthAvgUprcTab", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/updateCurmthAvgUprcTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectCurmthAvgUprcTab")
	public @ResponseBody Object selectCurmthAvgUprcTab(@RequestBody String jsonBody,HttpServletRequest request) {
		logger.info("url:/account/curmthAvgUprcTab/selectCurmthAvgUprcTab");
		logger.info("���������"+jsonBody);
		String resp="";
		Map map=null;
		Integer pageNo=null;
		Integer pageSize=null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int)map.get("pageNo");
			pageSize = (int)map.get("pageSize");
			if(pageNo==0 || pageSize==0) {
				resp=BaseJson.returnRespList("/account/curmthAvgUprcTab/selectCurmthAvgUprcTab", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/selectCurmthAvgUprcTab �쳣˵����",e1);
			try {
				resp=TransformJson.returnRespList("/account/curmthAvgUprcTab/selectCurmthAvgUprcTab", false, "���������������������������Ƿ���ȷ����ҳ��ѯʧ�ܣ�",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthAvgUprcTab/selectCurmthAvgUprcTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=curmthAvgUprcTabService.selectCurmthAvgUprcTabList(map);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/selectCurmthAvgUprcTab �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectCurmthAvgUprcTabById")
	public @ResponseBody Object selectCurmthAvgUprcTabById(@RequestBody String jsonData) {
		logger.info("url:/account/curmthAvgUprcTab/selectCurmthAvgUprcTabById");
		logger.info("���������"+jsonData);
		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/curmthAvgUprcTab/selectCurmthAvgUprcTabById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/curmthAvgUprcTab/selectCurmthAvgUprcTabById",false,"���������������������������Ƿ���ȷ����ѯʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/curmthAvgUprcTab/selectCurmthAvgUprcTabById �쳣˵����",e);
			}
			return resp;
		}
		try {
			CurmthAvgUprcTab curmthAvgUprcTab = curmthAvgUprcTabService.selectCurmthAvgUprcTabByOrdrNum(ordrNum);
			if(curmthAvgUprcTab!=null) {
				 resp=BaseJson.returnRespObj("/account/curmthAvgUprcTab/selectCurmthAvgUprcTabById",true,"����ɹ���", curmthAvgUprcTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/curmthAvgUprcTab/selectCurmthAvgUprcTabById",false,"û�в鵽���ݣ�", curmthAvgUprcTab);
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			logger.error("url:/account/curmthAvgUprcTab/selectCurmthAvgUprcTabById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	

	/*����*/
	@RequestMapping(value="exportExcel")
	public @ResponseBody void exportExcel(HttpServletRequest request, HttpServletResponse response) {
		logger.info("url:/account/curmthAvgUprcTab/exportExcel");
		
		ExcelUtils excelUtils = new ExcelUtils();
		
		String[] excelHeader = {"���","�����","�����","�������","����","���","ƽ������","�������"};
	
	    String[] ds_titles = {"ordrNum","acctYr","acctiMth","invtyEncd","qty","amt","avgUprc","invtyNm"};
	    
	    try{
	    	 List<Map<String, String>> excelList = new ArrayList<Map<String, String>>();
	  	   
	 	    Map<String,String> map = new HashMap<String,String>();
	 	    
	 	    List<CurmthAvgUprcTab>  list = curmthAvgUprcTabService.getCurmthAvgUprcList(map);
	 	    	 	  
	 	   
		    for(CurmthAvgUprcTab c:list) {
		    	Map<String,String> map1 = new HashMap<String,String>();
		    	map1.put(ds_titles[0],String.valueOf(c.getOrdrNum()));
		    	map1.put(ds_titles[1], c.getAcctYr());
		    	map1.put(ds_titles[2], c.getAcctiMth());
		    	map1.put(ds_titles[3], c.getInvtyEncd());
		    	map1.put(ds_titles[4],String.valueOf(c.getQty()) );
		    	map1.put(ds_titles[5], String.valueOf(c.getAmt()));
		    	map1.put(ds_titles[6],String.valueOf(c.getAvgUprc()));
		    	map1.put(ds_titles[7],String.valueOf(c.getInvtydoc().getInvtyNm()));
		    	excelList.add(map1);
		    }  
			  try {
					excelUtils.fileUploadExcel("����ƽ�����۱�", excelHeader, ds_titles, excelList, response);
				} catch (IOException e) {	
					e.printStackTrace();
					logger.error("url:/account/curmthAvgUprcTab/exportExcel �쳣˵����",e);
				}
	    }catch (Exception e) {
	    	logger.error("url:/account/curmthAvgUprcTab/exportExcel �쳣˵����",e);
		}  
	  
	}


	

	
	
	
	

}



