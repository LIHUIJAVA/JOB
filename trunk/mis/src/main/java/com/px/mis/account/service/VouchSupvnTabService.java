package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.VouchSupvnTab;

public interface VouchSupvnTabService {

	ObjectNode insertVouchSupvnTab(VouchSupvnTab vouchSupvnTab);
	
	ObjectNode updateVouchSupvnTabByOrdrNum(VouchSupvnTab vouchSupvnTab);
	
	ObjectNode deleteVouchSupvnTabByOrdrNum(Integer ordrNum);
	
	VouchSupvnTab selectVouchSupvnTabByOrdrNum(Integer ordrNum);
	
    String selectVouchSupvnTabList(Map map);
}
