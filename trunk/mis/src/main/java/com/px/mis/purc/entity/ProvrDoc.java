package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class ProvrDoc {
    private String provrId; //��Ӧ�̱�� 
    private String provrNm; //��Ӧ������ 
    private String provrShtNm; //��Ӧ�̼��
    private String provrClsId; //��Ӧ�̷�����
    private String devDt; //��չ���� 
    private String provrTotlCorp; //��Ӧ���ܹ�˾
    private String belgZone;//��������
    private String belgCls;//��������
    private String contcr;//��ϵ��  
    private String opnBnk;//��������
    private BigDecimal rgstCap;//ע���ʽ� 
    private String bkatNum;//�����˺� 
    private String bankEncd;//�������б�� 
    private String lpr;//���� 
    private String taxNum;//˰��
    private String addr;//��ַ 
    private String tel;//�绰 
    private String zipCd;//�ʱ� 
    private BigDecimal taxRate;//˰�� 
    private Integer isNtPurs;//�Ƿ�ɹ� 
    private Integer isOutsource;//�Ƿ�ί�� 
    private Integer isNtServ;//�Ƿ���� 
    private String setupPers;//������ 
    private String setupDt;//����ʱ��
    private String mdfr;//�޸���
    private String modiDt;//�޸�ʱ��
    private String memo;//��ע

	public String getProvrId() {
        return provrId;
    }

    public void setProvrId(String provrId) {
        this.provrId = provrId == null ? null : provrId.trim();
    }

    public String getProvrNm() {
        return provrNm;
    }

    public void setProvrNm(String provrNm) {
        this.provrNm = provrNm == null ? null : provrNm.trim();
    }

    public String getProvrShtNm() {
        return provrShtNm;
    }

    public void setProvrShtNm(String provrShtNm) {
        this.provrShtNm = provrShtNm == null ? null : provrShtNm.trim();
    }

    public String getProvrClsId() {
        return provrClsId;
    }

    public void setProvrClsId(String provrClsId) {
        this.provrClsId = provrClsId == null ? null : provrClsId.trim();
    }

    public String getDevDt() {
        return devDt;
    }

    public void setDevDt(String devDt) {
        this.devDt = devDt;
    }

    public String getBankEncd() {
		return bankEncd;
	}

	public void setBankEncd(String bankEncd) {
		this.bankEncd = bankEncd;
	}

	public String getProvrTotlCorp() {
        return provrTotlCorp;
    }

    public void setProvrTotlCorp(String provrTotlCorp) {
        this.provrTotlCorp = provrTotlCorp == null ? null : provrTotlCorp.trim();
    }

    public String getBelgZone() {
        return belgZone;
    }

    public void setBelgZone(String belgZone) {
        this.belgZone = belgZone == null ? null : belgZone.trim();
    }

    public String getBelgCls() {
        return belgCls;
    }

    public void setBelgCls(String belgCls) {
        this.belgCls = belgCls == null ? null : belgCls.trim();
    }

    public String getContcr() {
        return contcr;
    }

    public void setContcr(String contcr) {
        this.contcr = contcr == null ? null : contcr.trim();
    }

    public String getOpnBnk() {
        return opnBnk;
    }

    public void setOpnBnk(String opnBnk) {
        this.opnBnk = opnBnk == null ? null : opnBnk.trim();
    }

    public BigDecimal getRgstCap() {
        return rgstCap;
    }

    public void setRgstCap(BigDecimal rgstCap) {
        this.rgstCap = rgstCap;
    }

    public String getBkatNum() {
        return bkatNum;
    }

    public void setBkatNum(String bkatNum) {
        this.bkatNum = bkatNum == null ? null : bkatNum.trim();
    }

    public String getLpr() {
        return lpr;
    }

    public void setLpr(String lpr) {
        this.lpr = lpr == null ? null : lpr.trim();
    }

    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum == null ? null : taxNum.trim();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getZipCd() {
        return zipCd;
    }

    public void setZipCd(String zipCd) {
        this.zipCd = zipCd == null ? null : zipCd.trim();
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getIsNtPurs() {
        return isNtPurs;
    }

    public void setIsNtPurs(Integer isNtPurs) {
        this.isNtPurs = isNtPurs;
    }

    public Integer getIsOutsource() {
        return isOutsource;
    }

    public void setIsOutsource(Integer isOutsource) {
        this.isOutsource = isOutsource;
    }

    public Integer getIsNtServ() {
        return isNtServ;
    }

    public void setIsNtServ(Integer isNtServ) {
        this.isNtServ = isNtServ;
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    }

    public String getSetupDt() {
        return setupDt;
    }

    public void setSetupDt(String setupDt) {
        this.setupDt = setupDt == null ? null : setupDt.trim();
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr == null ? null : mdfr.trim();
    }

    public String getModiDt() {
        return modiDt;
    }

    public void setModiDt(String modiDt) {
        this.modiDt = modiDt == null ? null : modiDt.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}