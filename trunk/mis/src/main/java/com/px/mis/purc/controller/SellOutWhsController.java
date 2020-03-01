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
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.SellOutWhsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*���۳��ⵥ��������*/
@RequestMapping(value="purc/SellOutWhs")
@Controller
public class SellOutWhsController {
	
	private Logger logger = LoggerFactory.getLogger(SellOutWhsController.class);
	
	@Autowired
	private SellOutWhsService sows;
    //����
	@Autowired
	FormBookService formBookService;
	
	//�������۳��ⵥ��Ϣ
	@RequestMapping(value="addSellOutWhs",method = RequestMethod.POST)
	@ResponseBody
	private String addPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/addSellOutWhs");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			
			SellOutWhs sellOutWhs = BaseJson.getPOJO(jsonBody, SellOutWhs.class);
			sellOutWhs.setSetupPers(userName);
			List<Map> mList=BaseJson.getList(jsonBody);
			List<SellOutWhsSub> sellOutWhsSubList=new ArrayList<>();
			for (Map map : mList) {
				SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();
				if(map.get("baoZhiQi")==null || map.get("baoZhiQi")=="") {
					sellOutWhsSub.setBaoZhiQi(0);
				}else {
					int baoZhiQi = Integer.parseInt(map.get("baoZhiQi").toString());
					sellOutWhsSub.setBaoZhiQi(baoZhiQi);
				}
				BeanUtils.populate(sellOutWhsSub, map);
				sellOutWhsSubList.add(sellOutWhsSub);
			}
			resp=sows.addSellOutWhs(userId, sellOutWhs, sellOutWhsSubList,loginTime);
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�޸����۳��ⵥ��Ϣ
	@RequestMapping(value="editSellOutWhs",method = RequestMethod.POST)
	@ResponseBody
	private String editPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/editSellOutWhs");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			SellOutWhs sellOutWhs = BaseJson.getPOJO(jsonBody, SellOutWhs.class);
			sellOutWhs.setMdfr(userName);
			List<Map> mList=BaseJson.getList(jsonBody);
			List<SellOutWhsSub> sellOutWhsSubList=new ArrayList<>();
			for (Map map : mList) {
				SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();
				int baoZhiQi;
				if(map.get("baoZhiQi").toString().equals("") || map.get("baoZhiQi").toString()==null ) {
					baoZhiQi = 0;
				}else {
					baoZhiQi = Integer.parseInt(map.get("baoZhiQi").toString());
				}
				sellOutWhsSub.setBaoZhiQi(baoZhiQi);
				BeanUtils.populate(sellOutWhsSub, map);
				sellOutWhsSubList.add(sellOutWhsSub);
			}
			resp=sows.editSellOutWhs(sellOutWhs, sellOutWhsSubList);
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//ɾ�����۳��ⵥ��Ϣ
	@RequestMapping(value="deleteSellOutWhs",method = RequestMethod.POST)
	@ResponseBody
	private String deletePursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/deleteSellOutWhs");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=sows.deleteSellOutWhs(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//��ѯ���۳��ⵥ��Ϣ
	@RequestMapping(value="querySellOutWhs",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhs");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=sows.querySellOutWhs(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="querySellOutWhsList",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrList(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhsList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			
			//�ֿ�Ȩ�޿���
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/SellOutWhs/querySellOutWhsList", false, "�û�û�иòֿ�Ȩ��" , null);
			}else {
			    map.put("whsId", whsId);
			    resp=sows.querySellOutWhsList(map);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�������۳��ⵥ��Ϣɾ��
	@RequestMapping(value="deleteSellOutWhsList",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteSellOutWhsList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/SellOutWhs/deleteSellOutWhsList");
		logger.info("���������"+jsonBody);
		String resp="";
		resp=sows.deleteSellOutWhsList(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
	    logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�����������۳��ⵥ���״̬
	@RequestMapping(value="updateSellOutWhsIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updateSellOutWhsIsNtChk(@RequestBody String jsonBody) throws Exception {	
		
//		logger.info("url:purc/SellOutWhs/updateSellOutWhsIsNtChk");
//		logger.info("���������"+jsonBody);
		Map<String,Object> result = new HashMap<>();
		String resp="";
		boolean isSuccess = false;
		List<SellOutWhs> SellOutWhsList=new ArrayList();
		try
		{
			String userId=BaseJson.getReqHead(jsonBody).get("accNum").asText();
			String userName=BaseJson.getReqHead(jsonBody).get("userName").asText();
			String loginTime=BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/SellOutWhs/updateSellOutWhsIsNtChk", false, "�����ѷ��ˣ�", null);
				logger.info("���ز�����" + resp);
				return resp;
			}
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//		    JSONArray jsonArray = jsonObjectList.getJSONArray("list");
//		    SellOutWhsList= jsonArray.toJavaList(SellOutWhs.class);	
			SellOutWhsList=BaseJson.getPOJOList(jsonBody, SellOutWhs.class);
		    for(SellOutWhs sellOutWhs :SellOutWhsList) {
				try
				{
					sellOutWhs.setChkr(userName);
					result=sows.updateSellOutWhsIsNtChk(sellOutWhs);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}
		    resp=BaseJson.returnRespObj("purc/SellOutWhs/updateSellOutWhsIsNtChk", isSuccess, resp, null);
//			logger.info("���ز�����"+resp);
		}catch (Exception ex) {
//			logger.info("���ز�����"+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/SellOutWhs/updateSellOutWhsIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}
	
	//��ӡ�����������ѯȫ�����۳��ⵥ
	@RequestMapping("printingSellOutWhsList")
	@ResponseBody
	public Object printingSellOutWhsList(@RequestBody String jsonBody){
		
		logger.info("url:purc/SellOutWhs/printingSellOutWhsList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			
			//�ֿ�Ȩ�޿���
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/SellOutWhs/printingSellOutWhsList", false, "�û�û�иòֿ�Ȩ��" , null);
			}else {
			    map.put("whsId", whsId);
			    resp=sows.printingSellOutWhsList(map);
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//������ϸ��
	@RequestMapping(value="querySellOutWhsByInvtyEncd",method = RequestMethod.POST)
	@ResponseBody
	private String querySellOutWhsByInvtyEncd(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhsByInvtyEncd");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
				int pageNo=(int)map.get("pageNo");
				int pageSize=(int)map.get("pageSize");
				map.put("index", (pageNo-1)*pageSize);
				map.put("num", pageSize);
			}
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			resp=sows.querySellOutWhsByInvtyEncd(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	//������ϸ��--����
	@RequestMapping(value="querySellOutWhsByInvtyEncdPrint",method = RequestMethod.POST)
	@ResponseBody
	private String querySellOutWhsByInvtyEncdPrint(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhsByInvtyEncdPrint");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			resp=sows.querySellOutWhsByInvtyEncdPrint(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�������۳��ⵥ
	@RequestMapping("uploadSellOutWhsFile")
	@ResponseBody
	public Object uploadSellOutWhsFile(HttpServletRequest request) throws IOException{
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
			    return sows.uploadFileAddDb(file,0);
		    }else {
		    	return BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFile", false, e.getMessage(), null);
		}
	}
	//�������۳��ⵥ
	@RequestMapping("uploadSellOutWhsFileU8")
	@ResponseBody
	public Object uploadSellOutWhsFileU8(HttpServletRequest request) throws IOException{
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
			    return sows.uploadFileAddDb(file,1);
		    }else {
		    	return BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFileU8", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFileU8", false, e.getMessage(), null);
		}
	}
	//��ҳ+�������۳��ⵥ
	@RequestMapping(value="querySellOutWhsListOrderBy",method = RequestMethod.POST)
	@ResponseBody
	private String querySellOutWhsListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/SellOutWhs/querySellOutWhsListOrderBy");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String outWhsDt1 = (String) map.get("outWhsDt1");
			String outWhsDt2 = (String) map.get("outWhsDt2");
			if (outWhsDt1!=null && outWhsDt1!="") {
				outWhsDt1 = outWhsDt1 + " 00:00:00";
			}
			if (outWhsDt2!=null  && outWhsDt2!="") {
				outWhsDt2 = outWhsDt2 + " 23:59:59";
			}
			map.put("outWhsDt1", outWhsDt1);
			map.put("outWhsDt2", outWhsDt2);
			
			//�ֿ�Ȩ�޿���
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/SellOutWhs/querySellOutWhsListOrderBy", false, "�û�û�иòֿ�Ȩ��" , null);
			}else {
			    map.put("whsId", whsId);
			    resp=sows.querySellOutWhsListOrderBy(map);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
}
