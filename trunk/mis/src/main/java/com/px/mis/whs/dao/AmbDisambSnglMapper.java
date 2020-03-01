package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.AmbDisambSngl;
import com.px.mis.whs.entity.AmbDisambSnglMap;
import com.px.mis.whs.entity.AmbDisambSnglubTab;


public interface AmbDisambSnglMapper {

    //新增组装拆卸单 
    public int insertAmbDisambSngl(AmbDisambSngl record);

    public int insertAmbDisambSnglubTab(List<AmbDisambSnglubTab> aList);//批增子表

    public int exInsertAmbDisambSngl(AmbDisambSngl record);

    public int exInsertAmbDisambSnglubTab(List<AmbDisambSnglubTab> aList);//批增子表

    //修改组装拆卸单
    public int updateAmbDisambSngl(AmbDisambSngl record);

    //删除组装拆卸单
    public int deleteAmbDisambSnglubTab(String formNum);

    public int deleteAllAmbDisambSngl(List<String> formNum);

    //简单查 组装拆卸单
    public AmbDisambSngl selectAmbDisambSngl(@Param("formNum") String formNum);

    public List<AmbDisambSnglubTab> selectAmbDisambSnglubTabList(@Param("formNum") String formNum);

    //批查
    public List<AmbDisambSngl> selectAmbDisambSnglList(@Param("formNum") List<String> formNum);

    //分页查
    public List<AmbDisambSnglMap> selectList(Map map);

    public int selectCount(Map map);

    //审核
    public int updateASnglChk(List<AmbDisambSngl> cList);

    public AmbDisambSngl selectISChr(@Param("formNum") String formNum);//查询是否已审核

    //弃审
    public int updateASnglNoChk(List<AmbDisambSngl> cList);

    //打印
    public List<AmbDisambSngl> selectListDaYin(Map map);

    //新增组装拆卸单删除后数据
    public Integer insertAmbDisambSnglDl(List<String> list);

    public Integer insertAmbDisambSnglubTabDl(List<String> list);//批增子表


}