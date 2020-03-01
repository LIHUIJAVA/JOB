package com.px.mis.purc.entity;

import java.math.BigDecimal;

public class ProvrDoc {
    private String provrId; //供应商编号 
    private String provrNm; //供应商名称 
    private String provrShtNm; //供应商简称
    private String provrClsId; //供应商分类编号
    private String devDt; //发展日期 
    private String provrTotlCorp; //供应商总公司
    private String belgZone;//所属地区
    private String belgCls;//所属分类
    private String contcr;//联系人  
    private String opnBnk;//开户银行
    private BigDecimal rgstCap;//注册资金 
    private String bkatNum;//银行账号 
    private String bankEncd;//所属银行编号 
    private String lpr;//法人 
    private String taxNum;//税号
    private String addr;//地址 
    private String tel;//电话 
    private String zipCd;//邮编 
    private BigDecimal taxRate;//税率 
    private Integer isNtPurs;//是否采购 
    private Integer isOutsource;//是否委外 
    private Integer isNtServ;//是否服务 
    private String setupPers;//创建人 
    private String setupDt;//创建时间
    private String mdfr;//修改人
    private String modiDt;//修改时间
    private String memo;//备注

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