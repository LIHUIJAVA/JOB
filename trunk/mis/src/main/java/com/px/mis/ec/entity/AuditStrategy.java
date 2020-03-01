package com.px.mis.ec.entity;

import java.util.List;

public class AuditStrategy {
	private int id;//�������id ����
	private String name;//�����������
	private int buyerNote;//��ұ�ע�����ؼ���, 0:û�� 1����ѡ
	private int sellerNote;//���ұ�ע�����ؼ��֣� 0:û�� 1����ѡ
	private int auditWay;//��˷�ʽ(0.����1.����������2.����)
	private int buyerNoteNull;//������Էǿ� 0:û�� 1����ѡ
	private int sellerNoteNull;//�������Էǿ� 0��û��  1����ѡ
	private int includeSku;//�Ƿ��������sku 0�������� 1������
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
