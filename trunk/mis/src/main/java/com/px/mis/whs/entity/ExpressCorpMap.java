package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//��ݹ�˾
public class ExpressCorpMap {

    //	private String expressEncd;// ��ݹ�˾���
//
//	private String expressNm;// ��ݹ�˾����
//
//	private String expressTyp;// �������
//
//	private String expOrderId;// ��ݵ���
//
//	private String sellHomeInfo;// ������Ϣ
//
//	private String buyHomeInfo;// �����Ϣ
//
//	private Integer isNtSprtGdsToPay;// �Ƿ�֧�ֻ�������
//
//	private BigDecimal gdsToPayServCost;// ������������
//
//	private Integer isNtStpUse;// �Ƿ�ͣ��
//
//	private BigDecimal firstWet;// ����
//
//	private BigDecimal firstWetStrPrice;// �������
//
//	private Integer printCnt;// ��ӡ����
//
//	private String memo;// ��ע
//
//	private Integer isNtChk;// �Ƿ����
//
//	private String setupPers;// ������
//
//	private String setupTm;// ����ʱ��
//
//	private String mdfr;// �޸���
//
//	private String modiTm;// �޸�ʱ��
//
//	private String chkr;// �����
//
//	private String chkTm;// ���ʱ��
//
//	private String deliver;// ����������
//
//	private String deliverPhone;// �������ֻ�
//
//	private String deliverMobile;// ����������
//
//	private String companyCode;// ��ݹ�˾����
//
//	private String province;// ������ʡ
//
//	private String city;// ��������
//
//	private String country;// ��������
//
//	private String detailedAddress;// ��ϸ��ַ
    @JsonProperty("��ݹ�˾����")
    private String expressEncd;// ��ݹ�˾���
    @JsonProperty("��ݹ�˾����")
    private String expressNm;// ��ݹ�˾����
    //	@JsonProperty("�������")
    @JsonIgnore
    private String expressTyp;// �������
    //	@JsonProperty("��ݵ���")
    @JsonIgnore
    private String expOrderId;// ��ݵ���
    //	@JsonProperty("������Ϣ")
    @JsonIgnore
    private String sellHomeInfo;// ������Ϣ
    //	@JsonProperty("�����Ϣ")
    @JsonIgnore
    private String buyHomeInfo;// �����Ϣ
    //	@JsonProperty("�Ƿ�֧�ֻ�������")
    @JsonIgnore
    private Integer isNtSprtGdsToPay;// �Ƿ�֧�ֻ�������
    //	@JsonProperty("������������")
    @JsonIgnore
    private BigDecimal gdsToPayServCost;// ������������
    @JsonProperty("�Ƿ�ͣ��")
    private Integer isNtStpUse;// �Ƿ�ͣ��
    @JsonProperty("����")
    private BigDecimal firstWet;// ����
    @JsonProperty("�������")
    private BigDecimal firstWetStrPrice;// �������
    //	@JsonProperty("��ӡ����")
    @JsonIgnore
    private Integer printCnt;// ��ӡ����
    @JsonProperty("��ע")
    private String memo;// ��ע
    //	@JsonProperty("�Ƿ����")
    @JsonIgnore
    private Integer isNtChk;// �Ƿ����
    @JsonProperty("������")
    private String setupPers;// ������
    @JsonProperty("����ʱ��")
    private String setupTm;// ����ʱ��
    @JsonProperty("�޸���")
    private String mdfr;// �޸���
    @JsonProperty("�޸�ʱ��")
    private String modiTm;// �޸�ʱ��
    //	@JsonProperty("�����")
    @JsonIgnore
    private String chkr;// �����
    //	@JsonProperty("���ʱ��")
    @JsonIgnore
    private String chkTm;// ���ʱ��
    @JsonProperty("����������")
    private String deliver;// ����������
    @JsonProperty("�������ֻ�")
    private String deliverPhone;// �������ֻ�
    @JsonProperty("����������")
    private String deliverMobile;// ����������
    @JsonProperty("��ݹ�˾����")
    private String companyCode;// ��ݹ�˾����
    @JsonProperty("������ʡ")
    private String province;// ������ʡ
    @JsonProperty("��������")
    private String city;// ��������
    @JsonProperty("��������")
    private String country;// ��������
    @JsonProperty("��ϸ��ַ")
    private String detailedAddress;// ��ϸ��ַ

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getDeliverPhone() {
        return deliverPhone;
    }

    public void setDeliverPhone(String deliverPhone) {
        this.deliverPhone = deliverPhone;
    }

    public String getDeliverMobile() {
        return deliverMobile;
    }

    public void setDeliverMobile(String deliverMobile) {
        this.deliverMobile = deliverMobile;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getExpressEncd() {
        return expressEncd;
    }

    public void setExpressEncd(String expressEncd) {
        this.expressEncd = expressEncd == null ? null : expressEncd.trim();
    }

    public String getExpressNm() {
        return expressNm;
    }

    public void setExpressNm(String expressNm) {
        this.expressNm = expressNm == null ? null : expressNm.trim();
    }

    public String getExpressTyp() {
        return expressTyp;
    }

    public void setExpressTyp(String expressTyp) {
        this.expressTyp = expressTyp == null ? null : expressTyp.trim();
    }

    public String getExpOrderId() {
        return expOrderId;
    }

    public void setExpOrderId(String expOrderId) {
        this.expOrderId = expOrderId == null ? null : expOrderId.trim();
    }

    public String getSellHomeInfo() {
        return sellHomeInfo;
    }

    public void setSellHomeInfo(String sellHomeInfo) {
        this.sellHomeInfo = sellHomeInfo == null ? null : sellHomeInfo.trim();
    }

    public String getBuyHomeInfo() {
        return buyHomeInfo;
    }

    public void setBuyHomeInfo(String buyHomeInfo) {
        this.buyHomeInfo = buyHomeInfo == null ? null : buyHomeInfo.trim();
    }

    public Integer getIsNtSprtGdsToPay() {
        return isNtSprtGdsToPay;
    }

    public void setIsNtSprtGdsToPay(Integer isNtSprtGdsToPay) {
        this.isNtSprtGdsToPay = isNtSprtGdsToPay;
    }

    public BigDecimal getGdsToPayServCost() {
        return gdsToPayServCost;
    }

    public void setGdsToPayServCost(BigDecimal gdsToPayServCost) {
        this.gdsToPayServCost = gdsToPayServCost;
    }

    public Integer getIsNtStpUse() {
        return isNtStpUse;
    }

    public void setIsNtStpUse(Integer isNtStpUse) {
        this.isNtStpUse = isNtStpUse;
    }

    public BigDecimal getFirstWet() {
        return firstWet;
    }

    public void setFirstWet(BigDecimal firstWet) {
        this.firstWet = firstWet;
    }

    public BigDecimal getFirstWetStrPrice() {
        return firstWetStrPrice;
    }

    public void setFirstWetStrPrice(BigDecimal firstWetStrPrice) {
        this.firstWetStrPrice = firstWetStrPrice;
    }

    public Integer getPrintCnt() {
        return printCnt;
    }

    public void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }
}