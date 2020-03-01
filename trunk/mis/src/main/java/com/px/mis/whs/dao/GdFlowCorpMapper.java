package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.GdFlowCorp;
import com.px.mis.whs.entity.GdFlowCorpMap;

public interface GdFlowCorpMapper {

    //新增物流公司
    public int insertGdFlowCorp(GdFlowCorp record);

    //修改物流公司
    public int updateGdFlowCorp(GdFlowCorp record);

    //删除物流公司
    public int deleteGdFlowCorp(String gdFlowEncd);

    public int deleteGFlowCorpList(List<String> gdFlowEncd);//批删

    //批查
    public List<GdFlowCorp> selectGFlowCorpAllList(@Param("gdFlowEncd") List<String> gdFlowEncd);

    //简单查 物流公司
    public GdFlowCorp selectGdFlowCorp(@Param("gdFlowEncd") String gdFlowEncd);

    public List<GdFlowCorp> selectGdFlowCorpList(@Param("gdFlowEncd") String gdFlowEncd);

    //分页查
    public List<GdFlowCorp> selectList(Map map);

    public int selectCount(Map map);

    //打印
    public List<GdFlowCorpMap> selectListDaYin(Map map);

}