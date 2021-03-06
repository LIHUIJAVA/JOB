package com.px.mis.purc.entity;

import java.math.BigDecimal;

/*委托代销发货单子表*/
public class EntrsAgnDelvSub implements Comparable<EntrsAgnDelvSub> {
	
    private Long ordrNum;//序号
    private String whsEncd;//仓库编码
    private String invtyEncd;//存货编码
    private String delvSnglId;//委托代销发货单编号
    private BigDecimal qty;//数量
    private BigDecimal quotn;//报价
    private BigDecimal noTaxUprc;//无税单价
    private BigDecimal noTaxAmt;//无税金额
    private BigDecimal taxRate;//税率
    private BigDecimal taxAmt;//税额
    private BigDecimal cntnTaxUprc;//含税单价
    private BigDecimal prcTaxSum;//价税合计
    private BigDecimal unBllgRtnGoodsQty;//可退数量
    private BigDecimal bllgQty;//开票数量
    private Integer rtnGoodsInd;//退货标识
    private BigDecimal stlUprc;//结算单价
    private BigDecimal stlQty;//结算数量
    private BigDecimal stlAmt;//结算金额
    private BigDecimal accmStlAmt;//累计结算金额
    private BigDecimal finalStlQty;//最后结算数量
    private BigDecimal finalStlAmt;//最后结算金额
    private BigDecimal accmOutWhsQty;//累计出库数量
    private String prdcDt;//生产日期
    private Integer baoZhiQi;//保质期
    private String invldtnDt;//失效日期
    private String batNum;//批号
    private String intlBat;//国际批次
    private Integer isComplimentary;//是否赠品
    private String memo;//备注
    private Integer isNtRtnGoods;//是否退货
    private BigDecimal bxQty;//箱数
    private String projEncd;//项目编号
    private String projNm;//项目名称
    private String discntRatio;//折扣比例
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
    private Integer shdTaxLabour;//应税劳务
    private Long toOrdrNum;//来源子表序号
    private BigDecimal unBllgQty;//未开票数量
    private Integer isNtDiscnt;//是否折扣
    
	public BigDecimal getUnBllgQty() {
		return unBllgQty;
	}

	public void setUnBllgQty(BigDecimal unBllgQty) {
		this.unBllgQty = unBllgQty;
	}

	public Long getToOrdrNum() {
		return toOrdrNum;
	}

	public void setToOrdrNum(Long toOrdrNum) {
		this.toOrdrNum = toOrdrNum;
	}

	public String getProjNm() {
		return projNm;
	}

	public void setProjNm(String projNm) {
		this.projNm = projNm;
	}

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

	public String getDiscntRatio() {
		return discntRatio;
	}

	public void setDiscntRatio(String discntRatio) {
		this.discntRatio = discntRatio;
	}

	public String getIntlBat() {
		return intlBat;
	}

	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
	}

	public Integer getIsNtRtnGoods() {
		return isNtRtnGoods;
	}

	public void setIsNtRtnGoods(Integer isNtRtnGoods) {
		this.isNtRtnGoods = isNtRtnGoods;
	}

	public Integer getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(Integer baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
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

    public String getDelvSnglId() {
        return delvSnglId;
    }

    public void setDelvSnglId(String delvSnglId) {
        this.delvSnglId = delvSnglId == null ? null : delvSnglId.trim();
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
    
    public Integer getShdTaxLabour() {
		return shdTaxLabour;
	}

	public void setShdTaxLabour(Integer shdTaxLabour) {
		this.shdTaxLabour = shdTaxLabour;
	}

	public Integer getIsNtDiscnt() {
		return isNtDiscnt;
	}

	public void setIsNtDiscnt(Integer isNtDiscnt) {
		this.isNtDiscnt = isNtDiscnt;
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
	public int compareTo(EntrsAgnDelvSub entrsAgnDelvSub) {
		int flag = this.invtyEncd.compareTo(entrsAgnDelvSub.getInvtyEncd());
        if(flag != 0) {
        	return flag;
        }
        flag = this.whsEncd.compareTo(entrsAgnDelvSub.getWhsEncd());
        
        if(flag != 0) {
        	return flag;
        }
        
		return this.batNum.compareTo(entrsAgnDelvSub.getBatNum());
	}
}