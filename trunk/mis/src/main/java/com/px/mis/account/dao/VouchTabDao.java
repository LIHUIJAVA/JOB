package com.px.mis.account.dao;

import com.px.mis.account.entity.VouchTab;
import com.px.mis.account.entity.VouchTabSub;

import java.util.List;
import java.util.Map;

public interface VouchTabDao {
	
	int insertVouchTab(VouchTab vouchTab);
	
	int updateVouchTabByOrdrNum(VouchTab vouchTab);
	
	int deleteVouchTabByOrdrNum(Integer ordrNum);
	
	VouchTab selectVouchTabByOrdrNum(Integer ordrNum);
	
	VouchTab selectVouchTabBycomnVouchId(String comnVouchId);
	
    List<VouchTab> selectVouchTabList(Map map);
    
    int selectVouchTabCount(Map map);
    //查询凭证
	List<VouchTab> selectVouchTabMap(Map map);
	//批量删除凭证
	int deleteVouchTabList(List<VouchTab> list);
	//导出凭证
	List<VouchTab> exportVouchTabList(Map map);
	//批量删除凭证以及子表
	void delectVouchTabSubList(List<VouchTabSub> tabSubList);
	
	void updateVouchList(List<VouchTab> list);
	//查询最后一张凭证
	VouchTab selectLastTab();
	//删除留存
	void insertVouchTabDlList(List<VouchTabSub> tabSubList);

	List<VouchTab> selectVouchTab(Map<String, Object> dataMap);
	
	
	
	
}
