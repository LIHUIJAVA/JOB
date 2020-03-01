package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.MthEndStl;

/**
 * 月末结账dao
 */

public interface MthEndStlDao  {
	//查询月末结账
	List<MthEndStl> selectByMap(Map map);
	
	int updateIsMthEndStl(MthEndStl mthEndStl);

	int delectByYear(String year);
	//批量新增
	int insertListAllYear(List<MthEndStl> list);
	
}