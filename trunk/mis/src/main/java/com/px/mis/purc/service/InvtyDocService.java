package com.px.mis.purc.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.InvtyDoc;

public interface InvtyDocService {
	
	String insertInvtyDoc(InvtyDoc invtyDoc);
	
	ObjectNode updateInvtyDocByInvtyDocEncd(InvtyDoc invtyDoc);
	
//	ObjectNode deleteInvtyDocByInvtyDocEncd(String invtyEncd);
	
	InvtyDoc selectInvtyDocByInvtyDocEncd(String invtyEncd);
	
	String selectInvtyDocList(Map map);
	
	String printingInvtyDocList(Map map);

	String deleteInvtyDocList(String invtyEncd);
	
	//存货档案导入
	public String uploadFileAddDb(MultipartFile file,int i);
	
	String selectInvtyEncdLike(Map map);
}
