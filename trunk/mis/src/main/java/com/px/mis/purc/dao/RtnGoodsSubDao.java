package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.RtnGoodsSub;

public interface RtnGoodsSubDao {

	// 删除退货单
	Integer deleteRtnGoodsSubByRtnGoodsId(String rtnGoodsId);

	// 批量新增退货单
	Integer insertRtnGoodsSub(List<RtnGoodsSub> rtnGoodsSubList);

//    废弃备份
	Integer insertRtnGoodsSubDl(List<String> lists2);

	// 按照退货单号查询退货单子表信息
	List<RtnGoodsSub> selectRtnGoodsSubByRtnGoodsId(String rtnGoodsId);

	// 根据存货信息和子表序号查询退货单数量
	BigDecimal selectRtnGoodsSubQty(RtnGoodsSub rtnGoodsSub);

	// 根据子表序号修改退货单未开票数量
	int updateRtnGoodsUnBllgQtyByOrdrNum(Map map);

	// 根据子表序号查询退货单未开票数量
	BigDecimal selectRtnGoodsUnBllgQtyByOrdrNum(Map map);

	// 根据退货单子表序号查询退货子表信息,参照带数据时使用
	RtnGoodsSub selectRtnGoodsSubByRtGoIdAndOrdrNum(Map map);

	// 参照时根据退货单号及子表未开票数量批量查询子表信息
	List<RtnGoodsSub> selectRtnGoodsSubByRtnGoodsIdAndUnBllgQty(String rtnGoodsId);

	// 根据子表序号查询退货单中来源子表需要
	RtnGoodsSub selectRtnGoodsSubToOrdrNum(Long ordrNum);

}