package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class SellOutWhsSub implements Comparable<SellOutWhsSub>{
    private Long ordrNum;//序号
    private String whsEncd;//仓库编码
    private String invtyEncd;//存货编码
    private String outWhsId;//关联主表，出库编号
    private BigDecimal qty;//数量
    private BigDecimal bxQty;//箱数
    private BigDecimal cntnTaxUprc;//含税单价
    private BigDecimal prcTaxSum;//价税合计
    private BigDecimal noTaxUprc;//无税单价
    private BigDecimal noTaxAmt;//无税金额
    private BigDecimal taxAmt;//税额
    private BigDecimal taxRate;//税率
    private String batNum;//批号
    private String intlBat;//国际批号
    private Integer baoZhiQi;//保质期
	private String prdcDt;//生产日期
    private String invldtnDt;//失效日期
    private Integer isComplimentary;//是否赠品
    private Integer isNtRtnGoods;//是否退货
    private String memo;//备注
    private String returnMemo;//拒收备注
    private BigDecimal returnQty;//拒收数量
    private String gdsBitEncd;//货位编码--------
    private String projEncd;//项目编码
    private String projNm;//项目名称
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
    private String measrCorpId;//计量单位编号
    private String measrCorpNm;//计量单位名称
    private String whsNm;//仓库名称
    private String crspdBarCd;//对应条形码
    private String invtyClsEncd;//存货分类名称
    private String invtyClsNm;//存货分类名称
    private String regnEncd;//区域编码--------
    private Long toOrdrNum;//来源单据子表标识
    private BigDecimal carrOvrUprc;//结转单价
    private BigDecimal carrOvrAmt;//结转金额
    private BigDecimal rebaUprc;//结转单价
    private BigDecimal rebaAmt;//结转金额

	public BigDecimal getCarrOvrUprc() {
		return carrOvrUprc;
	}

	public void setCarrOvrUprc(BigDecimal carrOvrUprc) {
		this.carrOvrUprc = carrOvrUprc;
	}

	public BigDecimal getCarrOvrAmt() {
		return carrOvrAmt;
	}

	public void setCarrOvrAmt(BigDecimal carrOvrAmt) {
		this.carrOvrAmt = carrOvrAmt;
	}

	public BigDecimal getRebaUprc() {
		return rebaUprc;
	}

	public void setRebaUprc(BigDecimal rebaUprc) {
		this.rebaUprc = rebaUprc;
	}

	public BigDecimal getRebaAmt() {
		return rebaAmt;
	}

	public void setRebaAmt(BigDecimal rebaAmt) {
		this.rebaAmt = rebaAmt;
	}

	public Long getToOrdrNum() {
		return toOrdrNum;
	}

	public void setToOrdrNum(Long toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}

	public String getRegnEncd() {
  		return regnEncd;
  	}

  	public void setRegnEncd(String regnEncd) {
  		this.regnEncd = regnEncd;
  	}

    
    public String getProjNm() {
		return projNm;
	}

	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}

	public String getProjEncd() {
		return projEncd;
	}

	public void setProjEncd(String projEncd) {
		this.projEncd = projEncd;
	}

	public String getIntlBat() {
		return intlBat;
	}

	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
	}

	public BigDecimal getBxQty() {
		return bxQty;
	}

	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}

	public String getGdsBitEncd() {
		return gdsBitEncd;
	}

	public void setGdsBitEncd(String gdsBitEncd) {
		this.gdsBitEncd = gdsBitEncd;
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

	public Integer getIsNtRtnGoods() {
		return isNtRtnGoods;
	}

	public Integer getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(Integer baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public void setIsNtRtnGoods(Integer isNtRtnGoods) {
		this.isNtRtnGoods = isNtRtnGoods;
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

    public String getOutWhsId() {
        return outWhsId;
    }

    public void setOutWhsId(String outWhsId) {
        this.outWhsId = outWhsId == null ? null : outWhsId.trim();
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

	public String getMeasrCorpId() {
		return measrCorpId;
	}

	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}

	public String getInvtyClsEncd() {
		return invtyClsEncd;
	}

	public void setInvtyClsEncd(String invtyClsEncd) {
		this.invtyClsEncd = invtyClsEncd;
	}

	public String getInvtyClsNm() {
		return invtyClsNm;
	}

	public void setInvtyClsNm(String invtyClsNm) {
		this.invtyClsNm = invtyClsNm;
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
	
	@Override
	public int compareTo(SellOutWhsSub sellOutWhsSub) {
		int flag = this.invtyEncd.compareTo(sellOutWhsSub.getInvtyEncd());
        if(flag != 0) {
        	return flag;
        }
        flag = this.whsEncd.compareTo(sellOutWhsSub.getWhsEncd());
        
        if(flag != 0) {
        	return flag;
        }
        
		return this.batNum.compareTo(sellOutWhsSub.getBatNum());
	}
}