package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.VouchCateDoc;

public interface VouchCateDocDao {
	
	int insertVouchCateDoc(VouchCateDoc vouchCateDoc);
	
	int updateVouchCateDocByVouchCateWor(VouchCateDoc vouchCateDoc);
	
	int deleteVouchCateDocByVouchCateWor(List<String> vouchCateWor);
	
	VouchCateDoc selectVouchCateDocByVouchCateWor(String vouchCateWor);

    List<VouchCateDoc> selectVouchCateDocList(Map map);
    
    int selectVouchCateDocCount();
}
