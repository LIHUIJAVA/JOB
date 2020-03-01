package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.account.entity.AcctItmDoc;

public interface AcctItmDocService {

	String insertAcctItmDoc(AcctItmDoc acctItmDoc);
	
	String updateAcctItmDocById(List<AcctItmDoc> acctItmDoc);
	
	String deleteAcctItmDocById(String subjId);
	
	String selectAcctItmDocById(String subjId);
	
    String queryAcctItmDocList(Map map);

	String queryAcctItmDocPrint(Map map);

	String uploadFileAddDb(MultipartFile file);
}
