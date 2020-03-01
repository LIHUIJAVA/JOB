package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.PlatOrder;

public interface OrderAdjustService {
	//调整商品之前的展示
	public String showAdjustGoods(String orderId);
	//调整商品
	public String editAdjustGoods(List<Map> list);
	//手工拆单之前的展示
	public String showSplitOrder(String orderId);
	//手工拆单
	public String editSplitOrder(PlatOrder platOrder);
	//手工合并之前的展示
	public String showMergeOrder(String orderIds);
	//手工合并
	public String editMergeOrder(List<Map> list,String orderId);
}
