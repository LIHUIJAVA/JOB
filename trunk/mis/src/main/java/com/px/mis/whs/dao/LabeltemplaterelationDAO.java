package com.px.mis.whs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.LabelSteup;
import com.px.mis.whs.entity.LabelTemplateRelation;

public interface LabeltemplaterelationDAO {
    int deleteByPrimaryKey(List<String> list);

    int insert(LabelTemplateRelation record);

    int insertSelective(LabelTemplateRelation record);

    LabelTemplateRelation selectByPrimaryKey(Integer idx);

    List<LabelTemplateRelation> selectByPrimaryId(
            @Param("labelTemplateRelation") LabelTemplateRelation labelTemplateRelation, @Param("index") Integer index,
            @Param("num") Integer num);

    int selectByTemplateCount(@Param("labelTemplateRelation") LabelTemplateRelation labelTemplateRelation);

    int updateByPrimaryKeySelective(LabelTemplateRelation record);

    int updateByPrimaryKey(LabelTemplateRelation record);
}