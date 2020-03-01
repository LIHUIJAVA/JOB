package com.px.mis.account.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.OutIntoWhsAdjSnglSubTab;

public interface OutIntoWhsAdjSnglSubTabService {
	
	ObjectNode insertOutIntoWhsAdjSnglSubTab(OutIntoWhsAdjSnglSubTab outIntoWhsAdjSnglSubTab);
	
	ObjectNode updateOutIntoWhsAdjSnglSubTabById(OutIntoWhsAdjSnglSubTab outIntoWhsAdjSnglSubTab);
	
	Integer deleteOutIntoWhsAdjSnglSubTabByFormNum(String formNum);
	
}
