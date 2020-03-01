package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

//�ִ�������

/**
 * @author Administrator
 */
public class NowStokReport {
	@JsonProperty("����������")
	private BigDecimal sellWhsQty;// ����������
	@JsonProperty("����������")
	private BigDecimal intoWhsQty;// ����������
	@JsonProperty("������;����")
	private BigDecimal inTransitQty;// ������;����
	@JsonProperty("Ԥ���������")
	private BigDecimal othIntoQty;// Ԥ���������
	@JsonProperty("Ԥ�Ƴ�������")
	private BigDecimal othOutQty;// Ԥ�Ƴ�������
	@JsonProperty("������������")
	private BigDecimal cannibIntoQty;// ������������
	@JsonProperty("������������")
	private BigDecimal cannibOutQty;// ������������
	@JsonProperty("�ִ���")
	private BigDecimal nowStok;// �ִ���
	@JsonProperty("ʧЧ����")
	private String invldtnDt;// ʧЧ����
	@JsonProperty("��������")
	private String prdcDt;// ��������
	@JsonProperty("����")
	private String batNum;// ����
	@JsonProperty("������")
	private BigDecimal avalQty;// ������
	@JsonProperty("δ˰����")
	private String unTaxUprc;// δ˰����
	@JsonProperty("��˰����")
	private String cntnTaxUprc;// ��˰����
	@JsonProperty("δ˰���")
	private String unTaxAmt;// δ˰���
	@JsonProperty("��˰���")
	private String cntnTaxAmt;// ��˰���
	@JsonProperty("�������")
	private String invtyEncd;// �������
	@JsonProperty("�ֿ����")
	private String whsEncd;// �ֿ����
	@JsonProperty("������")
	private String baoZhiQi;// ������
	@JsonProperty("��������λ����")
	private String measrCorpNm;// ��������λ����
	@JsonProperty("����ۼ�")
	private String loSellPrc;// ����ۼ�
	@JsonProperty("��������λ����")
	private String measrCorpId;// ��������λ����
	@JsonProperty("����")
	private String weight;// ����
	@JsonProperty("���")
	private String bxRule;// ���
	@JsonProperty("���")
	private String vol;// ���
	@JsonProperty("����ͺ�")
	private String spcModel;// ����ͺ�
	@JsonProperty("�������")
	private String invtyNm;// �������
	@JsonProperty("�ο��ۼ�")
	private String refSellPrc;// �ο��ۼ�
	@JsonProperty("�ο��ɱ�")
	private String refCost;// �ο��ɱ�
	@JsonProperty("�ֿ�����")
	private String whsNm;// �ֿ�����
	@JsonProperty("�������")
	private String invtyCd;// �������

	public BigDecimal getSellWhsQty() {
		return sellWhsQty;
	}

	public void setSellWhsQty(BigDecimal sellWhsQty) {
		this.sellWhsQty = sellWhsQty;
	}

	public BigDecimal getIntoWhsQty() {
		return intoWhsQty;
	}

	public void setIntoWhsQty(BigDecimal intoWhsQty) {
		this.intoWhsQty = intoWhsQty;
	}

	public BigDecimal getInTransitQty() {
		return inTransitQty;
	}

	public void setInTransitQty(BigDecimal inTransitQty) {
		this.inTransitQty = inTransitQty;
	}

	public BigDecimal getOthIntoQty() {
		return othIntoQty;
	}

	public void setOthIntoQty(BigDecimal othIntoQty) {
		this.othIntoQty = othIntoQty;
	}

	public BigDecimal getOthOutQty() {
		return othOutQty;
	}

	public void setOthOutQty(BigDecimal othOutQty) {
		this.othOutQty = othOutQty;
	}

	public BigDecimal getCannibIntoQty() {
		return cannibIntoQty;
	}

	public void setCannibIntoQty(BigDecimal cannibIntoQty) {
		this.cannibIntoQty = cannibIntoQty;
	}

	public BigDecimal getCannibOutQty() {
		return cannibOutQty;
	}

	public void setCannibOutQty(BigDecimal cannibOutQty) {
		this.cannibOutQty = cannibOutQty;
	}

	public BigDecimal getNowStok() {
		return nowStok;
	}

	public void setNowStok(BigDecimal nowStok) {
		this.nowStok = nowStok;
	}

	public String getInvldtnDt() {
		return invldtnDt;
	}

	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public String getPrdcDt() {
		return prdcDt;
	}

	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}

	public String getBatNum() {
		return batNum;
	}

	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}

	public BigDecimal getAvalQty() {
		return avalQty;
	}

	public void setAvalQty(BigDecimal avalQty) {
		this.avalQty = avalQty;
	}

	public String getUnTaxUprc() {
		return unTaxUprc;
	}

	public void setUnTaxUprc(String unTaxUprc) {
		this.unTaxUprc = unTaxUprc;
	}

	public String getCntnTaxUprc() {
		return cntnTaxUprc;
	}

	public void setCntnTaxUprc(String cntnTaxUprc) {
		this.cntnTaxUprc = cntnTaxUprc;
	}

	public String getUnTaxAmt() {
		return unTaxAmt;
	}

	public void setUnTaxAmt(String unTaxAmt) {
		this.unTaxAmt = unTaxAmt;
	}

	public String getCntnTaxAmt() {
		return cntnTaxAmt;
	}

	public void setCntnTaxAmt(String cntnTaxAmt) {
		this.cntnTaxAmt = cntnTaxAmt;
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

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

	public String getLoSellPrc() {
		return loSellPrc;
	}

	public void setLoSellPrc(String loSellPrc) {
		this.loSellPrc = loSellPrc;
	}

	public String getMeasrCorpId() {
		return measrCorpId;
	}

	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getBxRule() {
		return bxRule;
	}

	public void setBxRule(String bxRule) {
		this.bxRule = bxRule;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getSpcModel() {
		return spcModel;
	}

	public void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}

	public String getInvtyNm() {
		return invtyNm;
	}

	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

	public String getRefSellPrc() {
		return refSellPrc;
	}

	public void setRefSellPrc(String refSellPrc) {
		this.refSellPrc = refSellPrc;
	}

	public String getRefCost() {
		return refCost;
	}

	public void setRefCost(String refCost) {
		this.refCost = refCost;
	}

	public String getWhsNm() {
		return whsNm;
	}

	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public String getInvtyCd() {
		return invtyCd;
	}

	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}

}