package com.px.mis.purc.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.CustDoc;

public interface CustDocService {
	
	String insertCustDoc(CustDoc custDoc);
	
	ObjectNode updateCustDocByCustId(CustDoc custDoc);
	
/*	ObjectNode deleteCustDocByCustId(String custId);*/
	
	String selectCustDocByCustId(String custId);

    String selectCustDocList(Map map);
    
    String printingCustDocList(Map map); 
    
    String deleteCustDocList(String custId);
    
	//导入客户档案
	public String uploadFileAddDb(MultipartFile  file);
    
}