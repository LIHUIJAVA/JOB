package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckSngl;
import com.px.mis.whs.entity.CheckSnglMap;
import com.px.mis.whs.entity.CheckSnglSubTab;
import com.px.mis.whs.entity.InvtyTab;


public interface CheckSnglMapper {

    //1.新增盘点单
    public int insertCheckSngl(CheckSngl record);

    public int insertCheckSnglSubTab(List<CheckSnglSubTab> record);

    //导入
    public int exInsertCheckSngl(CheckSngl record);

    public int exInsertCheckSnglSubTab(List<CheckSnglSubTab> record);

    //2.修改盘点单
    public int updateCheckSngl(CheckSngl record);

    //3.删除盘点单   批删
    public int deleteCheckSnglSubTab(String checkFormNum);

    public int deleteAllCheckSngl(List<String> checkFormNum);

    //查询
    public CheckSngl selectCheckSngl(@Param("checkFormNum") String checkFormNum);

    public List<CheckSnglSubTab> selectCheckSnglSubTab(@Param("checkFormNum") String checkFormNum);

    //批查
    public List<CheckSngl> selectCheckSnglsList(@Param("checkFormNum") List<String> checkFormNum);

    //分页查
    public List<CheckSnglMap> selectList(Map map);

    public int selectCount(Map map);

    //打印
    public List<CheckSngl> selectListDaYin(Map map);

    //通过仓库、盘点批号、存货大类编码、失效日期日、账面为零时是否盘点
    //查询盘点单列表（存货编码等信息）
    public List<CheckSngl> selectCheckSnglList(Map map);

    public List<CheckSngl> selectCheckSnglListZero(Map map);

    //审核
    public int updateCSnglChk(List<CheckSngl> cList);

    public CheckSngl selectIsChkr(@Param("checkFormNum") String checkFormNum);//查看是否审核

    public List<CheckSnglSubTab> selectCheckQty(@Param("checkFormNum") String checkFormNum);//查询实盘数是否全部填写

    //弃审
    public int updateCSnglNoChk(List<CheckSngl> cList);

    public List<CheckPrftLoss> selectLossIsDel(@Param("srcFormNum") String srcFormNum);//盘点损益表是否已经删除

    //PDA 返回盘点参数
    public List<CheckSngl> checkSnglList(@Param("whsEncd") List<String> whsEncd);

    //pda 根据子表序号
    public int updateCheckTab(List<CheckSnglSubTab> cSubTabList);

    public int updateCheckStatus(CheckSngl checkSngl);

    public List<CheckSnglSubTab> selectOrdNum();//查询盘点中的子表

    //下载之后锁定状态  不让PC段进行更改  保证数据同步
    public int updateStatus(List<CheckSngl> cList);

    public List<InvtyTab> selectCheckSnglGdsBitList(Map map);

    //新增盘点单删除后数据
    public Integer insertCheckSnglDl(List<String> checkFormNum);

    public Integer insertCheckSnglSubTabDl(List<String> checkFormNum);
}