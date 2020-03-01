package com.px.mis.ec.dao;

import java.util.List;

import com.px.mis.ec.entity.StrategyGoods;

public interface StrategyGoodsDao {
	
	public void insert(List<StrategyGoods> list);
	
	public List<StrategyGoods> detaillsit(int strategyId);

}
