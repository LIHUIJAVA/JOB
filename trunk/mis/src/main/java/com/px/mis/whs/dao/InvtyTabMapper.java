package com.px.mis.whs.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.BaoZhiQi;
import com.px.mis.whs.entity.InvldtnDt;
import com.px.mis.whs.entity.InvtyStandReport;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.NowStokReport;
import com.px.mis.whs.entity.NtChkNo;
import com.px.mis.whs.entity.OutIngWaterReport;
import com.px.mis.whs.entity.TransceiverSummaryReport;

@SuppressWarnings("rawtypes")
public interface InvtyTabMapper {


    //保质期预警
    public List<BaoZhiQi> selectBaoZhiQiList(Map map);

    public int selectBaoZhiQiCount(Map map);

    public BaoZhiQi selectBaoZhiQiSUM(Map map);

    //收发存汇总
    public List<TransceiverSummaryReport> selectTSummaryList(Map map);

    public int selectTSummaryCount(Map map);

    public TransceiverSummaryReport selectTSummary(Map map);

    public TransceiverSummaryReport selectTSummaryWhs(TransceiverSummaryReport report);

    public List<InvtyTab> selectothOutIntoWhsInto(Map map);

    public List<InvtyTab> selectothOutIntoWhsOut(Map map);

    //收发存汇总库存期初
    public TransceiverSummaryReport selectInvtyTermBgnTab(Map map);


    public List<InvtyTab> selectothIntoWhs(Map map);

    public List<InvtyTab> selectothSellOutWhs(Map map);

    //现存量查询
    public List<NowStokReport> selectExtantQtyList(Map map);

    public NowStokReport selectExtantQtySUM(Map map);

    public List<InvtyTab> selectToGdsSngl(Map map);

    public List<InvtyTab> selectSellSngl(Map map);

    public List<InvtyTab> selectSellOutWhs(Map map);

    public List<InvtyTab> selectCannibSngl(Map map);

    public List<InvtyTab> selectIntoWhsSub(Map map);

    public List<InvtyTab> selectNoIntoWhsSub(Map map);

    public List<InvtyTab> selectOutIntoWhsOut(Map map);

    public List<InvtyTab> selectOutIntoWhsIn(Map map);


    public NowStokReport selectWhsSub(NowStokReport nowStokReport);

    public int selectExtantQtyCount(Map map);

    //出入库流水
    public List<OutIngWaterReport> selectOutIngWaterList(Map map);

    public int selectOutIngWaterCount(Map map);

    public OutIngWaterReport selectOutIngWaterSUM(Map map);

    //    出入库类型
    public List<Map> outIntoWhsTyp(Map map);

    //库存台账
    public List<Map> selectInvtyStandList(Map map);

    public List<InvtyStandReport> selectOutIngWater(Map map);

    public InvtyStandReport selectOutIngSUM(Map map);

    public int selectInvtyStandCount(Map map);

    public InvtyTab selectInvtyTabSum(Map invtyTab);

    public List<Map> selectInvty(Map map);//查询所有存货信息  分组返回前端

    //=====zds create=================================
    public void updateByReverse(InvtyTab invtyTab);//逆向操作修改库存时添加

    public InvtyTab selectByReverse(InvtyTab invtyTab);//逆向操作修改库存时添加

    //--------hj---库存表查询------------------------------------------------------------
    List<InvtyTab> selectInvtyTabList(Map map);

    int selectInvtyTabCount(Map map);

    //  失效日期维护
    public List<InvldtnDt> selectInvldtnDtList(Map map);

    public InvldtnDt selectInvldtnDtSUM(Map map);

    public int selectInvldtnDtCount(Map map);
//	// 批次汇总表
//	public List<InvldtnDt> selectBatNumSummaryList(Map map);
//	public int selectBatNumSummaryCount(Map map);

    //  未审核单据
    public List<NtChkNo> selectNtChkNoList(Map map);

    public int selectNtChkNoCount(Map map);

    public int exInvtyTab(List<InvtyTab> values);
}