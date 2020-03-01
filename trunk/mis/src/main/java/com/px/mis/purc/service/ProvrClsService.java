package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.ProvrCls;

public interface ProvrClsService {
	
	ObjectNode insertProvrCls(ProvrCls provrCls);
	
	ObjectNode updateProvrClsByProvrClsId(ProvrCls provrCls);
	
	ObjectNode deleteProvrClsByProvrClsId(String provrClsId);
	
	List<Map> selectProvrCls();
	
	ProvrCls selectProvrClsByProvrClsId(String provrClsId);

	public String uploadFileAddDb(MultipartFile file);


}
