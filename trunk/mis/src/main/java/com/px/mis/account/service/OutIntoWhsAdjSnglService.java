package com.px.mis.account.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.OutIntoWhsAdjSngl;

public interface OutIntoWhsAdjSnglService {

	ObjectNode insertOutIntoWhsAdjSngl(String userId,OutIntoWhsAdjSngl outIntoWhsAdjSngl,String loginTime);
	
	ObjectNode updateOutIntoWhsAdjSnglByFormNum(OutIntoWhsAdjSngl outIntoWhsAdjSngl);
	
	ObjectNode deleteOutIntoWhsAdjSnglByFormNum(String formNum);
	
	OutIntoWhsAdjSngl selectOutIntoWhsAdjSnglByFormNum(String formNum);
	
    String selectOutIntoWhsAdjSnglList(Map map);

    String selectOutIntoWhsAdjSnglPrint(Map map);

    String uploadFileAddDb(MultipartFile file) throws Exception;
    
}
