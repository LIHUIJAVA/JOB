package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.MeasrCorpDoc;

public interface MeasrCorpDocService {
	
	ObjectNode deleteMeasrCorpDocByMeasrCorpId(String measrCorpId);
	
	String deleteMeasrCorpDocList(String measrCorpId);

	ObjectNode insertMeasrCorpDoc(MeasrCorpDoc measrCorpDoc);

	MeasrCorpDoc selectMeasrCorpDocByMeasrCorpId(String measrCorpId) ;

	ObjectNode updateMeasrCorpDocByMeasrCorpId(MeasrCorpDoc measrCorpDoc);
	
	String updateMeasrCorpDocByMeasrCorpId(List<MeasrCorpDoc> measrCorpDoc);
	
	String selectMeasrCorpDocList(Map map);
	
	//导入功能
	public String uploadFileAddDb(MultipartFile  file);
	
	String printingMeasrCorpDocList();

}
