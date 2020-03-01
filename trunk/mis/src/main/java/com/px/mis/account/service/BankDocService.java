package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.BankDoc;

public interface BankDocService {

	ObjectNode insertBankDoc(BankDoc bankDoc);
	
	ObjectNode updateBankDocByordrNum(List<BankDoc> bankDoc);
	
	ObjectNode deleteBankDocByOrdrNum(String id);
	
	BankDoc selectBankDocByordrNum(String id);
	
    String selectBankDocList(Map map);

	String selectBankDocPrint(Map map);

	String uploadFileAddDb(MultipartFile file);
}
