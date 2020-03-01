package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.SellComnInv;
import com.px.mis.purc.entity.PayApplForm;
import com.px.mis.purc.service.impl.PayApplFormServiceImpl;
import com.px.mis.purc.service.impl.PayApplFormServiceImpl.zizhu;

public interface PayApplFormDao {
	/** 删除主子表 */
	int deleteByPrimaryKeyList(List<String> list);

	/** 新增 */
	int insertPayApplForm(PayApplForm record);

// 删除之前，插入备份
	int insertPayApplFormDl(List<String> lists);

	/** 按照主表编码查询主表信息 */
	PayApplForm selectByPrimaryKey(String payApplId);

	/** 按照多个主表编码查询主表信息 */
	List<PayApplForm> selectByPrimaryKeyLsit(List<String> list);

	/** 更新主表 */
	int updateByPrimaryKeySelective(PayApplForm record);

	/** 查主子表详情 */
	PayApplForm selectByPrimaryList(String payApplId);

	/** 分页查 */
	List<PayApplForm> selectPayApplFormList(Map map);

// 分页+排序
	List<zizhu> selectPayApplFormListOrderBy(Map map);

	/** 分页时计数 */
	int selectPayApplFormCount(Map map);

	/** 审核付款申请单 */
	int updatePayApplFormIsNtChk(PayApplForm payApplForm);

	/** 查询付款申请单审核状态 */
	int selectPayApplIsNtChk(String payApplId);

	/** 查询付款申请单付款状态 */
	int selectPayApplIsNtPay(String payApplId);

	Map selectPayApplFormListSums(Map map);

	List<zizhu> printPayApplFormList(Map map);

	// 根据idlist批量查询
	List<PayApplForm> selectPayApplForms(List<String> idList);

}