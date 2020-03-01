package com.px.mis.whs.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.GdsBitTyp;

//货位管理
public interface GdsBitService {

    //新增货位档案
    public ObjectNode insertGdsBit(GdsBit record);

    //修改货位档案
    public ObjectNode updateGdsBit(GdsBit record);

    //删除货位档案
    public ObjectNode deleteGdsBit(String gdsBitEncd);

    public String deleteGdsBitList(String gdsBitEncd);

    //简单查  货位档案
    public GdsBit selectGdsBit(String gdsBitEncd);

    public List<GdsBit> selectGdsBitList(String gdsBitEncd);

    //分页查
    public String queryList(Map map);

    //查询所有货位类型
    public String selectgTypList();

    //打印
    public String queryListDaYin(Map map);

    public String uploadFileAddDb(MultipartFile file);
}
