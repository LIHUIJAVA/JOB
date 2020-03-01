package com.px.mis.account.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.VouchCateDocSubTab;

public interface VouchCateDocSubTabService {
	
	ObjectNode insertVouchCateSubTabDoc(VouchCateDocSubTab vouchCateSubTabDoc);
	
	ObjectNode updateVouchCateDocSubTabById(VouchCateDocSubTab vouchCateSubTabDoc);
	
	Integer deleteVouchCateDocSubTabByVouchCateWor(String vouchCateWor);
	
	VouchCateDocSubTab selectVouchCateDocSubTabById(String vouchCateSubTabId);
	
    List<VouchCateDocSubTab> selectVouchCateDocSubTabList();
}
