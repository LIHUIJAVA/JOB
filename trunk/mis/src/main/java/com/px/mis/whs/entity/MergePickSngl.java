package com.px.mis.whs.entity;

import java.math.BigDecimal;


//合并拣货单
public class MergePickSngl {

    private Long mergeNum;//合并拣货单编码

    private String pickSnglNum;//拣货单号
    private String pickNum;//未合并的拣货单号

    private String whsEncd;//仓库编码

    private String whsNm;//仓库名称

    private String invtyEncd;//存货编码

    private String invtyNm;//存货名称

    private String barCd;//条形码

    private String batNum;//批号

    private String prdcDt;//生产日期

    private String invldtnDt;//失效日期

    private String gdsBitEncd;//货位编码

    private BigDecimal qty;//数量

    private String operator;//操作人
    private String operatorId;//操作人编码

    private Integer isFinshPick;//是否拣货
    private String finshPickTm;//拣货时间


    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getIsFinshPick() {
        return isFinshPick;
    }

    public void setIsFinshPick(Integer isFinshPick) {
        this.isFinshPick = isFinshPick;
    }

    public String getFinshPickTm() {
        return finshPickTm;
    }

    public void setFinshPickTm(String finshPickTm) {
        this.finshPickTm = finshPickTm;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPickNum() {
        return pickNum;
    }

    public void setPickNum(String pickNum) {
        this.pickNum = pickNum;
    }

    public Long getMergeNum() {
        return mergeNum;
    }

    public void setMergeNum(Long mergeNum) {
        this.mergeNum = mergeNum;
    }

    public String getPickSnglNum() {
        return pickSnglNum;
    }

    public void setPickSnglNum(String pickSnglNum) {
        this.pickSnglNum = pickSnglNum;
    }

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

    public String getInvtyNm() {
        return invtyNm;
    }

    public void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public String getBarCd() {
        return barCd;
    }

    public void setBarCd(String barCd) {
        this.barCd = barCd;
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

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }


}
