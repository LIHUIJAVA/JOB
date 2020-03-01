package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class Near15DaysOrder {
	//最近15天订单
	private String date;
	private int count;
	private BigDecimal money;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money.setScale(2);
	}
	
	

}
