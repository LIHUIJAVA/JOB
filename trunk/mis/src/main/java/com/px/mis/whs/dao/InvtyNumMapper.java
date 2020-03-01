package com.px.mis.whs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.OthOutIntoWhs;

//计算库存表数量
public interface InvtyNumMapper {

    //回传数据  指定货位 【库存表需要出库增加可用量  修改出入库单的入库状态[处理中--处理完成]】
    public int updateInvtyTab(List<InvtyTab> invtyTab);//现存量减少(减法)

    public int updateInvtyTabJia(List<InvtyTab> invtyTab);//现存量增加(加法)

    public int updateAInvtyTab(List<InvtyTab> invtyTab);//可用量减少(减法)

    public int updateAInvtyTabJia(List<InvtyTab> invtyTab);//可用量增加(加法)

    //修改库存表的数量  金额等信息
    public int updateInvtyTabAmtJia(List<InvtyTab> invtyTab);//其他出入库审核时更新库存表 现存量增加

    public int updateInvtyTabAmt(List<InvtyTab> invtyTab);//其他出入库审核时更新库存表 现存量减少

    public List<MovBitTab> selectAllMbit(List<MovBitTab> movBitTab);//查出入库单分配的货位是否原有货位（有则增加数量，未有则增加新货位）

    public int insertMovBitTab(List<MovBitTab> movBitTab);//新增移位表

    //public InvtyTab selectInvtyTab(InvtyTab invtyTab);//查询现存量--
    public List<GdsBit> selectRegn(@Param("gdsBitEncd") String gdsBitEncd);//通过货位查询区域--

    public int updateMovbitTab(List<MovBitTab> movBitTab);//修改移位表数量(减法)

    public int updateMovbitTabJia(List<MovBitTab> movBitTab);//修改移位表数量(加法)

    //查询库存表
    InvtyTab selectInvtyTabByTerm(InvtyTab invtyTab);

    /**
     * 新增库存表
     * <p>
     * 审核 新建库存 会更改金额值
     */
    int insertInvtyTabList(List<InvtyTab> invtyTabList);

    public List<InvtyTab> selectInvtyTabByBatNum(InvtyTab invtyTabList);


    public MovBitTab selectMbit(MovBitTab movBitTab);//查出入库单分配的货位是否原有货位（有则增加数量，未有则增加新货位）

    public int updateJiaMbit(MovBitTab movBitTab);//有则增加数量

    public int updateJianMbit(MovBitTab movBitTab);//未有则增加

    public int insertMovBitTabJia(MovBitTab movBitTab);//新增移位表
    //删除
//	public int insertMovBitTabJia(MovBitTab movBitTab);//新增移位表


}
