package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.GoodsActivityMiddle;


public interface GoodsActivityMiddleDao {
	
	int insertGoodsActivityMiddle(GoodsActivityMiddle goodsActivityMiddle);
	
	int updateGoodsActivityMiddleById(GoodsActivityMiddle goodsActivityMiddle);
	
	int deleteGoodsActivityMiddleById(Integer no);
	
	GoodsActivityMiddle selectGoodsActivityMiddleById(Integer no);
	
	GoodsActivityMiddle selectGoodsActivityMiddleByPriority(Integer priority);
	
	List<GoodsActivityMiddle> selectGoodsActivityMiddleByStoreId(@Param("nowTime") String nowTime,@Param("storeId") String storeId);
	
	List<GoodsActivityMiddle> selectGoodsActivityMiddleByInvtyEncd(@Param("nowTime") String nowTime,@Param("storeId") String storeId,@Param("invtyEncd") String invtyEncd);

    List<GoodsActivityMiddle> selectGoodsActivityMiddleList(Map map);
    
    int selectGoodsActivityMiddleCount();
	 GoodsActivityMiddle selectGoodsActivityMiddleByInvtyEncdORDERLIMIT(@Param("nowTime") String nowTime,@Param("storeId") String storeId,@Param("invtyEncd") String invtyEncd);

    
}
