package com.px.mis.sell.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

//�����ִ�������
public class SellNowStokReport {
	// �ֿ����1
	@JsonProperty("�ֿ����")
	private String whsEncd;
	// �ֿ�����
	@JsonProperty("�ֿ�����")
	private String whsNm;
	// �������1
	@JsonProperty("�������")
	private String invtyEncd;
	@JsonProperty("�������")
	private String invtyCd;//�������
	//����
	@JsonProperty("����")
	private BigDecimal qty;
	// �������
	@JsonProperty("�������")
	private String invtyNm;
	// ����ͺ�
	@JsonProperty("����ͺ�")
	private String spcModel;// ����ͺ�
	// ��������λ
	@JsonProperty("��������λ")
	private String measrCorpNm;// ������λ����
	// ���
	@JsonProperty("���")
	private BigDecimal bxRule;// ���
	// �������
	@JsonProperty("�������")
	private BigDecimal nowStok;// �ִ���
	// ��������
	@JsonProperty("��������")
	private BigDecimal avalQty;// ������
	// ���������
	@JsonProperty("���������")
	private BigDecimal intoWhsQty;// ������
	// ����������
	@JsonProperty("����������")
	private BigDecimal sellWhsQty;// ������
	// ������������
	@JsonProperty("������������")
	private BigDecimal outIntoQty;// ������
	// ����1
	@JsonProperty("����")
	private String batNum;// ����
	// ��������
	@JsonProperty("��������")
	private String prdcDt;// ��������
	// ������
	@JsonProperty("������")
	private String baoZhiQi;// ������
	// ʧЧ����
	@JsonProperty("ʧЧ����")
	private String invldtnDt;// ʧЧ����
	@JsonProperty("ʣ������")
	private String overdueDays;//ʣ������
	// �ο��ۼ�
	@JsonProperty("�ο��ۼ�")
	private BigDecimal refSellPrc;// �ο��ۼ�
	// ����ۼ�
	@JsonProperty("����ۼ�")
	private BigDecimal loSellPrc;// ����ۼ�
	// ������������
	@JsonProperty("������������")
	private BigDecimal outWhsQty;// ������
	// ��λ����
	@JsonProperty("��λ����")
	private BigDecimal weight;// ����
	// ��λ���
	@JsonProperty("��λ���")
	private String vol;// ���
	// ƽ����˰����
	@JsonProperty("��˰����")
	private BigDecimal noTaxUprc;
	// ��˰���
	@JsonProperty("��˰���")
	private BigDecimal noTaxAmt;
	// ƽ����˰����
//	@JsonProperty("ƽ����˰����")
//	private BigDecimal cntnTaxUprc;
//	// ��˰�ϼ�
//	@JsonProperty("��˰�ϼ�")
//	private BigDecimal prcTaxSum;
//	// ˰��
//	@JsonProperty("˰��")
//	private BigDecimal taxAmt;


	public String getWhsEncd() {
		return whsEncd;
	}

	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}

	public String getWhsNm() {
		return whsNm;
	}

	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public String getInvtyCd() {
		return invtyCd;
	}

	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
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

	public BigDecimal getBxRule() {
		return bxRule;
	}

	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}

	public BigDecimal getNowStok() {
		return nowStok;
	}

	public void setNowStok(BigDecimal nowStok) {
		this.nowStok = nowStok;
	}

	public BigDecimal getAvalQty() {
		return avalQty;
	}

	public void setAvalQty(BigDecimal avalQty) {
		this.avalQty = avalQty;
	}

	public BigDecimal getIntoWhsQty() {
		return intoWhsQty;
	}

	public void setIntoWhsQty(BigDecimal intoWhsQty) {
		this.intoWhsQty = intoWhsQty;
	}

	public BigDecimal getSellWhsQty() {
		return sellWhsQty;
	}

	public void setSellWhsQty(BigDecimal sellWhsQty) {
		this.sellWhsQty = sellWhsQty;
	}

	public BigDecimal getOutIntoQty() {
		return outIntoQty;
	}

	public void setOutIntoQty(BigDecimal outIntoQty) {
		this.outIntoQty = outIntoQty;
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

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getInvldtnDt() {
		return invldtnDt;
	}

	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public String getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(String overdueDays) {
		this.overdueDays = overdueDays;
	}

	public BigDecimal getRefSellPrc() {
		return refSellPrc;
	}

	public void setRefSellPrc(BigDecimal refSellPrc) {
		this.refSellPrc = refSellPrc;
	}

	public BigDecimal getLoSellPrc() {
		return loSellPrc;
	}

	public void setLoSellPrc(BigDecimal loSellPrc) {
		this.loSellPrc = loSellPrc;
	}

	public BigDecimal getOutWhsQty() {
		return outWhsQty;
	}

	public void setOutWhsQty(BigDecimal outWhsQty) {
		this.outWhsQty = outWhsQty;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
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
}
