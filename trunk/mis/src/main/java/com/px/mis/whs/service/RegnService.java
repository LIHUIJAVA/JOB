package com.px.mis.whs.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.Regn;


//区域档案
public interface RegnService {

    //新增区域档案
    public ObjectNode insertRegn(Regn record);

    //修改区域档案
    public ObjectNode updateRegn(Regn record);

    //删除区域档案
    public ObjectNode deleteRegn(String regnEncd);

    public String deleteRegnList(String regnEncd);

    //简单查  区域档案
    public Regn selectRegn(String regnEncd);

    public List<Regn> selectRegnList(String regnEncd);

    //分页查
    public String queryList(Map map);

    //打印
    public String queryListDaYin(Map map);

    //导入
    public String uploadFileAddDb(MultipartFile file);

}
