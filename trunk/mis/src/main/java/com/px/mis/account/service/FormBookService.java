package com.px.mis.account.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntry;
/** 
 * 单据记账服务层
 */
public interface FormBookService {
	/**
	 * 查询正常单据记账列表
	 */
	String selectNormalInvtyAccount(Map map) throws Exception;
	/**
	 * 查询单据
	 */
	Object selectDoument(Map map);
	
	/**
	 * 记账
	 */
	String formBook(List<FormBookEntry> list, String userName, String loginTime) throws Exception;
	
	/**
	 * 恢复记账
	 */
	String backFormBook(String formNum, String userName, String loginTime) throws IOException;
	/**
	 * 未生成凭证单据列表
	 */
	String selectNovoucherList (Map map,String isNtMakeVouch);
	/**
	 * 未期末处理存货
	 */
	String selectNoFinalDealList (Map map,String loginTime) throws IOException;
	/**
	 * 期末处理
	 */
	String finalDealForm(Map map,String loginTime,String accNm,String userName) throws Exception;
	/**
	 * 恢复期末处理
	 */
	String finalBackDealForm(Map map, String loginTime, String accNm) throws Exception;
	/**
	 * 月末记账
	 */
	String finalMonthDeal(Map map, String accNum, String loginTime) throws Exception;
	/**
	 * 恢复月末记账
	 */
	String finalMonthDealBack(Map map, String accNum, String loginTime) throws IOException;
	/**
	 * 查询月末记账月度
	 */
	String selectFinalMonthDealList(Map map, String loginTime) throws IOException;
	/**
	 * 恢复记账单据list
	 */
	String selectBackFormList(Map map,String loginTime) throws IOException;
	/**
	 * 设置月末结账月份
	 */
	String settingDealMonth(Map map,String accNum,String loginTime) throws IOException;
	/**
	 * 记账-期初余额
	 */
	String finalTermBgnBook(Map map, String accNum, String loginTime) throws Exception;
	/**
	 * 恢复记账-期初余额
	 */
	String finalTermBgnBackBook(Map map, String accNum, String loginTime) throws Exception;
	/**
	 * 回冲单列表
	 */
	String backFlushFormList(Map map, String url) throws Exception;
	/**
	 * 记账账时封账
	 */
	String mthSealBook(Map map, String url, String loginTime, String accNum) throws Exception;
	/**
	 * 当前月份是否封账
	 * @param loginTime 登录日期
	 * @return true-已封账 false-未封账n
	 */
	boolean isMthSeal(String loginTime);
	/**
	 * 出入库调整单记账
	 */
	String outIntoAdjFormBook(Map map, String url, String loginTime, String accNum) throws Exception;
	/**
	 * 出入库调整单恢复记账
	 */
	String outIntoAdjBackFormBook(Map map, String url, String loginTime, String userName) throws Exception;
}
