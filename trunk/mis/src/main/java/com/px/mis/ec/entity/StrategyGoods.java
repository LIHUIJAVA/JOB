package com.px.mis.ec.entity;

public class StrategyGoods {
	private int id;//自增id
	private int strategyId;//审核策略id
	private int vaType;//条件方式，0：卖家留言包含字段，1：买家留言包含字段，2：包含sku
	private String va;//存储的值，存字段或sku的值 abc
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(int strategyId) {
		this.strategyId = strategyId;
	}
	public int getVaType() {
		return vaType;
	}
	public void setVaType(int vaType) {
		this.vaType = vaType;
	}
	public String getVa() {
		return va;
	}
	public void setVa(String va) {
		this.va = va;
	}
	
	

}
