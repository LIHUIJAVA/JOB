package com.px.mis.account.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import com.px.mis.account.entity.FormBookEntry;

public interface InvtyAccountService {
	
	
	/**
	 * ��ϸ�� 
	 */
	String selectDetailList(Map map,String loginTime) throws IllegalAccessException, InvocationTargetException;
	/**
	 * ��ϸ��-�շ�����ܱ���ת
	 */
	String selectDetailedList(Map map, String loginTime, String url) throws Exception;
	/**
	 * ��ϸ��-����
	 */
	String selectDetailedListExport(Map map, String loginTime) throws Exception;
	/**
	 *������Ʒ��ϸ��
	 */
	String selectsendProductList(Map map,String loginTime) throws Exception;
	/**
	 *������Ʒ��ϸ��-����
	 * @throws Exception 
	 */
	String selectsendProductListExport(Map map, String loginTime) throws Exception;
	/**
	 * ��ˮ��
	 */
	String selectStreamList(Map map,String loginTime) throws Exception;
	/**
	 * ��ˮ��-����
	 * @throws Exception 
	 */
	String selectStreamListExport(Map map, String loginTime) throws Exception;
	/**
	 * �շ�����ܱ�
	 */
	String sendAndReceivePool(Map map, String loginTime) throws Exception;

	/**
	 * ������Ʒ���ܱ�
	 */
	String sendProductsPool(Map map, String loginTime) throws Exception;
	/**
	 * ������Ʒ���ܱ�-����
	 */
	String sendProductsPoolExport(Map map, String loginTime) throws Exception;
	/**
	 * ������ͳ�Ʊ�
	 */
	String invoicingPool(Map map, String loginTime)  throws Exception;
	/**
	 * ������ͳ�Ʊ�-����
	 **/
	String invoicingPoolExport(Map map, String loginTime) throws Exception;
	/**
	 *  �շ��������ܱ�
	 */
	String sendAndReceiveInvtyClsPool(Map map, String loginTime) throws Exception;
	/**
	 * �շ�����ܱ�-����
	 */
	String sendAndReceiveInvtyClsPoolExport(Map map, String loginTime)throws Exception;
	
	
	
	
	
	
	


	

	
	
	
		
}
