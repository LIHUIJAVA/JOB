package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class PursOrdrSub implements Comparable<PursOrdrSub>{
	
    private Long ordrNum;//���
    private String pursOrdrId;//�ɹ��������
    private String invtyEncd;//�������
    private BigDecimal noTaxUprc;//��˰����
    private BigDecimal noTaxAmt;//��˰���
	private BigDecimal taxAmt;//˰��
    private BigDecimal cntnTaxUprc;//��˰����
    private BigDecimal prcTaxSum;//��˰�ϼ�
    private BigDecimal taxRate;//˰��
    private BigDecimal discntAmt;//�ۿ۶�(Ŀǰδ�õ�)
    private BigDecimal qty;//����
    private BigDecimal bxQty;//����
    private String planToGdsDt;//�ƻ�����ʱ��
    private String measrCorpId;//������λ���
    private Integer isComplimentary;//�Ƿ���Ʒ
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
    private String measrCorpNm;//������λ����
    private String crspdBarCd;//��Ӧ������
    
    private BigDecimal unToGdsQty;//δ��������
    private BigDecimal unApplPayQty;//δ���븶������
    private BigDecimal unApplPayAmt;//δ���븶����
    private String baoZhiQi;//������
    private String formTypName;//������������
    private String memo;//���屸ע
    
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public BigDecimal getUnApplPayAmt() {
		return unApplPayAmt;
	}

	public void setUnApplPayAmt(BigDecimal unApplPayAmt) {
		this.unApplPayAmt = unApplPayAmt;
	}

	public BigDecimal getUnApplPayQty() {
		return unApplPayQty;
	}

	public void setUnApplPayQty(BigDecimal unApplPayQty) {
		this.unApplPayQty = unApplPayQty;
	}

	public Long getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Long ordrNum) {
		this.ordrNum = ordrNum;
	}

	public BigDecimal getUnToGdsQty() {
		return unToGdsQty;
	}

	public void setUnToGdsQty(BigDecimal unToGdsQty) {
		this.unToGdsQty = unToGdsQty;
	}

	public String getPursOrdrId() {
		return pursOrdrId;
	}

	public void setPursOrdrId(String pursOrdrId) {
		this.pursOrdrId = pursOrdrId;
	}

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
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

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getBxQty() {
		return bxQty;
	}

	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}

	public String getPlanToGdsDt() {
		return planToGdsDt;
	}

	public void setPlanToGdsDt(String planToGdsDt) {
		this.planToGdsDt = planToGdsDt;
	}

	public String getMeasrCorpId() {
		return measrCorpId;
	}

	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}

	public Integer getIsComplimentary() {
		return isComplimentary;
	}

	public void setIsComplimentary(Integer isComplimentary) {
		this.isComplimentary = isComplimentary;
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

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
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
	
	@Override
	public int compareTo(PursOrdrSub pursOrdrSub) {
		return this.invtyEncd.compareTo(pursOrdrSub.getInvtyEncd());
	}

}