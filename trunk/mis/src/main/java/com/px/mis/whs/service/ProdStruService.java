package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;

public interface ProdStruService {


    //新增产品结构
    public String insertProdStru(ProdStru prodStru, List<ProdStruSubTab> pList);

    //修改产品结构
    public String updateProdStru(ProdStru prodStru, List<ProdStruSubTab> pList);

    //删除产品结构
    //批量删
    public String deleteAllProdStru(String ordrNum);

    //简单查  产品结构
    public String query(String ordrNum);//返回详情信息（主：实体；子：集合）

    public String queryList(Map map);//分页查询

    //查询 获取该母件的产品结构信息
    public String selectProdStruByMom(String ordrNum);

    //审核
    public String updatePStruChk(List<ProdStru> cList);

    //弃审
    public String updatePStruNoChk(List<ProdStru> cList);

    public String uploadFileAddDb(MultipartFile file);

    //打印
    public String queryListPrint(Map map);

    public String queryAmbDisambSngl(Map map);//返回能组装的详情信息（主：实体；子：集合）

}
