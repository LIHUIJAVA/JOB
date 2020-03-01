package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.IntoWhsSub;

public interface IntoWhsSubDao {

	int deleteIntoWhsSubByIntoWhsSnglId(String intoWhsSnglId);

	int insertIntoWhsSub(List<IntoWhsSub> intoWhsSubList);

	// 删除时候備份到廢棄表一份
	int insertIntoWhsSubDl(List<String> list2);

	List<IntoWhsSub> selectIntoWhsSubByIntoWhsSnglId(String intoWhsSnglId);

	// 采购发票参照时查询子表信息
	List<IntoWhsSub> selectIntoWhsSubByIntoWhsSnglIds(List<String> intoWhsSnglId);

	// 修改采购入库单子表中未开票数量
	int updateIntoWhsSubByInvWhsBat(Map map);

	// 根据采购入库单序号查询未开票数量
	BigDecimal selectUnBllgQtyByOrdrNum(Map map);

	// 采购退货单参照时查询子表信息
	List<IntoWhsSub> selectIntoWhsSubByUnReturnQty(List<String> intoWhsSnglId);

	// 修改采购入库单子表中未退货数量
	int updateIntoWhsSubUnReturnQty(Map map);

	// 根据采购入库单序号查询未退货数量
	BigDecimal selectUnReturnQtyByOrdrNum(Map map);
}