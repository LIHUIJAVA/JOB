package com.px.mis.whs.service;

import java.util.List;

import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;

public interface IntoWhsService {

    //1.采购入
    public String selectToGdsSnglList(String whsEncd);//查询所有到货单

    //新增到货拒收单信息
    public String insertToGdsSngl(String userId, ToGdsSngl toGdsSngl, List<ToGdsSnglSub> toGdsSnglSubList);

    //新增入库单
    public String addIntoWhs(String userId, String userName,
                             IntoWhs intoWhs, List<IntoWhsSub> intoWhsSubList,
                             List<InvtyTab> invtyTab, List<MovBitTab> movBitTab,
                             ToGdsSngl toGdsSngl, List<ToGdsSnglSub> tSnglSubList) throws Exception;


    //2.销售出
    public String selectSellOutWhsList(String whsEncd);//查询销售出库单

    //回传销售出库单
    public String updatesOutWhs(SellOutWhs sellOutWhs, List<SellOutWhsSub> sellOutWhsSub,
                                List<InvtyTab> invtyTab, List<MovBitTab> movBitTab);

    //拒收原因
    public String selectReason();

    //红字出库单
    public String selectRedSellOutWhsList(String whsEncd);

    //红字出库单(回传)
    public String updatesRedOutWhs(SellOutWhs sOutWhs, List<MovBitTab> list);

    // 查询红字入库单
    String selectIntoWhsList(String whsEncd);

    //红字入库单(回传)
    public String updatesRedIntoWhs(IntoWhs intoWhs, List<MovBitTab> list);
}
