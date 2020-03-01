package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.CustCls;

public interface CustClsService {

	String insertCustCls(CustCls custCls);
	
	String updateCustClsByClsId(CustCls custCls);
	
	String deleteCustClsByClsId(String clsId);

	String selectCustClsByClsId(String clsId);
	
	List<Map> selectCustCls();
	
	public String uploadFileAddDb(MultipartFile file);

}
