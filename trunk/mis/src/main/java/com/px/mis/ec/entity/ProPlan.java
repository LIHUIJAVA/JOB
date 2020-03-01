package com.px.mis.ec.entity;

public class ProPlan {
	
    private String proPlanId;//促销方案编号
    private String proPlanName;//促销方案名称
    private Integer proWay;//促销方式
    private Long proCriteria;//促销条件
    private Integer giftMul;//赠品倍增
    private String memo;//备注

    public String getProPlanId() {
        return proPlanId;
    }

    public void setProPlanId(String proPlanId) {
        this.proPlanId = proPlanId == null ? null : proPlanId.trim();
    }

    public String getProPlanName() {
        return proPlanName;
    }

    public void setProPlanName(String proPlanName) {
        this.proPlanName = proPlanName == null ? null : proPlanName.trim();
    }

    public Integer getProWay() {
        return proWay;
    }

    public void setProWay(Integer proWay) {
        this.proWay = proWay;
    }

    public Long getProCriteria() {
        return proCriteria;
    }

    public void setProCriteria(Long proCriteria) {
        this.proCriteria = proCriteria;
    }

    public Integer getGiftMul() {
        return giftMul;
    }

    public void setGiftMul(Integer giftMul) {
        this.giftMul = giftMul;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}