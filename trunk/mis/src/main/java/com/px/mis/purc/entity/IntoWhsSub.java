package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class IntoWhsSub implements Comparable<IntoWhsSub>{
	
    private Long ordrNum;//���
    private String intoWhsSnglId;//��ⵥ��
    private String invtyEncd;//�������
    private String whsEncd;//�ֿ����
    private String pursOrdrSubTabInd;//�ɹ������ӱ��ʶ
    private String pursToGdsSnglSubTabInd;//�ɹ��������ӱ��ʶ
    private BigDecimal qty;//����
    private BigDecimal bxQty;//����
    private BigDecimal taxRate;//˰��
    private BigDecimal noTaxUprc;//��˰����
    private BigDecimal noTaxAmt;//��˰���
    private BigDecimal taxAmt;//˰��
    private BigDecimal cntnTaxUprc;//��˰����
    private BigDecimal prcTaxSum;//��˰�ϼ�
    private BigDecimal stlQty;//��������
    private BigDecimal stlUprc;//���㵥��
    private BigDecimal stlAmt;//������
    private BigDecimal unBllgQty;//δ��Ʊ����
    private BigDecimal unBllgUprc;//δ��Ʊ����
    private BigDecimal unBllgAmt;//δ��Ʊ���
    private BigDecimal teesQty;//�ݹ�����
    private BigDecimal teesUprc;//�ݹ�����
    private BigDecimal teesAmt;//�ݹ����
    private String crspdInvNum;//��Ӧ��Ʊ��
    private String baoZhiQi;//������
    private String intlBat;//��������
    private String batNum;//����
    private String prdcDt;//��������
    private String invldtnDt;//ʧЧ����
    private String gdsBitEncd;//��λ����
    private Integer isComplimentary;//�Ƿ���Ʒ
    private Integer isNtRtnGoods;//�Ƿ��˻���1:���  0���˻���
    private String returnMemo;//���ձ�ע
    private BigDecimal returnQty;//δ��������
    
    //�����������ֶΡ�������λ����
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
    
    private String projEncd;//��Ŀ����
    private String projNm;//��Ŀ����
    
    private Long pursOrdrNum;//�ɹ������ӱ����
    private BigDecimal actlGdsQty;//ʵ�ʵ�������
    private BigDecimal refuAcptQty;//��������
    private String memo;//���屸ע

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getActlGdsQty() {
		return actlGdsQty;
	}

	public void setActlGdsQty(BigDecimal actlGdsQty) {
		this.actlGdsQty = actlGdsQty;
	}

	public BigDecimal getRefuAcptQty() {
		return refuAcptQty;
	}

	public void setRefuAcptQty(BigDecimal refuAcptQty) {
		this.refuAcptQty = refuAcptQty;
	}

	public Long getPursOrdrNum() {
		return pursOrdrNum;
	}

	public void setPursOrdrNum(Long pursOrdrNum) {
		this.pursOrdrNum = pursOrdrNum;
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

	public BigDecimal getBxQty() {
		return bxQty;
	}

	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
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

	public void setIsNtRtnGoods(Integer isNtRtnGoods) {
		this.isNtRtnGoods = isNtRtnGoods;
	}

	public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getIntoWhsSnglId() {
        return intoWhsSnglId;
    }

    public void setIntoWhsSnglId(String intoWhsSnglId) {
        this.intoWhsSnglId = intoWhsSnglId == null ? null : intoWhsSnglId.trim();
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

    public String getPursOrdrSubTabInd() {
        return pursOrdrSubTabInd;
    }

    public void setPursOrdrSubTabInd(String pursOrdrSubTabInd) {
        this.pursOrdrSubTabInd = pursOrdrSubTabInd == null ? null : pursOrdrSubTabInd.trim();
    }

    public String getPursToGdsSnglSubTabInd() {
        return pursToGdsSnglSubTabInd;
    }

    public void setPursToGdsSnglSubTabInd(String pursToGdsSnglSubTabInd) {
        this.pursToGdsSnglSubTabInd = pursToGdsSnglSubTabInd == null ? null : pursToGdsSnglSubTabInd.trim();
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

    public BigDecimal getStlQty() {
        return stlQty;
    }

    public void setStlQty(BigDecimal stlQty) {
        this.stlQty = stlQty;
    }

    public BigDecimal getStlUprc() {
        return stlUprc;
    }

    public void setStlUprc(BigDecimal stlUprc) {
        this.stlUprc = stlUprc;
    }

    public BigDecimal getStlAmt() {
        return stlAmt;
    }

    public void setStlAmt(BigDecimal stlAmt) {
        this.stlAmt = stlAmt;
    }

    public BigDecimal getUnBllgQty() {
        return unBllgQty;
    }

    public void setUnBllgQty(BigDecimal unBllgQty) {
        this.unBllgQty = unBllgQty;
    }

    public BigDecimal getUnBllgUprc() {
        return unBllgUprc;
    }

    public void setUnBllgUprc(BigDecimal unBllgUprc) {
        this.unBllgUprc = unBllgUprc;
    }

    public BigDecimal getUnBllgAmt() {
        return unBllgAmt;
    }

    public void setUnBllgAmt(BigDecimal unBllgAmt) {
        this.unBllgAmt = unBllgAmt;
    }

    public BigDecimal getTeesQty() {
        return teesQty;
    }

    public void setTeesQty(BigDecimal teesQty) {
        this.teesQty = teesQty;
    }

    public BigDecimal getTeesUprc() {
        return teesUprc;
    }

    public void setTeesUprc(BigDecimal teesUprc) {
        this.teesUprc = teesUprc;
    }

    public BigDecimal getTeesAmt() {
        return teesAmt;
    }

    public void setTeesAmt(BigDecimal teesAmt) {
        this.teesAmt = teesAmt;
    }

    public String getCrspdInvNum() {
        return crspdInvNum;
    }

    public void setCrspdInvNum(String crspdInvNum) {
        this.crspdInvNum = crspdInvNum == null ? null : crspdInvNum.trim();
    }

    public String getBaoZhiQi() {
        return baoZhiQi;
    }

    public void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi == null ? null : baoZhiQi.trim();
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

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd == null ? null : gdsBitEncd.trim();
    }

    public Integer getIsComplimentary() {
        return isComplimentary;
    }

    public void setIsComplimentary(Integer isComplimentary) {
        this.isComplimentary = isComplimentary;
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
	public int compareTo(IntoWhsSub intoWhsSub) {
		int flag = this.invtyEncd.compareTo(intoWhsSub.getInvtyEncd());
        if(flag != 0) {
        	return flag;
        }
        flag = this.whsEncd.compareTo(intoWhsSub.getWhsEncd());
        
        if(flag != 0) {
        	return flag;
        }
        
		return this.batNum.compareTo(intoWhsSub.getBatNum());
	}
	
}