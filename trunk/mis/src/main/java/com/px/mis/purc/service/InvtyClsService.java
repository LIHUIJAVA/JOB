package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.InvtyCls;

public interface InvtyClsService {
	
	ObjectNode insertInvtyCls(InvtyCls invtyCls);
	
	ObjectNode updateInvtyClsByInvtyClsEncd(InvtyCls invtyCls);
	
	ObjectNode deleteInvtyClsByInvtyClsEncd(String invtyClsEncd);
	
	InvtyCls selectInvtyClsByInvtyClsEncd(String invtyClsEncd);
	
	List<Map> selectInvtyCls();

	public String uploadFileAddDb(MultipartFile file);
}
