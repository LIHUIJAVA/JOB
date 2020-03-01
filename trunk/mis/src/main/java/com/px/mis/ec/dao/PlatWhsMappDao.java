package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.PlatWhsMapp;


public interface PlatWhsMappDao {
	
	public String select(Map map);
	//新增平台仓映射
	public void insert(PlatWhsMapp platWhsMapp);
	//修改平台仓映射
	public void update(PlatWhsMapp platWhsMapp);
	//删除平台仓映射
	public void delete(PlatWhsMapp platWhsMapp);
	//查询平台仓映射列表
	public List<PlatWhsMapp> selectList(Map map);

	public int selectCount(Map map);
}
