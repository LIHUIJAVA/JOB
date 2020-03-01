package com.px.mis.account.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.VouchCateDoc;
import com.px.mis.account.entity.VouchCateDocSubTab;

public interface VouchCateDocService {

	ObjectNode insertVouchCateDoc(VouchCateDoc vouchCateDoc);
	
	ObjectNode updateVouchCateDocById(List<VouchCateDoc> vouchCateDocList);
	
	String deleteVouchCateDocByVouchCateWor(String vouchCateWor) throws IOException;
	
	VouchCateDoc selectVouchCateDocById(String vouchCateId);
	
    String selectVouchCateDocList(Map map);

    String selectVouchCateDocPrint(Map map);

    String uploadFileAddDb(MultipartFile file);
}
