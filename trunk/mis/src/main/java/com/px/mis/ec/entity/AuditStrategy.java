package com.px.mis.ec.entity;

import java.util.List;

public class AuditStrategy {
	private int id;//免审策略id 自增
	private String name;//免审策略名称
	private int buyerNote;//买家备注包含关键字, 0:没勾 1：勾选
	private int sellerNote;//卖家备注包含关键字， 0:没勾 1：勾选
	private int auditWay;//审核方式(0.免审；1.有条件免审；2.必审)
	private int buyerNoteNull;//买家留言非空 0:没勾 1：勾选
	private int sellerNoteNull;//卖家留言非空 0：没勾  1：勾选
	private int includeSku;//是否包含特殊sku 0：不包含 1：包含
	private List<StrategyGoods> strategyGoodsList;
	
	public List<StrategyGoods> getStrategyGoodsList() {
		return strategyGoodsList;
	}

	public void setStrategyGoodsList(List<StrategyGoods> strategyGoodsList) {
		this.strategyGoodsList = strategyGoodsList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public int getBuyerNote() {
		return buyerNote;
	}

	public void setBuyerNote(int buyerNote) {
		this.buyerNote = buyerNote;
	}

	public int getSellerNote() {
		return sellerNote;
	}

	public void setSellerNote(int sellerNote) {
		this.sellerNote = sellerNote;
	}

	public int getAuditWay() {
		return auditWay;
	}

	public int getBuyerNoteNull() {
		return buyerNoteNull;
	}

	public void setBuyerNoteNull(int buyerNoteNull) {
		this.buyerNoteNull = buyerNoteNull;
	}

	public int getSellerNoteNull() {
		return sellerNoteNull;
	}

	public void setSellerNoteNull(int sellerNoteNull) {
		this.sellerNoteNull = sellerNoteNull;
	}

	public void setAuditWay(int auditWay) {
		this.auditWay = auditWay;
	}

	public int getIncludeSku() {
		return includeSku;
	}

	public void setIncludeSku(int includeSku) {
		this.includeSku = includeSku;
	}

}
