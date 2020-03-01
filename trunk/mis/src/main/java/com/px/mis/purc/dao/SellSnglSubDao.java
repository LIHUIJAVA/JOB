package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.SellSnglSub;

public interface SellSnglSubDao {
	
    int deleteSellSnglSubBySellSnglId(String sellSnglId);

    int insertSellSnglSub(List<SellSnglSub> sellSnglSubList );
    //删除时候备份一份
    int insertSellSnglSubDl(List<String> list2);
    
    List<SellSnglSub> selectSellSnglSubBySellSnglId(String sellSnglId);
    
    //查询销售单中的数量
    BigDecimal selectSellSnglSubQty(SellSnglSub sellSnglSub);

	int insertSellSnglSubUpload(List<SellSnglSub> sellSnglSub);
	
	//按照存货、仓库、批号查询销售单子表信息
	SellSnglSub selectSellSnglSubByInvWhsBat(Map map);
	
	//根据销售单子表序号查询销售子表信息
	SellSnglSub selectSellSnglSubBySelSnIdAndOrdrNum(Map map);
	
	//修改销售单未开票数量
	int updateSellSnglUnBllgQtyByOrdrNum(Map map);
	//查询销售单未开票数量
	BigDecimal selectSellSnglUnBllgQtyByOrdrNum(Map map);
	//参照时根据销售单号批量查询子表信息
	List<SellSnglSub> selectSellSnglSubBySellSnglIdAndUnBllgQty(String sellSnglId);
	
	//修改销售单未开票数量
	int updateSellSnglRtnblQtyByOrdrNum(Map map);
	//查询销售单未开票数量
	BigDecimal selectRtnblQtyByOrdrNum(Map map);
	//参照时根据销售单号批量查询子表信息
	List<SellSnglSub> selectSellSnglSubByRtnblQty(List<String> sellSnglId);
    
}