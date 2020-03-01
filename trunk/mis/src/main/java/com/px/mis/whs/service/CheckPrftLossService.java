package com.px.mis.whs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckPrftLossSubTab;


public interface CheckPrftLossService {

    //新增盘点单
    public String insertCheckSnglLoss(String userId, CheckPrftLoss cSngl, List<CheckPrftLossSubTab> cSubTabList,String loginTime);

    //修改盘点单
    public String updateCheckSnglLoss(CheckPrftLoss cSngl, List<CheckPrftLossSubTab> cSubTabList);

    //删除盘点单
    //批量删
    public String deleteAllCheckSnglLoss(String checkFormNum);

    //简单查 盘点单
    public String query(String checkFormNum);

    public String queryList(Map map);

    //审核
    public String updateCSnglLossChk(String userId, String jsonBody, String userName, String formDate);

    //弃审
    public String updateCSnglLossNoChk(String userId, String jsonBody);

    //打印
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);

}
