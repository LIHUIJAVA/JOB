package com.px.mis.account.dao;

import java.util.List;

import com.px.mis.account.entity.VouchCateDocSubTab;

public interface VouchCateDocSubTabDao {
	int insertVouchCateSubTabDoc(VouchCateDocSubTab vouchCateSubTabDoc);
	
	int updateVouchCateDocSubTabById(VouchCateDocSubTab vouchCateSubTabDoc);
	
	int deleteVouchCateDocSubTabByVouchCateWor(String vouchCateWor);
	
	VouchCateDocSubTab selectVouchCateDocSubTabById(Integer ordrNum);
	
    List<VouchCateDocSubTab> selectVouchCateDocSubTabList();
}
