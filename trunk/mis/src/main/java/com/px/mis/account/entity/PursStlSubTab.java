package com.px.mis.account.entity;

import java.math.BigDecimal;
//�ɹ������ӱ�
public class PursStlSubTab {
	private Integer incrsId;//����id
	private String stlSnglId;//���㵥��
	private String invtyEncd;//�������
	private String whsEncd;//�ֿ����
	private BigDecimal qty;//����
	private BigDecimal bxQty;//����
	private BigDecimal taxRate;//˰��
	private BigDecimal taxAmt;//˰��
	private BigDecimal noTaxUprc;//��˰����
	private BigDecimal noTaxAmt;//��˰���
	private BigDecimal cntnTaxUprc;//��˰����
	private BigDecimal prcTaxSum;//��˰�ϼ�
	private String batNum;//����
	private String prdcDt;//��������
	private String baoZhiQi;//������
	private String invldtnDt;//ʧЧ����
	private String intlBat;//��������
	private String memo;//��ע
	
	public String getIntlBat() {
		return intlBat;
	}
	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
	}
	public Integer getIncrsId() {
		return incrsId;
	}
	public void setIncrsId(Integer incrsId) {
		this.incrsId = incrsId;
	}
	public String getStlSnglId() {
		return stlSnglId;
	}
	public void setStlSnglId(String stlSnglId) {
		this.stlSnglId = stlSnglId;
	}
	public String getInvtyEncd() {
		return invtyEncd;
	}
	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}
	public String getWhsEncd() {
		return whsEncd;
	}
	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
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
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getBaoZhiQi() {
		return baoZhiQi;
	}
	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}
	
	 //�����������ֶΡ�������λ���ơ��ֿ�����
    private String invtyNm;//�������
    private String spcModel;//����ͺ�
    private String invtyCd;//�������
    private BigDecimal bxRule;//���
    private String baoZhiQiDt;//����������
    private BigDecimal iptaxRate;//����˰��
    private BigDecimal optaxRate;//����˰��
    private BigDecimal refCost;//�ο��ɱ�
    private BigDecimal refSellPrc;//�ο��ۼ�
    private String measrCorpNm;//������λ����
    private String whsNm;//�ֿ�����
    private String crspdBarCd;//��Ӧ������

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
	public String getBaoZhiQiDt() {
		return baoZhiQiDt;
	}
	public void setBaoZhiQiDt(String baoZhiQiDt) {
		this.baoZhiQiDt = baoZhiQiDt;
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
	public String getCrspdBarCd() {
		return crspdBarCd;
	}
	public void setCrspdBarCd(String crspdBarCd) {
		this.crspdBarCd = crspdBarCd;
	}
}
