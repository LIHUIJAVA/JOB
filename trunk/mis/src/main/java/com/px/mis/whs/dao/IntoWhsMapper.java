package com.px.mis.whs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.RefuseReason;

public interface IntoWhsMapper {

    // 1.采购入
    public List<ToGdsSngl> selectToGdsSnglList(List<String> whsEncd);// 查询所有到货单
    // --新增到货拒收单信息

    public int insertToGdsSngl(ToGdsSngl toGdsSngl);

    public int insertToGdsSnglSub(List<ToGdsSnglSub> toGdsSnglSubList);

    // 按照编号查询到货单信息
    public ToGdsSngl selectToGdsSnglByToGdsSnglId(@Param("toGdsSnglId") String toGdsSnglId);

    // 查询是否含有到货单编号
    public ToGdsSngl selectToGdsSnglId(@Param("toGdsSnglId") String toGdsSnglId);

    // --新增入库单
    public int insertIntoWhs(IntoWhs intoWhs);

    public int insertIntoWhsSub(List<IntoWhsSub> intoWhsSubList);

    // 到货单有一个处理状态（处理中-处理完成） 到货单完成新增入库单
    public int updateTGdsGngl(List<ToGdsSnglSub> toGdsSngl);

    // 按照采购入库单编号查询采购入库单信息
    public IntoWhs selectIntoWhsByIntoWhsSnglId(@Param("intoWhsSnglId") String intoWhsSnglId);

    // 查询是否含有采购入库单编号
    public IntoWhs selectIntoWhsId(@Param("intoWhsSnglId") String intoWhsSnglId);

    // 入库 推荐货位
    public List<MovBitTab> selectIntogBit(MovBitTab oBitTab);

    // 出库 推荐货位
    public List<MovBitTab> selectOutgBit(MovBitTab oBitTab);

    // 2.销售出
    public List<SellOutWhs> selectSellOutWhsList(List<String> whsEncd);// 查询销售出库单
    // 审核销售出库单

    public int updateSOutWhs(SellOutWhs sellOutWhs);

    // 查询拒收原因
    public List<RefuseReason> selectReason();

    /**
     * 序号查子表 到货单
     *
     * @param ordrNum 序号
     * @return ToGdsSnglSub 明细表
     */
    public ToGdsSnglSub selectToGdsByOrdrNumKey(@Param("ordrNum") Long ordrNum);

    /**
     * 序号查子表 销售出库单
     *
     * @param ordrNum 序号
     * @return ToGdsSnglSub 明细表
     */
    public SellOutWhsSub selectSellByOrdrNumKey(@Param("ordrNum") Long ordrNum);

    /**
     * 红字出库单查询
     */
    public List<SellOutWhs> selectRedSellOutWhsList(List<String> list);

    /**
     * 红字入库
     */
    public List<IntoWhs> selectIntoWhsList(List<String> whsEncd);

}
