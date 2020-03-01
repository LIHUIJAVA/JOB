package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.DeptDoc;

public interface DeptDocService {
	
	String insertDeptDoc(DeptDoc deptDoc);
	
	ObjectNode updateDeptDocByDeptEncd(DeptDoc deptDoc);
	
	String updateDeptDocByDeptEncd(List<DeptDoc> deptDoc);
	
//	ObjectNode deleteDeptDocByDeptEncd(String deptEncd);
	
	DeptDoc selectDeptDocByDeptEncd(String deptEncd) ;
	
	String selectDeptDocList(Map map);
	
	String printingDeptDocList(Map map);
	
	String deleteDeptDocList(String deptId);

	public String uploadFileAddDb(MultipartFile file);
}
