package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.GdsBitTyp;

//货位管理
public interface GdsBitMapper {

    //新增货位档案
    public int insertGdsBit(GdsBit record);

    //修改货位档案
    public int updateGdsBit(GdsBit record);

    //删除货位档案
    public int deleteGdsBit(@Param("gdsBitEncd") String gdsBitEncd);

    public int deleteGdsBitList(@Param("list") List<String> gdsBitEncd);//批删

    //简单查  货位档案
    public GdsBit selectGdsBitbyId(@Param("gdsBitEncd") String gdsBitEncd);

    public GdsBit selectGdsBit(@Param("gdsBitEncd") String gdsBitEncd);

    public List<GdsBit> selectGdsBitList(@Param("gdsBitEncd") String gdsBitEncd);

    //批查
    public List<GdsBit> selectGBitList(@Param("gdsBitEncd") List<String> gdsBitEncd);

    //分页查
    public List<GdsBit> selectList(Map map);

    public int selectCount(Map map);

    //查询所有货位类型
    public List<GdsBitTyp> selectgTypList();

    //打印
    public List<GdsBit> selectListDaYin(Map map);


}