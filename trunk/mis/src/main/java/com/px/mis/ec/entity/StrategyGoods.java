package com.px.mis.ec.entity;

public class StrategyGoods {
	private int id;//����id
	private int strategyId;//��˲���id
	private int vaType;//������ʽ��0���������԰����ֶΣ�1��������԰����ֶΣ�2������sku
	private String va;//�洢��ֵ�����ֶλ�sku��ֵ abc
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
