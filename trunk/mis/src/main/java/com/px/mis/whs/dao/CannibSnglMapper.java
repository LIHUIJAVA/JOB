package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.CannibSngl;
import com.px.mis.whs.entity.CannibSnglMap;
import com.px.mis.whs.entity.CannibSnglSubTab;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.OthOutIntoWhs;

//调拨单表
public interface CannibSnglMapper {

    //1.添加调拨单
    public int insertCSngl(CannibSngl record);

    public int insertCSnglSubTab(List<CannibSnglSubTab> record);//批增子表

    //Ex添加调拨单
    public int exInsertCSngl(CannibSngl record);

    public int exInsertCSnglSubTab(List<CannibSnglSubTab> record);//批增子表

    //2.修改调拨单
    public int updateCSngl(CannibSngl record);

    //3.删除调拨单   批删
    public int deleteCSnglSubTab(String formNum);

    public int deleteAllCSngl(List<String> formNum);

    //4.简单查  调拨单
    public CannibSngl selectCSngl(@Param("formNum") String formNum);

    public List<CannibSnglSubTab> selectCSnglSubTabList(@Param("formNum") String formNum);

    //批查
    public List<CannibSngl> selectCSnglList(@Param("formNum") List<String> formNum);

    //分页查
    public List<CannibSnglMap> selectList(Map map);

    public int selectCount(Map map);

    //调拨单审核
    public int updateCSnglChk(List<CannibSngl> cList);

    public CannibSngl selectCanSnglChk(@Param("formNum") String formNum);//查询是否已审核

    //调拨单弃审
    public int updateCSnglNoChk(List<CannibSngl> cList);

    //查看是否有其他出入库
    public List<OthOutIntoWhs> selectOthIsDelete(@Param("srcFromNum") String srcFromNum);

    //PDA 入库单  已保存未审核的单据(查询)
    public List<CannibSngl> selectCSnglChkr();

    public int updateCSnglSubTab(CannibSnglSubTab cSubTab);
    //PDA 出库单  其他出入库中调拨出库的列表展示

    //打印
    public List<CannibSngl> selectListDaYin(Map map);

    //查询库存表的现存量  可用量
    public List<InvtyTab> selectInvty(Map map);

    //------------------------------
    //新增
//    public int updateAInvtyTab(List<InvtyTab> invtyTab);//可用量减少(减法)  转出仓库
//
//    public int updateAInvtyTabJia(List<InvtyTab> invtyTab);//可用量减少(减法)  转入仓库
//
//    //审核
//    public int updateInvtyTab(List<InvtyTab> invtyTab);//现存量减少(减法)
//
//    public int updateInvtyTabJia(List<InvtyTab> invtyTab);//现存量增加(加法)

    //弃审
//    public int updateInvtyTabNo(List<InvtyTab> invtyTab);//现存量减少(减法)
//
//    public int updateInvtyTabJiaNo(List<InvtyTab> invtyTab);//现存量增加(加法)
//
//    public int updateAInvtyTabNo(List<InvtyTab> invtyTab);//可用量减少(减法)  转入仓库
//
//    public int updateAInvtyTabJiaNo(List<InvtyTab> invtyTab);//可用量增加(加法)  转出仓库

    //查询库存表
//    InvtyTab selectInvtyTabByTermOut(InvtyTab invtyTab);//转出仓库
//
//    InvtyTab selectInvtyTabByTermIn(InvtyTab invtyTab);//转入仓库

    //添加调拨单删除后数据
    public Integer insertCSnglDl(List<String> formNum);

    public Integer insertCSnglSubTabDl(List<String> formNum);

}