package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class SellOutWhsSub implements Comparable<SellOutWhsSub>{
    private Long ordrNum;//���
    private String whsEncd;//�ֿ����
    private String invtyEncd;//�������
    private String outWhsId;//��������������
    private BigDecimal qty;//����
    private BigDecimal bxQty;//����
    private BigDecimal cntnTaxUprc;//��˰����
    private BigDecimal prcTaxSum;//��˰�ϼ�
    private BigDecimal noTaxUprc;//��˰����
    private BigDecimal noTaxAmt;//��˰���
    private BigDecimal taxAmt;//˰��
    private BigDecimal taxRate;//˰��
    private String batNum;//����
    private String intlBat;//��������
    private Integer baoZhiQi;//������
	private String prdcDt;//��������
    private String invldtnDt;//ʧЧ����
    private Integer isComplimentary;//�Ƿ���Ʒ
    private Integer isNtRtnGoods;//�Ƿ��˻�
    private String memo;//��ע
    private String returnMemo;//���ձ�ע
    private BigDecimal returnQty;//��������
    private String gdsBitEncd;//��λ����--------
    private String projEncd;//��Ŀ����
    private String projNm;//��Ŀ����
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
    private String measrCorpId;//������λ���
    private String measrCorpNm;//������λ����
    private String whsNm;//�ֿ�����
    private String crspdBarCd;//��Ӧ������
    private String invtyClsEncd;//�����������
    private String invtyClsNm;//�����������
    private String regnEncd;//�������--------
    private Long toOrdrNum;//��Դ�����ӱ��ʶ
    private BigDecimal carrOvrUprc;//��ת����
    private BigDecimal carrOvrAmt;//��ת���
    private BigDecimal rebaUprc;//��ת����
    private BigDecimal rebaAmt;//��ת���

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