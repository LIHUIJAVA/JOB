package com.px.mis.ec.entity;

public class ProActivitys {
	private Integer no;				//���
	private String proActId;		//��������
	private String proActName;		//���������
	private Integer allGoods;		//ȫ����Ʒ
	private String goodsRange;		//��Ʒ��Χ
	private String planCreator;		//�����Ƶ���
	private Integer giftNum;			//��Ʒ����
	private Integer hasGiftNum;			//��������
	private String memo;			//��ע
	private String proPlanId;			//������������
	private String proPlanName;			//������������
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
