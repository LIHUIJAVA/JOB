package com.px.mis.system.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.MisLog;

public interface MisLogDAO {
//新增
	int insert(MisLog record);

	int insertSelective(MisLog record);

//修改
	int updateByPrimaryKeySelective(MisLog record);

	int updateByPrimaryKey(MisLog record);

	int updateByList(List<MisLog> record);

//删除
	int deleteList(List<String> list);

	int deleteByPrimaryKey(Long id);

//查询
	List<MisLog> selectList(Map map);

	int selectCount(Map map);

	MisLog selectByPrimaryKey(Long id);

}