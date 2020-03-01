package com.px.mis.account.dao;

import com.px.mis.account.entity.VouchTabSub;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * ƾ֤�ӱ�
 */
@Repository
public interface VouchTabSubDao {

	void insertList(List<VouchTabSub> tabSubList);
	
	void insertTabSub(VouchTabSub vouchTabSub);
	
	//��ѯƾ֤�ӱ�p
	List<VouchTabSub> selectVouchTabSubListByMap(Map map);
	
	void insertVouchTabSubDlList(List<VouchTabSub> tabSubList);
}