package com.px.mis.purc.entity;

import java.math.BigDecimal;

//ί�д����������ӱ�
public class EntrsAgnAdjSub {
	
    private Long ordrNum;//���

    private String whsEncd;//�ֿ����

    private String invtyEncd;//�������

    private String adjSnglId;//������������

    private BigDecimal qty;//����

    private BigDecimal quotn;//����

    private BigDecimal noTaxUprc;//��˰����

    private BigDecimal noTaxAmt;//��˰���

    private BigDecimal taxRate;//˰��

    private BigDecimal discntAmt;//�ۿ۶�

    private BigDecimal taxAmt;//˰��

    private BigDecimal cntnTaxUprc;//��˰����

    private BigDecimal prcTaxSum;//��˰�ϼƣ��ֽ���ۣ�

    private BigDecimal unBllgRtnGoodsQty;//δ��Ʊ�˻�����

    private BigDecimal bllgQty;//��Ʊ����

    private Integer rtnGoodsInd;//�˻���ʶ

    private BigDecimal stlUprc;//���㵥��

    private BigDecimal stlQty;//��������

    private BigDecimal stlAmt;//�����ԭ����ۣ�

    private BigDecimal accmStlAmt;//�ۼƽ�����

    private BigDecimal finalStlQty;//����������

    private BigDecimal finalStlAmt;//��������

    private BigDecimal accmOutWhsQty;//�ۼƳ�������

    private String prdcDt;//��������

    private String invldtnDt;//ʧЧ����

    private String batNum;//����
    
    private String intlBat;//��������

    private Integer isComplimentary;//�Ƿ���Ʒ

    private String memo;//��ע
    
    private BigDecimal stlPrcTaxSum;//������˰�ϼ�
    
    private String delvSnglId;//ί�д������������
    
    private BigDecimal bxQty;//����
    
    private String projEncd;//��Ŀ���
    
    private String projNm;//��Ŀ����
    
    private String discntRatio;//�ۿ۱���

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