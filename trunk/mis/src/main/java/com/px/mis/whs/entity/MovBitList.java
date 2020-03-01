package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.px.mis.purc.entity.InvtyDoc;

//移位清单
public class MovBitList {

    private Long ordrNum;// 序号

    private String oalBit;// 原始货位

    private BigDecimal oalBitNum;// 原始货位数量

    private String targetBit;// 目标货位

    private BigDecimal targetBitNum;// 目标货位数量

    private String invtyEncd;// 存货编码

    private String batNum;// 批号

    private String prdcDt;// 生产日期

    private String whsEncd;// 仓库编码

    private String regnEncd;// 原区域编码

    private String targetRegnEncd;// 目标区域编码

    private String operator;// 操作人
    private String operatorId;// 操作人编码
    private Integer isOporFish;// 是否操作完成

    // 移位清单
    private WhsDoc whsDoc;
    private Regn regn;
    private InvtyDoc invtyDoc;

    private String whsNm;// 仓库名称
    private String invtyNm;// 存货名称
    private String spcModel;// 规格型号

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
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

    public String getTargetRegnEncd() {
        return targetRegnEncd;
    }

    public void setTargetRegnEncd(String targetRegnEncd) {
        this.targetRegnEncd = targetRegnEncd;
    }

    public WhsDoc getWhsDoc() {
        return whsDoc;
    }

    public void setWhsDoc(WhsDoc whsDoc) {
        this.whsDoc = whsDoc;
    }

    public Regn getRegn() {
        return regn;
    }

    public void setRegn(Regn regn) {
        this.regn = regn;
    }

    public InvtyDoc getInvtyDoc() {
        return invtyDoc;
    }

    public void setInvtyDoc(InvtyDoc invtyDoc) {
        this.invtyDoc = invtyDoc;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getIsOporFish() {
        return isOporFish;
    }

    public void setIsOporFish(Integer isOporFish) {
        this.isOporFish = isOporFish;
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getOalBit() {
        return oalBit;
    }

    public void setOalBit(String oalBit) {
        this.oalBit = oalBit;
    }

    public BigDecimal getOalBitNum() {
        return oalBitNum;
    }

    public void setOalBitNum(BigDecimal oalBitNum) {
        this.oalBitNum = oalBitNum;
    }

    public String getTargetBit() {
        return targetBit;
    }

    public void setTargetBit(String targetBit) {
        this.targetBit = targetBit;
    }

    public BigDecimal getTargetBitNum() {
        return targetBitNum;
    }

    public void setTargetBitNum(BigDecimal targetBitNum) {
        this.targetBitNum = targetBitNum;
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
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

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd;
    }

}
