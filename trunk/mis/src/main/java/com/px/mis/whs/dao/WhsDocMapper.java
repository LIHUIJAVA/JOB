package com.px.mis.whs.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.AvalQtyCtrlMode;
import com.px.mis.whs.entity.City;
import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.RealWhsMap;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.ValtnMode;
import com.px.mis.whs.entity.WhsAttr;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.WhsGds;

public interface WhsDocMapper {

    //添加仓库档案
    public int insertWhsDoc(WhsDoc record);

    public int selectWDoc(@Param("whsEncd") String whsEncd);

    public int exInsertWhsDoc(WhsDoc record);

    //修改仓库档案
    public int updateWhsDoc(WhsDoc record);

    //删除仓库档案
    public int deleteWhsDoc(String whsEncd);

    public int deleteWDocList(List<String> whsEncd);

    //简单查 仓库档案
    public WhsDoc selectWhsDoc(@Param("whsEncd") String whsEncd);

    public List<WhsDoc> selectWhsDocList(@Param("whsEncd") String whsEncd);

    //批量查询
    public List<WhsDoc> selectAllWDocList(@Param("whsEncd") List<String> whsEncd);

    //分页查
    public List<WhsDoc> selectList(Map map);

    public int selectCount(Map map);

    //打印
    public List<WhsDoc> selectListDaYin(Map map);

    //省 市  县
    public List<City> selectCity(City city);

    //省 
    public List<City> selectProvinces();

    //市
    public List<City> selectCities(@Param("superiorCode") String superiorCode);

    //县
    public List<City> selectCounties(@Param("superiorCode") String superiorCode);

    //计价方式
    public List<ValtnMode> selectValtnMode();

    //仓库属性
    public List<WhsAttr> selectWhsAttr();

    //可用量控制方式
    public List<AvalQtyCtrlMode> selectAMode();

    //用户仓库
    public int insertUserWhs(List<UserWhs> userWhs);

    public int updateUserWhs(UserWhs wDoc);

    public List<UserWhs> selectUserWhsList(Map map);

    public int selectUserWhsCount(Map map);

    public int deleteUserWhsList(List<String> list);

    //货位仓库对应
    public int insertWhsGds(List<WhsGds> userWhs);

    public int deleteWhsGds(List<String> list);

    public List<WhsGds> selectWhsGdsList(Map map);

    public int selectWhsGdsListCount(Map map);

    public WhsGds selectWhsGds(WhsGds userWhs);

    public String selectWhsGdsReal(@Param(value = "whsEncd") String whsEncd, @Param(value = "gdsBitEncd") String gdsBitEncd);

    //pc货位查询
    public List<WhsGds> selectWhsGdsRealList(Map map);

    //根据总仓查询业务仓库
    public List<String> selectRealWhsList(List<String> list);

    //
    public Integer selectisNtPrgrGdsBitMgmtWhs(@Param(value = "whsEncd") String whsEncd);

    //总
    public int insertRealWhs(RealWhs wDoc);

    public RealWhs selectRealWhs(@Param("realWhs") String realWhs);

    public int updateRealWhs(RealWhs record);

    public List<RealWhs> queryRealWhsList(Map map);

    public int deleteRealWhsList(List<String> list);

    public int queryRealWhsCount(Map map);

    public List<RealWhs> queryRealWhsListDaYin(Map map);

    //仓库和总仓
    public RealWhsMap selectRealWhsMap(RealWhsMap gds);

    public int insertRealWhsMap(RealWhsMap realWhsMap);

    public List<RealWhsMap> selectRealWhsMapList(Map map);

    public int deleteRealWhsMap(List<String> list);
}