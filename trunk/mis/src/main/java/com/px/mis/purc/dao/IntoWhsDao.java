package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.service.impl.PurcIntoWhsServiceImpl;
import com.px.mis.purc.service.impl.PurcIntoWhsServiceImpl.zizhu;
import com.px.mis.whs.service.impl.IntoWhsServiceImpl;

public interface IntoWhsDao {

	
    int deleteIntoWhsByIntoWhsSnglId(String intoWhsSnglId);

    int insertIntoWhs(IntoWhs intoWhs);
    //刪除時候，備份到廢棄表一份
    int insertIntoWhsDl(List<String> list2);
    
    int insertIntoWhsUpload(List<IntoWhs> intoWhs);

    int updateIntoWhsByIntoWhsSnglId(IntoWhs intoWhs);
    
    IntoWhs selectIntoWhsByIntoWhsSnglId(String intoWhsSnglId);
    
    IntoWhs selectIntoWhsById(String intoWhsSnglId);
    
    List<IntoWhs> selectIntoWhsList(Map map);
    
    List<PurcIntoWhsServiceImpl.zizhu> selectIntoWhsListOrderBy(Map map);
	
	int selectIntoWhsCount(Map map);
	
	int selectIntoWhsCountLess(Map map);
	
	int deleteIntoWhsList(List<String> intoWhsSnglId);
	
	int updateIntoWhsIsNtChk(List<IntoWhs> intoWhs);
	
	int updateIntoWhsIsNtChkByIntoWhs(IntoWhs intoWhs);
	
	List<zizhu> printingIntoWhsList(Map map);
	
	//查询采购入库单的审核状态
	int selectIntoWhsIsNtChk(String intoWhsSnglId);
	//查询采购入库单的记账状态
	int selectIntoWhsIsNtBookEntry(String intoWhsSnglId);
	//查询采购入库单的开发票状态
	int selectIntoWhsIsNtBllg(String intoWhsSnglId);
	//查询采购入库单的结算状态
	int selectIntoWhsIsNtStl(String intoWhsSnglId);
	//查询采购入库单的退货状态
	int selectIntoWhsIsNtRtn(String intoWhsSnglId);
	
	//根据到货单编号查询入库单信息
	List<IntoWhs> selectIntoWhsByToGdsSnglId(String ToGdsSnglId);
	
	//采购入库单明细查询
	List<Map> selectIntoWhsByInvtyEncd(Map map);
	
	int selectIntoWhsByInvtyEncdCount(Map map);
	
	//采购订收货统计表展示
	List<Map> selectIntoWhsAndPursOrdr(Map map);
	
	/**
	 *  查询未记账的采购入库单
	 */
	List<IntoWhs> selectIntoWhsIsInvty(Map map);
	
	//修改采购入库单的记账状态
	int updateIntoWhsBookEntryList(List<IntoWhs> intoWhs);
	
	//修改开票状态为已开
	int updateIntoWhsIsNtBllgOK(String intoWhsSnglId);
	//修改开票状态为未开
    int updateIntoWhsIsNtBllgNO(String intoWhsSnglId);
	
	//修改结算状态
	int updateIntoWhsIsNtStl(String intoWhsSnglId);
	//分页
	List<IntoWhs> selectIntoWhsStream(Map map);
	int countSelectIntoWhsStream(Map map);
	
	//查询最近一次入库单价
	BigDecimal selectUnTaxUprc(Map map);
	//查询采购入库单原单的开票数量
	List<IntoWhs> selectIntoWhsByInvty(Map<String, Object> map);
	
	//参照时查询主表信息
    List<IntoWhs> selectIntoWhsLists(Map map);
	int selectIntoWhsCounts(Map map);

	Map selectIntoWhsListSum(Map map);

	Map selectIntoWhsAndPursOrdrSums(Map map);
	
	// 查询采购订单对应的采购入库单中数量为多少
	List<Map> selectIntoWhsQtyByPursOrdrId(Map map);
	//入库明细表-导出接口
	List<Map> selectIntoWhsByInvtyEncdPrint(Map map);

	
	
	
	
}