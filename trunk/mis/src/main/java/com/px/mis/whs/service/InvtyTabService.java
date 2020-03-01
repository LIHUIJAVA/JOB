package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.InvtyTab;

public interface InvtyTabService {

    //保质期预警
    public String queryBaoZhiQiList(Map map);

    //出入库流水
    public String outIngWaterList(Map map);

    //库存台帐
    public String InvtyStandList(Map map);

    public String selectInvty(Map map);//查询所有存货信息  分组返回前端

    //现存量查询
    public String selectAvalQty(Map map);//

    //收发存汇总表
    public String selectTSummary(Map map);//

    //--------hj---库存表查询------------------------------------------------------------
    String queryInvtyTabList(Map map);

    //<!--  根据仓库 货物编码 查批次 数量>0  的数据-->
    List<InvtyTab> selectInvtyTabByBatNum(InvtyTab invtyTab);

    //  出入库类型
    public String outIntoWhsTyp(Map map);

    //失效日期维护
    String queryInvldtnDtList(Map map);

    String selectNtChkNoList(Map map);

    public String uploadFileAddDb(MultipartFile file);
}
