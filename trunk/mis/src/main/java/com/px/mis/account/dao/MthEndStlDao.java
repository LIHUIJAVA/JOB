package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.MthEndStl;

/**
 * ��ĩ����dao
 */

public interface MthEndStlDao  {
	//��ѯ��ĩ����
	List<MthEndStl> selectByMap(Map map);
	
	int updateIsMthEndStl(MthEndStl mthEndStl);

	int delectByYear(String year);
	//��������
	int insertListAllYear(List<MthEndStl> list);
	
}