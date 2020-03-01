package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.RecvSendCate;

public interface RecvSendCateService {
	
    
    ObjectNode deleteRecvSendCateByRecvSendCateId(String recvSendCateId);

    ObjectNode insertRecvSendCate(RecvSendCate recvSendCate);
    
    RecvSendCate selectRecvSendCateByRecvSendCateId(String recvSendCateId);
    
    ObjectNode updateRecvSendCateByRecvSendCateId(RecvSendCate recvSendCate);
    
    List<Map>  selectRecvSendCate();
    
	//导入收发类别
	public String uploadFileAddDb(MultipartFile  file);

}
