package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class PlatOrders implements Cloneable{
	
    private Long no;//���
    private String goodId;//��Ʒ���
    private Integer goodNum;//��Ʒ���� ԭ������
    private BigDecimal goodMoney;//��Ʒ���
    private BigDecimal payMoney;//ʵ�����
    private String goodName;//ƽ̨��Ʒ����
    private String goodSku;//��Ʒsku
    private String orderId;//�������
    private String batchNo;//����
    private String expressCom;//��ݹ�˾
    private String proActId;//��������
    private BigDecimal discountMoney;//ϵͳ�Żݽ��
    private BigDecimal adjustMoney;//���ҵ������
    private String memo;//��ע
    private BigDecimal goodPrice;//��Ʒ����
    private BigDecimal payPrice;//ʵ������
    private String deliverWhs;//�����ֿ���� 
    private BigDecimal sellerPrice;//���㵥��
    private String ecOrderId;//ƽ̨������
    private String invId;//�������
    private Integer invNum;//�������
    private String ptoCode;//ĸ������
    private String ptoName;//ĸ������
    private int canRefNum;//��������
    private BigDecimal canRefMoney;//���˽��
    private Integer splitNum;//�������
    private int isGift;//�Ƿ���Ʒ��0��1��
    
    private String prdcDt;//��������
    private String invldtnDt;//ʧЧ����
    private String invtyName;//�������
    
    public String getInvtyName() {
		return invtyName;
	}

	public void setInvtyName(String invtyName) {
		this.invtyName = invtyName;
	}

	public String getPrdcDt() {
		return prdcDt;
	}

	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}

	public String getInvldtnDt() {
		return invldtnDt;
	}

	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public Long getNo() {
        return no;
    }

    public int getIsGift() {
		return isGift;
	}

	public void setIsGift(int isGift) {
		this.isGift = isGift;
	}

	public String getPtoCode() {
		return ptoCode;
	}

	public int getCanRefNum() {
		return canRefNum;
	}

	public void setCanRefNum(int canRefNum) {
		this.canRefNum = canRefNum;
	}

	public BigDecimal getCanRefMoney() {
		return canRefMoney;
	}

	public void setCanRefMoney(BigDecimal canRefMoney) {
		this.canRefMoney = canRefMoney;
	}

	public void setPtoCode(String ptoCode) {
		this.ptoCode = ptoCode;
	}

	public String getPtoName() {
		return ptoName;
	}

	public void setPtoName(String ptoName) {
		this.ptoName = ptoName;
	}

	public final String getInvId() {
		return invId;
	}

	public final void setInvId(String invId) {
		this.invId = invId;
	}

	public final Integer getInvNum() {
		return invNum;
	}

	public final void setInvNum(Integer invNum) {
		this.invNum = invNum;
	}

	public void setNo(Long no) {
        this.no = no;
    }

    public String getGoodId() {
        return goodId;
    }

    public String getEcOrderId() {
		return ecOrderId;
	}

	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
	}

	public void setGoodId(String goodId) {
        this.goodId = goodId == null ? null : goodId.trim();
    }

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }

    public String getGoodSku() {
        return goodSku;
    }

    public void setGoodSku(String goodSku) {
        this.goodSku = goodSku == null ? null : goodSku.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getExpressCom() {
        return expressCom;
    }

    public void setExpressCom(String expressCom) {
        this.expressCom = expressCom == null ? null : expressCom.trim();
    }


    public String getProActId() {
        return proActId;
    }

    public void setProActId(String proActId) {
        this.proActId = proActId == null ? null : proActId.trim();
    }

    public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

	public Integer getSplitNum() {
		return splitNum;
	}

	public void setSplitNum(Integer splitNum) {
		this.splitNum = splitNum;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public BigDecimal getGoodMoney() {
		return goodMoney;
	}

	public void setGoodMoney(BigDecimal goodMoney) {
		this.goodMoney = goodMoney;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public BigDecimal getDiscountMoney() {
		return discountMoney;
	}

	public void setDiscountMoney(BigDecimal discountMoney) {
		this.discountMoney = discountMoney;
	}

	public BigDecimal getAdjustMoney() {
		return adjustMoney;
	}

	public void setAdjustMoney(BigDecimal adjustMoney) {
		this.adjustMoney = adjustMoney;
	}

	public BigDecimal getGoodPrice() {
		return goodPrice;
	}

	public void setGoodPrice(BigDecimal goodPrice) {
		this.goodPrice = goodPrice;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public String getDeliverWhs() {
		return deliverWhs;
	}

	public void setDeliverWhs(String deliverWhs) {
		this.deliverWhs = deliverWhs;
	}

	public BigDecimal getSellerPrice() {
		return sellerPrice;
	}

	public void setSellerPrice(BigDecimal sellerPrice) {
		this.sellerPrice = sellerPrice;
	}

	@Override
	public String toString() {
		return "PlatOrders [no=" + no + ", goodId=" + goodId + ", goodNum=" + goodNum + ", goodMoney=" + goodMoney
				+ ", payMoney=" + payMoney + ", goodSku=" + goodSku + ", orderId=" + orderId + ", batchNo=" + batchNo
				+ ", expressCom=" + expressCom + ", proActId=" + proActId + ", discountMoney=" + discountMoney
				+ ", adjustMoney=" + adjustMoney + ", memo=" + memo + ", goodPrice=" + goodPrice + ", payPrice="
				+ payPrice + ", deliverWhs=" + deliverWhs + ", splitNum=" + splitNum + "]";
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}