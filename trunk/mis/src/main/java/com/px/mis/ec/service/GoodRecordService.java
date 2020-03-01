package com.px.mis.ec.service;

import java.io.File;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.ec.entity.GoodRecord;

public interface GoodRecordService {

	public String add(GoodRecord goodRecord);
	
	public String edit(GoodRecord goodRecord);
	
	public String delete(String id);
	
	public String query(Integer id);
	
	public String queryList(Map map);

	public String download(String accNum,String storeId);
	
	public String uploadFile(MultipartFile file,String userID);
	
	public String exportList(Map map);
}
