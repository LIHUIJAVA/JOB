package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class ToGdsSnglSub implements Comparable<ToGdsSnglSub>{
	
    private Long ordrNum;//序号
    private String toGdsSnglId;//到货单编号
	private String invtyEncd;//存货编码
    private String whsEncd;//仓库编码
    private String pursOrdrNum;//采购订单号
    private BigDecimal qty;//数量
    private BigDecimal bxQty;//箱数
    private BigDecimal taxRate;//税率
    private BigDecimal noTaxUprc;//无税单价
    private BigDecimal noTaxAmt;//无税金额
    private BigDecimal taxAmt;//税额
    private BigDecimal cntnTaxUprc;//含税单价
    private BigDecimal prcTaxSum;//价税合计
    private String baoZhiQi;//保质期
    private String intlBat;//国际批次
    private String batNum;//批号
    private String prdcDt;//生产日期
    private String invldtnDt;//失效日期
    private Integer isComplimentary;//是否赠品
    private Integer isNtRtnGoods;//是否退货（1:到货  0：退货）
    private String returnMemo;//拒收备注
    private BigDecimal returnQty;//未拒收数量
    private String gdsBitEncd;//货位编码--------
	//联查存货档案字段、计量单位名称、仓库名称
    private String invtyNm;//存货名称
    private String spcModel;//规格型号
    private String invtyCd;//存货代码
    private BigDecimal bxRule;//箱规
    private BigDecimal iptaxRate;//进项税率
    private BigDecimal optaxRate;//销项税率
    private BigDecimal highestPurPrice;//最高进价
    private BigDecimal loSellPrc;//最低售价
    private BigDecimal refCost;//参考成本
    private BigDecimal refSellPrc;//参考售价
    private BigDecimal ltstCost;//最新成本
    private String measrCorpNm;//计量单位名称
    private String whsNm;//仓库名称
    private String crspdBarCd;//对应条形码
    private BigDecimal unIntoWhsQty;//未入库数量
    private String formTypName;//单据类型名称
    private Long toOrdrNum;//来源单据子表标识
    private String dealStat;//处理状态
    private String memo;//表体备注

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDealStat() {
		return dealStat;
	}

	public void setDealStat(String dealStat) {
		this.dealStat = dealStat;
	}

	public Long getToOrdrNum() {
		return toOrdrNum;
	}

	public void setToOrdrNum(Long toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public BigDecimal getBxQty() {
		return bxQty;
	}

	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getGdsBitEncd() {
		return gdsBitEncd;
	}

	public void setGdsBitEncd(String gdsBitEncd) {
		this.gdsBitEncd = gdsBitEncd;
	}
	
    public Long getOrdrNum() {
        return ordrNum;
    }

    public String getReturnMemo() {
		return returnMemo;
	}

	public void setReturnMemo(String returnMemo) {
		this.returnMemo = returnMemo;
	}

	public BigDecimal getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(BigDecimal returnQty) {
		this.returnQty = returnQty;
	}

	public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getToGdsSnglId() {
		return toGdsSnglId;
	}

	public void setToGdsSnglId(String toGdsSnglId) {
		this.toGdsSnglId = toGdsSnglId;
	}
    
    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public String getPursOrdrNum() {
        return pursOrdrNum;
    }

    public void setPursOrdrNum(String pursOrdrNum) {
        this.pursOrdrNum = pursOrdrNum == null ? null : pursOrdrNum.trim();
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getNoTaxUprc() {
        return noTaxUprc;
    }

    public void setNoTaxUprc(BigDecimal noTaxUprc) {
        this.noTaxUprc = noTaxUprc;
    }

    public BigDecimal getNoTaxAmt() {
        return noTaxAmt;
    }

    public void setNoTaxAmt(BigDecimal noTaxAmt) {
        this.noTaxAmt = noTaxAmt;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public BigDecimal getCntnTaxUprc() {
        return cntnTaxUprc;
    }

    public void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
        this.cntnTaxUprc = cntnTaxUprc;
    }

    public BigDecimal getPrcTaxSum() {
        return prcTaxSum;
    }

    public void setPrcTaxSum(BigDecimal prcTaxSum) {
        this.prcTaxSum = prcTaxSum;
    }

    public String getIntlBat() {
        return intlBat;
    }

    public void setIntlBat(String intlBat) {
        this.intlBat = intlBat == null ? null : intlBat.trim();
    }

    public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
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

    public Integer getIsComplimentary() {
        return isComplimentary;
    }

    public void setIsComplimentary(Integer isComplimentary) {
        this.isComplimentary = isComplimentary;
    }
    
    public Integer getIsNtRtnGoods() {
		return isNtRtnGoods;
	}

	public void setIsNtRtnGoods(Integer isNtRtnGoods) {
		this.isNtRtnGoods = isNtRtnGoods;
	}

	public String getCrspdBarCd() {
		return crspdBarCd;
	}

	public void setCrspdBarCd(String crspdBarCd) {
		this.crspdBarCd = crspdBarCd;
	}

	public String getInvtyNm() {
		return invtyNm;
	}

	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

	public String getSpcModel() {
		return spcModel;
	}

	public void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}

	public String getInvtyCd() {
		return invtyCd;
	}

	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}

	public BigDecimal getBxRule() {
		return bxRule;
	}

	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}

	public BigDecimal getIptaxRate() {
		return iptaxRate;
	}

	public void setIptaxRate(BigDecimal iptaxRate) {
		this.iptaxRate = iptaxRate;
	}

	public BigDecimal getOptaxRate() {
		return optaxRate;
	}

	public void setOptaxRate(BigDecimal optaxRate) {
		this.optaxRate = optaxRate;
	}

	public BigDecimal getHighestPurPrice() {
		return highestPurPrice;
	}

	public void setHighestPurPrice(BigDecimal highestPurPrice) {
		this.highestPurPrice = highestPurPrice;
	}

	public BigDecimal getLoSellPrc() {
		return loSellPrc;
	}

	public void setLoSellPrc(BigDecimal loSellPrc) {
		this.loSellPrc = loSellPrc;
	}

	public BigDecimal getRefCost() {
		return refCost;
	}

	public void setRefCost(BigDecimal refCost) {
		this.refCost = refCost;
	}

	public BigDecimal getRefSellPrc() {
		return refSellPrc;
	}

	public void setRefSellPrc(BigDecimal refSellPrc) {
		this.refSellPrc = refSellPrc;
	}

	public BigDecimal getLtstCost() {
		return ltstCost;
	}

	public void setLtstCost(BigDecimal ltstCost) {
		this.ltstCost = ltstCost;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

	public String getWhsNm() {
		return whsNm;
	}

	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public BigDecimal getUnIntoWhsQty() {
		return unIntoWhsQty;
	}

	public void setUnIntoWhsQty(BigDecimal unIntoWhsQty) {
		this.unIntoWhsQty = unIntoWhsQty;
	}

	@Override
	public int compareTo(ToGdsSnglSub toGdsSnglSub) {
		int flag = this.invtyEncd.compareTo(toGdsSnglSub.getInvtyEncd());
        if(flag != 0) {
        	return flag;
        }
        flag = this.whsEncd.compareTo(toGdsSnglSub.getWhsEncd());
        
        if(flag != 0) {
        	return flag;
        }
        
		return this.batNum.compareTo(toGdsSnglSub.getBatNum());
	}
	
}