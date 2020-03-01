package com.px.mis.whs.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.whs.entity.AmbDisambSngl;
import com.px.mis.whs.entity.AmbDisambSnglubTab;


public interface AmbDisambSnglService {

    //新增组装拆卸单
    public String insertAmbDisambSngl(String userId, AmbDisambSngl aSngl, List<AmbDisambSnglubTab> aList,String loginTime);

    //修改组装拆卸单
    public String updateAmbDisambSngl(AmbDisambSngl aSngl, List<AmbDisambSnglubTab> aList);

    //删除组装拆卸单   批删
    public String deleteAllAmbDisambSngl(String formNum);

    //简单查 组装拆卸单
    public String query(String formNum);

    public String queryList(Map map);

    //审核
    public String updateASnglChk(String userId, String jsonBody, String name, String formDate);

    //弃审
    public String updateASnglNoChk(String userId, String jsonBody, String name);

    //打印
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);

    public String uploadFileAddDbU8(MultipartFile file);

}
