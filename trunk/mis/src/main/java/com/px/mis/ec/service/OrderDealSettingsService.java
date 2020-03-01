package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.OrderDealSettings;
import com.px.mis.ec.entity.PlatOrder;

public interface OrderDealSettingsService {

	public String edit(OrderDealSettings orderDealSettings);
	
	public String query(String settingId);
	/**
	 * 匹配仓库
	 * @param platOrder订单实体
	 * @param platId平台编号
	 * @return Map
	 * message 错误信息
	 * isSuccess 是否匹配成功
	 * whsCode 仓库编码
	 */
	public Map<String,Object> matchWareHouse(PlatOrder platOrder,String platId);
	
	//查询特殊存货
	public String selectPlatWhsSpecialList(String jsonBody);
	//添加特殊存货
	public String addPlatWhsSpecial(String jsonBody);
	//批量删除特殊存货
	public String deletePlatWhs(String jsonBody);
	//修改特殊存货
	public String updatePlatWhs(String jsonBody);
}
