package com.px.mis.account.dao;

import com.px.mis.account.entity.InvtyDetails;
import com.px.mis.account.entity.PursComnInv;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.service.impl.SellComnInvServiceImpl.zizhu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SellComnInvDao {

	// 新增销售发票
	int insertSellComnInv(SellComnInv sellComnInv);

	// 修改销售发票
	int updateSellComnInvBySellInvNum(SellComnInv sellComnInv);

	// 单个删除销售发票
	int deleteSellComnInvBySellInvNum(String sellSnglNum);

	// 批量删除销售发票
	int deleteSellComnInvList(List<String> sellInvNum);

	// 按照发票编码查询发票主子表信息
	SellComnInv selectSellComnInvBySellInvNum(String sellInvNum);

	// 分页查询销售发票数据
	List<SellComnInv> selectSellComnInvList(Map map);

	int selectSellComnInvCount(Map map);

	// 查询销售发票审核状态
	int selectSellComnInvIsNtChk(String sellInvNum);

	// 修改审核状态
	int updateSellComnInvIsNtChk(SellComnInv sellComnInv);

	// 查询记账状态
	int selectSellComnInvIsNtBookEntry(String sellInvNum);

	// 根据销售发票单号，查询销售发票
	String selectSellSnglNumBySellComnInv(String sellInvNum);

	// 导入时新增销售发票主表
	int insertSellComnInvUpload(List<SellComnInv> sellComnInv);

	List<SellComnInvSub> selectSellComnInvToQty(Map map);

	// 根据存货/仓库/批次/客户 sum总开票数量和总出库成本
	List<SellComnInvSub> selectUnBllgQtyAndAmt(Map<String, Object> dataMap);

	// sum销售数量/销售发票-取发票
	SellComnInvSub sumSellQtyAndAmt(Map map);

	// 进销存统计表时 sum销售成本
	SellComnInvSub sumSellCost(Map<String, Object> dataMap);

	// 查询-发出商品明细帐
	List<InvtyDetails> selectSellComnDetailList(Map<String, Object> paramMap);

	// 原来的导出接口,不敢删
	List<SellComnInv> printingSellComnInvList(Map map);

	// 导出接口
	List<zizhu> printSellComnInvList(Map map);

	BigDecimal selectSellComnInvQty(Map map);

	// 根据单号list,批量查询主子表对象
	List<SellComnInv> selectComnInvs(List<String> idList);

	// 存货过程
	BigDecimal selectSellQtyByStored(Map map);

	// 推送U8成功回写状态
	int updatePushed(String dscode);

}
