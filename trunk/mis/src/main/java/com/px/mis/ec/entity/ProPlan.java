package com.px.mis.ec.entity;

public class ProPlan {
	
    private String proPlanId;//�����������
    private String proPlanName;//������������
    private Integer proWay;//������ʽ
    private Long proCriteria;//��������
    private Integer giftMul;//��Ʒ����
    private String memo;//��ע

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