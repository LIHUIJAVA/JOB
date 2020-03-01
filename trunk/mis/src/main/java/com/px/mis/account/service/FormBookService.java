package com.px.mis.account.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntry;
/** 
 * ���ݼ��˷����
 */
public interface FormBookService {
	/**
	 * ��ѯ�������ݼ����б�
	 */
	String selectNormalInvtyAccount(Map map) throws Exception;
	/**
	 * ��ѯ����
	 */
	Object selectDoument(Map map);
	
	/**
	 * ����
	 */
	String formBook(List<FormBookEntry> list, String userName, String loginTime) throws Exception;
	
	/**
	 * �ָ�����
	 */
	String backFormBook(String formNum, String userName, String loginTime) throws IOException;
	/**
	 * δ����ƾ֤�����б�
	 */
	String selectNovoucherList (Map map,String isNtMakeVouch);
	/**
	 * δ��ĩ������
	 */
	String selectNoFinalDealList (Map map,String loginTime) throws IOException;
	/**
	 * ��ĩ����
	 */
	String finalDealForm(Map map,String loginTime,String accNm,String userName) throws Exception;
	/**
	 * �ָ���ĩ����
	 */
	String finalBackDealForm(Map map, String loginTime, String accNm) throws Exception;
	/**
	 * ��ĩ����
	 */
	String finalMonthDeal(Map map, String accNum, String loginTime) throws Exception;
	/**
	 * �ָ���ĩ����
	 */
	String finalMonthDealBack(Map map, String accNum, String loginTime) throws IOException;
	/**
	 * ��ѯ��ĩ�����¶�
	 */
	String selectFinalMonthDealList(Map map, String loginTime) throws IOException;
	/**
	 * �ָ����˵���list
	 */
	String selectBackFormList(Map map,String loginTime) throws IOException;
	/**
	 * ������ĩ�����·�
	 */
	String settingDealMonth(Map map,String accNum,String loginTime) throws IOException;
	/**
	 * ����-�ڳ����
	 */
	String finalTermBgnBook(Map map, String accNum, String loginTime) throws Exception;
	/**
	 * �ָ�����-�ڳ����
	 */
	String finalTermBgnBackBook(Map map, String accNum, String loginTime) throws Exception;
	/**
	 * �س嵥�б�
	 */
	String backFlushFormList(Map map, String url) throws Exception;
	/**
	 * ������ʱ����
	 */
	String mthSealBook(Map map, String url, String loginTime, String accNum) throws Exception;
	/**
	 * ��ǰ�·��Ƿ����
	 * @param loginTime ��¼����
	 * @return true-�ѷ��� false-δ����n
	 */
	boolean isMthSeal(String loginTime);
	/**
	 * ��������������
	 */
	String outIntoAdjFormBook(Map map, String url, String loginTime, String accNum) throws Exception;
	/**
	 * �����������ָ�����
	 */
	String outIntoAdjBackFormBook(Map map, String url, String loginTime, String userName) throws Exception;
}
