package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyCntPtySubjSetTab;

public interface InvtyCntPtySubjSetTabService {

	ObjectNode insertInvtyCntPtySubjSetTab(InvtyCntPtySubjSetTab invtyCntPtySubjSetTab);
	
	ObjectNode updateInvtyCntPtySubjSetTabByOrdrNum(List<InvtyCntPtySubjSetTab> invtyCntPtySubjSetTab);
	
	ObjectNode deleteInvtyCntPtySubjSetTabByOrdrNum(Integer ordrNum);
	
	InvtyCntPtySubjSetTab selectInvtyCntPtySubjSetTabByOrdrNum(Integer ordrNum);
	
    String selectInvtyCntPtySubjSetTabList(Map map);
    
    String deleteInvtyCntPtySubjSetTabList(String ordrNum);

	String selectInvtyCntPtySubjSetTabListPrint(Map map);

	String uploadFileAddDb(MultipartFile file);
}
