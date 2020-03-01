package com.px.mis.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.ProActivitys;

public interface ProActivitysDao {

	public void insert(List<ProActivitys> proActList);
	
	public void delete(String proActId);
	
	public List<ProActivitys> select(String proActId);
	
	public ProActivitys selectById(Integer no);
	
	//=======================zds 更新促销活动子表中的已赠数量=============================
	public void update(ProActivitys proActivitys);
	
	public void updateHasGiftNum(ProActivitys proActivitys);
	
	   public List<ProActivitys> selectProActIdAllGoods(@Param("proActId")String proActId,@Param("goodsRange")String goodsRange);


}
