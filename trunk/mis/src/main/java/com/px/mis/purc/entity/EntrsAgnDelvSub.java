package com.px.mis.purc.entity;

import java.math.BigDecimal;

/*ί�д����������ӱ�*/
public class EntrsAgnDelvSub implements Comparable<EntrsAgnDelvSub> {
	
    private Long ordrNum;//���
    private String whsEncd;//�ֿ����
    private String invtyEncd;//�������
    private String delvSnglId;//ί�д������������
    private BigDecimal qty;//����
    private BigDecimal quotn;//����
    private BigDecimal noTaxUprc;//��˰����
    private BigDecimal noTaxAmt;//��˰���
    private BigDecimal taxRate;//˰��
    private BigDecimal taxAmt;//˰��
    private BigDecimal cntnTaxUprc;//��˰����
    private BigDecimal prcTaxSum;//��˰�ϼ�
    private BigDecimal unBllgRtnGoodsQty;//��������
    private BigDecimal bllgQty;//��Ʊ����
    private Integer rtnGoodsInd;//�˻���ʶ
    private BigDecimal stlUprc;//���㵥��
    private BigDecimal stlQty;//��������
    private BigDecimal stlAmt;//������
    private BigDecimal accmStlAmt;//�ۼƽ�����
    private BigDecimal finalStlQty;//����������
    private BigDecimal finalStlAmt;//��������
    private BigDecimal accmOutWhsQty;//�ۼƳ�������
    private String prdcDt;//��������
    private Integer baoZhiQi;//������
    private String invldtnDt;//ʧЧ����
    private String batNum;//����
    private String intlBat;//��������
    private Integer isComplimentary;//�Ƿ���Ʒ
    private String memo;//��ע
    private Integer isNtRtnGoods;//�Ƿ��˻�
    private BigDecimal bxQty;//����
    private String projEncd;//��Ŀ���
    private String projNm;//��Ŀ����
    private String discntRatio;//�ۿ۱���
    //�����������ֶΡ�������λ���ơ��ֿ�����
    private String invtyNm;//�������
    private String spcModel;//����ͺ�
    private String invtyCd;//�������
    private BigDecimal bxRule;//���
    private BigDecimal iptaxRate;//����˰��
    private BigDecimal optaxRate;//����˰��
    private BigDecimal highestPurPrice;//��߽���
    private BigDecimal loSellPrc;//����ۼ�
    private BigDecimal refCost;//�ο��ɱ�
    private BigDecimal refSellPrc;//�ο��ۼ�
    private BigDecimal ltstCost;//���³ɱ�
    private String measrCorpNm;//������λ����
    private String whsNm;//�ֿ�����
    private String crspdBarCd;//��Ӧ������
    private Integer shdTaxLabour;//Ӧ˰����
    private Long toOrdrNum;//��Դ�ӱ����
    private BigDecimal unBllgQty;//δ��Ʊ����
    private Integer isNtDiscnt;//�Ƿ��ۿ�
    
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