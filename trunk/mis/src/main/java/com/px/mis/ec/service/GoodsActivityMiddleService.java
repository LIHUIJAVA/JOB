package com.px.mis.ec.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.GoodsActivityMiddle;

public interface GoodsActivityMiddleService {

	ObjectNode insertGoodsActivityMiddle(GoodsActivityMiddle goodsActivityMiddle);
	
	ObjectNode updateGoodsActivityMiddleById(GoodsActivityMiddle goodsActivityMiddle);
	
	ObjectNode deleteGoodsActivityMiddleById(Integer no);
	
	GoodsActivityMiddle selectGoodsActivityMiddleById(Integer no);
	
    String selectGoodsActivityMiddleList(Map map);
}
