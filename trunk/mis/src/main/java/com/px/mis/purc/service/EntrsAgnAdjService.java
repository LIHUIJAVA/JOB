package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnAdj;
import com.px.mis.purc.entity.EntrsAgnAdjSub;

public interface EntrsAgnAdjService {

	public String addEntrsAgnAdj(String userId,List<EntrsAgnAdj> entrsAgnAdj,String loginTime);
	
	public String editEntrsAgnAdj(EntrsAgnAdj entrsAgnAdj,List<EntrsAgnAdjSub> entrsAgnAdjSubList);
	
	public String deleteEntrsAgnAdj(String adjSnglId);
	
	public String queryEntrsAgnAdj(String adjSnglId);
	
	public String queryEntrsAgnAdjList(Map map);
	
	String deleteEntrsAgnAdjList(String adjSnglId);
	
	String printingEntrsAgnAdjList(Map map);
	
	String queryEntrsAgnAdjOrEntrsAgn(Map map);

}
