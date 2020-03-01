package com.px.mis.whs.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.whs.entity.City;
import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.RealWhsMap;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.WhsGds;


public interface WhsDocService {

    //添加仓库档案
    public ObjectNode insertWhsDoc(WhsDoc record);

    //修改仓库档案
    public ObjectNode updateWhsDoc(WhsDoc record);

    //删除仓库档案
    public ObjectNode deleteWhsDoc(String whsEncd);

    public String deleteWDocList(String whsEncd);

    //简单查 仓库档案
    public WhsDoc selectWhsDoc(String whsEncd) throws IOException;

    public String selectWhsDocList(String whsEncd, String userId) throws IOException;

    //分页查
    public String queryList(Map map);

    //打印
    public String queryListDaYin(Map map);

    //省 市 县
    public String selectCity(City city);

    //省 
    public String selectProvinces();

    //市
    public String selectCities(String superiorCode);

    //县
    public String selectCounties(String superiorCode);

    //计价方式
    public String selectValtnMode();

    //仓库属性
    public String selectWhsAttr();

    //可用量控制方式
    public String selectAMode();

    public String uploadFileAddDb(MultipartFile file);

    //仓库用户
    public ObjectNode insertUserWhs(List<UserWhs> userWhs);

    public ObjectNode updateUserWhs(UserWhs wDoc);

    public String deleteUserWhsList(String id);

    public String selectUserWhsList(Map map);

    //货位与仓库
    public String deleteWhsGds(List<String> id);

    public ObjectNode insertWhsGds(List<WhsGds> userWhs);

    public String selectWhsGdsList(Map map);

    //pda查询
    public String selectWhsGdsRealList(Map list);

    //总仓
    public ObjectNode insertRealWhs(RealWhs wDoc);

    public ObjectNode updateRealWhs(RealWhs wDoc);

    public String deleteRealWhsList(String whsEncd);

    public RealWhs selectRealWhs(String realWhs);

    public String queryRealWhsListDaYin(Map map);

    public String queryRealWhsList(Map map);

    //总仓和仓库
    public ObjectNode insertRealWhsMap(RealWhsMap realWhsMap);

    public String selectRealWhsMap(Map map);

    public String deleteRealWhsMap(List<String> list);

}
