package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.MergePickSngl;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.PickSnglSubTab;
import com.px.mis.whs.entity.PickSnglTab;

public interface PickSnglService {

    //查询销售单
    public String selectSellById(Map sellSnglId) throws Exception;

    //添加拣货单(不要了  和查询销售单合并了)
//	public String insertPickSngl(PickSnglTab pickTab,List<PickSnglSubTab> pickList);
    public String insertPickSngl(String pickTab, String pickList, String userName,String loginTime);

    //拣货单主列表显示
    public String queryAllList(Map map);

    //通过主表拣货单据号查询子表信息
    public String selectPSubTabById(String pickSnglNum);

    //拣货单主-子列表显示
    public String queryList(Map map);

    //合并拣货单
    public String selectPSubTab(String userId, String pickSnglNum, String whsEncd);

    //删除拣货 合并单
    public String deleteMerPickSngl(List<MergePickSngl> mSnglList);

    public String deletePickSngl(String mSnglList);

    //PDA  显示所有拣货单列表-
    public String selectAllMerge(String whsEncd);

    public String selectAllById(String pickSnglNum);

    //PDA 回传接口  拣货完成标识  拣货完成时间
    public String updatePTabPda(PickSnglTab pSnglTab, List<MovBitTab> list);

    //pc 回传接口  拣货完成标识  拣货完成时间
    public String updatePTabPC(PickSnglTab pSnglTab);

    //打印
    public String queryListPrint(Map map);
}
