package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class ToGdsSnglSub implements Comparable<ToGdsSnglSub>{
	
    private Long ordrNum;//���
    private String toGdsSnglId;//���������
	private String invtyEncd;//�������
    private String whsEncd;//�ֿ����
    private String pursOrdrNum;//�ɹ�������
    private BigDecimal qty;//����
    private BigDecimal bxQty;//����
    private BigDecimal taxRate;//˰��
    private BigDecimal noTaxUprc;//��˰����
    private BigDecimal noTaxAmt;//��˰���
    private BigDecimal taxAmt;//˰��
    private BigDecimal cntnTaxUprc;//��˰����
    private BigDecimal prcTaxSum;//��˰�ϼ�
    private String baoZhiQi;//������
    private String intlBat;//��������
    private String batNum;//����
    private String prdcDt;//��������
    private String invldtnDt;//ʧЧ����
    private Integer isComplimentary;//�Ƿ���Ʒ
    private Integer isNtRtnGoods;//�Ƿ��˻���1:����  0���˻���
    private String returnMemo;//���ձ�ע
    private BigDecimal returnQty;//δ��������
    private String gdsBitEncd;//��λ����--------
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
    private BigDecimal unIntoWhsQty;//δ�������
    private String formTypName;//������������
    private Long toOrdrNum;//��Դ�����ӱ��ʶ
    private String dealStat;//����״̬
    private String memo;//���屸ע

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