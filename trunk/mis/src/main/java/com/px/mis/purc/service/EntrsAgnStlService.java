package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.EntrsAgnStl;
import com.px.mis.purc.entity.EntrsAgnStlSub;

public interface EntrsAgnStlService {

	public String addEntrsAgnStl(String userId,EntrsAgnStl entrsAgnStl,List<EntrsAgnStlSub> entrsAgnStlSubList,String loginTime);
	
	public String editEntrsAgnStl(EntrsAgnStl EntrsAgnStl,List<EntrsAgnStlSub> entrsAgnStlSubList);
	
	public String deleteEntrsAgnStl(String stlSnglId);
	
	public String queryEntrsAgnStl(String stlSnglId);
	
	public String queryEntrsAgnStlList(Map map);
	
	String deleteEntrsAgnStlList(String stlSnglId);
	
	String printingEntrsAgnStlList(Map map);
	
	Map<String,Object> updateEntrsAgnStlIsNtChk(String userId,EntrsAgnStl entrsAgnStl,String loginTime)  throws Exception;
	
	public String uploadFileAddDb(MultipartFile file);

	String queryEntrsAgnStlListOrderBy(Map map);
}
