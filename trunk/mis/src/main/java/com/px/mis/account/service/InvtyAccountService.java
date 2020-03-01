package com.px.mis.account.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import com.px.mis.account.entity.FormBookEntry;

public interface InvtyAccountService {
	
	
	/**
	 * 明细表 
	 */
	String selectDetailList(Map map,String loginTime) throws IllegalAccessException, InvocationTargetException;
	/**
	 * 明细帐-收发存汇总表跳转
	 */
	String selectDetailedList(Map map, String loginTime, String url) throws Exception;
	/**
	 * 明细帐-导出
	 */
	String selectDetailedListExport(Map map, String loginTime) throws Exception;
	/**
	 *发出商品明细帐
	 */
	String selectsendProductList(Map map,String loginTime) throws Exception;
	/**
	 *发出商品明细帐-导出
	 * @throws Exception 
	 */
	String selectsendProductListExport(Map map, String loginTime) throws Exception;
	/**
	 * 流水帐
	 */
	String selectStreamList(Map map,String loginTime) throws Exception;
	/**
	 * 流水帐-导出
	 * @throws Exception 
	 */
	String selectStreamListExport(Map map, String loginTime) throws Exception;
	/**
	 * 收发存汇总表
	 */
	String sendAndReceivePool(Map map, String loginTime) throws Exception;

	/**
	 * 发出商品汇总表
	 */
	String sendProductsPool(Map map, String loginTime) throws Exception;
	/**
	 * 发出商品汇总表-导出
	 */
	String sendProductsPoolExport(Map map, String loginTime) throws Exception;
	/**
	 * 进销存统计表
	 */
	String invoicingPool(Map map, String loginTime)  throws Exception;
	/**
	 * 进销存统计表-导出
	 **/
	String invoicingPoolExport(Map map, String loginTime) throws Exception;
	/**
	 *  收发存分类汇总表
	 */
	String sendAndReceiveInvtyClsPool(Map map, String loginTime) throws Exception;
	/**
	 * 收发存汇总表-导出
	 */
	String sendAndReceiveInvtyClsPoolExport(Map map, String loginTime)throws Exception;
	
	
	
	
	
	
	


	

	
	
	
		
}
