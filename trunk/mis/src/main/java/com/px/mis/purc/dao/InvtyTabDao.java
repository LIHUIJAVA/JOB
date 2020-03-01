package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.TermBgnBal;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.whs.entity.InvtyTab;

public interface InvtyTabDao {
	
	//批量新增库存表
	int insertInvtyTabList(List<IntoWhsSub> invtySubList);
	//采购入库审核时库存表执行的操作
	int insertInvtyTabToIntoWhs(IntoWhsSub invtySub);//将采购入库单的子表信息 新增 到库存表中
	int updateInvtyTabJiaToIntoWhs(IntoWhsSub intoWhsSub); //采购入库审核时 修改 库存表数据 加
	int updateInvtyTabJianToIntoWhs(IntoWhsSub intoWhsSub);//采购入库审核时 修改 库存表数据 减
	int updateInvtyTabJiaToIntoWhsReturn(IntoWhsSub intoWhsSub); //采购退货审核时 修改 库存表数据 加
	int updateInvtyTabJianToIntoWhsReturn(IntoWhsSub intoWhsSub);//采购退货审核时 修改 库存表数据 减
	
	//到货单审核时库存表执行的操作
	int insertInvtyTabByToGdsSngl(ToGdsSnglSub toGdsSnglSub);//将到货单的子表信息新增到库存表中
	
	//普通销售退货单审核新增库存表信息
	int insertInvtyTabToRtnGoods(RtnGoodsSub rtnGoodsSub);
	//委托代销退货单审核新增库存表信息
    int insertInvtyTabToEnAgDelSub(EntrsAgnDelvSub entrsAgnDelvSub);
	
    int insertInvtyTabBySellOutWhsSub(SellOutWhsSub sellOutWhsSub);//销售出库单审核时新增库存数据
	int updateInvtyTabJiaToSellOutWhs(SellOutWhsSub sellOutWhsSub);//销售出库单审核时 修改 库存表数据 加
	int updateInvtyTabJianToSellOutWhs(SellOutWhsSub sellOutWhsSub);//销售出库单审核时 修改 库存表数据 减
	
	//现存量和可用量单个更新
	int updateInvtyTabNowStokJia(InvtyTab invtyTab);//现存量单个相加
	int updateInvtyTabNowStokJian(InvtyTab invtyTab);//现存量单个相减
	int updateInvtyTabAvalQtyJia(InvtyTab invtyTab);//可存量单个相加
	int updateInvtyTabAvalQtyJian(InvtyTab invtyTab);//可存量单个相减
	
	/**查询库存表根据采购入库子表 --带有排他锁 不建议查询使用**/
	@Deprecated
	InvtyTab selectInvtyTabByTerm(IntoWhsSub intoWhsSub);
	@Deprecated
	/** 查询库存表信息--带有for update锁 效率较低 不建议平时查询使用* */
	InvtyTab selectInvtyTabsByTerm(InvtyTab invtyTab);
	
	List<InvtyTab> selectInvtyTabTerm();
	BigDecimal sumNowStockIn(Map<String,Object> parmMap);
	TermBgnBal sumNowStockOut(InvtyTab tab);
	BigDecimal sumAvalStockIn(Map<String,Object> parmMap);
	BigDecimal sumAvalStockOut(Map<String,Object> parmMap);
	void insertInvtyTab(List<InvtyTab> itList);
	
	List<TermBgnBal> selectInvtyTabIn();
	TermBgnBal sumTerm(InvtyTab it);
	
	void dealUpdate(InvtyTab it);
	
//	//现存量批量增加(减法)
//	int updateInvtyTabList(List<InvtyTab> invtyTab);
//	//现存量批量减少(减法)
//	int updateInvtyTabJiaList(List<InvtyTab> invtyTab); 
	
}