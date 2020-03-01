package com.px.mis.ec.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.StoreRecord;

public interface StoreRecordService {

	public String add(StoreRecord storeRecord);
	
	public String edit(StoreRecord storeRecord);
	
	public String delete(String storeId);
	
	public String query(String storeId);
	
	public String queryAll(String jsonBody);
	
	public String queryList(Map map);

	public String download(ObjectNode jsonBody);
	
	public String exportList(Map map);
	
	public String importStoreRecord(MultipartFile file, String userId);
}
