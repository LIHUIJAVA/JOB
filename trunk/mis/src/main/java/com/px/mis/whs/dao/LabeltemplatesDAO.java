package com.px.mis.whs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.LabelTemplates;

public interface LabeltemplatesDAO {
    int deleteByPrimaryKey(List<String> list);

    int insert(LabelTemplates record);

    int insertSelective(LabelTemplates record);

    LabelTemplates selectByPrimaryKey(Long idx);

    List<LabelTemplates> selectByTemplateId(@Param("labelTemplates") LabelTemplates labelTemplates,
                                            @Param("index") Integer index, @Param("num") Integer num);

    int selectByTemplateCount(@Param("labelTemplates") LabelTemplates labelTemplates);

    int updateByPrimaryKeySelective(LabelTemplates record);

    int updateByPrimaryKey(LabelTemplates record);
}