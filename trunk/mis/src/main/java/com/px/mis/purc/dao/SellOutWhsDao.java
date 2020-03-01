package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntrySub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.impl.SellOutWhsServiceImpl.zizhu;

public interface SellOutWhsDao {
	
	Integer deleteSellOutWhsByOutWhsId(String outWhsId);
	
	Integer deleteSellOutWhsBySellOrdrInd(String sellOrdrInd);

    Integer insertSellOutWhs(SellOutWhs sellOutWhs);
//    删除之前，备份一份
    Integer insertSellOutWhsDl(List<String> lists2);
    
    Integer insertSellOutWhsUpload(SellOutWhs sellOutWhs);
    
    Integer batchInsertSellOutWhs(List<SellOutWhs> sellOutWhsList);

    SellOutWhs selectSellOutWhsByOutWhsId(String outWhsId);
    
    SellOutWhs selectSellOutWhsById(String outWhsId);

    Integer updateSellOutWhsByOutWhsId(SellOutWhs sellOutWhs);

    List<SellOutWhs> selectSellOutWhsList(Map map);
    //分页+排序
    List<com.px.mis.purc.service.impl.SellOutWhsServiceImpl.zizhu> selectSellOutWhsListOrderBy(Map map);
    
    Integer selectSellOutWhsCount(Map map);
	
    Integer deleteSellOutWhsList(List<String> outWhsId);
	
	Integer updateSellOutWhsIsNtChkList(List<SellOutWhs> sellOutWhsList);
	
	Integer updateSellOutWhsIsNtChk(SellOutWhs sellOutWhs);
	
	Integer selectIsNtBllgByOutWhsId(String outWhsId);
	
	Integer selectIsNtStlByOutWhsId(String outWhsId);
	
	Integer selectIsNtBookEntryByOutWhsId(String outWhsId);
	
	Integer selectIsNtRtnGoodByOutWhsId(String outWhsId);
	
	List<zizhu> printingSellOutWhsList(Map map);
	//根据销售标识查询销售出库单的审核状态
	Integer selectIsNtChkBySellOrdrInd(String sellOrdrInd);
	//根据销售出库单号查询销售出库单的审核状态
	Integer selectIsNtChkByOutWhsId(String outWhsId);
	//根据销售标识查询销售出库单编号
	List<SellOutWhs> selectOutWhsIdBySellOrdrInd(String SellOrdrInd);
	//根据退货单编号查询销售出库单中的信息
	SellOutWhs selectSellOutWhsByRtnGoodsId(String rtnGoodsId);
	//根据销售标识查询销售出库单主子表中的数据
	SellOutWhs selectSellOutWhsBySellOrdrInd(String SellOrdrInd);
	
	//根据仓库编码、存货编码、批号查子表数据 
	List<BigDecimal> selectSellOutWhsSubByInWhBn(Map map);
	
	//=================================zds create=================================
	SellOutWhs selectSellOutWhsBySellSnglId(String sellSnglId);
	
	//出库明细表查询
    List<Map> selectSellOutWhsByInvtyEncd(Map map);
    Integer selectSellOutWhsByInvtyEncdCount(Map map);
	
	/**
	 *  查询未记账的销售出库单
	 */
	List<SellOutWhs> selectSellOutWhsIsInvty(Map map);
	
	//批量修改记账状态
	Integer updateSellOutWhsBookEntryList(List<SellOutWhs> sellOutWhsList);
	
	//根据委托代销退货单和销售出库单的仓库编码、存货编码、批号查子表数据 
	List<BigDecimal> selectSellOutWhsSubByEntAgnDel(Map map);
	//查询未记账的销售出库单分页
	List<SellOutWhs> selectSellOutWhsStream(Map map);
	int countSellOutWhsIsInvty(Map map);
	//记账时回填销售成本
	void updateCost(List<FormBookEntrySub> subList);
	//根据来源子表序号查询无税单价(退货单审核时使用)
	BigDecimal selectSellOutWhsSubByToOrdrNum(Map map);
	
	//只返回主子表信息
	SellOutWhs selectSellOutWhsAndSub(String outWhsId);

	Map selectSellOutWhsListSums(Map map);

	Map selectSellOutWhsByInvtyEncdSums(Map map);
	//出库明细表-导出
	List<?> selectSellOutWhsByInvtyEncdPrint(Map map);
	
}