package com.px.mis.account.service;

import com.px.mis.account.entity.TermBgnBal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TermBgnBalService {

	String insertTermBgnBal(TermBgnBal termBgnBal);
	
	String updateTermBgnBalByOrdrNum(List<TermBgnBal> termBgnBalList);
	
	String deleteTermBgnBalByOrdrNum(String ordrNum);
	
	String uploadFileAddDb(MultipartFile file);
	
    String queryTermBgnBalList(Map map);
    //ÆÚ³õÓà¶î-µ¼³ö
	String queryTermBgnBalListPrint(Map map);
    
}
