package com.px.mis.account.entity;

import java.math.BigDecimal;
//�ɹ���ͨ��Ʊ�ӱ�
public class PursComnInvSub {
	private Integer ordrNum;//���
	private String pursInvNum;//�ɹ���Ʊ��
	private String invtyFstLvlCls;//���һ������
	private String crspdIntoWhsSnglNum;//��Ӧ��ⵥ��
	private Long intoWhsSnglSubtabId;//��ⵥ�ӱ�id 
	private String invtyEncd;//�������
	private String whsEncd;//�ֿ���
	private BigDecimal qty;//����
	private BigDecimal noTaxUprc;//��˰���� ����
	private BigDecimal noTaxAmt;//��˰��� ���
	private BigDecimal taxAmt;//˰��
	private BigDecimal cntnTaxUprc;//��˰����
	private BigDecimal prcTaxSum;//��˰�ϼ�
	private BigDecimal taxRate;//˰��
	private String batNum;//���� ����
	private String intlBat;//��������
	private String measrCorpId;//������λ���
	private String stlDt;//��������
	private String stlTm;//����ʱ��
	private String memo;//��ע
	
	private String whsNm;//�ֿ�
	private String invtyNm;//�������
	private String invtyCd;//�������
	private String spcModel;//����ͺ�
	private BigDecimal bxRule;//���
	private BigDecimal bxQty;//����
	private Integer isNtRtnGoods;//�Ƿ��˻�
	private String measrCorpNm;//������λ
	private String baoZhiQiDt;//����������
	private String crspdBarCd;//��Ӧ������
	private String gift;//��Ʒ
	
	private String projEncd;//��Ŀ����
    private String projNm;//��Ŀ����
	
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
	public PursComnInvSub() {
	}
	public Integer getOrdrNum() {
		return ordrNum;
	}
	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
	public String getPursInvNum() {
		return pursInvNum;
	}
	public void setPursInvNum(String pursInvNum) {
		this.pursInvNum = pursInvNum;
	}
	public String getInvtyFstLvlCls() {
		return invtyFstLvlCls;
	}
	public void setInvtyFstLvlCls(String invtyFstLvlCls) {
		this.invtyFstLvlCls = invtyFstLvlCls;
	}
	public String getCrspdIntoWhsSnglNum() {
		return crspdIntoWhsSnglNum;
	}
	public void setCrspdIntoWhsSnglNum(String crspdIntoWhsSnglNum) {
		this.crspdIntoWhsSnglNum = crspdIntoWhsSnglNum;
	}
	
	public Long getIntoWhsSnglSubtabId() {
		return intoWhsSnglSubtabId;
	}
	public void setIntoWhsSnglSubtabId(Long intoWhsSnglSubtabId) {
		this.intoWhsSnglSubtabId = intoWhsSnglSubtabId;
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
	public String getBatNum() {
		return batNum;
	}
	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}
	public String getMeasrCorpId() {
		return measrCorpId;
	}
	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}
	
	public String getStlDt() {
		return stlDt;
	}
	public void setStlDt(String stlDt) {
		this.stlDt = stlDt;
	}
	public String getStlTm() {
		return stlTm;
	}
	public void setStlTm(String stlTm) {
		this.stlTm = stlTm;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public String getGift() {
		return gift;
	}
	public void setGift(String gift) {
		this.gift = gift;
	}
	public String getIntlBat() {
		return intlBat;
	}
	public void setIntlBat(String intlBat) {
		this.intlBat = intlBat;
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
	public BigDecimal getBxRule() {
		return bxRule;
	}
	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}
	public BigDecimal getBxQty() {
		return bxQty;
	}
	public void setBxQty(BigDecimal bxQty) {
		this.bxQty = bxQty;
	}
	public Integer getIsNtRtnGoods() {
		return isNtRtnGoods;
	}
	public void setIsNtRtnGoods(Integer isNtRtnGoods) {
		this.isNtRtnGoods = isNtRtnGoods;
	}
	public String getBaoZhiQiDt() {
		return baoZhiQiDt;
	}
	public void setBaoZhiQiDt(String baoZhiQiDt) {
		this.baoZhiQiDt = baoZhiQiDt;
	}
	public String getCrspdBarCd() {
		return crspdBarCd;
	}
	public void setCrspdBarCd(String crspdBarCd) {
		this.crspdBarCd = crspdBarCd;
	}
	
}
