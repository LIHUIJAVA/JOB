package com.px.mis.ec.entity;

public class ProActivitys {
	private Integer no;				//序号
	private String proActId;		//促销活动编号
	private String proActName;		//促销活动名称
	private Integer allGoods;		//全部商品
	private String goodsRange;		//商品范围
	private String planCreator;		//方案制单人
	private Integer giftNum;			//赠品数量
	private Integer hasGiftNum;			//已赠数量
	private String memo;			//备注
	private String proPlanId;			//促销方案编码
	private String proPlanName;			//促销方案名称
	public String getProActId() {
		return proActId;
	}
	public void setProActId(String proActId) {
		this.proActId = proActId;
	}
	public String getProActName() {
		return proActName;
	}
	public void setProActName(String proActName) {
		this.proActName = proActName;
	}
	public Integer getAllGoods() {
		return allGoods;
	}
	public void setAllGoods(Integer allGoods) {
		this.allGoods = allGoods;
	}
	public String getGoodsRange() {
		return goodsRange;
	}
	public void setGoodsRange(String goodsRange) {
		this.goodsRange = goodsRange;
	}
	public String getPlanCreator() {
		return planCreator;
	}
	public void setPlanCreator(String planCreator) {
		this.planCreator = planCreator;
	}
	public Integer getGiftNum() {
		return giftNum;
	}
	public void setGiftNum(Integer giftNum) {
		this.giftNum = giftNum;
	}
	public Integer getHasGiftNum() {
		return hasGiftNum;
	}
	public void setHasGiftNum(Integer hasGiftNum) {
		this.hasGiftNum = hasGiftNum;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public String getProPlanId() {
		return proPlanId;
	}
	public void setProPlanId(String proPlanId) {
		this.proPlanId = proPlanId;
	}
	public String getProPlanName() {
		return proPlanName;
	}
	public void setProPlanName(String proPlanName) {
		this.proPlanName = proPlanName;
	}
    @Override
    public String toString() {
        return String.format(
                "ProActivitys [no=%s, proActId=%s, proActName=%s, allGoods=%s, goodsRange=%s, planCreator=%s, giftNum=%s, hasGiftNum=%s, memo=%s, proPlanId=%s, proPlanName=%s]",
                no, proActId, proActName, allGoods, goodsRange, planCreator, giftNum, hasGiftNum, memo, proPlanId,
                proPlanName);
    }
	
}
