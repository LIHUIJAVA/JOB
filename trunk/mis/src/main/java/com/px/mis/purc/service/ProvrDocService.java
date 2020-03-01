package com.px.mis.purc.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.ProvrCls;
import com.px.mis.purc.entity.ProvrDoc;

public interface ProvrDocService {
	
	/*String deleteProvrDocByProvrId(String provrId);*/

	String insertProvrDoc(ProvrDoc provrDoc);

	String updateProvrDocByProvrId(ProvrDoc provrDoc);
    
	ProvrCls selectProvrDocByProvrId(String provrId);
	
	String selectProvrDocList(Map map);
	
	String printingProvrDocList(Map map);
	
	String deleteProvrDocList(String provrId);
	
	/*String selectProvrDocByProvrClsId(String provrClsId);*/
	
	//导入接口
	public String uploadFileAddDb(MultipartFile file);

}
