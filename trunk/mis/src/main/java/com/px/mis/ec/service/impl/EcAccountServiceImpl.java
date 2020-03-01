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
				resp = BaseJson.returnRespObj("ec/ecAccount/insert", false, "���������쳣��������", null);
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
						//�����ҽ������1ʱ����ɾ��
						newIds.add(ecAccount.getBillNo());
					}
				}
			}
			if(newIds.size()>0) {
				ecAccountDao.delete(newIds);
			}
			message="ɾ����ɣ��ɹ�ɾ��"+newIds.size()+"��";
			resp = BaseJson.returnRespObj("ec/ecAccount/delete", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				// TODO: handle exception
				resp = BaseJson.returnRespObj("ec/ecAccount/delete", false, "ɾ�������쳣��������", null);
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
			resp = BaseJson.returnRespList("ec/ecAccount/selectList", true, "��ѯ�ɹ���", count, pageNo, pageSize, 0, 0,
					ecAccounts);
		} catch (IOException e) {
			logger.error("URL��ec/ecAccount/selectList �쳣˵����", e);
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
						ecAccount.setCheckerId(userId);//���ù�����
						ecAccount.setCheckTime(checkTime);
						ecAccount.setCheckResult(1);
						ecAccountDao.update(ecAccount);
					}
					successCount++;
				}
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "�������,���γɹ����Ҷ���"+successCount+"��", null);
		} catch (Exception e) {
			try {
				// TODO: handle exception
				resp = BaseJson.returnRespObj("ec/ecAccount/insert", false, "����"+successCount+"��������쳣��������", null);
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
					//ģ���ļ��з��÷���ʱ�䡢�Ʒ�ʱ�䡢����ʱ����Ҫ����text("yyyy-MM-dd HH:mm:ss")����תһ�¸�ʽ��
					//���̱�š��˵������ֶ���Ҫ��text("#")����ת����ʽ
					resp = jdImport(file, userId, storeId,mapps);
					break;
				case "TM":
					//ģ���ļ��з���ʱ����Ҫ��ת�������ڸ�ʽ������text("yyyy-MM-dd HH:mm:ss")����תһ�¸�ʽ��
					resp = tmImport(file, userId, storeId,mapps);
					break;
				case "SN":
					//ģ���ļ���ǰ�����Ч�ַ�����Ҫ��ɾ��
					resp = snImport(file, userId, storeId,mapps);
					break;
				case "PDD":
					//ģ���ļ��з���ʱ����Ҫ��text("yyyy-MM-dd HH:mm:ss")����תһ�¸�ʽ��
					resp = pddImport(file, userId, storeId,mapps);
					break;
				case "MaiDu":
					//ģ���ļ��м���ʱ����Ҫ��text("yyyy-MM-dd HH:mm:ss")����תһ�¸�ʽ���Դ���`��Ҫ�滻��',����ı���ʽ
					resp = maiDuImport(file, userId, storeId,mapps);
					break;
				case "XHS":
					//ģ���ļ���Ҫ������Ʒ���ۡ�sheet�ŵ���һ��sheet�Դ�ļ��������˻���Ϣ
					resp = XHSImport(file, userId, storeId, mapps);
					break;
				default:
					resp = BaseJson.returnRespObj("ec/ecAccount/importAccount", true, "��ѡ���̶�Ӧƽ̨���ݲ�֧�ֶ��˵�����", null);
					break;
				}
			}else {
				resp = BaseJson.returnRespObj("ec/ecAccount/importAccount", true, "��ѡ���̶�Ӧƽ̨��δ���ö��˵��������������", null);
			}
		}else {
			resp = BaseJson.returnRespObj("ec/ecAccount/importAccount", true, "��ѡ���̲�����,����ʧ��", null);
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/importAccount", true, "�����쳣��������", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}
	/**
	 * ����С������˵�����
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
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			System.out.println(lastRowNum);
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				System.out.println(j);
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
					continue;
				}
				//������mapps�е����Ͳ����룬������ȷ��Ψһ�������ظ�����
				if(!mapps.contains(GetCellData(r,"ҵ������"))) {
					continue;
				}
				//��ƽ̨������Ϊ��ʱ������
				if(StringUtils.isEmpty(GetCellData(r,"ƽ̨������"))) {
					continue;
				}
				String billNo = GetCellData(r,"΢��֧��ҵ�񵥺�");
				//�����Ѵ��ڣ����ظ�����
				if(ecAccountDao.selectByBillNo(billNo)!=null) {
					continue;
				}
				
				String ecOrderid=GetCellData(r,"ƽ̨������");
				//����ʵ����
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(billNo);
				
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"ҵ������"));//����������ҵ������
				ecAccount.setGoodNo("");//û����Ʒ���
				ecAccount.setShOrderId(GetCellData(r,"�ʽ���ˮ����"));//�̻����������ʽ���ˮ����
				ecAccount.setGoodName("");
				ecAccount.setStartTime(GetCellData(r,"����ʱ��"));
				ecAccount.setJifeiTime(GetCellData(r,"����ʱ��"));//�Ʒ�ʱ���÷���ʱ��
				ecAccount.setJiesuanTime(GetCellData(r,"����ʱ��"));//����ʱ���÷���ʱ��
				ecAccount.setCostType(GetCellData(r,"ҵ������"));//��������ҵ������
				
				if (GetCellData(r, "ҵ������").equals("����")) {
					//��ҵ�������ǽ���ʱ������������룬����
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "��֧���(Ԫ)"), 2));
					ecAccount.setMoneyType(0);
					ecAccount.setDirection(0);
					ecAccount.setDirection(0);
				}else {
					//��ҵ�����Ͳ��ǽ���ʱ���������֧��������
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "��֧���(Ԫ)"), 2).negate());
					ecAccount.setMoneyType(1);
					ecAccount.setDirection(1);
					ecAccount.setDirection(1);
				}
				ecAccount.setIsCheckType(1);
				ecAccount.setMemo(GetCellData(r,"��ע"));
				ecAccount.setShopId("");//û�е��̺�
				
				ecAccount.setCostDate(GetCellData(r,"����ʱ��"));//�˵���������֧ʱ��
				ecAccount.setCreator(misUser.getUserName());//������
				ecAccount.setCreateTime(time);//����ʱ��
				ecAccount.setCheckResult(0);//���ù��ҽ��Ϊδ����
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//��list��С����1000ʱִ�в���
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//������ɺ����list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//ѭ����Ϻ�ִ�в���
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "������ɣ����γɹ�������˵�"+successCount+"��", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "�ļ��ĵ�"+j+"�е����ʽ�����޷�����!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	
	/**
	 * ƴ�����˵�����
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
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			System.out.println(lastRowNum);
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
					continue;
				}
				String billNo="";
				billNo = GetCellData(r,"�̻�������")+GetCellData(r,"�����+Ԫ��")+GetCellData(r,"֧����-Ԫ��");
				//�̻�������Ϊ�������Ͳ���"����"������
				if(StringUtils.isEmpty(GetCellData(r,"�̻�������"))) {
					if(!GetCellData(r,"��������").equals("����")) {
						continue;
					}else if(!GetCellData(r,"��ע").contains("�̼�С����")) {
						//������Ϊ���������������̼�С����ʱ������
						continue;
					}else {
						billNo=GetCellData(r,"��ע").substring(GetCellData(r,"��ע").indexOf("(")+1,GetCellData(r,"��ע").indexOf(")"))
								+GetCellData(r,"�����+Ԫ��")+GetCellData(r,"֧����-Ԫ��");
					}
					
				}
				//�����Ѵ��ڣ����ظ�����
				if(ecAccountDao.selectByBillNo(billNo)!=null) {
					continue;
				}
				//����ʵ����
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(billNo);
				String ecOrderid="";
				if(GetCellData(r,"��������").equals("����")) {
					ecOrderid = GetCellData(r,"��ע").substring(GetCellData(r,"��ע").indexOf("(")+1,GetCellData(r,"��ע").indexOf(")"));
				}else {
					ecOrderid = GetCellData(r,"�̻�������");
				}
				
				
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//����ecOrderId��ѯ�����Ƿ����
					//������ʱ��������˵�
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"��������"));//������������������
				ecAccount.setGoodNo("");//û����Ʒ���
				ecAccount.setShOrderId(ecOrderid);
				ecAccount.setGoodName("");
				ecAccount.setStartTime(GetCellData(r,"����ʱ��"));
				ecAccount.setJifeiTime(GetCellData(r,"����ʱ��"));//�Ʒ�ʱ���÷���ʱ��
				ecAccount.setJiesuanTime(GetCellData(r,"����ʱ��"));//����ʱ���÷���ʱ��
				ecAccount.setCostType(GetCellData(r,"��������"));//����������������
				
				if (GetBigDecimal(GetCellData(r, "�����+Ԫ��"), 2).compareTo(BigDecimal.ZERO)==0) {
					//���������0ʱ��ȡ֧�����
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "֧����-Ԫ��"), 2));
					ecAccount.setMoneyType(1);
					ecAccount.setDirection(1);
					ecAccount.setDirection(1);
				}else {
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "�����+Ԫ��"), 2));
					ecAccount.setMoneyType(0);
					ecAccount.setDirection(0);
					ecAccount.setDirection(0);
				}
				ecAccount.setMemo(GetCellData(r,"��ע"));
				if(mapps.contains(GetCellData(r,"��������").toString())) {
					ecAccount.setIsCheckType(1);
				}else if(GetCellData(r,"��������").equals("����")) {
					ecAccount.setIsCheckType(1);
				}else {
					ecAccount.setIsCheckType(0);
				}
				ecAccount.setShopId("");//û�е��̺�
				
				ecAccount.setCostDate(GetCellData(r,"����ʱ��"));//�˵���������֧ʱ��
				ecAccount.setCreator(misUser.getUserName());//������
				ecAccount.setCreateTime(time);//����ʱ��
				ecAccount.setCheckResult(0);//���ù��ҽ��Ϊδ����
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//��list��С����1000ʱִ�в���
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//������ɺ����list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//ѭ����Ϻ�ִ�в���
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "������ɣ����γɹ�������˵�"+successCount+"��", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "�ļ��ĵ�"+j+"�е����ʽ�����޷�����!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	/**
	 * �������˵�����
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
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
				continue;
				}
				//�����Ѵ��ڣ����ظ�����
				if(ecAccountDao.selectByBillNo(GetCellData(r,"������ˮ��"))!=null) {
					continue;
				}
				//�������ų��Ȳ�����11ʱ������ϵͳ
				if(GetCellData(r,"������").length()!=11) {
					continue;
				}
				//�����Ͳ�����֧������ת��ʱ������
				if(!GetCellData(r,"����").equals("֧��")&&!GetCellData(r,"����").equals("ת��")) {
					continue;
				}
				//����ʵ����
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(GetCellData(r,"������ˮ��"));
				
				
				
				String ecOrderid = GetCellData(r,"������");
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//����ecOrderId��ѯ�����Ƿ����
					//������ʱ��������˵�
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"����"));//��������������
				ecAccount.setGoodNo("");//û����Ʒ���
				ecAccount.setShOrderId(GetCellData(r,"�̻�����ID"));
				ecAccount.setGoodName(GetCellData(r,"��������"));
				ecAccount.setStartTime(GetCellData(r,"��������ʱ��"));
				ecAccount.setJifeiTime(GetCellData(r,"��������ʱ��"));//�Ʒ�ʱ���ô���ʱ��
				ecAccount.setJiesuanTime(GetCellData(r,"��֧ʱ��"));//����ʱ������֧ʱ��
				ecAccount.setCostType(GetCellData(r,"֧������"));//��������֧������
				
				if (GetBigDecimal(GetCellData(r, "������(+)Ԫ"), 2).compareTo(BigDecimal.ZERO)==0) {
					//���������0ʱ��ȡ֧�����
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "֧�����(-)Ԫ"), 2));
					ecAccount.setMoneyType(1);
					ecAccount.setDirection(1);
					ecAccount.setDirection(1);
				}else {
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "������(+)Ԫ"), 2));
					ecAccount.setMoneyType(0);
					ecAccount.setDirection(0);
					ecAccount.setDirection(0);
				}
				ecAccount.setMemo(GetCellData(r,"��ע"));
				if(mapps.contains(GetCellData(r,"����").toString())) {
					ecAccount.setIsCheckType(1);
				}else {
					ecAccount.setIsCheckType(0);
				}
				ecAccount.setShopId("");//û�е��̺�
				
				ecAccount.setCostDate(GetCellData(r,"��֧ʱ��"));//�˵���������֧ʱ��
				ecAccount.setCreator(misUser.getUserName());//������
				ecAccount.setCreateTime(time);//����ʱ��
				ecAccount.setCheckResult(0);//���ù��ҽ��Ϊδ����
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//��list��С����1000ʱִ�в���
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//������ɺ����list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//ѭ����Ϻ�ִ�в���
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "������ɣ����γɹ�������˵�"+successCount+"��", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "�ļ��ĵ�"+j+"�е����ʽ�����޷�����!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	
	/**
	 * �������˵�����
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
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
				continue;
				}
				//�����Ѵ��ڣ����ظ�����
				if(ecAccountDao.selectByBillNo(GetCellData(r,"���ݱ��"))!=null) {
					continue;
				}
				//����ʵ����
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(GetCellData(r,"���ݱ��"));
				
				
				String ecOrderid = GetCellData(r,"�������");
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//����ecOrderId��ѯ�����Ƿ����
					//������ʱ��������˵�
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"��������"));
				ecAccount.setGoodNo(GetCellData(r,"��Ʒ���"));
				ecAccount.setShOrderId(GetCellData(r,"�̻�������"));
				ecAccount.setGoodName(GetCellData(r,"��Ʒ����"));
				ecAccount.setStartTime(GetCellData(r,"���÷���ʱ��"));
				ecAccount.setJifeiTime(GetCellData(r,"���üƷ�ʱ��"));
				ecAccount.setJiesuanTime(GetCellData(r,"���ý���ʱ��"));
				ecAccount.setCostType(GetCellData(r,"������"));
				
				ecAccount.setMoney(GetBigDecimal(GetCellData(r,"���"),2));
				if(GetCellData(r,"�̼�Ӧ��/Ӧ��").equals("Ӧ��")) {
					ecAccount.setMoneyType(1);
				}else {
					ecAccount.setMoneyType(0);
				}
				if(GetCellData(r,"��֧����").equals("����")) {
					ecAccount.setDirection(0);
				}else {
					ecAccount.setDirection(1);
				}
				ecAccount.setMemo(GetCellData(r,"Ǯ�����㱸ע"));
				if(mapps.contains(GetCellData(r,"Ǯ�����㱸ע").toString())) {
					ecAccount.setIsCheckType(1);
				}else {
					ecAccount.setIsCheckType(0);
				}
				ecAccount.setShopId(GetCellData(r,"���̺�"));
				
				String costDate = GetCellData(r,"��������");
				//System.out.println(costDate);
				if (StringUtils.isNotEmpty(costDate)) {
					ecAccount.setCostDate(costDate.substring(0, 4)+"-"+costDate.substring(4,6)+"-"+costDate.substring(6,8)+" 00:00:00");
				}
				//System.out.println(ecAccount.getCostDate());
				
				ecAccount.setCreator(misUser.getUserName());//������
				ecAccount.setCreateTime(time);//����ʱ��
				ecAccount.setCheckResult(0);//���ù��ҽ��Ϊδ����
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//��list��С����1000ʱִ�в���
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//������ɺ����list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//ѭ����Ϻ�ִ�в���
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "������ɣ����γɹ�������˵�"+successCount+"��", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "�ļ��ĵ�"+j+"�е����ʽ�����޷�����!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	/**
	 *	С������˵�����
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
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
				continue;
				}
				//�����Ѵ��ڣ����ظ�����  С�����˵������ö�����+��Ʒid��Ψһ
				if(ecAccountDao.selectByBillNo(GetCellData(r,"������")+GetCellData(r,"��Ʒid"))!=null) {
					continue;
				}
				//����ʵ����
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(GetCellData(r,"������")+GetCellData(r,"��Ʒid"));
				
				
				String ecOrderid = GetCellData(r,"������");
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//����ecOrderId��ѯ�����Ƿ����
					//������ʱ��������˵�
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"��������"));
				ecAccount.setGoodNo(GetCellData(r,"��Ʒid"));
				ecAccount.setShOrderId(GetCellData(r,"������"));
				ecAccount.setGoodName(GetCellData(r,"��Ʒ����"));
				String costDate = GetCellData(r,"�û��µ�ʱ��");
				ecAccount.setStartTime(costDate);
				ecAccount.setJifeiTime(costDate);
				ecAccount.setJiesuanTime(costDate);
				ecAccount.setCostType(GetCellData(r,"��������"));
				
				//������˵�����ģ�ʵ������+����С���鵥��ƽ̨�Żݽ�*����
				ecAccount.setMoney((GetBigDecimal(GetCellData(r,"ʵ������"),2).add(GetBigDecimal(GetCellData(r,"������ƷС�����Ż�"),2)).multiply(GetBigDecimal(GetCellData(r,"��Ʒ����"),2))));
				/*if(GetCellData(r,"�̼�Ӧ��/Ӧ��").equals("Ӧ��")) {
					ecAccount.setMoneyType(1);
				}else {*/
					ecAccount.setMoneyType(0);//Ӧ������
				//}
				//if(GetCellData(r,"��֧����").equals("����")) {
					ecAccount.setDirection(0);
				/*
				 * }else { ecAccount.setDirection(1); }
				 */
				ecAccount.setMemo("");//�ޱ�ע
				//if(mapps.contains(GetCellData(r,"Ǯ�����㱸ע").toString())) {
					ecAccount.setIsCheckType(1);//���ö��ǹ�����
				/*
				 * }else { ecAccount.setIsCheckType(0); }
				 */
				ecAccount.setShopId(GetCellData(r,"����ID"));
				ecAccount.setCostDate(costDate);
				
				ecAccount.setCreator(misUser.getUserName());//������
				ecAccount.setCreateTime(time);//����ʱ��
				ecAccount.setCheckResult(0);//���ù��ҽ��Ϊδ����
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//��list��С����1000ʱִ�в���
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//������ɺ����list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//ѭ����Ϻ�ִ�в���
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "������ɣ����γɹ�������˵�"+successCount+"��", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "�ļ��ĵ�"+j+"�е����ʽ�����޷�����!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	/**
	 * ��è���˵�����
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
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
				continue;
				}
				//�����Ѵ��ڣ����ظ�����
				if(ecAccountDao.selectByBillNo(GetCellData(r,"������ˮ��"))!=null) {
					continue;
				}
				//����ʵ����
				EcAccount ecAccount=new EcAccount();
				ecAccount.setBillNo(GetCellData(r,"������ˮ��"));
				
				
				String ecOrderid = GetCellData(r,"�̻�������").toString().substring(5);
				/*if(platOrderDao.checkExsits1(ecOrderid)==0) {
					//����ecOrderId��ѯ�����Ƿ����
					//������ʱ��������˵�
					continue;
				}*/
				ecAccount.setEcOrderId(ecOrderid);
				ecAccount.setStoreId(storeId);
				ecAccount.setOrderType(GetCellData(r,"ҵ������"));//����������ҵ������
				ecAccount.setGoodNo("");//��è���˵�û����Ʒ���
				ecAccount.setShOrderId(GetCellData(r,"�̻�������"));
				ecAccount.setGoodName(GetCellData(r,"��Ʒ����"));
				ecAccount.setStartTime(GetCellData(r,"����ʱ��"));
				ecAccount.setJifeiTime(GetCellData(r,"����ʱ��"));//�Ʒ�ʱ���÷���ʱ��
				ecAccount.setJiesuanTime(GetCellData(r,"����ʱ��"));//�Ʒ�ʱ���÷���ʱ��
				ecAccount.setCostType(GetCellData(r,"ҵ������"));//��������ҵ������
				
				if (GetBigDecimal(GetCellData(r, "�����+Ԫ��"), 2).compareTo(BigDecimal.ZERO)==0) {
					//���������0ʱ��ȡ֧�����
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "֧����-Ԫ��"), 2));
					ecAccount.setMoneyType(1);
					ecAccount.setDirection(1);
				}else {
					ecAccount.setMoney(GetBigDecimal(GetCellData(r, "�����+Ԫ��"), 2));
					ecAccount.setMoneyType(0);
					ecAccount.setDirection(0);
				}
				
				ecAccount.setMemo(GetCellData(r,"��ע"));
				if(mapps.contains(GetCellData(r,"ҵ������").toString())) {
					ecAccount.setIsCheckType(1);
				}else {
					ecAccount.setIsCheckType(0);
				}
				if(GetCellData(r,"ҵ������").toString().equals("����")) {
					//��ҵ�����͵�������ʱ���жϱ�ע�����Ƿ�"�̼ұ�֤����������"����ʱ���ù�����1
					if(GetCellData(r,"��ע").toString().contains("�̼ұ�֤������")) {
						ecAccount.setIsCheckType(1);
					}
				}
				if(ecAccount.getCostType().equals("�����˿�")) {
					//������Ϊ�����˿�ʱ
					//�̻������ŵ�ecOrderId�ֶ�ֵ������ԭʼ������ƥ�䣬��ҪѰ�ұ�ע�ֶ�����Ķ����Ž�ȡ
					ecAccount.setEcOrderId(GetCellData(r,"��ע").substring(GetCellData(r,"��ע").indexOf("T200P")+5));
				}
				ecAccount.setShopId("");//���̺�
				
				ecAccount.setCostDate(GetCellData(r,"����ʱ��"));//�˵������÷���ʱ��
				ecAccount.setCreator(misUser.getUserName());//������
				ecAccount.setCreateTime(time);//����ʱ��
				ecAccount.setCheckResult(0);//���ù��ҽ��Ϊδ����
				ecAccounts.add(ecAccount);
				if(ecAccounts.size()==1000) {
					//��list��С����1000ʱִ�в���
					ecAccountDao.insert(ecAccounts);
					successCount=successCount+1000;
					//������ɺ����list
					ecAccounts.clear();
				}
			
			}
			fileIn.close();
			//ѭ����Ϻ�ִ�в���
			if(ecAccounts.size()>0) {
				ecAccountDao.insert(ecAccounts);
				successCount=successCount+ecAccounts.size();
			}
			resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "������ɣ����γɹ�������˵�"+successCount+"��", null);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/check", true, "�ļ��ĵ�"+j+"�е����ʽ�����޷�����!", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	   return resp;   
		
	}
	
	
	
	
	
	
	
	/**
	 * ���˵�����
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
				resp = BaseJson.returnRespObj("ec/ecAccount/download", true, storeRecord.getStoreName()+"Ŀǰ��֧�ֶ��˵�������", null);
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
			objectNode.put("type", 1);// ��ѯ��ʽ,ʾ����1:���˵��ղ�ѯ��2:�������ղ�ѯ��
			objectNode.put("pageNum", pageNo);
			if (StringUtils.isNotEmpty(storeSettings.getVenderId())) {
				objectNode.put("memberId", storeSettings.getVenderId());//�̼�ID
			} else {
				return resp = BaseJson.returnRespObj("ec/ecAccount/download", false, "���������ж�Ӧ�����̼�IDΪ�գ�������", null);
			}
			String json = objectNode.toString();
			String jdRespStr = ECHelper.getJD("jingdong.pop.ledger.bill.queryJdpayDetail", storeSettings, json);
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			// �ж������Ƿ���ִ����д���ʱֱ�ӷ��ء�
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
					ecAccount.setMoneyType(0);//����
					ecAccount.setDirection(0);
					ecAccount.setMoney(new BigDecimal(orderInfo.get("incomeAmount").asText()));//���
				}else {
					ecAccount.setMoneyType(1);//֧��
					ecAccount.setDirection(1);
					ecAccount.setMoney(new BigDecimal(orderInfo.get("expendAmount").asText()));//���
				}
				ecAccount.setOrderType(orderInfo.get("detailDesc").asText());//�������� ��������
				ecAccount.setShOrderId(orderInfo.get("outTradeNo").asText());//�̻�������
				ecAccount.setMemo(orderInfo.get("tradeDesc").asText());//���ױ�ע
				ecAccount.setEcOrderId(orderInfo.get("tradeNo").asText());//���׶�����
				ecAccount.setBillNo(orderInfo.get("bizTradeNo").asText());//ҵ�񵥺�
				ecAccount.setStoreId(storeSettings.getStoreId());
				
				
				
				/*private String storeId;//����id
				private String storeName;//��������
				private String billNo;//ҵ�񵥺�
				private String ecOrderId;//���̶�����
				private String orderType;//��������
				private String shOrderId;//�̻�������
				private String goodNo;//��Ʒ���
				private String goodName;//��Ʒ����
				private String startTime;//���÷���ʱ��
				private String jifeiTime;//�Ʒ�ʱ��
				private String jiesuanTime;//����ʱ��
				private String costType;//������
				private BigDecimal money;//���
				private Integer moneyType;//��֧����0��1֧
				private String memo;//��ע
				private String shopId;//���̺�
				private int direction;//��֧����0����1֧��
				private String costDate;//�˵�����
				private int checkResult;//���ҽ��0δ����1�ѹ���
				private String checkerId;//������id
				private String checkerName;//������
				private String  checkTime;//����ʱ��
				private int isCheckType;//�Ƿ񹴶���0��1�ǣ�Ϊ1ʱ�������ͳ��'
				private String creator;//������
				private String createTime;//����ʱ��
*/				
			}
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			try {
				resp = BaseJson.returnRespObj("ec/platOrder/download", false, "����ʱ�����쳣��������", null);
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
				resp = BaseJson.returnRespObj("ec/ecAccount/autoCheck", true, "�Զ��������", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/ecAccount/autoCheck", false, "�Զ������쳣", null);
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
			resp = BaseJson.returnRespList("ec/ecAccount/goToCheck", true, "��ѯ�ɹ���", count, pageNo, pageSize, 0, 0,
					diffs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resp;
	}
	
	
	
}
