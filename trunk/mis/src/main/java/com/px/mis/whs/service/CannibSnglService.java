package com.px.mis.whs.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.CannibSngl;
import com.px.mis.whs.entity.CannibSnglSubTab;


/**
 * @author zhangxiaoyu
 * @version 创建时间：2018年11月15日 下午2:18:39
 * @ClassName 类名称
 * @Description 类描述
 */
public interface CannibSnglService {

    //添加调拨单
    public String insertCSngl(String userId, CannibSngl cSngl,
                              List<CannibSnglSubTab> cList,String loginTime);

    //修改调拨单
    public String updateCSngl(CannibSngl cSngl, List<CannibSnglSubTab> cList);

    //删除调拨单 批删
    public String deleteAllCSngl(String formNum);

    //简单查  调拨单
    public String query(String formNum);

    public String queryList(Map map);

    //审核
    public String updateCSnglChk(String userId, String jsonBody, String userName, String formDate);

    //弃审
    public String updateCSnglNoChk(String userId, String jsonBody, String userName);

    //打印
    public String queryListDaYin(Map map);

    //PDA 查询
    public String selectCSnglChkr();

    public String updateCSnglSubTab(CannibSnglSubTab cSubTab);//修改子表

    public String updateCSngl(CannibSngl cSngl);//修改主表

    //查询库存表的现存量  可用量
    public String selectInvty(Map map);


    public String uploadFileAddDb(MultipartFile file);

    public String uploadFileAddDbU8(MultipartFile file);
}
