package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruMap;
import com.px.mis.whs.entity.ProdStruSubTab;

public interface ProdStruMapper {

    //1.新增产品结构
    public int insertProdStru(ProdStru record);

    public int insertProdStruSubTab(List<ProdStruSubTab> pList); //批增子表

    //产品结构
    public int exIinsertProdStru(ProdStru record);

    //    public int exInsertProdStruSubTab(List<ProdStruSubTab> pList); //批增子表
    //2.修改产品结构
    public int updateProdStru(ProdStru record);

    //3.删除产品结构   单删、批删除
    public int deleteProdStruSubTab(String ordrNumMom);

    public int deleteAllProdStru(List<String> ordrNum);

    //4.简单查  产品结构
    public List<ProdStru> selectProdStruList(List<ProdStru> cList);

    //简单查  产品结构
    public List<ProdStru> selectMomEncdAmbDisambSngl(Map map);

    public int countMomEncdAmbDisambSngl(Map map);

    public ProdStru selectMomEncd(@Param("momEncd") String Momencd);

    public ProdStru selectProdStru(@Param("ordrNum") String ordrNum);

    public List<ProdStruSubTab> selectProdStruSubTabList(@Param("ordrNum") String ordrNum);

    //分页查
    public List<ProdStru> selectList(Map map);

    public int selectCount(Map map);

    //查询 获取该母件的产品结构信息
    public List<ProdStru> selectProdStruByMom(@Param("ordrNum") String ordrNum);


    //审核
    public int updatePStruChk(List<ProdStru> cList);

    //弃审
    public int updatePStruNoChk(List<ProdStru> cList);

    //打印
    public List<ProdStruMap> queryListPrint(Map map);
}