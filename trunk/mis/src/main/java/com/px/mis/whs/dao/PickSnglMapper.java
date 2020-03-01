package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.whs.entity.MergePickSngl;
import com.px.mis.whs.entity.PickSnglSubTab;
import com.px.mis.whs.entity.PickSnglTab;
import com.px.mis.whs.entity.SellSnglWhs;

//拣货单
public interface PickSnglMapper {

    //修改销售订单的是否拣货标识
    public int updateSellSngl(@Param("list") List<String> sellSngl, @Param("ispick") String ispick);

    public int selectSellSngl(@Param("list") List<String> sellSngl, @Param("ispick") String ispick);

    public List<String> selectSellSnglTab(String sellSngl);

    //查询销售单
    public List<SellSngl> selectSellById(@Param("sellId") List<String> sellSnglId);

    public List<SellSngl> selectDistinctId(@Param("sellId") List<String> sellSnglId);

    // 根据生成拣货单
    public List<SellSnglWhs> selectSellWhsIsPick(Map whs);

    public int selectSellWhsIsPickCount(Map whs);

    public List<PickSnglSubTab> selectDistinctWhs(List<String> sellSngl);

    public int updateSellSnglWhs(@Param("list") List<String> sellSngl, @Param("number") String Number);


    //查询拣货单号是否已存在
    public PickSnglTab selectPick(@Param("pickSnglNum") String pickSnglNum);

    //添加拣货单
    public int insertPickSngl(PickSnglTab pickTab);

    public int insertPickSnglSubTab(List<PickSnglSubTab> pickList);

    //拣货单主列表显示
    public List<PickSnglTab> selectAllPickSngl(Map map);

    public int selectAllPickCount(Map map);

    //通过主表拣货单据号查询子表信息
    public List<PickSnglSubTab> selectPSubTabById(@Param("pickSnglNum") String pickSnglNum);

    //拣货单主-子列表显示(不要了)
    public List<PickSnglTab> selectList(Map map);

    public int selectCount(Map map);

    //合并拣货单
    public List<PickSnglSubTab> selectPSubTab(@Param("pickSnglNum") String pickSnglNum,
                                              @Param("whsEncd") String whsEncd);

    public int insertmergePick(List<MergePickSngl> mPickList);

    //删除合并拣货单
    public int updateSellSnglZero(SellSngl sellSngl);

    public int deleteMerPickSngl(String pickSnglNum);//删除合并拣货单(单据号)

    public int deletePickSnglTab(PickSnglSubTab pSnglSubTab);//删除拣货单(单据号 仓库编码 存货编码 批号)

    public int selectPSubTabByIdCount(@Param("pickSnglNum") String pickSnglNum);//selectPSubTabById 查询子表中还是否有主表单据号（1） 如果有则删除子表单条（2） 如果没有则删除子表+主表

    public int deletePickSngl(String pickSnglNum);

    public PickSnglSubTab selectSellId(PickSnglSubTab pSubTab);//通过拣货单子表字段查询销售单号

    //PDA  显示所有拣货单列表-
    public List<PickSnglTab> selectAllMerge(List<String> list);

    public List<MergePickSngl> selectAllById(@Param("pickSnglNum") String pickSnglNum);

    public int updatePTab(PickSnglTab pSnglTab);

    //PDA 回传接口  拣货完成标识  拣货完成时间
    public int updatePTabPda(PickSnglTab pSnglTab);

    public int updateJHTabPda(MergePickSngl mergePickSngl);

    public int deleteMergePick(String pickSnglNum);//如果拣货完成则删除拣货合并表数据

    //=======================zds create====================================
    public int deletePickSnglTabBySellSnglId(String sellSnglId);//根据销售单号删除拣货单

    public int updateTabPda(MergePickSngl mergePickSngl);//pda单个拣货单拣货

    public int selectPickSnglSubTabCount(MergePickSngl mergePickSngl);//pda单个拣货单拣货未拣货计数


    /**
     * 根据拣货单号查询出库单
     */
    List<SellOutWhsSub> selectPickSnglOutTabList(String pickSnglNum);


    //添加拣货单
    public Integer insertPickSnglDl(List<String> list);

    public Integer insertPickSnglSubTabDl(List<String> list);
}
