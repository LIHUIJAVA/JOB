package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class RefundOrders {
	
    private Long no;//序号
    private String refId;//退款单编号
    private String goodId;//商品编号
    private String goodName;//商品名称
    private String goodSku;//商品sku
    private Integer canRefNum;//可退货数量
    private BigDecimal canRefMoney;//可退货金额
    private BigDecimal refMoney;//退货金额
    private Integer refNum;//退货数量
    private String batchNo;//批号
    private String refWhs;//退货仓库
    private String memo;//备注
    private String ecGoodId;//店铺商品编码
    private String prdcDt;//生产日期
    private String invldtnDt;//失效日期
    private Integer baozhiqi;//保质期天数
    
    private int isGift;//是否赠品
    
    
	public int getIsGift() {
		return isGift;
	}
	public void setIsGift(int isGift) {
		this.isGift = isGift;
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
	public Integer getBaozhiqi() {
		return baozhiqi;
	}
	public void setBaozhiqi(Integer baozhiqi) {
		this.baozhiqi = baozhiqi;
	}
	public String getEcGoodId() {
		return ecGoodId;
	}
	public void setEcGoodId(String ecGoodId) {
		this.ecGoodId = ecGoodId;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getGoodSku() {
		return goodSku;
	}
	public void setGoodSku(String goodSku) {
		this.goodSku = goodSku;
	}
	public Integer getCanRefNum() {
		return canRefNum;
	}
	public void setCanRefNum(Integer canRefNum) {
		this.canRefNum = canRefNum;
	}
	public BigDecimal getCanRefMoney() {
		return canRefMoney;
	}
	public void setCanRefMoney(BigDecimal canRefMoney) {
		this.canRefMoney = canRefMoney;
	}
	public BigDecimal getRefMoney() {
		return refMoney;
	}
	public void setRefMoney(BigDecimal refMoney) {
		this.refMoney = refMoney;
	}
	public Integer getRefNum() {
		return refNum;
	}
	public void setRefNum(Integer refNum) {
		this.refNum = refNum;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getRefWhs() {
		return refWhs;
	}
	public void setRefWhs(String refWhs) {
		this.refWhs = refWhs;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baozhiqi == null) ? 0 : baozhiqi.hashCode());
		result = prime * result + ((batchNo == null) ? 0 : batchNo.hashCode());
		result = prime * result + ((canRefMoney == null) ? 0 : canRefMoney.hashCode());
		result = prime * result + ((canRefNum == null) ? 0 : canRefNum.hashCode());
		result = prime * result + ((ecGoodId == null) ? 0 : ecGoodId.hashCode());
		result = prime * result + ((goodId == null) ? 0 : goodId.hashCode());
		result = prime * result + ((goodName == null) ? 0 : goodName.hashCode());
		result = prime * result + ((goodSku == null) ? 0 : goodSku.hashCode());
		result = prime * result + ((invldtnDt == null) ? 0 : invldtnDt.hashCode());
		result = prime * result + ((memo == null) ? 0 : memo.hashCode());
		result = prime * result + ((prdcDt == null) ? 0 : prdcDt.hashCode());
		result = prime * result + ((refId == null) ? 0 : refId.hashCode());
		result = prime * result + ((refMoney == null) ? 0 : refMoney.hashCode());
		result = prime * result + ((refNum == null) ? 0 : refNum.hashCode());
		result = prime * result + ((refWhs == null) ? 0 : refWhs.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefundOrders other = (RefundOrders) obj;
		if (baozhiqi == null) {
			if (other.baozhiqi != null)
				return false;
		} else if (!baozhiqi.equals(other.baozhiqi))
			return false;
		if (batchNo == null) {
			if (other.batchNo != null)
				return false;
		} else if (!batchNo.equals(other.batchNo))
			return false;
		if (canRefMoney == null) {
			if (other.canRefMoney != null)
				return false;
		} else if (!canRefMoney.equals(other.canRefMoney))
			return false;
		if (canRefNum == null) {
			if (other.canRefNum != null)
				return false;
		} else if (!canRefNum.equals(other.canRefNum))
			return false;
		if (ecGoodId == null) {
			if (other.ecGoodId != null)
				return false;
		} else if (!ecGoodId.equals(other.ecGoodId))
			return false;
		if (goodId == null) {
			if (other.goodId != null)
				return false;
		} else if (!goodId.equals(other.goodId))
			return false;
		if (goodName == null) {
			if (other.goodName != null)
				return false;
		} else if (!goodName.equals(other.goodName))
			return false;
		if (goodSku == null) {
			if (other.goodSku != null)
				return false;
		} else if (!goodSku.equals(other.goodSku))
			return false;
		if (invldtnDt == null) {
			if (other.invldtnDt != null)
				return false;
		} else if (!invldtnDt.equals(other.invldtnDt))
			return false;
		if (memo == null) {
			if (other.memo != null)
				return false;
		} else if (!memo.equals(other.memo))
			return false;
		if (prdcDt == null) {
			if (other.prdcDt != null)
				return false;
		} else if (!prdcDt.equals(other.prdcDt))
			return false;
		if (refId == null) {
			if (other.refId != null)
				return false;
		} else if (!refId.equals(other.refId))
			return false;
		if (refMoney == null) {
			if (other.refMoney != null)
				return false;
		} else if (!refMoney.equals(other.refMoney))
			return false;
		if (refNum == null) {
			if (other.refNum != null)
				return false;
		} else if (!refNum.equals(other.refNum))
			return false;
		if (refWhs == null) {
			if (other.refWhs != null)
				return false;
		} else if (!refWhs.equals(other.refWhs))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RefundOrders [no=" + no + ", refId=" + refId + ", goodId=" + goodId + ", goodName=" + goodName
				+ ", goodSku=" + goodSku + ", canRefNum=" + canRefNum + ", canRefMoney=" + canRefMoney + ", refMoney="
				+ refMoney + ", refNum=" + refNum + ", batchNo=" + batchNo + ", refWhs=" + refWhs + ", memo=" + memo
				+ ", ecGoodId=" + ecGoodId + ", prdcDt=" + prdcDt + ", invldtnDt=" + invldtnDt + ", baozhiqi="
				+ baozhiqi + "]";
	}
	
	
    
}