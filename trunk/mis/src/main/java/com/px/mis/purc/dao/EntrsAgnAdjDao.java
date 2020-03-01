package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnAdj;

public interface EntrsAgnAdjDao {
	
    int deleteEntrsAgnAdjByAdjSnglId(String adjSnglId);

    int insertEntrsAgnAdj(EntrsAgnAdj entrsAgnAdj);

    EntrsAgnAdj selectEntrsAgnAdjByAdjSnglId(String adjSnglId);
    
    EntrsAgnAdj selectEntrsAgnAdjById(String adjSnglId);

    int updateEntrsAgnAdjByAdjSnglId(EntrsAgnAdj entrsAgnAdj);
  
    List<EntrsAgnAdj> selectEntrsAgnAdjList(Map map);
	
	int selectEntrsAgnAdjCount(Map map);
	
	int deleteEntrsAgnAdjList(List<String> adjSnglId);
	
	List<EntrsAgnAdj> printingEntrsAgnAdjList(Map map);
	
	EntrsAgnAdj selectAdjSnglIdByDelvSnglId(String delvSnglId);
}