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
    //��ѯƾ֤
	List<VouchTab> selectVouchTabMap(Map map);
	//����ɾ��ƾ֤
	int deleteVouchTabList(List<VouchTab> list);
	//����ƾ֤
	List<VouchTab> exportVouchTabList(Map map);
	//����ɾ��ƾ֤�Լ��ӱ�
	void delectVouchTabSubList(List<VouchTabSub> tabSubList);
	
	void updateVouchList(List<VouchTab> list);
	//��ѯ���һ��ƾ֤
	VouchTab selectLastTab();
	//ɾ������
	void insertVouchTabDlList(List<VouchTabSub> tabSubList);

	List<VouchTab> selectVouchTab(Map<String, Object> dataMap);
	
	
	
	
}
