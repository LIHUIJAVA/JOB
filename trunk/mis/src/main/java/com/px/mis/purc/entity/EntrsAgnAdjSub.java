package com.px.mis.purc.entity;

import java.math.BigDecimal;

//委托代销调整单子表
public class EntrsAgnAdjSub {
	
    private Long ordrNum;//序号

    private String whsEncd;//仓库编码

    private String invtyEncd;//存货编码

    private String adjSnglId;//调整单主表编号

    private BigDecimal qty;//数量

    private BigDecimal quotn;//报价

    private BigDecimal noTaxUprc;//无税单价

    private BigDecimal noTaxAmt;//无税金额

    private BigDecimal taxRate;//税率

    private BigDecimal discntAmt;//折扣额

    private BigDecimal taxAmt;//税额

    private BigDecimal cntnTaxUprc;//含税单价

    private BigDecimal prcTaxSum;//价税合计（现结算价）

    private BigDecimal unBllgRtnGoodsQty;//未开票退货数量

    private BigDecimal bllgQty;//开票数量

    private Integer rtnGoodsInd;//退货标识

    private BigDecimal stlUprc;//结算单价

    private BigDecimal stlQty;//结算数量

    private BigDecimal stlAmt;//结算金额（原结算价）

    private BigDecimal accmStlAmt;//累计结算金额

    private BigDecimal finalStlQty;//最后结算数量

    private BigDecimal finalStlAmt;//最后结算金额

    private BigDecimal accmOutWhsQty;//累计出库数量

    private String prdcDt;//生产日期

    private String invldtnDt;//失效日期

    private String batNum;//批号
    
    private String intlBat;//国际批次

    private Integer isComplimentary;//是否赠品

    private String memo;//备注
    
    private BigDecimal stlPrcTaxSum;//调整价税合计
    
    private String delvSnglId;//委托代销发货单编号
    
    private BigDecimal bxQty;//箱数
    
    private String projEncd;//项目编号
    
    private String projNm;//项目名称
    
    private String discntRatio;//折扣比例

    public BigDecimal getBxQty() {
		return bxQty;
	}

	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}

	public String getProjEncd() {
		return projEncd;
	}

	public void setProjEncd(String projEncd) {
		this.projEncd = projEncd;
	}

	public String getProjNm() {
		return projNm;
	}

	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}

	public String getDiscntRatio() {
		return discntRatio;
	}

	public void setDiscntRatio(String discntRatio) {
		this.discntRatio = discntRatio;
	}

	public String getDelvSnglId() {
		return delvSnglId;
	}

	public void setDelvSnglId(String delvSnglId) {
		this.delvSnglId = delvSnglId;
	}

	public String getIntlBat() {
		return intlBat;
	}

	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
	}

	public BigDecimal getStlPrcTaxSum() {
		return stlPrcTaxSum;
	}

	public void setStlPrcTaxSum(BigDecimal stlPrcTaxSum) {
		this.stlPrcTaxSum = stlPrcTaxSum;
	}

	public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public String getAdjSnglId() {
        return adjSnglId;
    }

    public void setAdjSnglId(String adjSnglId) {
        this.adjSnglId = adjSnglId == null ? null : adjSnglId.trim();
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getQuotn() {
        return quotn;
    }

    public void setQuotn(BigDecimal quotn) {
        this.quotn = quotn;
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

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getDiscntAmt() {
        return discntAmt;
    }

    public void setDiscntAmt(BigDecimal discntAmt) {
        this.discntAmt = discntAmt;
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

    public BigDecimal getUnBllgRtnGoodsQty() {
        return unBllgRtnGoodsQty;
    }

    public void setUnBllgRtnGoodsQty(BigDecimal unBllgRtnGoodsQty) {
        this.unBllgRtnGoodsQty = unBllgRtnGoodsQty;
    }

    public BigDecimal getBllgQty() {
        return bllgQty;
    }

    public void setBllgQty(BigDecimal bllgQty) {
        this.bllgQty = bllgQty;
    }

    public Integer getRtnGoodsInd() {
        return rtnGoodsInd;
    }

    public void setRtnGoodsInd(Integer rtnGoodsInd) {
        this.rtnGoodsInd = rtnGoodsInd;
    }

    public BigDecimal getStlUprc() {
        return stlUprc;
    }

    public void setStlUprc(BigDecimal stlUprc) {
        this.stlUprc = stlUprc;
    }

    public BigDecimal getStlQty() {
        return stlQty;
    }

    public void setStlQty(BigDecimal stlQty) {
        this.stlQty = stlQty;
    }

    public BigDecimal getStlAmt() {
        return stlAmt;
    }

    public void setStlAmt(BigDecimal stlAmt) {
        this.stlAmt = stlAmt;
    }

    public BigDecimal getAccmStlAmt() {
        return accmStlAmt;
    }

    public void setAccmStlAmt(BigDecimal accmStlAmt) {
        this.accmStlAmt = accmStlAmt;
    }

    public BigDecimal getFinalStlQty() {
        return finalStlQty;
    }

    public void setFinalStlQty(BigDecimal finalStlQty) {
        this.finalStlQty = finalStlQty;
    }

    public BigDecimal getFinalStlAmt() {
        return finalStlAmt;
    }

    public void setFinalStlAmt(BigDecimal finalStlAmt) {
        this.finalStlAmt = finalStlAmt;
    }

    public BigDecimal getAccmOutWhsQty() {
        return accmOutWhsQty;
    }

    public void setAccmOutWhsQty(BigDecimal accmOutWhsQty) {
        this.accmOutWhsQty = accmOutWhsQty;
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

	public String getBatNum() {
        return batNum;
    }

    public void setBatNum(String batNum) {
        this.batNum = batNum == null ? null : batNum.trim();
    }

    public Integer getIsComplimentary() {
        return isComplimentary;
    }

    public void setIsComplimentary(Integer isComplimentary) {
        this.isComplimentary = isComplimentary;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}