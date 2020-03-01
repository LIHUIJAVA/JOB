package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class ProPlans {
	
    private Long no;//序号
    private BigDecimal money;//金额
    private Integer number;//数量
    private Long integralMul;//积分倍数
    private Integer giftNum;//赠品数量
    private Integer giftWay;//赠品方式
    private String giftRange;//赠品范围
    private String proPlanId;//主表编号
    private String memo;//备注

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getIntegralMul() {
        return integralMul;
    }

    public void setIntegralMul(Long integralMul) {
        this.integralMul = integralMul;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Integer getGiftWay() {
        return giftWay;
    }

    public void setGiftWay(Integer giftWay) {
        this.giftWay = giftWay;
    }

    public String getGiftRange() {
        return giftRange;
    }

    public void setGiftRange(String giftRange) {
        this.giftRange = giftRange == null ? null : giftRange.trim();
    }

    public String getProPlanId() {
        return proPlanId;
    }

    public void setProPlanId(String proPlanId) {
        this.proPlanId = proPlanId == null ? null : proPlanId.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}