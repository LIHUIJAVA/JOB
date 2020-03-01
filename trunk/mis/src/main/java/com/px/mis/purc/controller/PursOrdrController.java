package com.px.mis.purc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.entity.PursOrdrSub;
import com.px.mis.purc.service.PursOrdrService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

/*采购订单功能描述*/
@RequestMapping(value = "purc/PursOrdr")
@Controller
public class PursOrdrController {

	private Logger logger = LoggerFactory.getLogger(PursOrdrController.class);

	@Autowired
	private PursOrdrService pos;
	// 记账
	@Autowired
	FormBookService formBookService;
//	private MisUserServiceImpl misUserServiceImpl;

	// 新增采购订单信息
	@RequestMapping(value = "addPursOrdr", method = RequestMethod.POST)
	@ResponseBody
	private String addPursOrdr(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/PursOrdr/addPursOrdr");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		
		try {
			String userId="";
			String loginTime="";
			PursOrdr pursOrdr=null;
			List<PursOrdrSub> pursOrdrSubList=null;
			try {
				ObjectNode aString = BaseJson.getReqHead(jsonBody);
				userId = aString.get("accNum").asText();
				String userName = aString.get("userName").asText();
				loginTime = aString.get("loginTime").asText();

				pursOrdr = BaseJson.getPOJO(jsonBody, PursOrdr.class);
				pursOrdrSubList = BaseJson.getPOJOList(jsonBody, PursOrdrSub.class);
				pursOrdr.setSetupPers(userName);
			} catch (Exception e) {
				resp = BaseJson.returnRespObj("purc/PursOrdr/addPursOrdr", false,"数据格式有误,请检查录入数据!", null);
			}
			resp = pos.addPursOrdr(userId, pursOrdr, pursOrdrSubList, loginTime);
		} catch (Exception e) {
			resp = BaseJson.returnRespObj("purc/PursOrdr/addPursOrdr", false, e.getMessage(), null);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 修改采购订单信息
	@RequestMapping(value = "editPursOrdr", method = RequestMethod.POST)
	@ResponseBody
	private String editPursOrdr(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/PursOrdr/editPursOrdr");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			ObjectNode aString = BaseJson.getReqHead(jsonBody);
			String userName = aString.get("userName").asText();
			PursOrdr pursOrdr = BaseJson.getPOJO(jsonBody, PursOrdr.class);
			pursOrdr.setMdfr(userName);

			List<Map> mList = BaseJson.getList(jsonBody);
			List<PursOrdrSub> pursOrdrSubList = new ArrayList<>();
			for (Map map : mList) {
				PursOrdrSub pursOrdrSub = new PursOrdrSub();
				pursOrdrSub.setPursOrdrId(pursOrdr.getPursOrdrId());
				BeanUtils.populate(pursOrdrSub, map);
				pursOrdrSubList.add(pursOrdrSub);
			}
			resp = pos.editPursOrdr(pursOrdr, pursOrdrSubList);
		} catch (Exception e) {
			resp = BaseJson.returnRespObj("purc/PursOrdr/editPursOrdr", false, e.getMessage(), null);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 删除采购订单信息
	@RequestMapping(value = "deletePursOrdr", method = RequestMethod.POST)
	@ResponseBody
	private String deletePursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/deletePursOrdr");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = pos.deletePursOrdr(BaseJson.getReqBody(jsonBody).get("pursOrdrId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 在采购订单列表中点击某一条订单查询订单信息
	@RequestMapping(value = "queryPursOrdr", method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/queryPursOrdr");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = pos.queryPursOrdr(BaseJson.getReqBody(jsonBody).get("pursOrdrId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 采购订单列表页中条件查询
	@RequestMapping(value = "queryPursOrdrList", method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrList(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/queryPursOrdrList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String pursOrdrDt1 = (String) map.get("pursOrdrDt1");
			String pursOrdrDt2 = (String) map.get("pursOrdrDt2");
			if (pursOrdrDt1 != null && pursOrdrDt1 != "") {
				pursOrdrDt1 = pursOrdrDt1 + " 00:00:00";
			}
			if (pursOrdrDt2 != null && pursOrdrDt2 != "") {
				pursOrdrDt2 = pursOrdrDt2 + " 23:59:59";
			}
			map.put("pursOrdrDt1", pursOrdrDt1);
			map.put("pursOrdrDt2", pursOrdrDt2);

			resp = pos.queryPursOrdrList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 批量采购订单信息删除
	@RequestMapping(value = "deletePursOrdrList", method = RequestMethod.POST)
	@ResponseBody
	public Object deletePursOrdrList(@RequestBody String jsonBody) throws IOException {

		logger.info("url:purc/PursOrdr/deletePursOrdrList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = pos.deletePursOrdrList(BaseJson.getReqBody(jsonBody).get("pursOrdrId").asText());
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 批量更新采购订单审核状态
	@RequestMapping(value = "updatePursOrdrIsNtChk", method = RequestMethod.POST)
	@ResponseBody
	public Object updatePursOrdrIsNtChk(@RequestBody String jsonBody) throws Exception {

		logger.info("url:purc/PursOrdr/updatePursOrdrIsNtChk");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		List<Map> cTabMap = BaseJson.getList(jsonBody);
		List<PursOrdr> cList = new ArrayList();
		ObjectNode aString = BaseJson.getReqHead(jsonBody);
		String userName = aString.get("userName").asText();
		String loginTime = aString.get("loginTime").asText();
		if (formBookService.isMthSeal(loginTime)) {
			resp = BaseJson.returnRespObj("purc/PursOrdr/updatePursOrdrIsNtChk", false, "当月已封账！", null);
			logger.info("返回参数：" + resp);
			return resp;
		}
		for (Map map : cTabMap) {
			PursOrdr pursOrdr = new PursOrdr();
			if (map.get("pursOrdrId") == null || map.get("pursOrdrId").toString().length() == 0) {
				logger.info("请选择待审核单据。");
				cList.clear();
				return ("请选择待审核单据。");
			} else {
				map.put("chkr", userName);
				BeanUtils.populate(pursOrdr, map);
				cList.add(pursOrdr);
			}
		}
		resp = pos.updatePursOrdrIsNtChkList(cList);
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 打印及输入输出查询全部采购订单
	@RequestMapping("printingPursOrdrList")
	@ResponseBody
	public Object printingPursOrdrList(@RequestBody String jsonBody) {

		logger.info("url:purc/PursOrdr/printingPursOrdrList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String pursOrdrDt1 = (String) map.get("pursOrdrDt1");
			String pursOrdrDt2 = (String) map.get("pursOrdrDt2");
			if (pursOrdrDt1 != null && pursOrdrDt1 != "") {
				pursOrdrDt1 = pursOrdrDt1 + " 00:00:00";
			}
			if (pursOrdrDt2 != null && pursOrdrDt2 != "") {
				pursOrdrDt2 = pursOrdrDt2 + " 23:59:59";
			}
			map.put("pursOrdrDt1", pursOrdrDt1);
			map.put("pursOrdrDt2", pursOrdrDt2);
			resp = pos.printingPursOrdrList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 采购明细表
	@RequestMapping(value = "queryPursOrdrByInvtyEncd", method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrByInvtyEncd(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/queryPursOrdrByInvtyEncd");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
				int pageNo = (int) map.get("pageNo");
				int pageSize = (int) map.get("pageSize");
				map.put("index", (pageNo - 1) * pageSize);
				map.put("num", pageSize);
			}
			String bllgDt1 = (String) map.get("bllgDt1");
			String bllgDt2 = (String) map.get("bllgDt2");
			if (bllgDt1 != null && bllgDt1 != "") {
				bllgDt1 = bllgDt1 + " 00:00:00";
			}
			if (bllgDt2 != null && bllgDt2 != "") {
				bllgDt2 = bllgDt2 + " 23:59:59";
			}
			map.put("bllgDt1", bllgDt1);
			map.put("bllgDt2", bllgDt2);
			resp = pos.queryPursOrdrByInvtyEncd(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 采购明细表--导出
	@RequestMapping(value = "queryPursOrdrByInvtyEncdPrint", method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrByInvtyEncdPrint(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/queryPursOrdrByInvtyEncdPrint");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			String bllgDt1 = (String) map.get("bllgDt1");
			String bllgDt2 = (String) map.get("bllgDt2");
			if (bllgDt1 != null && bllgDt1 != "") {
				bllgDt1 = bllgDt1 + " 00:00:00";
			}
			if (bllgDt2 != null && bllgDt2 != "") {
				bllgDt2 = bllgDt2 + " 23:59:59";
			}
			map.put("bllgDt1", bllgDt1);
			map.put("bllgDt2", bllgDt2);
			resp = pos.queryPursOrdrByInvtyEncdPrint(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 导入采购订单
	@RequestMapping("uploadPursOrdrFile")
	@ResponseBody
	public Object uploadPursOrderFile(HttpServletRequest request) throws IOException {
		try {
			//创建一个通用的多部分解析器
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //判断 request 是否有文件上传,即多部分请求
		    if(multipartResolver.isMultipart(request)) {
		        //转换成多部分request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
		      	MultipartFile file = mRequest.getFile("file");
		      	
		    	String loginTime = mRequest.getHeader("loginTime");
		    	String accNum = mRequest.getHeader("accNum");
		    	HashMap<String,Object> params=new HashMap<String,Object>();
		    	params.put("loginTime", loginTime);
		    	params.put("accNum", accNum);
		    	
		    	System.out.println("参数:::::"+params);
		    	
		    	if(file == null) {
				   return BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFile", false, "请选择文件！", null);
			    }
			    return pos.uploadFileAddDb(file,0,params);
		    }else {
		    	return BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
				return BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFile", false, e.getMessage(), null);
		}
	}

	// 导入采购订单
	@RequestMapping("uploadPursOrdrFileU8")
	@ResponseBody
	public Object uploadPursOrderFileU8(HttpServletRequest request) throws IOException {
		try {
			// 创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// request强制转换注意
				MultipartFile file = mRequest.getFile("file");
				if (file == null) {
					return BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFileU8", false, "请选择文件！", null);
				}
				return pos.uploadFileAddDb(file, 1,null);
			} else {
				return BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFileU8", false, "请选择文件！", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFileU8", false, e.getMessage(), null);
		}
	}

	// 付款申请单参照采购订单时点击主表信息查询多个未申请付款子表明细
	@RequestMapping("selectUnApplPayQtyByPursOrdrId")
	@ResponseBody
	public Object selectUnApplPayQtyByPursOrdrId(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/selectUnApplPayQtyByPursOrdrId");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = pos.selectUnApplPayQtyByPursOrdrId(BaseJson.getReqBody(jsonBody).get("pursOrdrId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 到货单参照采购订单时点击主表信息查询多个未到货子表明细
	@RequestMapping("selectUnToGdsQtyByPursOrdrId")
	@ResponseBody
	public Object selectUnToGdsQtyByPursOrdrId(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/selectUnToGdsQtyByPursOrdrId");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = pos.selectUnToGdsQtyByPursOrdrId(BaseJson.getReqBody(jsonBody).get("pursOrdrId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 付款申请单参照时条件查询采购订单主表信息
	@RequestMapping(value = "queryPursOrdrLists", method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrLists(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/queryPursOrdrLists");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String pursOrdrDt1 = (String) map.get("pursOrdrDt1");
			String pursOrdrDt2 = (String) map.get("pursOrdrDt2");
			if (pursOrdrDt1 != null && pursOrdrDt1 != "") {
				pursOrdrDt1 = pursOrdrDt1 + " 00:00:00";
			}
			if (pursOrdrDt2 != null && pursOrdrDt2 != "") {
				pursOrdrDt2 = pursOrdrDt2 + " 23:59:59";
			}
			map.put("pursOrdrDt1", pursOrdrDt1);
			map.put("pursOrdrDt2", pursOrdrDt2);

			resp = pos.queryPursOrdrLists(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 分页+排序采购订单列表页中条件查询
	@RequestMapping(value = "queryPursOrdrListOrderBy", method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/PursOrdr/queryPursOrdrListOrderBy");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			String pursOrdrDt1 = (String) map.get("pursOrdrDt1");
			String pursOrdrDt2 = (String) map.get("pursOrdrDt2");
			if (pursOrdrDt1 != null && pursOrdrDt1 != "") {
				pursOrdrDt1 = pursOrdrDt1 + " 00:00:00";
			}
			if (pursOrdrDt2 != null && pursOrdrDt2 != "") {
				pursOrdrDt2 = pursOrdrDt2 + " 23:59:59";
			}
			map.put("pursOrdrDt1", pursOrdrDt1);
			map.put("pursOrdrDt2", pursOrdrDt2);

			resp = pos.queryPursOrdrListOrderBy(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

}
