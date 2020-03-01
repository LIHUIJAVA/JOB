package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.ProjCls;



public interface ProjClsDao {

	//新增项目分类
	int insertProjCls(ProjCls projCls);
	
	//修改项目分类
	int updateProjClsByOrdrNum(ProjCls projCls);
	
	//删除项目分类
	int deleteProjClsByOrdrNum(Integer OrdrNum);
	
	//通过自增序号查询项目分类明细
	ProjCls selectProjClsByOrdrNum(Integer OrdrNum);
	//通过项目编码查询项目分类明细
	ProjCls selectProjClsByProjEncd(String projEncd);

	List<ProjCls> selectList(Map map);

	int selectCount(Map map);

	void delProjCls(List<String> ordrNumList);
	
	
}
