package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyMthTermBgnTab;

public interface InvtyMthTermBgnTabDao {
	//查询核算各月期初
	List<InvtyMthTermBgnTab> selectByMthTerm(Map<String, Object> paramMap);
	//查询核算各月期初
	List<InvtyMthTermBgnTab> selectMthTermList(Map<String, Object> paramMap);
		
	int countSelectByMthTerm(Map<String,Object> paramMap);
	
	int insertMth(InvtyMthTermBgnTab mth);
	//修改结存数量/金额
	int updateMth(InvtyMthTermBgnTab mth);

	List<InvtyMthTermBgnTab> selectIsFinalDeal(Map map);
	//批量修改
	int updateMthList(List<InvtyMthTermBgnTab> termList);
	
	//批量新增
	int insertMthList(List<InvtyMthTermBgnTab> list);
	//批量删除
	int deleteMthList(List<InvtyMthTermBgnTab> mthList);
	//删除
	int deleteByordrNum(int ordrNum);
	/**
	 * 查询期初数据--明细账
	 */
	List<InvtyMthTermBgnTab> selectMthTermByInvty(Map map);
}
