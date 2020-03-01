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
	 * 生成凭证
	 */
	String voucherGenerate(Map map) throws IOException,IllegalAccessException, InvocationTargetException;
	/**
	 * 查询凭证
	 */
	String selectvoucherList(Map map);
	/**
	 * 删除凭证
	 */
	String voucherDel(Map map) throws IOException;
	/**
	 * 导出凭证
	 */
	String exportvoucherList (Map map) throws Exception;
	/**
	 * 导入凭证
	 */
	String importVoucherList(MultipartFile file,String accNum) throws Exception;

}
