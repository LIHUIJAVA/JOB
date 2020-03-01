package com.px.mis.account.dao;

import com.px.mis.account.entity.OutIntoWhsAdjSnglSubTab;

public interface OutIntoWhsAdjSnglSubTabDao {
	
	int insertOutIntoWhsAdjSnglSubTab(OutIntoWhsAdjSnglSubTab outIntoWhsAdjSnglSubTabDoc);
	
	/*int updateOutIntoWhsAdjSnglSubTabByOrdrNum(OutIntoWhsAdjSnglSubTab outIntoWhsAdjSnglSubTabDoc);*/
	
	int deleteOutIntoWhsAdjSnglSubTabByFormNum(String formNum);
	
	/*OutIntoWhsAdjSnglSubTab selectOutIntoWhsAdjSnglSubTabByOrdrNum(Integer ordrNum);
	
    List<OutIntoWhsAdjSnglSubTab> selectOutIntoWhsAdjSnglSubTabList();*/
}
