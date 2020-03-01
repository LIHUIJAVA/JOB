package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyPayblSubj;

public interface InvtyPayblSubjService {

	ObjectNode insertInvtyPayblSubj(InvtyPayblSubj invtyPayblSubj);
	
	ObjectNode updateInvtyPayblSubjById(List<InvtyPayblSubj> invtyPayblSubj);
	
	ObjectNode deleteInvtyPayblSubjById(Integer incrsId);
	
	InvtyPayblSubj selectInvtyPayblSubjById(Integer incrsId);
	
    String selectInvtyPayblSubjList(Map map);
    
    String deleteInvtyPayblSubjList(String incrsId);

    String selectInvtyPayblSubjListPrint(Map map);

    String uploadFileAddDb(MultipartFile file);
}
