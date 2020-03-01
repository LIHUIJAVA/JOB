package com.px.mis.ec.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.RepaintManager;
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Count;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.AftermarketDao;
import com.px.mis.ec.dao.EcAccountDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.EcAccount;
import com.px.mis.ec.entity.EcAccountDiff;
import com.px.mis.ec.entity.EcAccountMapp;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.EcAccountService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.suning.api.entity.retailer.StorelocationGetRequest;
@Transactional
@Service
public class EcAccountServiceImpl extends poiTool implements EcAccountService {

	private Logger logger = LoggerFactory.getLogger(EcAccountServiceImpl.class);
	

	@Autowired
	private StoreRecordDao storeRecordDao;

	@Autowired
	private StoreSettingsDao storeSettingsDao;

	@Autowired
	private MisUserDao misUserDao;
	
	@Autowired
	private RefundOrderDao refundOrderDao;
	
	@Autowired
	private RefundOrdersDao refundOrdersDao;
	
	@Autowired
	private PlatOrderDao platOrderDao;

	
	@Autowired
	private SellSnglSubDao sellSnglSubDao;
	
	@Autowired
	private SellSnglDao sellSnglDao;
	
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private EcAccountDao ecAccountDao;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public String insert(List<EcAccount> ecAccounts) {
		// TODO Auto-generated method stub
		String resp = "";
		boolean isSuccess = true;
		String message ="";
		try {
			ecAccountDao.insert(ecAccounts);
			resp = BaseJson.returnRespObj("ec/ecAccount/insert", isSuccess, message, null);
		} catch (Exception e) {
			try {
				// TODO: handle exception
				resp = BaseJson.returnRespObj("ec/ecAccount/insert", false, "新增出现异常，请重试", null);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return resp;
	}
	@Override
	public String delete(String ids) {
		// TODO Auto-generated method stub
		String resp = "";
		boolean isSuccess = true;
		String message ="";
		try {
			List<String> idStrings = Arrays.asList(ids.split(","));
			List<String> newIds = new ArrayList<String>();
			for (int i = 0; i < idStrings.size(); i++) {
				EcAccount ecAccount = ecAccountDao.selectByBillNo(idStrings.get(i));
				if(ecAccount!=null) {
					if(ecAccount.getCheckResult()==0) {
						//当勾兑结果等于1时不能删除
						newIds.add(ecAccount.getBillNo());
					}
				}
			}
			if(newIds.size()>0) {
				ecAccountDao.delete(newIds);
			}
			message="删除完成，成功删除"+newIds.size()+"条";
			resp = BaseJson.returnRespObj("ec/ecAccount/delete", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				// TODO: handle exception
				resp = BaseJson.returnRespObj("ec/ecAccount/delete", false, "删除出现异常，请重试", null);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return resp;
	}

	@Override
	public String selectList(Map map) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			List<EcAccount> ecAccounts = ecAccountDao.selectList(map);
			int count = ecAccountDao.selectCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/ecAccount/selectList", true, "查询成功！", count, pageNo, pageSize, 0, 0,
					ecAccounts);
		} catch (IOException e) {
			logger.error("URL：ec/ecAccount/selectList 异常说明：", e);
		}
		return resp;
	}
	
	@Override
	public String update(EcAccount ecAccount) {
		// TODO Auto-generated method stub
		String resp = "";
		return resp;
	}
	@Override
	public String check(String userId, String ecOrderIds,String startDate,String endDate) {
		// TODO Auto-generated method stub
		String resp = "";
		int successCount = 0;
		List<String> ecOrderIds1 = Arrays.asList(ecOrderIds.split(","));
		String checkTime = sdf.format(new Date());
		try {
			for (int i = 0; i < ecOrderIds1.size(); i++) {
				List<EcAccount> ecAccounts = ecAccountDao.selectByEcOrderId(ecOrderIds1.get(i),startDate,endDate);
				if(ecAccounts.size()>0) {
					for (int j = 0; j < ecAccounts.size(); j++) {
						EcAccount ecAccount = ecAccounts.get(j);
						ecAccount.setCheckerId(userId);//设置勾兑人
						ecAccount.setCheckTime(checkTime);
						ecAccount.setCheckResult(1);
						ecAccountDao.update(ecAccount);
					}
					successCount++;
				}
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "勾兑完成,本次成功勾兑订单"+successCount+"条", null);
		} catch (Exception e) {
			try {
				// TODO: handle exception
				resp = BaseJson.returnRespObj("ec/ecAccount/insert", false, "勾兑"+successCount+"条后出现异常，请重试", null);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return resp;
	}
	@Override
	public String importAccount(MultipartFile file, String userId,String storeId) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		
		if(storeRecord!=null) {
			List<EcAccountMapp> mapplist = ecAccountDao.selectMapp(storeRecord.getEcId());
			if (mapplist.size()>0) {
				List<String> mapps = new ArrayList<String>();
				for (int i = 0; i < mapplist.size(); i++) {
					mapps.add(mapplist.get(i).getTypeName());
				}
				switch (storeRecord.getEcId()) {
				case "JD":
					//模板文件中费用发生时间、计费时间、结算时间需要先用text("yyyy-MM-dd HH:mm:ss")函数转一下格式，
					//店铺编号、账单日期字段需要用text("#")函数转换格式
					resp = jdImport(file, userId, storeId,mapps);
					break;
				case "TM":
					//模板文件中发生时间需要先转换成日期格式，再用text("yyyy-MM-dd HH:mm:ss")函数转一下格式，
					resp = tmImport(file, userId, storeId,mapps);
					break;
				case "SN":
					//模板文件中前后的无效字符行需要先删除
					resp = snImport(file, userId, storeId,mapps);
					break;
				case "PDD":
					//模板文件中发生时间需要用text("yyyy-MM-dd HH:mm:ss")函数转一下格式，
					resp = pddImport(file, userId, storeId,mapps);
					break;
				case "MaiDu":
					//模板文件中记账时间需要用text("yyyy-MM-dd HH:mm:ss")函数转一下格式，自带的`需要替换成',变成文本格式
					resp = maiDuImport(file, userId, storeId,mapps);
					break;
				case "XHS":
					//模板文件需要将“商品销售”sheet放到第一个sheet里，源文件不包含退货信息
					resp = XHSImport(file, userId, storeId, mapps);
					break;
				default:
					resp = BaseJson.returnRespObj("ec/ecAccount/importAccount", true, "所选店铺对应平台尚暂不支持对账单导入", null);
					break;
				}
			}else {
				resp = BaseJson.returnRespObj("ec/ecAccount/importAccount", true, "所选店铺对应平台尚未设置对账单勾兑项，请先设置", null);
			}
		}else {
			resp = BaseJson.returnRespObj("ec/ecAccount/importAccount", true, "所选店铺不存在,导入失败", null);
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/importAccount", true, "导入异常，请重试", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}
	/**
	 * 脉度小程序对账单导入
	 * @param file
	 * @param userId
	 * @param storeId
	 * @param mapps
	 * @return
	 */
	private String maiDuImport(MultipartFile file, String userId,String storeId,List<String> mapps) {
		String resp="";
		List<EcAccount> ecAccounts = new ArrayList<EcAccount>();
		int successCount=0;
		int j=0;
		try {
			MisUser misUser = misUserDao.select(userId);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			System.out.println(lastRowNum);
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				System.out.println(j);
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
					continue;
				}
				//不存在mapps中的类型不导入，否则不能确定唯一，避免重复导入
				if(!mapps.contains(GetCellData(r,"业务类型"))) {
					continue;
				}
				//当平台订单号为空时不导入
				if(StringUtils.isEmpty(GetCellData(r,"平台订单号"))) {
					continue;
				}
				String billNo = GetCellData(r,"微信支付业务单号");
				//单号已存在，不重复导入
				if(ecAccountDao.selectByBillNo(billNo)!=null) {
					continue;
				}
				
				String ecOrderid=GetCellData(r,"平台订单号");
				//创建实体类
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(billNo);
				
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"业务类型"));//单据类型用业务类型
				ecAccount.setGoodNo("");//没有商品编号
				ecAccount.setShOrderId(GetCellData(r,"资金流水单号"));//商户订单号用资金流水单号
				ecAccount.setGoodName("");
				ecAccount.setStartTime(GetCellData(r,"记账时间"));
				ecAccount.setJifeiTime(GetCellData(r,"记账时间"));//计费时间用发生时间
				ecAccount.setJiesuanTime(GetCellData(r,"记账时间"));//结算时间用发生时间
				ecAccount.setCostType(GetCellData(r,"业务类型"));//费用项用业务类型
				
				if (GetCellData(r, "业务类型").equals("交易")) {
					//当业务类型是交易时，金额属于收入，正数
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "收支金额(元)"), 2));
					ecAccount.setMoneyType(0);
					ecAccount.setDirection(0);
					ecAccount.setDirection(0);
				}else {
					//当业务类型不是交易时，金额属于支出，负数
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "收支金额(元)"), 2).negate());
					ecAccount.setMoneyType(1);
					ecAccount.setDirection(1);
					ecAccount.setDirection(1);
				}
				ecAccount.setIsCheckType(1);
				ecAccount.setMemo(GetCellData(r,"备注"));
				ecAccount.setShopId("");//没有店铺号
				
				ecAccount.setCostDate(GetCellData(r,"记账时间"));//账单日期用收支时间
				ecAccount.setCreator(misUser.getUserName());//创建人
				ecAccount.setCreateTime(time);//创建时间
				ecAccount.setCheckResult(0);//设置勾兑结果为未勾兑
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//当list大小等于1000时执行插入
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//插入完成后清空list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//循环完毕后执行插入
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "导入完成，本次成功导入对账单"+successCount+"条", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "文件的第"+j+"行导入格式有误，无法导入!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	
	/**
	 * 拼多多对账单导入
	 * @param file
	 * @param userId
	 * @param storeId
	 * @param mapps
	 * @return
	 */
	private String pddImport(MultipartFile file, String userId,String storeId,List<String> mapps) {
		String resp="";
		List<EcAccount> ecAccounts = new ArrayList<EcAccount>();
		int successCount=0;
		int j=0;
		try {
			MisUser misUser = misUserDao.select(userId);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			System.out.println(lastRowNum);
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
					continue;
				}
				String billNo="";
				billNo = GetCellData(r,"商户订单号")+GetCellData(r,"收入金额（+元）")+GetCellData(r,"支出金额（-元）");
				//商户订单号为空且类型不是"其他"不导入
				if(StringUtils.isEmpty(GetCellData(r,"商户订单号"))) {
					if(!GetCellData(r,"账务类型").equals("其他")) {
						continue;
					}else if(!GetCellData(r,"备注").contains("商家小额打款")) {
						//当类型为其他，但不包含商家小额打款时不导入
						continue;
					}else {
						billNo=GetCellData(r,"备注").substring(GetCellData(r,"备注").indexOf("(")+1,GetCellData(r,"备注").indexOf(")"))
								+GetCellData(r,"收入金额（+元）")+GetCellData(r,"支出金额（-元）");
					}
					
				}
				//单号已存在，不重复导入
				if(ecAccountDao.selectByBillNo(billNo)!=null) {
					continue;
				}
				//创建实体类
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(billNo);
				String ecOrderid="";
				if(GetCellData(r,"账务类型").equals("其他")) {
					ecOrderid = GetCellData(r,"备注").substring(GetCellData(r,"备注").indexOf("(")+1,GetCellData(r,"备注").indexOf(")"));
				}else {
					ecOrderid = GetCellData(r,"商户订单号");
				}
				
				
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//根据ecOrderId查询订单是否存在
					//不存在时不导入对账单
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"账务类型"));//单据类型用账务类型
				ecAccount.setGoodNo("");//没有商品编号
				ecAccount.setShOrderId(ecOrderid);
				ecAccount.setGoodName("");
				ecAccount.setStartTime(GetCellData(r,"发生时间"));
				ecAccount.setJifeiTime(GetCellData(r,"发生时间"));//计费时间用发生时间
				ecAccount.setJiesuanTime(GetCellData(r,"发生时间"));//结算时间用发生时间
				ecAccount.setCostType(GetCellData(r,"账务类型"));//费用项用账务类型
				
				if (GetBigDecimal(GetCellData(r, "收入金额（+元）"), 2).compareTo(BigDecimal.ZERO)==0) {
					//收入金额等于0时，取支出金额
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "支出金额（-元）"), 2));
					ecAccount.setMoneyType(1);
					ecAccount.setDirection(1);
					ecAccount.setDirection(1);
				}else {
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "收入金额（+元）"), 2));
					ecAccount.setMoneyType(0);
					ecAccount.setDirection(0);
					ecAccount.setDirection(0);
				}
				ecAccount.setMemo(GetCellData(r,"备注"));
				if(mapps.contains(GetCellData(r,"账务类型").toString())) {
					ecAccount.setIsCheckType(1);
				}else if(GetCellData(r,"账务类型").equals("其他")) {
					ecAccount.setIsCheckType(1);
				}else {
					ecAccount.setIsCheckType(0);
				}
				ecAccount.setShopId("");//没有店铺号
				
				ecAccount.setCostDate(GetCellData(r,"发生时间"));//账单日期用收支时间
				ecAccount.setCreator(misUser.getUserName());//创建人
				ecAccount.setCreateTime(time);//创建时间
				ecAccount.setCheckResult(0);//设置勾兑结果为未勾兑
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//当list大小等于1000时执行插入
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//插入完成后清空list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//循环完毕后执行插入
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "导入完成，本次成功导入对账单"+successCount+"条", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "文件的第"+j+"行导入格式有误，无法导入!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	/**
	 * 苏宁对账单导入
	 * @param file
	 * @param userId
	 * @param storeId
	 * @param mapps
	 * @return
	 */
	private String snImport(MultipartFile file, String userId,String storeId,List<String> mapps) {
		String resp="";
		List<EcAccount> ecAccounts = new ArrayList<EcAccount>();
		int successCount=0;
		int j=0;
		try {
			MisUser misUser = misUserDao.select(userId);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
				continue;
				}
				//单号已存在，不重复导入
				if(ecAccountDao.selectByBillNo(GetCellData(r,"账务流水号"))!=null) {
					continue;
				}
				//当订单号长度不等于11时不导入系统
				if(GetCellData(r,"订单号").length()!=11) {
					continue;
				}
				//当类型不等于支付或者转账时不导入
				if(!GetCellData(r,"类型").equals("支付")&&!GetCellData(r,"类型").equals("转账")) {
					continue;
				}
				//创建实体类
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(GetCellData(r,"账务流水号"));
				
				
				
				String ecOrderid = GetCellData(r,"订单号");
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//根据ecOrderId查询订单是否存在
					//不存在时不导入对账单
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"类型"));//单据类型用类型
				ecAccount.setGoodNo("");//没有商品编号
				ecAccount.setShOrderId(GetCellData(r,"商户订单ID"));
				ecAccount.setGoodName(GetCellData(r,"订单名称"));
				ecAccount.setStartTime(GetCellData(r,"订单创建时间"));
				ecAccount.setJifeiTime(GetCellData(r,"订单创建时间"));//计费时间用创建时间
				ecAccount.setJiesuanTime(GetCellData(r,"收支时间"));//结算时间用收支时间
				ecAccount.setCostType(GetCellData(r,"支付渠道"));//费用项用支付渠道
				
				if (GetBigDecimal(GetCellData(r, "收入金额(+)元"), 2).compareTo(BigDecimal.ZERO)==0) {
					//收入金额等于0时，取支出金额
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "支出金额(-)元"), 2));
					ecAccount.setMoneyType(1);
					ecAccount.setDirection(1);
					ecAccount.setDirection(1);
				}else {
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "收入金额(+)元"), 2));
					ecAccount.setMoneyType(0);
					ecAccount.setDirection(0);
					ecAccount.setDirection(0);
				}
				ecAccount.setMemo(GetCellData(r,"备注"));
				if(mapps.contains(GetCellData(r,"类型").toString())) {
					ecAccount.setIsCheckType(1);
				}else {
					ecAccount.setIsCheckType(0);
				}
				ecAccount.setShopId("");//没有店铺号
				
				ecAccount.setCostDate(GetCellData(r,"收支时间"));//账单日期用收支时间
				ecAccount.setCreator(misUser.getUserName());//创建人
				ecAccount.setCreateTime(time);//创建时间
				ecAccount.setCheckResult(0);//设置勾兑结果为未勾兑
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//当list大小等于1000时执行插入
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//插入完成后清空list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//循环完毕后执行插入
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "导入完成，本次成功导入对账单"+successCount+"条", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "文件的第"+j+"行导入格式有误，无法导入!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	
	/**
	 * 京东对账单导入
	 * @param file
	 * @param userId
	 * @param storeId
	 * @param mapps
	 * @return
	 */
	private String jdImport(MultipartFile file, String userId,String storeId,List<String> mapps) {
		String resp="";
		List<EcAccount> ecAccounts = new ArrayList<EcAccount>();
		int successCount=0;
		int j=0;
		try {
			MisUser misUser = misUserDao.select(userId);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
				continue;
				}
				//单号已存在，不重复导入
				if(ecAccountDao.selectByBillNo(GetCellData(r,"单据编号"))!=null) {
					continue;
				}
				//创建实体类
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(GetCellData(r,"单据编号"));
				
				
				String ecOrderid = GetCellData(r,"订单编号");
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//根据ecOrderId查询订单是否存在
					//不存在时不导入对账单
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"单据类型"));
				ecAccount.setGoodNo(GetCellData(r,"商品编号"));
				ecAccount.setShOrderId(GetCellData(r,"商户订单号"));
				ecAccount.setGoodName(GetCellData(r,"商品名称"));
				ecAccount.setStartTime(GetCellData(r,"费用发生时间"));
				ecAccount.setJifeiTime(GetCellData(r,"费用计费时间"));
				ecAccount.setJiesuanTime(GetCellData(r,"费用结算时间"));
				ecAccount.setCostType(GetCellData(r,"费用项"));
				
				ecAccount.setMoney(GetBigDecimal(GetCellData(r,"金额"),2));
				if(GetCellData(r,"商家应收/应付").equals("应付")) {
					ecAccount.setMoneyType(1);
				}else {
					ecAccount.setMoneyType(0);
				}
				if(GetCellData(r,"收支方向").equals("收入")) {
					ecAccount.setDirection(0);
				}else {
					ecAccount.setDirection(1);
				}
				ecAccount.setMemo(GetCellData(r,"钱包结算备注"));
				if(mapps.contains(GetCellData(r,"钱包结算备注").toString())) {
					ecAccount.setIsCheckType(1);
				}else {
					ecAccount.setIsCheckType(0);
				}
				ecAccount.setShopId(GetCellData(r,"店铺号"));
				
				String costDate = GetCellData(r,"对账日期");
				//System.out.println(costDate);
				if (StringUtils.isNotEmpty(costDate)) {
					ecAccount.setCostDate(costDate.substring(0, 4)+"-"+costDate.substring(4,6)+"-"+costDate.substring(6,8)+" 00:00:00");
				}
				//System.out.println(ecAccount.getCostDate());
				
				ecAccount.setCreator(misUser.getUserName());//创建人
				ecAccount.setCreateTime(time);//创建时间
				ecAccount.setCheckResult(0);//设置勾兑结果为未勾兑
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//当list大小等于1000时执行插入
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//插入完成后清空list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//循环完毕后执行插入
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "导入完成，本次成功导入对账单"+successCount+"条", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "文件的第"+j+"行导入格式有误，无法导入!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	/**
	 *	小红书对账单导入
	 * @param file
	 * @param userId
	 * @param storeId
	 * @param mapps
	 * @return
	 */
	private String XHSImport(MultipartFile file, String userId,String storeId,List<String> mapps) {
		String resp="";
		List<EcAccount> ecAccounts = new ArrayList<EcAccount>();
		int successCount=0;
		int j=0;
		try {
			MisUser misUser = misUserDao.select(userId);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
				continue;
				}
				//单号已存在，不重复导入  小红书账单主键用订单号+商品id做唯一
				if(ecAccountDao.selectByBillNo(GetCellData(r,"订单号")+GetCellData(r,"商品id"))!=null) {
					continue;
				}
				//创建实体类
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(GetCellData(r,"订单号")+GetCellData(r,"商品id"));
				
				
				String ecOrderid = GetCellData(r,"订单号");
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//根据ecOrderId查询订单是否存在
					//不存在时不导入对账单
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"订单类型"));
				ecAccount.setGoodNo(GetCellData(r,"商品id"));
				ecAccount.setShOrderId(GetCellData(r,"订单号"));
				ecAccount.setGoodName(GetCellData(r,"商品名称"));
				String costDate = GetCellData(r,"用户下单时间");
				ecAccount.setStartTime(costDate);
				ecAccount.setJifeiTime(costDate);
				ecAccount.setJiesuanTime(costDate);
				ecAccount.setCostType(GetCellData(r,"订单类型"));
				
				//金额用账单里面的（实付单价+加上小红书单个平台优惠金额）*数量
				ecAccount.setMoney((GetBigDecimal(GetCellData(r,"实付单价"),2).add(GetBigDecimal(GetCellData(r,"单个商品小红书优惠"),2)).multiply(GetBigDecimal(GetCellData(r,"商品数量"),2))));
				/*if(GetCellData(r,"商家应收/应付").equals("应付")) {
					ecAccount.setMoneyType(1);
				}else {*/
					ecAccount.setMoneyType(0);//应收类型
				//}
				//if(GetCellData(r,"收支方向").equals("收入")) {
					ecAccount.setDirection(0);
				/*
				 * }else { ecAccount.setDirection(1); }
				 */
				ecAccount.setMemo("");//无备注
				//if(mapps.contains(GetCellData(r,"钱包结算备注").toString())) {
					ecAccount.setIsCheckType(1);//设置都是勾兑项
				/*
				 * }else { ecAccount.setIsCheckType(0); }
				 */
				ecAccount.setShopId(GetCellData(r,"卖家ID"));
				ecAccount.setCostDate(costDate);
				
				ecAccount.setCreator(misUser.getUserName());//创建人
				ecAccount.setCreateTime(time);//创建时间
				ecAccount.setCheckResult(0);//设置勾兑结果为未勾兑
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//当list大小等于1000时执行插入
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//插入完成后清空list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//循环完毕后执行插入
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "导入完成，本次成功导入对账单"+successCount+"条", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "文件的第"+j+"行导入格式有误，无法导入!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	/**
	 * 天猫对账单导入
	 * @param file
	 * @param userId
	 * @param storeId
	 * @param mapps
	 * @return
	 */
	private String tmImport(MultipartFile file, String userId,String storeId,List<String> mapps) {
		String resp="";
		List<EcAccount> ecAccounts = new ArrayList<EcAccount>();
		int successCount=0;
		int j=0;
		try {
			MisUser misUser = misUserDao.select(userId);
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
				continue;
				}
				//单号已存在，不重复导入
				if(ecAccountDao.selectByBillNo(GetCellData(r,"账务流水号"))!=null) {
					continue;
				}
				//创建实体类
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(GetCellData(r,"账务流水号"));
				
				
				String ecOrderid = GetCellData(r,"商户订单号").toString().substring(5);
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//根据ecOrderId查询订单是否存在
					//不存在时不导入对账单
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"业务类型"));//单据类型用业务类型
				ecAccount.setGoodNo("");//天猫对账单没有商品编号
				ecAccount.setShOrderId(GetCellData(r,"商户订单号"));
				ecAccount.setGoodName(GetCellData(r,"商品名称"));
				ecAccount.setStartTime(GetCellData(r,"发生时间"));
				ecAccount.setJifeiTime(GetCellData(r,"发生时间"));//计费时间用发生时间
				ecAccount.setJiesuanTime(GetCellData(r,"发生时间"));//计费时间用发生时间
				ecAccount.setCostType(GetCellData(r,"业务类型"));//费用项用业务类型
				
				if (GetBigDecimal(GetCellData(r, "收入金额（+元）"), 2).compareTo(BigDecimal.ZERO)==0) {
					//收入金额等于0时，取支出金额
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "支出金额（-元）"), 2));
					ecAccount.setMoneyType(1);
					ecAccount.setDirection(1);
				}else {
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "收入金额（+元）"), 2));
					ecAccount.setMoneyType(0);
					ecAccount.setDirection(0);
				}
				
				ecAccount.setMemo(GetCellData(r,"备注"));
				if(mapps.contains(GetCellData(r,"业务类型").toString())) {
					ecAccount.setIsCheckType(1);
				}else {
					ecAccount.setIsCheckType(0);
				}
				if(GetCellData(r,"业务类型").toString().equals("其他")) {
					//当业务类型等于其他时，判断备注里面是否含"商家保证金理赔字样"存在时设置勾兑项1
					if(GetCellData(r,"备注").toString().contains("商家保证金理赔")) {
						ecAccount.setIsCheckType(1);
					}
				}
				if(ecAccount.getCostType().equals("交易退款")) {
					//当类型为交易退款时
					//商户订单号的ecOrderId字段值可能与原始订单不匹配，需要寻找备注字段里面的订单号截取
					ecAccount.setEcOrderId(GetCellData(r,"备注").substring(GetCellData(r,"备注").indexOf("T200P")+5));
				}
				ecAccount.setShopId("");//店铺号
				
				ecAccount.setCostDate(GetCellData(r,"发生时间"));//账单日期用发生时间
				ecAccount.setCreator(misUser.getUserName());//创建人
				ecAccount.setCreateTime(time);//创建时间
				ecAccount.setCheckResult(0);//设置勾兑结果为未勾兑
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//当list大小等于1000时执行插入
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//插入完成后清空list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//循环完毕后执行插入
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "导入完成，本次成功导入对账单"+successCount+"条", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "文件的第"+j+"行导入格式有误，无法导入!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	
	
	
	
	
	
	/**
	 * 对账单下载
	 */
	@Override
	public String download(String userId, String storeId,String startDate,String endDate) {
		// TODO Auto-generated method stub
		String resp="";
		int pageSize = 100;
		int pageNo=1;
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		switch (storeRecord.getEcId()) {
		case "JD":
			resp = JDdownload(storeSettings, userId, pageSize, pageNo, startDate, endDate);
			break;
		
		default:
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/download", true, storeRecord.getStoreName()+"目前不支持对账单的下载", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return resp;
	}
	
	private String JDdownload(StoreSettings storeSettings,String userId,int pageSize,int pageNo,String startDate,String endDate) {
		String resp="";
		String message="";
		try {
			List<EcAccount> ecAccounts = new ArrayList<EcAccount>();
			ObjectNode objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("startDate", startDate);
			objectNode.put("endDate", endDate);
			objectNode.put("type", 1);// 查询方式,示例【1:按账单日查询，2:按到账日查询】
			objectNode.put("pageNum", pageNo);
			if (StringUtils.isNotEmpty(storeSettings.getVenderId())) {
				objectNode.put("memberId", storeSettings.getVenderId());//商家ID
			} else {
				return resp = BaseJson.returnRespObj("ec/ecAccount/download", false, "店铺设置中对应店铺商家ID为空，请设置", null);
			}
			String json = objectNode.toString();
			String jdRespStr = ECHelper.getJD("jingdong.pop.ledger.bill.queryJdpayDetail", storeSettings, json);
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			// 判断下载是否出现错误，有错误时直接返回。
			if (jdRespJson.has("error_response")) {
				message = jdRespJson.get("error_response").get("zh_desc").asText();
				resp = BaseJson.returnRespObj("ec/platOrder/download", false, message, null);
				return resp;
			}
			if(jdRespJson.get("jingdong_pop_ledger_bill_queryJdpayDetail_responce").get("result").get("success").asText().equals("false")) {
				message = jdRespJson.get("jingdong_pop_ledger_bill_queryJdpayDetail_responce").get("result").get("message").asText();
				resp = BaseJson.returnRespObj("ec/platOrder/download", false, message, null);
				return resp;
			}
			ArrayNode datas = (ArrayNode)jdRespJson.get("jingdong_pop_ledger_bill_queryJdpayDetail_response").get("result").get("data");
			for (Iterator<JsonNode> orderInfoIterator = datas.iterator(); orderInfoIterator.hasNext();) {
				JsonNode orderInfo = orderInfoIterator.next();
				EcAccount ecAccount = new EcAccount();
				ecAccount.setStartTime(orderInfo.get("detailCreateDate").asText());
				ecAccount.setCostDate(orderInfo.get("billDate").asText());
				if(orderInfo.get("balanceType").asText().equals("IN")) {
					ecAccount.setMoneyType(0);//收入
					ecAccount.setDirection(0);
					ecAccount.setMoney(new BigDecimal(orderInfo.get("incomeAmount").asText()));//金额
				}else {
					ecAccount.setMoneyType(1);//支出
					ecAccount.setDirection(1);
					ecAccount.setMoney(new BigDecimal(orderInfo.get("expendAmount").asText()));//金额
				}
				ecAccount.setOrderType(orderInfo.get("detailDesc").asText());//交易类型 单据类型
				ecAccount.setShOrderId(orderInfo.get("outTradeNo").asText());//商户订单号
				ecAccount.setMemo(orderInfo.get("tradeDesc").asText());//交易备注
				ecAccount.setEcOrderId(orderInfo.get("tradeNo").asText());//交易订单号
				ecAccount.setBillNo(orderInfo.get("bizTradeNo").asText());//业务单号
				ecAccount.setStoreId(storeSettings.getStoreId());
				
				
				
				/*private String storeId;//店铺id
				private String storeName;//店铺名称
				private String billNo;//业务单号
				private String ecOrderId;//电商订单号
				private String orderType;//单据类型
				private String shOrderId;//商户订单号
				private String goodNo;//商品编号
				private String goodName;//商品名称
				private String startTime;//费用发生时间
				private String jifeiTime;//计费时间
				private String jiesuanTime;//结算时间
				private String costType;//费用项
				private BigDecimal money;//金额
				private Integer moneyType;//收支类型0收1支
				private String memo;//备注
				private String shopId;//店铺号
				private int direction;//收支方向0收入1支出
				private String costDate;//账单日期
				private int checkResult;//勾兑结果0未勾兑1已勾兑
				private String checkerId;//勾兑人id
				private String checkerName;//勾兑人
				private String  checkTime;//勾兑时间
				private int isCheckType;//是否勾兑项0否1是，为1时参与汇总统计'
				private String creator;//创建人
				private String createTime;//创建时间
*/				
			}
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			try {
				resp = BaseJson.returnRespObj("ec/platOrder/download", false, "下载时发生异常，请重试", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}
	@Override
	public String autoCheck(String checker, String checkTime) {
		// TODO Auto-generated method stub
		String resp="";
		try {
			ecAccountDao.autoCheck(checker, checkTime);
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/autoCheck", true, "自动勾兑完成", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/autoCheck", false, "自动勾兑异常", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return resp;
	}
	@Override
	public String goToCheck(Map map) {
		// TODO Auto-generated method stub
		String resp="";
		
		StoreRecord storeRecord = storeRecordDao.select(map.get("storeId").toString());
		map.put("storeName", storeRecord.getStoreName());
		List<EcAccountDiff> diffs = ecAccountDao.goToCheck(map);
		int count = ecAccountDao.goToCheckCount(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		try {
			resp = BaseJson.returnRespList("ec/ecAccount/goToCheck", true, "查询成功！", count, pageNo, pageSize, 0, 0,
					diffs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resp;
	}
	
	
	
}
