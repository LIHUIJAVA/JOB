package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyMthTermBgnTab;

public interface InvtySendMthTermBgnTabDao {
	// 查询核算各月期初
	List<InvtyMthTermBgnTab> selectSendMthByMthTerm(Map<String, Object> paramMap);

	List<InvtyMthTermBgnTab> selectSendMthByMth(Map map);

	// 查询核算各月期初
	List<InvtyMthTermBgnTab> selectMthTermList(Map<String, Object> paramMap);

	int countSelectByMthTerm(Map<String, Object> paramMap);

	int insertSendMth(InvtyMthTermBgnTab mth);

	// 修改结存数量/金额
	int updateSendMth(InvtyMthTermBgnTab mth);

	List<InvtyMthTermBgnTab> selectIsFinalDeal(Map map);

	// 批量修改
	int updateSendMthList(List<InvtyMthTermBgnTab> termList);

	// 批量新增
	int insertMthList(List<InvtyMthTermBgnTab> list);

	// 批量删除
	int deleteSendMthList(List<InvtyMthTermBgnTab> mthList);

	// 删除
	int deleteByordrNum(int ordrNum);

	List<InvtyMthTermBgnTab> selectMthTermByInvty(Map map);

	/**
	 * 查询期初数据--发出商品汇总
	 */
	List<InvtyMthTermBgnTab> sendProductMthPool(Map map);

//	//导出数据
//	List<InvtyMthTermBgnTab> sendProductMthPoolExport(Map map);

	int countSendProductMthPool(Map map);

}
