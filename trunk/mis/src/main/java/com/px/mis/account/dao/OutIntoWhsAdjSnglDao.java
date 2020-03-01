package com.px.mis.account.dao;

import com.px.mis.account.entity.OutIntoWhsAdjSngl;

import java.util.List;
import java.util.Map;

public interface OutIntoWhsAdjSnglDao {
	
	int insertOutIntoWhsAdjSngl(OutIntoWhsAdjSngl outIntoWhsAdjSngl);
	
	int updateOutIntoWhsAdjSnglByFormNum(OutIntoWhsAdjSngl outIntoWhsAdjSngl);
	
	int deleteOutIntoWhsAdjSnglByFormNum(List<String> formNum);
	
	//批量删除出库调整单  delectOutIntoWhsAdjList
	int deleteOutIntoWhsAdjSnglList(List<String> formNum);
	
	OutIntoWhsAdjSngl selectOutIntoWhsAdjSnglByFormNum(String formNum);

    List<OutIntoWhsAdjSngl> selectOutIntoWhsAdjSnglList(Map map);
    
    int selectOutIntoWhsAdjSnglCount(Map map);

	List<OutIntoWhsAdjSngl> selectOutIntoWhsAdjSnglFinalDealList(Map map);
	
	int selectOutIntoWhsAdjSnglIsNtBookEntry(String formNum);

	int EXinsertOutIntoWhsAdjSngl(OutIntoWhsAdjSngl outIntoWhsAdjSngl);

	void updateOutIntoAdjList(List<OutIntoWhsAdjSngl> adjList);

}
