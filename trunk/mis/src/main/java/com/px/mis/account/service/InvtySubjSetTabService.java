package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtySubjSetTab;

public interface InvtySubjSetTabService {

	ObjectNode insertInvtySubjSetTab(InvtySubjSetTab invtySubjSetTab);
	
	ObjectNode updateInvtySubjSetTabById(List<InvtySubjSetTab> invtySubjSetTab);
	
	ObjectNode deleteInvtySubjSetTabByOrdrNum(Integer ordrNum);
	
	InvtySubjSetTab selectInvtySubjSetTabByOrdrNum(Integer ordrNum);
	
    String selectInvtySubjSetTabList(Map map);
    
    String deleteInvtySubjSetTabList(String ordrNum);

    String selectInvtySubjSetTabPrint(Map map);

    String uploadFileAddDb(MultipartFile file);
}
