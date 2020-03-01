package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.purc.entity.BeiYong;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.service.impl.SellSnglServiceImpl.zizhu;

public interface SellSnglDao {
	
    int deleteSellSnglBySellSnglId(String sellSnglId);

    int insertSellSngl(SellSngl sellSngl);
    //删除时候备份一份
    int insertSellSnglDl(List<String> lists2);

    SellSngl selectSellSnglBySellSnglId(String sellSnglId);
    
    SellSngl selectSellSnglById(String sellSnglId);

    int updateSellSnglBySellSnglId(SellSngl sellSngl);

    List<SellSngl> selectSellSnglList(Map map);
    //分页+排序
    List<zizhu> selectSellSnglListOrderBy(Map map);
	
	int selectSellSnglCount(Map map);
	
	int deleteSellSnglList(List<String> sellSnglId);
	
	//批量更新审核状态
	int updateSellSnglIsNtChkList(List<SellSngl> sellSngl);
	//单个更新审核状态
	int updateSellSnglIsNtChk(SellSngl sellSngl);
	//查询审核状态
	int selectSellSnglIsNtChk(String sellSnglId);
	//查询拣货状态
	int selectSellSnglIsPick(String sellSnglId);
	
	
	List<zizhu> printingSellSnglList(Map map);
	
	//销售单弃审时删除物流表中的信息
	int deleteLogisticsTab(String saleEncd);
	
	//===========================================================
	SellSngl selectSellSnglByOrderId(String orderId);
	
	//查询销售明细表
    List<BeiYong> selectSellSnglInvtyEncd(Map map);
	int selectSellSnglInvtyEncdCount(Map map);

	int insertSellSnglUpload(List<SellSngl> sellSnglList);
	
	//根据销售单号查询物流表信息
	LogisticsTab selectLogisticsTabBySellSnglId(String sellSnglId);
	
	int updateSellSnglIsNtBllgOK(String sellSnglId);
	
	int updateSellSnglIsNtBllgNO(String sellSnglId);
	
	//查询销售出库单子表出库单号 
	List<String> selectB(Map map);
	
	int updateA(String sellSnglId);
	
	SellSngl selectSellSnglAndSubBySellSnglId(String sellSnglId);
	
	List<Map> selectXSTJ(Map map);//销售统计表查询
	Integer selectXSTJCount(Map map);//查询销售统计表总条数
	
    List<InvtyDetail> selectSalesCountList(Map map);
	int countSalesCountList(Map map);
	BigDecimal selectSalesCost(Map map); //销售成本
	
	//查询销售发货主表信息，退货时参照时使用
	List<SellSngl> selectSellSnglListToCZ(Map map);
	int selectSellSnglListToCZCount(Map map);
	
	//查询销售发货主表信息，开票参照时使用
	List<SellSngl> selectSellSnglCZLists(Map map);
	int selectSellSnglCZListsCount(Map map);

	Map selectSellSnglListSums(Map map);

	Map selectSellSnglInvtyEncdSums(Map map);

}