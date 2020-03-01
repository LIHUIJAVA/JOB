package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckPrftLossMap;
import com.px.mis.whs.entity.CheckPrftLossSubTab;


public interface CheckPrftLossMapper {

    //1.新增盘点损益表
    public int insertCheckSnglLoss(CheckPrftLoss record);

    public int insertCheckSnglLossSubTab(List<CheckPrftLossSubTab> record);

    //2.修改盘点损益表
    public int updateCheckSnglLoss(CheckPrftLoss record);

    //3.删除盘点损益表   批删
    public int deleteCheckSnglSubLossTab(String checkFormNum);

    public int deleteAllCheckSnglLoss(List<String> checkFormNum);

    //查询
    public CheckPrftLoss selectCheckSnglLoss(@Param("checkFormNum") String checkFormNum);

    public List<CheckPrftLossSubTab> selectCheckSnglLossSubTab(@Param("checkFormNum") String checkFormNum);
    //查询来源
    public CheckPrftLoss selectSrcCheckSnglLoss(@Param("srcFormNum") String srcFormNum);

    //批查
    public List<CheckPrftLoss> selectCSnglLossList(@Param("checkFormNum") List<String> checkFormNum);

    //分页查
    public List<CheckPrftLossMap> selectList(Map map);

    public int selectCount(Map map);

    //审核
    public int updateCSnglLossChk(List<CheckPrftLoss> cList);

    public CheckPrftLoss selectIsChr(@Param("checkFormNum") String checkFormNum);//查询是否审核

    //弃审
    public int updateCSnglLossNoChk(List<CheckPrftLoss> cList);

    //打印
    public List<CheckPrftLoss> selectListDaYin(Map map);

    //修改盘点损益子表
    public int updateCheckPrftLossSubTab(List<CheckPrftLossSubTab> record);

    //盘点损益表删除后数据
    public Integer insertCheckSnglLossDl(List<String> checkFormNum);

    public Integer insertCheckSnglLossSubTabDl(List<String> checkFormNum);

    public Integer exInsertCSngl(CheckPrftLoss value);

}