package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.Regn;

//区域档案
public interface RegnMapper {

    //新增区域档案
    public int insertRegn(Regn record);

    //修改区域档案
    public int updateRegn(Regn record);

    //删除区域档案
    public int deleteRegn(@Param("regnEncd") String regnEncd);

    public int deleteRegnList(@Param("list") List<String> regnEncd);//批删

    //简单查  区域档案
    public Regn selectRegn(@Param("regnEncd") String regnEncd);

    public List<Regn> selectRegnList(@Param("regnEncd") String regnEncd);

    //批查
    public List<Regn> selectRegnAllList(@Param("regnEncd") List<String> regnEncd);

    //分页查
    public List<Regn> selectList(Map map);

    public int selectCount(Map map);

    //打印
    public List<Regn> selectListDaYin(Map map);
}