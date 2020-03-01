package com.px.mis.account.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.VouchTab;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface VouchTabService {

	ObjectNode insertVouchTab(VouchTab vouchTab);
	
	ObjectNode updateVouchTabByOrdrNum(VouchTab vouchTab);
	
	ObjectNode deleteVouchTabByOrdrNum(Integer ordrNum);
	
	VouchTab selectVouchTabByOrdrNum(Integer ordrNum);
	
    String selectVouchTabList(Map map) throws Exception;
       
	/**
	 * ����ƾ֤
	 */
	String voucherGenerate(Map map) throws IOException,IllegalAccessException, InvocationTargetException;
	/**
	 * ��ѯƾ֤
	 */
	String selectvoucherList(Map map);
	/**
	 * ɾ��ƾ֤
	 */
	String voucherDel(Map map) throws IOException;
	/**
	 * ����ƾ֤
	 */
	String exportvoucherList (Map map) throws Exception;
	/**
	 * ����ƾ֤
	 */
	String importVoucherList(MultipartFile file,String accNum) throws Exception;

}
