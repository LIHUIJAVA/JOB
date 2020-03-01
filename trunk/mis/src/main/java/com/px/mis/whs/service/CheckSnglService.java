package com.px.mis.whs.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.CheckSngl;
import com.px.mis.whs.entity.CheckSnglSubTab;

public interface CheckSnglService {


    //新增盘点单
    public String insertCheckSngl(String userId, CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList,String loginTime);

    //修改盘点单
    public String updateCheckSngl(CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList);

    //删除盘点单
    //批量删
    public String deleteAllCheckSngl(String checkFormNum);

    //简单查 盘点单
    public String query(String checkFormNum);

    public String queryList(Map map);

    //打印
    public String queryListDaYin(Map map);

    //盘点单列表
    //通过仓库、盘点批号、存货大类编码、失效日期日、账面为零时是否盘点
    public String selectCheckSnglList(Map map) throws IOException;

    //审核
    public String updateCSnglChk(String userId, String jsonBody, String userName, String formDate);

    //弃审
    public String updateCSnglNoChk(String userId, String jsonBody);

    //PDA 返回盘点参数
    public String checkSnglList(String whsEncd) throws IOException;

    //pda 根据子表序号
    public String updateCheckTab(CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList) throws IOException;

    //下载之后锁定状态  不让PC段进行更改  保证数据同步
    public String updateStatus(List<CheckSngl> cList);

    public String uploadFileAddDb(MultipartFile file);

    public String uploadFileAddDbU8(MultipartFile file);

    //通过仓库、盘点批号、存货大类编码、失效日期日、账面为零时是否盘点
    public String selectCheckSnglGdsBitList(Map map);
}
