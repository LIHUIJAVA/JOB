package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.LabelSteup;

public interface LabelsteupDAO {
    int deleteByPrimaryKey(List<String> list);

    int insert(LabelSteup record);

    int insertSelective(LabelSteup record);

    LabelSteup selectByPrimaryKey(Long idx);

    int updateByPrimaryKeySelective(LabelSteup record);

    int updateByPrimaryKey(LabelSteup record);

    List<LabelSteup> selectByPrimaryQuery(Map<String, Object> map);

    int selectByTemplateCount(Map<String, Object> map);
}