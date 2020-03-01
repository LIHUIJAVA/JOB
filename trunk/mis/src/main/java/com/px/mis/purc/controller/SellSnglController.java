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
/*���۵���������*/
@RequestMapping(value="purc/SellSngl")
@Controller
public class SellSnglController {
	
	private Logger logger = LoggerFactory.getLogger(SellSnglController.class);
	
	@Autowired
	private SellSnglService sss;
    //����
	@Autowired
	FormBookService formBookService;
	
	//�������۵���Ϣ
	@RequestMapping(value="addSellSngl",method = RequestMethod.POST)
	@ResponseBody
	private String addPursOrdr(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/SellSngl/addSellSngl");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�޸����۵���Ϣ
	@RequestMapping(value="editSellSngl",method = RequestMethod.POST)
	@ResponseBody
	private String editPursOrdr(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/SellSngl/editSellSngl");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//ɾ�����۵���Ϣ
	@RequestMapping(value="deleteSellSngl",method = RequestMethod.POST)
	@ResponseBody
	private String deletePursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/deleteSellSngl");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=sss.deleteSellSngl(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//��ѯ���۵���Ϣ
	@RequestMapping(value="querySellSngl",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSngl");
		logger.info("���������"+jsonBody);
		String resp="";
			try {
				resp=sss.querySellSngl(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="querySellSnglList",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrList(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSnglList");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�������۵���Ϣɾ��
	@RequestMapping(value="deleteSellSnglList",method = RequestMethod.POST)
	@ResponseBody
	public Object deletePursOrdrList(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/SellSngl/deleteSellSnglList");
		logger.info("���������"+jsonBody);
		String resp="";
		resp=sss.deleteSellSnglList(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText());
	    logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�����������۵����״̬
	@RequestMapping(value="updateSellSnglIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updateSellSnglIsNtChk(@RequestBody String jsonBody) throws Exception {	
		
		logger.info("url:purc/SellSngl/updateSellSnglIsNtChk");
		logger.info("���������"+jsonBody);
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
				resp = BaseJson.returnRespObj("purc/SellSngl/updateSellSnglIsNtChk", false, "�����ѷ��ˣ�", null);
				logger.info("���ز�����" + resp);
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
		    logger.info("���ز�����"+resp);
		}catch (Exception ex) {
			logger.info("���ز�����"+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/SellSngl/updateSellSnglIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}
	
	//������ϸ��
	@RequestMapping(value="querySellSnglByInvtyEncd",method = RequestMethod.POST)
	@ResponseBody
	private String querySellSnglByInvtyEncd(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSnglByInvtyEncd");
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	//����������ⵥ
	@RequestMapping("uploadSellSnglFile")
	@ResponseBody
	public Object uploadSellSnglFile(HttpServletRequest request) throws IOException{
		try {
			//����һ��ͨ�õĶಿ�ֽ�����
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
		    if(multipartResolver.isMultipart(request)) {
		        //ת���ɶಿ��request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return  ("��ѡ���ļ�!");
			    }
			    return sss.uploadFileAddDb(file,0);
		    }else {
		    	return  BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/SellSngl/uploadIntoWhsFile", false, e.getMessage(), null);
		}
	}
	//����������ⵥ
	@RequestMapping("uploadSellSnglFileU8")
	@ResponseBody
	public Object uploadSellSnglFileU8(HttpServletRequest request) throws IOException{
		try {
			//����һ��ͨ�õĶಿ�ֽ�����
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
		    if(multipartResolver.isMultipart(request)) {
		        //ת���ɶಿ��request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return  ("��ѡ���ļ�!");
			    }
			    return sss.uploadFileAddDb(file,1);
		    }else {
		    	return  BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFileU8", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFileU8", false, e.getMessage(), null);
		}
	}
	//��ӡ�����������ѯȫ�����۳��ⵥ
	@RequestMapping("printingSellSnglList")
	@ResponseBody
	public Object printingSellSnglList(@RequestBody String jsonBody){
		
		logger.info("url:purc/SellSngl/printingSellSnglList");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�������۳��ⵥ�������۵������š��������ڵ���Ϣ
	@RequestMapping(value="updateSellBySellOut",method = RequestMethod.POST)
	@ResponseBody
	private Object updateSellBySellOut(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/updateSellBySellOut");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp = sss.updateA(map);
		} catch (Exception e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//����ͳ�Ʊ�
	@RequestMapping(value="selectXSTJList",method = RequestMethod.POST)
	@ResponseBody
	private String selectXSTJList(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/selectXSTJList");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�ɹ��˻����ղɹ���ⵥʱ���ض���ɹ�����ӱ���Ϣ
	@RequestMapping("selectSellSnglSubByRtnblQty")
	@ResponseBody
	public Object selectSellSnglSubByRtnblQty(@RequestBody String jsonBody){
		logger.info("url:purc/SellSngl/selectSellSnglSubByRtnblQty");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=sss.selectSellSnglSubByRtnblQty(BaseJson.getReqBody(jsonBody).get("sellSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="querySellSnglLists",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrLists(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSnglLists");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	@RequestMapping(value="querySellSnglListOrderBy",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/SellSngl/querySellSnglListOrderBy");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	
}
